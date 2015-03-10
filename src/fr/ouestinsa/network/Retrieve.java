package fr.ouestinsa.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class Retrieve implements Runnable {
	public static final String API_URL = "http://api.ouest-insa.fr/";
	public static final String API_URL_GET_STUDIES = API_URL + "study";
	public static final String API_URL_GET_DETAILS = API_URL + "detail";

	private String url;
	private Object result = null;

	public Retrieve(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		JSONArray json;
		try {
			json = readJsonFromUrl(url);
			result = getFromJSON(json);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	abstract protected Object getFromJSON(JSONArray json);

	public Object getResult() {
		return result;
	}
}
