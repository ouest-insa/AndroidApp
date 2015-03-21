package fr.ouest_insa.network;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.widget.Toast;
import fr.ouest_insa.R;
import fr.ouest_insa.db.AccountDAO;
import fr.ouest_insa.db.ApplicableDAO;
import fr.ouest_insa.exception.AccountNotFillException;
import fr.ouest_insa.object.Account;
import fr.ouest_insa.object.Study;
import fr.ouest_insa.ui.activity.DetailsActivity;

public class ApplyStudy implements Runnable {
	public static final String API_URL_APPLY_STUDY = Retrieve.API_URL + "study";
	private Study study;
	private DetailsActivity a;
	private Handler mHandler;

	public ApplyStudy(Study study, DetailsActivity a, Handler mHandler) {
		this.study = study;
		this.a = a;
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		try {
			JSONObject dataJSON = getJSON();
			postJSON(dataJSON);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					a.updateButton(false, R.string.already_apply);
					ApplicableDAO.getInstance(null).justApply(study);
				}
			});
			sendToast(R.string.success_apply);
		} catch (FileNotFoundException e1) {
			resetButton();
			sendToast(R.string.error_account_not_fill);
			e1.printStackTrace();
		} catch (AccountNotFillException e1) {
			resetButton();
			sendToast(R.string.error_account_not_fill);
			e1.printStackTrace();
		} catch (JSONException e1) {
			resetButton();
			sendToast(R.string.error_apply);
			e1.printStackTrace();
		} catch (IOException e1) {
			resetButton();
			sendToast(R.string.error_apply);
			e1.printStackTrace();
		}
	}

	private JSONObject getJSON() throws IOException, JSONException,
			AccountNotFillException {
		JSONObject dataJSON = new JSONObject();

		AccountDAO accountDAO = AccountDAO.getInstance(a);
		Account account = accountDAO.load();

		dataJSON.put("study", study.toJSON());
		dataJSON.put("student", account.toJSON());

		return dataJSON;
	}

	private void postJSON(JSONObject dataJSON) throws IOException {
		String data = dataJSON.toString();
		String requestBody = new String(data.getBytes(), "ISO-8859-1");
		int lngth = requestBody.length();

		URL url = new URL(API_URL_APPLY_STUDY);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("charset", "UTF-8");
		con.setRequestProperty("Content-Length", ("" + lngth));
		con.setUseCaches(false);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestBody);
		wr.flush();
		wr.close();
		con.getResponseCode();
		con.disconnect();
	}

	private void sendToast(final int ressourceString) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(a, ressourceString, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void resetButton() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				a.updateButton(true, R.string.apply);
			}
		});
	}
}
