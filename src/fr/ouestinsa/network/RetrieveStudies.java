package fr.ouestinsa.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import fr.ouestinsa.db.StudyDAO;
import fr.ouestinsa.object.Study;

public class RetrieveStudies extends Retrieve<JSONArray> {

	public RetrieveStudies(String url) {
		super(url);
	}

	@Override
	protected Object getFromJSON(JSONArray json) {
		ArrayList<Study> studies = new ArrayList<Study>();

		try {
			for (int i = 0; i < json.length(); i++) {
				studies.add(parseJSON(json.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.e("CC", e.getMessage());
			e.printStackTrace();
		}

		return studies;
	}

	@Override
	protected JSONArray readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return new JSONArray(jsonText);
		} finally {
			is.close();
		}
	}

	@SuppressLint("DefaultLocale")
	private Study parseJSON(JSONObject jsonObject) throws JSONException {
		Study study = new Study();

		study.setId(jsonObject.getInt(StudyDAO.ID));
		study.setReference(jsonObject.getInt(StudyDAO.REFERENCE));
		try {
			study.setJeh(jsonObject.getInt(StudyDAO.JEH));
		} catch (JSONException e) {
			study.setJeh(-1);
		}
		study.setName(jsonObject.getString(StudyDAO.NAME));
		study.setStatus(fr.ouestinsa.object.Status.valueOf(jsonObject
				.getString(StudyDAO.STATUS).toUpperCase()));
		study.setType(jsonObject.getString(StudyDAO.TYPE));
		study.setTypeId(jsonObject.getInt(StudyDAO.TYPE_ID));

		return study;
	}
}
