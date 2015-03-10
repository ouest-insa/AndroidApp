package fr.ouestinsa.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import fr.ouestinsa.db.DAOFactory;
import fr.ouestinsa.db.SQLiteDAOFactory;
import fr.ouestinsa.db.properties.AccountDAO;
import fr.ouestinsa.exception.AccountNotFillException;
import fr.ouestinsa.object.Account;
import fr.ouestinsa.ui.activity.DetailsActivity;

public class ApplyStudy extends AsyncTask<DetailsActivity, Void, Exception> {
	public static final String API_URL_APPLY_STUDY = Retrieve.API_URL + "study";

	@Override
	protected Exception doInBackground(DetailsActivity... activity) {
		Exception e = null;
		try {
			JSONObject dataJSON = getJSON(activity[0]);
			postJSON(dataJSON);
		} catch (IOException e1) {
			e = e1;
		} catch (JSONException e1) {
			e = e1;
		} catch (AccountNotFillException e1) {
			e = e1;
		}
		return e;
	}

	private JSONObject getJSON(DetailsActivity activity) throws IOException,
			JSONException, AccountNotFillException {
		JSONObject dataJSON = new JSONObject();

		DAOFactory factoryPROP = SQLiteDAOFactory
				.getFactory(DAOFactory.PROPERTIES);
		AccountDAO accountDAO = factoryPROP.getAccountDAO(activity);
		accountDAO.open();
		Account account = null;
		account = accountDAO.load();
		dataJSON.put("study", activity.getCurrentStudy().toJSON());
		dataJSON.put("student", account.toJSON());
		accountDAO.close();

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
}
