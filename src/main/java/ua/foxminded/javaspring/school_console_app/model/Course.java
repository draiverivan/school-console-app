package ua.foxminded.javaspring.school_console_app.model;

import java.util.Objects;

public class Course {

	private long id;
	private String name;
	private String description;

	public Course(long l, String name, String description) {
		this.id = l;
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Course course = (Course) o;
		return id == course.id && Objects.equals(name, course.name) && Objects.equals(description, course.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

}
