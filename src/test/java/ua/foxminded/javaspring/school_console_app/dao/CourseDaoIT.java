package ua.foxminded.javaspring.school_console_app.dao;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.school_console_app.model.Course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class CourseDaoIT {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseDaoIT.class.getName());

	@Container
	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("school").withUsername("testuser").withPassword("testpassword");

	private Connection connection;
	private CourseDao courseDao;
	private static final String ERROR = "An error occurred: {}";

	@BeforeAll
	public static void setUpClass() {
		// Initialize the database schema and table
		try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
				postgresContainer.getUsername(), postgresContainer.getPassword());

				Statement statement = connection.createStatement()) {

			// Create the schema
			statement.executeUpdate("DROP SCHEMA IF EXISTS school cascade;" + "CREATE SCHEMA IF NOT EXISTS school");

			// Create the courses table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.courses (" + "course_id SERIAL PRIMARY KEY,"
					+ "course_name VARCHAR(50) UNIQUE NOT NULL," + "course_description VARCHAR(250) NOT NULL" + ")");
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	// Method to delete all records from the courses table
	private void cleanUpCoursesTable() {
	    try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM school.courses")) {
	        preparedStatement.executeUpdate();
	        connection.commit();
	    } catch (SQLException e) {
	        logger.error(ERROR, e.getMessage(), e);
	    }
	}

	@BeforeEach
	public void setUp() throws SQLException {
		String jdbcUrl = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();

		connection = DriverManager.getConnection(jdbcUrl, username, password);
		connection.setAutoCommit(false);
		courseDao = new CourseDao();
		// Clean up the courses table before running each test
		cleanUpCoursesTable();
	}

	@AfterEach
	public void tearDown() throws SQLException {
		connection.rollback();
		connection.close();

	}

	@Test
	void insertCourses_shouldInsertCoursesWhenGivenCourseList() {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Mathematics", "Advanced Math"));
		courses.add(new Course(2, "English", "Language and Literature"));

		courseDao.insertCourses(connection, courses);

		// Fetch the inserted courses and verify their details
		List<Course> insertedCourses = fetchCoursesFromDatabase();
		assertEquals(courses.size(), insertedCourses.size(), "Number of inserted courses should match");

		for (int i = 0; i < courses.size(); i++) {
			assertEquals(courses.get(i), insertedCourses.get(i), "Inserted course details should match");
		}
	}

	@Test
	void getCourseIds_shouldReturnCourseIdsWhenCoursesAreInserted() {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Mathematics", "Advanced Math"));
		courses.add(new Course(2, "English", "Language and Literature"));

		// Insert the courses
		courseDao.insertCourses(connection, courses);

		// Fetch the inserted course IDs and verify
		List<Integer> courseIds = courseDao.getCourseIds(connection);
		assertEquals(courses.size(), courseIds.size(), "Number of inserted course IDs should match");

	}

	// Helper method to fetch courses from the database
	private List<Course> fetchCoursesFromDatabase() {
		List<Course> courses = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM school.courses");
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				long courseId = resultSet.getLong("course_id");
				String courseName = resultSet.getString("course_name");
				String courseDescription = resultSet.getString("course_description");
				courses.add(new Course(courseId, courseName, courseDescription));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
		return courses;
	}

}
