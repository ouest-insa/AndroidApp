package fr.ouestinsa.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.object.Study;

public class RetrieveStudies extends AsyncTask<String, Void, ArrayList<Study>> {
	@Override
	protected ArrayList<Study> doInBackground(String... url) {
		ArrayList<Study> studies = new ArrayList<Study>();

		try {
			JSONArray json = readJsonFromUrl(url[0]);

			int i = 0;
			while (!json.isNull(i)) {
				studies.add(getStudyFromJSONObject(json.getJSONObject(i)));
				i++;
			}
		} catch (IOException e) {
			studies = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return studies;
	}

	@SuppressLint("DefaultLocale")
	private static Study getStudyFromJSONObject(JSONObject obj)
			throws JSONException {
		Study study = new Study();

		study.setId(obj.getInt(StudyDAO.ID));
		try {
			study.setJeh(obj.getInt(StudyDAO.JEH));
		} catch (JSONException e) {
			study.setJeh(-1);
		}
		study.setName(obj.getString(StudyDAO.NAME));
		study.setStatus(fr.ouestinsa.object.Status.valueOf(obj.getString(
				StudyDAO.STATUS).toUpperCase()));
		study.setType(obj.getString(StudyDAO.TYPE));

		return study;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
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
}
