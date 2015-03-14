package fr.ouestinsa.object;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ouestinsa.db.StudyDAO;

public class Study {
	private int id;
	private int jeh;
	private String name;
	private Status status;
	private String type;
	private int typeId;
	private String details;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJeh() {
		return jeh;
	}

	public void setJeh(int jeh) {
		this.jeh = jeh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(StudyDAO.ID, id);
		json.put(StudyDAO.JEH, jeh);
		json.put(StudyDAO.NAME, name);
		json.put(StudyDAO.STATUS, status);
		json.put(StudyDAO.TYPE, type);
		return json;
	}
}
