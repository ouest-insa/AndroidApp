package fr.ouestinsa.ui.activity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.DAOFactory;
import fr.ouestinsa.db.SQLiteDAOFactory;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.exception.AccountNotFillException;
import fr.ouestinsa.network.ApplyStudy;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.ui.OnSwipeTouchListener;

public class DetailsActivity extends ActionBarActivity implements
		OnClickListener {
	private Study study;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		DAOFactory factory = SQLiteDAOFactory.getFactory(DAOFactory.SQLITE);
		StudyDAO studyDAO = factory.getStudyDAO(this);
		studyDAO.open();
		study = studyDAO.get(Integer.valueOf(getIntent().getStringExtra(
				StudyDAO.ID)));
		studyDAO.close();

		((TextView) findViewById(R.id.name)).setText(study.getName());
		if (study.getStatus().equals(Status.CONTACT)) {
			((Button) findViewById(R.id.apply)).setOnClickListener(this);
		} else {
			((Button) findViewById(R.id.apply)).setEnabled(false);
		}
		((RelativeLayout) findViewById(R.id.base_layout))
				.setOnTouchListener(new OnSwipeTouchListener(this) {
					public void onSwipeRight() {
						finish();
					}
				});
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
							Toast.LENGTH_LONG).show();
				} else if (e != null) {
					Toast.makeText(this, R.string.error_apply,
							Toast.LENGTH_LONG).show();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Toast.makeText(this, R.string.error_apply, Toast.LENGTH_LONG)
						.show();
			} catch (ExecutionException e) {
				e.printStackTrace();
				Toast.makeText(this, R.string.error_apply, Toast.LENGTH_LONG)
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
			// overridePendingTransition(R.anim.fade_in,
			// R.anim.slide_out_right);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}