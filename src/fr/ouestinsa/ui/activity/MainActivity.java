package fr.ouestinsa.ui.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import fr.ouestinsa.R;
import fr.ouestinsa.db.AccountDAO;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.object.TypesStudy;
import fr.ouestinsa.ui.activity.background.GetStudies;

public class MainActivity extends ActionBarActivity implements
		OnRefreshListener {
	private SwipeRefreshLayout mSwipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AccountDAO.getInstance(this); // Just to place the context

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		mSwipeRefreshLayout.setOnRefreshListener(this);

		openSwipeRefresh();
		setStudies();
		new Thread(new GetStudies(this, new Handler())).start();
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
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://ouest-insa.fr"));
			startActivity(browserIntent);
			return true;
		} else if (itemId == R.id.account) {
			Intent i = new Intent(MainActivity.this, AccountActivity.class);
			startActivity(i);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onRefresh() {
		new Thread(new GetStudies(this, new Handler())).start();
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

	public void setStudies() {
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
				} else {
					rowView.setAlpha(0.5f);
					jeh.setVisibility(View.GONE);
				}
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
			}
		});
	}
}