package fr.ouestinsa.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;
import fr.ouestinsa.R;
import fr.ouestinsa.db.AccountDAO;
import fr.ouestinsa.exception.AccountNotFillException;
import fr.ouestinsa.object.Account;
import fr.ouestinsa.object.Study;

public class ApplyStudy implements Runnable {
	public static final String API_URL_APPLY_STUDY = Retrieve.API_URL + "study";
	private Study study;
	private Activity a;
	private Handler mHandler;
	
	public ApplyStudy(Study study, Activity a, Handler mHandler) {
		this.study = study;
		this.a = a;
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		try {
			JSONObject dataJSON = getJSON();
			postJSON(dataJSON);
		} catch (IOException e1) {
			sendToast(R.string.error_apply);
			e1.printStackTrace();
		} catch (JSONException e1) {
			sendToast(R.string.error_apply);
			e1.printStackTrace();
		} catch (AccountNotFillException e1) {
			sendToast(R.string.error_account_not_fill);
			e1.printStackTrace();
		}
	}

	private JSONObject getJSON() throws IOException,
			JSONException, AccountNotFillException {
		JSONObject dataJSON = new JSONObject();
		
		AccountDAO accountDAO = AccountDAO.getInstance(a);
		Account account = accountDAO.load();
		
		dataJSON.put("study", study.toJSON());
		dataJSON.put("student", account.toJSON());

		return dataJSON;
	}

	private void postJSON(JSONObject dataJSON) throws IOException {
		URL url = new URL(API_URL_APPLY_STUDY);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("charset", "UTF-8");
		con.setRequestProperty("Content-Length",
				Integer.toString(dataJSON.toString().getBytes().length - 2));
		con.setUseCaches(false);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(dataJSON.toString());
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
}
