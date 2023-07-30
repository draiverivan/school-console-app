package ua.foxminded.javaspring.school_console_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.foxminded.javaspring.school_console_app.services.SchoolDatabaseInteractionService;

public class SchoolDatabaseInteractionServiceTest {

	private SchoolDatabaseInteractionService service;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement statement;
	@Mock
	private ResultSet resultSet;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		service = new SchoolDatabaseInteractionService();
	}

	@Test
	public void shouldReturnCourseIdByName() throws SQLException {
		String courseName = "Math";
		int courseId = 10;

		when(connection.prepareStatement(any(String.class))).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(any(String.class))).thenReturn(courseId);

		int resultCourseId = service.getCourseIdByName(connection, courseName);

		assertEquals(courseId, resultCourseId);
	}

	@Test
	public void shouldReturnNegativeWhenCourseNotFound() throws SQLException {
		String courseName = "Unknown";

		when(connection.prepareStatement(any(String.class))).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		int resultCourseId = service.getCourseIdByName(connection, courseName);

		assertEquals(-1, resultCourseId);
	}

	@Test
	public void shouldReturnTrueWhenStudentExists() throws SQLException {
		int studentId = 5;

		when(connection.prepareStatement(any(String.class))).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(anyInt())).thenReturn(1);

		boolean studentExists = service.studentExists(connection, studentId);

		assertTrue(studentExists);
	}

	@Test
	public void shouldReturnFalseWhenStudentDoesNotExist() throws SQLException {
		int studentId = 999;

		when(connection.prepareStatement(any(String.class))).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(anyInt())).thenReturn(0);

		boolean studentExists = service.studentExists(connection, studentId);

		assertFalse(studentExists);
	}
}
