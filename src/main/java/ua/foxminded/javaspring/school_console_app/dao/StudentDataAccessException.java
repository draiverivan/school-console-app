package ua.foxminded.javaspring.school_console_app.dao;

public class StudentDataAccessException extends RuntimeException {
	public StudentDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
