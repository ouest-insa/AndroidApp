package fr.ouestinsa.ui.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import fr.ouestinsa.R;
import fr.ouestinsa.db.DAOFactory;
import fr.ouestinsa.db.SQLiteDAOFactory;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.network.RetrieveStudies;
import fr.ouestinsa.object.Study;

public class SplashScreen extends Activity {
	public static final String API_URL_GET_STUDIES = "http://siaje.deuxfleurs.fr/study";
	private static final long TEMPS_MIN_SPLASHSCREEN_MS = 2500;

	private long tempsDebut;
	private long tempsChargement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tempsDebut = System.currentTimeMillis();

		DAOFactory factory = SQLiteDAOFactory.getFactory(DAOFactory.SQLITE);

		StudyDAO studyDAO = factory.getStudyDAO(this);

		studyDAO.open();

		try {
			ArrayList<Study> studies = new RetrieveStudies().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, API_URL_GET_STUDIES).get();
			studyDAO.clear();

			for (Study study : studies) {
				studyDAO.add(study);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
		}, tempsAttente());
	}

	private long tempsAttente() {
		tempsChargement = TEMPS_MIN_SPLASHSCREEN_MS
				- (System.currentTimeMillis() - tempsDebut);
		if (tempsChargement > 0) {
			return tempsChargement;
		}
		return 0;
	}
}