package ua.foxminded.javaspring.school_console_app.exceptions;

public class CoursesStudentsDataAccessException extends RuntimeException {
    public CoursesStudentsDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
