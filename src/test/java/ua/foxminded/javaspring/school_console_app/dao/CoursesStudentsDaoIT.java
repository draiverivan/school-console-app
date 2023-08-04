package ua.foxminded.javaspring.school_console_app.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.javaspring.school_console_app.model.Course;
import ua.foxminded.javaspring.school_console_app.model.Group;
import ua.foxminded.javaspring.school_console_app.model.Student;

@Testcontainers
public class CoursesStudentsDaoIT {

	private static final Logger logger = LoggerFactory.getLogger(CoursesStudentsDaoIT.class.getName());

	@Container
	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("school").withUsername("testuser").withPassword("testpassword");

	private CourseStudentRelationDao coursesStudentsDao;
	private Connection connection;
	private CourseDao courseDao;
	private StudentDao studentDao;
	private GroupDao groupDao;
	private static final String ERROR = "An error occurred: {}";

	@BeforeAll
	public static void setUpClass() {
		// Initialize the database schema and table
		try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
				postgresContainer.getUsername(), postgresContainer.getPassword());

				Statement statement = connection.createStatement()) {

			// Create the schema
			statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS school");
			// Create the groups table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.groups (" + "group_id SERIAL PRIMARY KEY,"
					+ "group_name VARCHAR(50) UNIQUE NOT NULL" + ")");
			// Create the courses table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.courses (" + "course_id SERIAL PRIMARY KEY,"
					+ "course_name VARCHAR(50) UNIQUE NOT NULL," + "course_description VARCHAR(250) NOT NULL" + ")");
			// Create the students table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.students (" + "student_id SERIAL PRIMARY KEY,"
					+ "group_id INTEGER," + "FOREIGN KEY (group_id) REFERENCES school.groups(group_id),"
					+ "first_name VARCHAR(50) NOT NULL," + "last_name VARCHAR(50) NOT NULL" + ")");
			// Create the courses_students table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.course_student ("
					+ "junction_id SERIAL PRIMARY KEY," + "course_id INTEGER," + "student_id INTEGER,"
					+ "FOREIGN KEY (course_id) REFERENCES school.courses(course_id) ON DELETE CASCADE,"
					+ "FOREIGN KEY (student_id) REFERENCES school.students(student_id) ON DELETE CASCADE,"
					+ "UNIQUE (course_id, student_id)" + ")");
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
		courseDao = new CourseDao();
		studentDao = new StudentDao();
		groupDao = new GroupDao();
		coursesStudentsDao = new CourseStudentRelationDao(courseDao, studentDao);
		connection.setAutoCommit(false);

	}

	@AfterEach
	public void tearDown() throws SQLException {
		dropTables(connection);
		connection.rollback();
		connection.close();
	}

	@Test
	void insertCoursesStudents_shouldInsertCoursesStudentsWhenDataProvided() {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1, "Mathematics", "Advanced Math"));
		courses.add(new Course(2, "English", "Language and Literature"));
		courseDao.insertCourses(connection, courses);

		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "Group 1"));
		groups.add(new Group(2, "Group 2"));
		groupDao.insertGroups(connection, groups);

		List<Student> students = new ArrayList<>();
		students.add(new Student(1, null, "John", "Doe"));
		students.add(new Student(2, null, "Jane", "Doe"));
		studentDao.insertStudents(connection, students);

		assertDoesNotThrow(() -> coursesStudentsDao.insertCoursesStudents(connection));
		assertTrue(verifyInsertion(), "CoursesStudents insertion was not successful");
	}

	private boolean verifyInsertion() {
		String sql = "SELECT COUNT(*) FROM school.course_student";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
			if (resultSet.next()) {
				return resultSet.getInt(1) > 0;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	private void dropTables(Connection connection) {
		try (Statement statement = connection.createStatement()) {

			statement.executeUpdate("DROP SCHEMA IF EXISTS school cascade;" + "DROP SCHEMA IF EXISTS school cascade");

		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}
}
