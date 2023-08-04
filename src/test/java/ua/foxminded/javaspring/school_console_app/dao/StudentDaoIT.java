package ua.foxminded.javaspring.school_console_app.dao;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.javaspring.school_console_app.model.Group;
import ua.foxminded.javaspring.school_console_app.model.Student;

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
public class StudentDaoIT {

	private static final Logger logger = LoggerFactory.getLogger(StudentDaoIT.class.getName());

	@Container
	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("school").withUsername("testuser").withPassword("testpassword");

	private Connection connection;
	private StudentDao studentDao;
	private GroupDao groupDao;
	private static final String ERROR = "An error occurred: {}";

	@BeforeAll
	public static void setUpClass() {
		try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
				postgresContainer.getUsername(), postgresContainer.getPassword());

				Statement statement = connection.createStatement()) {

			// Create the schema
			statement.executeUpdate("DROP SCHEMA IF EXISTS school cascade;" + "CREATE SCHEMA IF NOT EXISTS school");

			// Create the groups table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.groups (" + "group_id SERIAL PRIMARY KEY,"
					+ "group_name VARCHAR(50) UNIQUE NOT NULL" + ")");

			// Create the students table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.students (" + "student_id SERIAL PRIMARY KEY,"
					+ "group_id INTEGER," + "FOREIGN KEY (group_id) REFERENCES school.groups(group_id),"
					+ "first_name VARCHAR(50) NOT NULL," + "last_name VARCHAR(50) NOT NULL" + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void cleanUpStudentsTable() {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM school.students");
			connection.commit();
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void cleanUpGroupsTable() {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM school.groups");
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
		studentDao = new StudentDao();
		groupDao = new GroupDao();

		cleanUpStudentsTable();
		cleanUpGroupsTable();
	}

	@AfterEach
	public void tearDown() throws SQLException {
		connection.rollback();
		connection.close();
	}

	@Test
	void insertStudents_shouldInsertStudentsWhenGivenStudentList() {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "Group 1"));
		groups.add(new Group(2, "Group 2"));
		groupDao.insertGroups(connection, groups);

		List<Student> students = new ArrayList<>();
		students.add(new Student(1, null, "John", "Doe"));
		students.add(new Student(2, null, "Jane", "Doe"));
		studentDao.insertStudents(connection, students);

		List<Student> insertedStudents = fetchStudentsFromDatabase();
		assertEquals(students.size(), insertedStudents.size(), "Number of inserted students should match");

		for (int i = 0; i < students.size(); i++) {
			assertEquals(students.get(i), insertedStudents.get(i), "Inserted student details should match");
		}
	}

	@Test
	void getStudentIds_shouldReturnStudentIdsWhenStudentsAreInserted() {

		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "Group 1"));
		groups.add(new Group(2, "Group 2"));
		groupDao.insertGroups(connection, groups);

		List<Student> students = new ArrayList<>();
		students.add(new Student(1, "Group 1", "John", "Doe"));
		students.add(new Student(2, "Group 2", "Jane", "Doe"));
		studentDao.insertStudents(connection, students);

		List<Integer> studentIds = studentDao.getStudentIds(connection);
		assertEquals(students.size(), studentIds.size(), "Number of inserted student IDs should match");
	}

	private List<Student> fetchStudentsFromDatabase() {
		List<Student> students = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM school.students");
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				long studentId = resultSet.getLong("student_id");
				String groupIdStudent = resultSet.getString("group_id");
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				students.add(new Student(studentId, groupIdStudent, firstName, lastName));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
		return students;
	}

}
