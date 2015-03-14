package fr.ouestinsa.object;

import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import fr.ouestinsa.db.AccountDAO;
import fr.ouestinsa.exception.AccountNotFillException;
import fr.ouestinsa.exception.MailInsaException;

public class Account {
	private String firstname;
	private String lastname;
	private Department department;
	private String mailINSA;
	private URL viadeo;
	private URL linkedIn;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getMailINSA() {
		return mailINSA;
	}

	public void setMailINSA(String mailINSA) throws MailInsaException {
		if (!Pattern.compile("[a-zA-Z.-]+@insa-rennes\\.fr").matcher(mailINSA).find()) {
			throw new MailInsaException("Address INSA incorrect");
		}
		this.mailINSA = mailINSA;
	}

	public URL getViadeo() {
		return viadeo;
	}

	public void setViadeo(URL viadeo) {
		this.viadeo = viadeo;
	}

	public URL getLinkedIn() {
		return linkedIn;
	}

	public void setLinkedIn(URL linkedIn) {
		this.linkedIn = linkedIn;
	}

	public JSONObject toJSON() throws AccountNotFillException {
		JSONObject json = new JSONObject();
		try {
			json.put(AccountDAO.FIRSTNAME, firstname);
			json.put(AccountDAO.LASTNAME, lastname);
			json.put(AccountDAO.DEPARTMENT, department);
			json.put(AccountDAO.MAIL_INSA, mailINSA);
			json.put(AccountDAO.VIADEO, viadeo);
			json.put(AccountDAO.LINKEDIN, linkedIn);
		} catch (JSONException e) {
			throw new AccountNotFillException("Données de profil non remplies");
		}
		return json;
	}
}
