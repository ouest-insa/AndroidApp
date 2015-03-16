package fr.ouestinsa.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import fr.ouestinsa.R;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.network.ApplyStudy;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.ui.OnSwipeTouchListener;
import fr.ouestinsa.ui.activity.background.GetDetails;

public class DetailsActivity extends ActionBarActivity implements
		OnClickListener {
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
			setDetails(study.getDetails());
		}

		new Thread(new GetDetails(this, new Handler(), study)).start();

		((TextView) findViewById(R.id.name)).setText("Numéro de l'étude : " + study.getId());

		if (study.getStatus().equals(Status.CONTACT)) {
			((Button) findViewById(R.id.apply)).setOnClickListener(this);
		} else {
			((Button) findViewById(R.id.apply)).setEnabled(false);
		}

		ScrollView scrollView = (ScrollView) findViewById(R.id.base_layout);
		scrollView.setOnTouchListener(new OnSwipeTouchListener(this) {
			public void onSwipeRight() {
				finish();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.apply) {
			new Thread(new ApplyStudy(study, this, new Handler())).start();
		}
	}

	public Study getCurrentStudy() {
		return study;
	}

	public void setDetails(String details) {
		this.details.setText(details);
	}

	public void addDetailsToDB() {
		studyDAO.addDetails(study.getId(), study.getDetails());
	}
}