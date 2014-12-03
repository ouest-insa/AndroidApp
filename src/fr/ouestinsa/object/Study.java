package fr.ouestinsa.object;

public class Study {
	private int id;
	private String name;
	private String description;
	private String logo;
	private String department;
	private String skill;
	private long created_date;
	private long starting_date;
	private long ending_date;
	private int student_number;
	private boolean active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public long getCreated_date() {
		return created_date;
	}

	public void setCreated_date(long created_date) {
		this.created_date = created_date;
	}

	public long getStarting_date() {
		return starting_date;
	}

	public void setStarting_date(long starting_date) {
		this.starting_date = starting_date;
	}

	public long getEnding_date() {
		return ending_date;
	}

	public void setEnding_date(long ending_date) {
		this.ending_date = ending_date;
	}

	public int getStudent_number() {
		return student_number;
	}

	public void setStudent_number(int student_number) {
		this.student_number = student_number;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
