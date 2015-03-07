package fr.ouestinsa.network;

import org.json.JSONObject;

import fr.ouestinsa.object.Study;

public class RetrieveDetails extends Retrieve {
	public RetrieveDetails(String url) {
		super(url);
	}

	protected Study getFromJSONObject(JSONObject jsonObject) {
		// TODO : Get details of study
		return null;
	}
}
