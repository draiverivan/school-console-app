package ua.foxminded.javaspring.school_console_app;

public class Course {

	private String name;
	private String decriptionCourse;

	public String getNameCourse() {
		return name;
	}

	public void setNameCourse(String nameCourse) {
		this.name = nameCourse;
	}

	public String getDecriptionCourse() {
		return decriptionCourse;
	}

	public void setDecriptionCourse(String decriptionCourse) {
		this.decriptionCourse = decriptionCourse;
	}

	public Course(String nameCourse, String decriptionCourse) {
		this.name = nameCourse;
		this.decriptionCourse = decriptionCourse;
	}
}
