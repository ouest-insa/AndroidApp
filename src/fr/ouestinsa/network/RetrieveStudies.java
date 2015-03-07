package fr.ouestinsa.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import fr.ouestinsa.db.sqlite.StudyDAO;
import fr.ouestinsa.object.Study;

public class RetrieveStudies extends Retrieve {

	public RetrieveStudies(String url) {
		super(url);
	}

	@SuppressLint("DefaultLocale")
	protected Study getFromJSONObject(JSONObject obj) throws JSONException {
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
		study.setTypeId(obj.getInt(StudyDAO.TYPE_ID));

		return study;
	}
}
