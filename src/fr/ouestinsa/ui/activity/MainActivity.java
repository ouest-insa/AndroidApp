package fr.ouestinsa.ui.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.network.Retrieve;
import fr.ouestinsa.network.RetrieveStudies;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.object.TypesStudy;

public class MainActivity extends ActionBarActivity implements
		OnRefreshListener {
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(this);

		openSwipeRefresh();
		setStudies();
		new Thread(new GetStudies(this)).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.website) {
			if (!isNetworkConnected()) {
				Toast.makeText(this, R.string.error_internet_connection,
						Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(MainActivity.this, WebviewActivity.class);
				startActivity(i);
			}
			return true;
		} else if (itemId == R.id.account) {
			Intent i = new Intent(MainActivity.this, AccountActivity.class);
			startActivity(i);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.fade_out);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onRefresh() {
		new Thread(new GetStudies(this)).start();
	}

	public void closeSwipeRefresh() {
		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	public void openSwipeRefresh() {
		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(true);
			}
		});
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	private class GetStudies implements Runnable {
		private MainActivity a;

		public GetStudies(MainActivity a) {
			this.a = a;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				@SuppressWarnings("rawtypes")
				Retrieve r = new RetrieveStudies(Retrieve.API_URL_GET_STUDIES);
				Thread t = new Thread(r);
				t.start();
				t.join();
				List<Study> studies = (List<Study>) r.getResult();

				if (studies != null) {
					StudyDAO studyDAO = StudyDAO.getInstance(a);
					studyDAO.clear();
					for (Study study : studies) {
						studyDAO.add(study);
					}

					mHandler.post(new Runnable() {
						@Override
						public void run() {
							setStudies();

							Toast.makeText(a, R.string.success_update,
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(a,
									R.string.error_internet_connection,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			a.closeSwipeRefresh();
		}

	}

	private void setStudies() {
		StudyDAO studyDAO = StudyDAO.getInstance(this);
		final List<Study> studies = studyDAO.getAll();

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(new ArrayAdapter<Study>(this, R.layout.list_study,
				studies) {
			@SuppressLint("ViewHolder")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View rowView = inflater.inflate(R.layout.list_study, parent,
						false);

				ImageView img = (ImageView) rowView.findViewById(R.id.img);
				TextView jeh = (TextView) rowView.findViewById(R.id.jeh);
				// TextView name = (TextView) rowView.findViewById(R.id.name);
				TextView type = (TextView) rowView.findViewById(R.id.type);

				try {
					img.setImageResource(TypesStudy.forInt(
							studies.get(position).getTypeId()).getRessource());
				} catch (IllegalArgumentException e) {
					Log.e("CC", studies.get(position).getName() + " : "
							+ studies.get(position).getTypeId());
				}
				if (studies.get(position).getStatus().equals(Status.CONTACT)) {
					if (studies.get(position).getJeh() > 0) {
						jeh.setText((studies.get(position).getJeh() > 3) ? (studies
								.get(position).getJeh() > 6) ? "€€€" : "€€"
								: "€");
					}
					// jeh.setText("On a besoin de toi !");
				} else {
					rowView.setAlpha(0.5f);
					jeh.setVisibility(View.GONE);
				}
				// name.setText(" ("
				// + String.valueOf(studies.get(position).getName()) + ")");
				type.setText(String.valueOf(studies.get(position).getType()));

				return rowView;
			}
		});

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Study selectedStudy = (Study) parent.getAdapter().getItem(
						position);

				Intent i = new Intent(getApplicationContext(),
						DetailsActivity.class);
				i.putExtra(StudyDAO.ID, String.valueOf(selectedStudy.getId()));
				startActivity(i);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.fade_out);
			}
		});
	}
}