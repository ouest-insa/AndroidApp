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
import fr.ouestinsa.db.ApplicableDAO;
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
				StudyDAO.REFERENCE)));

		details = (TextView) findViewById(R.id.details);
		if (study.getDetails() != null && !study.getDetails().equals("")) {
			setDetails(study.getDetails());
		}

		new Thread(new GetDetails(this, new Handler(), study)).start();


		((TextView) findViewById(R.id.number)).setText("Étude n°" + study.getId());
		((TextView) findViewById(R.id.name)).setText(study.getType());

		if (study.getStatus().equals(Status.CONTACT)) {
			Button apply = (Button) findViewById(R.id.apply);
			
			apply.setOnClickListener(this);
			if(ApplicableDAO.getInstance(this).isApplicable(study)) {
				apply.setEnabled(true);
			} else {
				apply.setText(R.string.already_apply);
			}
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

	public void setDetails(String str) {
		details.setText(str);
	}

	public void disableButton() {
		Button apply = (Button) findViewById(R.id.apply);
		apply.setEnabled(false);
		apply.setText(R.string.already_apply);
	}

	public void addDetailsToDB() {
		studyDAO.addDetails(study.getReference(), study.getDetails());
	}
}