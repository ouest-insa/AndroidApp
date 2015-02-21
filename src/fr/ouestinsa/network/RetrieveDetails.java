package fr.ouestinsa.network;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.ouestinsa.object.Study;

public class RetrieveDetails extends Retrieve {
	@Override
	protected ArrayList<Study> doInBackground(String... url) {
		ArrayList<Study> studies = new ArrayList<Study>();

		try {
			JSONArray json = readJsonFromUrl(url[0]);

			getDetailsFromJSONObject(json.getJSONObject(0));
		} catch (IOException e) {
			studies = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return studies;
	}

	private Study getDetailsFromJSONObject(JSONObject jsonObject) {
		// TODO : Get details of study
		return null;
	}
}
