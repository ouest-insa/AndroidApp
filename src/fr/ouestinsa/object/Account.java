package fr.ouestinsa.object;

import java.net.URL;

public class Account {
	private String firstname;
	private String lastname;
	private String department;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMailINSA() {
		return mailINSA;
	}

	public void setMailINSA(String mailINSA) {
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
}
