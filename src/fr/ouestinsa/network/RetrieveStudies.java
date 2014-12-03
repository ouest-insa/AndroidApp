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
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return studies;
	}

	private static Study getStudyFromJSONObject(JSONObject obj)
			throws JSONException {
		Study study = new Study();

		study.setId(obj.getInt(StudyDAO.ID));
		study.setName(obj.getString(StudyDAO.NAME));
		study.setDescription(obj.getString(StudyDAO.DESCRIPTION));
		study.setLogo(obj.getString(StudyDAO.LOGO));
		study.setDepartment(obj.getString(StudyDAO.DEPARTMENT));
		study.setSkill(obj.getString(StudyDAO.SKILL));
		study.setCreated_date(obj.getInt(StudyDAO.CREATED_DATE));
		study.setStarting_date(obj.getInt(StudyDAO.STARTING_DATE));
		study.setEnding_date(obj.getInt(StudyDAO.ENDING_DATE));
		study.setStudent_number(obj.getInt(StudyDAO.STUDENT_NUMBER));
		study.setActive((obj.getInt(StudyDAO.ACTIVE) >= 1 ? true : false));

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
