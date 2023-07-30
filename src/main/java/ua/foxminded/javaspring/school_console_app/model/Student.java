package ua.foxminded.javaspring.school_console_app.model;

import java.util.Objects;

public class Student {

	private long id;
	private String groupIdStudent;
	private String firstName;
	private String lastName;

	public Student(long studentId, String groupIdStudent, String firstName, String lastName) {
		this.id = studentId;
		this.groupIdStudent = groupIdStudent;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupIdStudent() {
		return groupIdStudent;
	}

	public void setGroupIdStudent(String groupIdStudent) {
		this.groupIdStudent = groupIdStudent;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Student student = (Student) o;
		return Objects.equals(firstName, student.firstName)
				&& Objects.equals(lastName, student.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName);
	}
}
