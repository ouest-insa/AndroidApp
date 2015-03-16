package fr.ouestinsa.ui.activity.background;

import java.util.List;

import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.network.Retrieve;
import fr.ouestinsa.network.RetrieveStudies;
import fr.ouestinsa.object.Study;
import fr.ouestinsa.ui.activity.MainActivity;

public class GetStudies implements Runnable {
	private MainActivity a;
	private Handler mHandler;

	public GetStudies(MainActivity a, Handler mHandler) {
		this.a = a;
		this.mHandler = mHandler;
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
						a.setStudies();
						((RelativeLayout) a.findViewById(R.id.connection_impossible)).setVisibility(View.GONE);
						
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
