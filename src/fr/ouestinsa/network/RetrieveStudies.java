package fr.ouestinsa.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.object.Study;

public class RetrieveStudies extends Retrieve {

	public RetrieveStudies(String url) {
		super(url);
	}

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

	@SuppressLint("DefaultLocale")
	private Study parseJSON(JSONObject jsonObject) throws JSONException {
		Study study = new Study();

		study.setId(jsonObject.getInt(StudyDAO.ID));
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
