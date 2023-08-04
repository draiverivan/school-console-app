package ua.foxminded.javaspring.school_console_app.exceptions;

public class CourseStudentRelationDataAccessException extends RuntimeException {
    public CourseStudentRelationDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
