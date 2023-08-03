package ua.foxminded.javaspring.school_console_app.exceptions;

public class GroupDataAccessException extends RuntimeException {
    public GroupDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
