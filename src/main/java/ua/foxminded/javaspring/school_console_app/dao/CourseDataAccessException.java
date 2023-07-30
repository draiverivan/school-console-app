package ua.foxminded.javaspring.school_console_app.dao;

public class CourseDataAccessException extends RuntimeException {
    public CourseDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}