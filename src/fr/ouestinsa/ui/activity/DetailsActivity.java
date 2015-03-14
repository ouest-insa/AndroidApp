package fr.ouestinsa.ui.activity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.exception.AccountNotFillException;
import fr.ouestinsa.network.ApplyStudy;
import fr.ouestinsa.network.Retrieve;
import fr.ouestinsa.network.RetrieveDetails;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.ui.OnSwipeTouchListener;

public class DetailsActivity extends ActionBarActivity implements
		OnClickListener {
	private Handler mHandler = new Handler();
	private Study study;
	private StudyDAO studyDAO;
	private TextView details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		studyDAO = StudyDAO.getInstance(this);
		study = studyDAO.get(Integer.valueOf(getIntent().getStringExtra(
				StudyDAO.ID)));

		details = (TextView) findViewById(R.id.details);
		if (study.getDetails() != null && !study.getDetails().equals("")) {
			details.setText(study.getDetails());
		}

		new Thread(new GetDetails(this)).start();

		((TextView) findViewById(R.id.name)).setText(study.getName());

		if (study.getStatus().equals(Status.CONTACT)) {
			((Button) findViewById(R.id.apply)).setOnClickListener(this);
		} else {
			((Button) findViewById(R.id.apply)).setEnabled(false);
		}

		ScrollView scrollView = (ScrollView) findViewById(R.id.base_layout);
		scrollView.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.slide_out_right);
			}
		});
	}

	private class GetDetails implements Runnable {
		private DetailsActivity a;

		public GetDetails(DetailsActivity a) {
			this.a = a;
		}

		@Override
		public void run() {
			try {
				@SuppressWarnings("rawtypes")
				Retrieve r = new RetrieveDetails(Retrieve.API_URL_GET_STUDIES
						+ "/" + study.getId());
				Thread t = new Thread(r);
				t.start();
				t.join();
				study.setDetails((String) r.getResult());

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (study.getDetails() == null) {
							details.setText(R.string.no_details);
						} else {
							details.setText(study.getDetails());
						}
					}
				});
				studyDAO.addDetails(study.getId(), study.getDetails());
			} catch (InterruptedException e) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(a, R.string.error_internet_connection,
								Toast.LENGTH_SHORT).show();
					}
				});
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.apply) {
			try {
				Exception e = new ApplyStudy().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, this).get();
				if (e != null
						&& (e instanceof AccountNotFillException || e instanceof IOException)) {
					Toast.makeText(this, R.string.error_account_not_fill,
							Toast.LENGTH_SHORT).show();
				} else if (e != null) {
					Toast.makeText(this, R.string.error_apply,
							Toast.LENGTH_SHORT).show();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Toast.makeText(this, R.string.error_apply, Toast.LENGTH_SHORT)
						.show();
			} catch (ExecutionException e) {
				e.printStackTrace();
				Toast.makeText(this, R.string.error_apply, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public Study getCurrentStudy() {
		return study;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.slide_out_right);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.slide_out_right);
	}
}