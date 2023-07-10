package ua.foxminded.javaspring.school_console_app;

public class Course {
	
	private String nameCourse;
	private String decriptionCourse;
	
	public String getNameCourse() {
		return nameCourse;
	}

	public void setNameCourse(String nameCourse) {
		this.nameCourse = nameCourse;
	}

	public String getDecriptionCourse() {
		return decriptionCourse;
	}

	public void setDecriptionCourse(String decriptionCourse) {
		this.decriptionCourse = decriptionCourse;
	}

	public Course(String nameCourse, String decriptionCourse) {
		super();
		this.nameCourse = nameCourse;
		this.decriptionCourse = decriptionCourse;
	}
	
	

}
