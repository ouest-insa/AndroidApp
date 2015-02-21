package fr.ouestinsa.ui.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.DAOFactory;
import fr.ouestinsa.db.SQLiteDAOFactory;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.network.Retrieve;
import fr.ouestinsa.network.RetrieveStudies;
import fr.ouestinsa.object.Study;

public class SplashScreen extends ActionBarActivity {
	private static final long MIN_TIME_SPLASHSCREEN_MS = 2500;

	private long startTime;
	private long loadTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		startTime = System.currentTimeMillis();

		DAOFactory factory = SQLiteDAOFactory.getFactory(DAOFactory.SQLITE);
		StudyDAO studyDAO = factory.getStudyDAO(this);
		studyDAO.open();

		try {
			ArrayList<Study> studies = new RetrieveStudies().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, Retrieve.API_URL_GET_STUDIES).get();
			if(studies == null) {
				Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
			} else {
				studyDAO.clear();

				for (Study study : studies) {
					studyDAO.add(study);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		studyDAO.close();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

				finish();
			}
		}, waitTime());
	}

	private long waitTime() {
		loadTime = MIN_TIME_SPLASHSCREEN_MS
				- (System.currentTimeMillis() - startTime);
		if (loadTime > 0) {
			return loadTime;
		}
		return 0;
	}
}