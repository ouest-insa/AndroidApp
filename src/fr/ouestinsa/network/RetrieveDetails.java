package fr.ouestinsa.network;

import org.json.JSONArray;

import fr.ouestinsa.object.Study;

public class RetrieveDetails extends Retrieve {
	public RetrieveDetails(String url) {
		super(url);
	}

	protected Study getFromJSON(JSONArray json) {
		// TODO : Get details of study
		return null;
	}
}
