package fr.ouest_insa.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class RetrieveDetails extends Retrieve<JSONObject> {
	public RetrieveDetails(String url) {
		super(url);
	}

	@Override
	protected String getFromJSON(JSONObject json) {
		try {
			return json.getString("summary");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return new JSONObject(jsonText);
		} finally {
			is.close();
		}
	}
}
