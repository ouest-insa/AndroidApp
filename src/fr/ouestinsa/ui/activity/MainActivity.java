package fr.ouestinsa.ui.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.DAOFactory;
import fr.ouestinsa.db.SQLiteDAOFactory;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.network.Retrieve;
import fr.ouestinsa.network.RetrieveStudies;
import fr.ouestinsa.object.Study;

public class MainActivity extends ActionBarActivity implements
		OnRefreshListener {
	private List<Study> studies;
	private ListView listview;
	SwipeRefreshLayout mSwipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.listview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(this);

		mSwipeRefreshLayout.setRefreshing(false);

		onRefresh();
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
		DAOFactory factory = SQLiteDAOFactory.getFactory(DAOFactory.SQLITE);
		StudyDAO studyDAO = factory.getStudyDAO(this);
		studyDAO.open();

		try {
			studies = new RetrieveStudies().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR,
					Retrieve.API_URL_GET_STUDIES).get();

			if (studies != null) {
				studyDAO.clear();

				for (Study study : studies) {
					studyDAO.add(study);
				}

				setStudies();

				Toast.makeText(this, R.string.success_update, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(this, R.string.error_internet_connection,
						Toast.LENGTH_LONG).show();
				studies = studyDAO.getAll();

				setStudies();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		studyDAO.close();

		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
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

	private void setStudies() {
		listview.setAdapter(new ArrayAdapter<Study>(this, R.layout.list_study,
				studies) {
			@SuppressLint("ViewHolder")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View rowView = inflater.inflate(R.layout.list_study, parent,
						false);

				TextView jeh = (TextView) rowView.findViewById(R.id.jeh);
				// TextView name = (TextView) rowView.findViewById(R.id.name);
				TextView type = (TextView) rowView.findViewById(R.id.type);

				if (studies.get(position).getJeh() > 0) {
					jeh.setText((studies.get(position).getJeh() > 3) ? (studies
							.get(position).getJeh() > 6) ? "€€€" : "€€" : "€");
				} else {
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
				overridePendingTransition(R.anim.display,
						android.R.anim.fade_out);
			}
		});
	}
}