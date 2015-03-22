package fr.ouest_insa.ui.activity.background;

import android.os.Handler;
import android.widget.Toast;
import fr.ouest_insa.R;
import fr.ouest_insa.network.Retrieve;
import fr.ouest_insa.network.RetrieveDetails;
import fr.ouest_insa.object.Study;
import fr.ouest_insa.ui.activity.DetailsActivity;

/**
 * This Thread allow to separate the action of retrieve 
 * details of a study from the UI.
 * @author Loïc Pelleau
 */
public class GetDetails implements Runnable {
	private DetailsActivity a;
	private Handler mHandler;
	private Study study;

	public GetDetails(DetailsActivity a, Handler mHandler, Study study) {
		this.a = a;
		this.mHandler = mHandler;
		this.study = study;
	}

	@Override
	public void run() {
		try {
			@SuppressWarnings("rawtypes")
			Retrieve r = new RetrieveDetails(Retrieve.API_URL_GET_STUDIES + "/"
					+ study.getId());
			Thread t = new Thread(r);
			t.start();
			t.join();
			study.setDetails((String) r.getResult());

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (study.getDetails() == null) {
						a.setDetails(a.getString(R.string.no_details));
					} else {
						a.setDetails(study.getDetails());
						a.addDetailsToDB();
					}
				}
			});
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
