package ua.foxminded.javaspring.school_console_app;

public class Student {

	private String groupIdStudent;
	private String firstNameStudent;
	private String lastNameStudent;

	public Student(String firstNameStudent, String lastNameStudent) {
		super();
		this.firstNameStudent = firstNameStudent;
		this.lastNameStudent = lastNameStudent;
	}

	public Student(String groupIdStudent, String firstNameStudent, String lastNameStudent) {
		super();
		this.groupIdStudent = groupIdStudent;
		this.firstNameStudent = firstNameStudent;
		this.lastNameStudent = lastNameStudent;
	}

	public String getGroupIdStudent() {
		return groupIdStudent;
	}

	public void setGroupIdStudent(String groupIdStudent) {
		this.groupIdStudent = groupIdStudent;
	}

	public String getFirstNameStudent() {
		return firstNameStudent;
	}

	public void setFirstNameStudent(String firstNameStudent) {
		this.firstNameStudent = firstNameStudent;
	}

	public String getLastNameStudent() {
		return lastNameStudent;
	}

	public void setLastNameStudent(String lastNameStudent) {
		this.lastNameStudent = lastNameStudent;
	}

}
