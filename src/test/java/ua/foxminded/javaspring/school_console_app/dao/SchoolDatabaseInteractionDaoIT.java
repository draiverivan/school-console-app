package ua.foxminded.javaspring.school_console_app.dao;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class SchoolDatabaseInteractionDaoIT {

	private static final Logger logger = LoggerFactory
			.getLogger(SchoolDatabaseInteractionDaoIT.class.getName());
	private static final String ERROR = "An error occurred: {}";

	@Container
	public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("school").withUsername("testuser").withPassword("testpassword")
			.withInitScript("init.sql"); // Initialize the database with init.sql file

	private Connection connection;
	private SchoolDatabaseInteractionDao schoolDatabaseInteractionDao;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		String jdbcUrl = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();

		connection = DriverManager.getConnection(jdbcUrl, username, password);
		connection.setAutoCommit(false);

		// initialize the script
		runInitScript(connection);

		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao();
	}

	private void runInitScript(Connection connection) throws SQLException, IOException {
		// read the script from resources folder
		InputStream is = getClass().getClassLoader().getResourceAsStream("init.sql");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		// join all lines of the script into a single string
		String script = reader.lines().collect(Collectors.joining("\n"));

		// execute the script
		try (Statement statement = connection.createStatement()) {
			statement.execute(script);
		}
	}

	@AfterEach
	public void tearDown() throws SQLException {
		if (connection != null) {
			connection.rollback();
			try (Statement statement = connection.createStatement()) {
				// Delete the test data after each test
				statement.executeUpdate("DROP SCHEMA IF EXISTS school CASCADE");
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
			connection.close();
		}
	}

	@Test
	void findGroupsWithLessOrEqualStudents_shouldFindGroupsWithLess10Stundets() {
		// Provide user input

		String simulatedUserInput = "10\n"; // User inputs 10
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);
		System.setIn(in);

		// Capture the console output
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Call the method
		try {
			schoolDatabaseInteractionDao.findGroupsWithLessOrEqualStudents(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Verify the results
		String output = outContent.toString();
		assertTrue(output.contains("Group: G1 | Number of Students: 10"),
				"Group G1 should be present with 10 students");
		assertTrue(output.contains("Group: G2 | Number of Students: 1"), "Group G2 should be present with 1 student");
		assertTrue(output.contains("Group: G3 | Number of Students: 1"), "Group G3 should be present with 1 student");
		assertTrue(output.contains("Group: G4 | Number of Students: 1"), "Group G4 should be present with 1 student");
		assertTrue(output.contains("Group: G5 | Number of Students: 2"), "Group G5 should be present with 2 students");
	}

	@Test
	void findStudentsByCourseName_shouldFindStudentsByMathematics() {
		// Provide user input
		String simulatedUserInput = "Mathematics\n"; // User inputs "Mathematics"
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Capture the console output
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		// Call the method
		try {
			schoolDatabaseInteractionDao.findStudentsByCourseName(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Verify the results
		String output = outContent.toString();
		assertTrue(output.contains("Student: John Doe"), "John Doe should be a student of the Mathematics course");
		// Add similar assertions for other expected students in the Mathematics course
	}

	@Test
	void addNewStudent_shouldAddJohnDoeStudent() {
		// Provide user input
		String simulatedUserInput = "John\nDoe\n"; // User inputs "John" as the first name and "Doe" as the last name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Call the method
		try {
			schoolDatabaseInteractionDao.addNewStudent(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Verify the results
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT first_name, last_name FROM school.students WHERE first_name = ? AND last_name = ?")) {
			statement.setString(1, "John");
			statement.setString(2, "Doe");
			ResultSet resultSet = statement.executeQuery();
			assertTrue(resultSet.next(), "The new student should be in the database");
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void deleteStudentById_shouldDeleteGregorSimStudent() {
		// Step 1: Add a student to the database
		String simulatedUserInput = "Gregor\nSim\n"; // User inputs "John" as the first name and "Doe" as the last name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Call the method to add a student
		int studentId = -1;
		try {
			schoolDatabaseInteractionDao.addNewStudent(connection);

			// Get the ID of the newly added student
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT student_id FROM school.students WHERE first_name = ? AND last_name = ?")) {
				statement.setString(1, "Gregor");
				statement.setString(2, "Sim");
				ResultSet resultSet = statement.executeQuery();
				assertTrue(resultSet.next(), "The new student should be in the database");
				studentId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Step 2: Delete the student
		simulatedUserInput = studentId + "\n"; // User inputs the student ID
		in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Call the method to delete the student
		try {
			schoolDatabaseInteractionDao.deleteStudentById(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Step 3: Verify the results
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT student_id FROM school.students WHERE student_id = ?")) {
			statement.setInt(1, studentId);
			ResultSet resultSet = statement.executeQuery();
			assertFalse(resultSet.next(), "The student should no longer be in the database");
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void getAvailableCoursesForStudent_shouldGetAvailableCoursesForTestStudent() {
		// Step 1: Add a student to the database
		String simulatedUserInput = "Mike\nWendy\n"; // User inputs "John" as the first name and "Doe" as the last name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Call the method to add a student
		int studentId = -1;
		try {
			schoolDatabaseInteractionDao.addNewStudent(connection);

			// Get the ID of the newly added student
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT student_id FROM school.students WHERE first_name = ? AND last_name = ?")) {
				statement.setString(1, "Mike");
				statement.setString(2, "Wendy");
				ResultSet resultSet = statement.executeQuery();
				assertTrue(resultSet.next(), "The new student should be in the database");
				studentId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Step 2: Assign the student to some courses
		try (PreparedStatement statement = connection
				.prepareStatement("INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?), (?, ?)")) {
			statement.setInt(1, 1);
			statement.setInt(2, studentId);
			statement.setInt(3, 2);
			statement.setInt(4, studentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Step 3: Call the method and verify the results
		try {
			List<String> availableCourses = schoolDatabaseInteractionDao.getAvailableCoursesForStudent(connection,
					studentId);
			assertFalse(availableCourses.contains("Mathematics"), "Course 1 should not be available");
			assertFalse(availableCourses.contains("Physics"), "Course 2 should not be available");
			assertTrue(availableCourses.contains("Chemistry"), "Course 3 should be available");
			assertTrue(availableCourses.contains("Biology"), "Course 4 should be available");
			// Add similar assertions for the rest of the courses
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void getCourseIdByName_shouldGetIdOfTestCourse() {
		// Step 1: Add a course to the database
		String simulatedUserInput = "Course Test\n"; // User inputs "Course Test" as the course name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		// Call the method to add a course
		int courseId = -1;
		try {
			String sql = "INSERT INTO school.courses (course_name) VALUES (?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, simulatedUserInput);
			statement.executeUpdate();

			// Get the ID of the newly added course
			try (PreparedStatement statement2 = connection
					.prepareStatement("SELECT course_id FROM school.courses WHERE course_name = ?")) {
				statement2.setString(1, "Course Test");
				ResultSet resultSet = statement2.executeQuery();
				assertTrue(resultSet.next(), "The new course should be in the database");
				courseId = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}

		// Step 2: Call the method and verify the result
		try {
			int resultCourseId = schoolDatabaseInteractionDao.getCourseIdByName(connection, "Course Test");
			assertEquals(courseId, resultCourseId, "The returned course ID should match the actual course ID");
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void addStudentToCourse_shouldAddTestStudentToTestCourse() {
		// Step 1: Add a student and a course to the database
		String simulatedUserInput = "Test Student\nTest Lastname\nTest Course\n";
		// User inputs "Test Student" as the student name, "Test Lastname" as the
		// student lastname and "Test Course" as the course name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		int studentId = -1;
		int courseId = -1;

		try {
			schoolDatabaseInteractionDao.addNewStudent(connection);
			String sql = "INSERT INTO school.courses (course_name) VALUES (?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, "Test Course");
			statement.executeUpdate();

			// Get the ID of the newly added student
			try (PreparedStatement statement2 = connection.prepareStatement(
					"SELECT student_id FROM school.students WHERE first_name = ? AND last_name = ?")) {
				statement2.setString(1, "Test Student");
				statement2.setString(2, "Test Lastname");
				ResultSet resultSet = statement2.executeQuery();
				assertTrue(resultSet.next(), "The new student should be in the database");
				studentId = resultSet.getInt(1);
			}

			// Get the ID of the newly added course
			try (PreparedStatement statement3 = connection
					.prepareStatement("SELECT course_id FROM school.courses WHERE course_name = ?")) {
				statement3.setString(1, "Test Course");
				ResultSet resultSet = statement3.executeQuery();
				assertTrue(resultSet.next(), "The new course should be in the database");
				courseId = resultSet.getInt(1);
			}

			// Step 2: Call the method to add the student to the course
			schoolDatabaseInteractionDao.addStudentToCourse(connection, studentId, courseId);

			// Step 3: Verify that the student was added to the course
			try (PreparedStatement statement4 = connection
					.prepareStatement("SELECT * FROM school.course_student WHERE student_id = ? AND course_id = ?")) {
				statement4.setInt(1, studentId);
				statement4.setInt(2, courseId);
				ResultSet resultSet = statement4.executeQuery();
				assertTrue(resultSet.next(), "The student should be added to the course");
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void removeStudentFromCourse_shouldRemoveStudentFromMathematics() {
		String simulatedUserInput = "1\nMathematics\n";
		// User inputs "1" as the student ID and "Mathematics" as the course name
		InputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
		System.setIn(in);

		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao(in);

		int studentId = -1;
		int courseId = -1;

		try {
			// Get the ID of the student
			try (PreparedStatement statement = connection
					.prepareStatement("SELECT student_id FROM school.students WHERE student_id = ?")) {
				statement.setInt(1, 1); // Set student ID
				ResultSet resultSet = statement.executeQuery();
				assertTrue(resultSet.next(), "The student should be in the database");
				studentId = resultSet.getInt(1);
			}

			// Get the ID of the course
			try (PreparedStatement statement = connection
					.prepareStatement("SELECT course_id FROM school.courses WHERE course_name = ?")) {
				statement.setString(1, "Mathematics"); // Set course name
				ResultSet resultSet = statement.executeQuery();
				assertTrue(resultSet.next(), "The course should be in the database");
				courseId = resultSet.getInt(1);
			}

			// Verify that the student is currently enrolled in the course
			try (PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM school.course_student WHERE student_id = ? AND course_id = ?")) {
				statement.setInt(1, studentId);
				statement.setInt(2, courseId);
				ResultSet resultSet = statement.executeQuery();
				assertTrue(resultSet.next(), "The student should be enrolled in the course");
			}

			// Call the method to remove the student from the course
			schoolDatabaseInteractionDao.removeStudentFromCourse(connection);

			// Verify that the student was removed from the course
			try (PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM school.course_student WHERE student_id = ? AND course_id = ?")) {
				statement.setInt(1, studentId);
				statement.setInt(2, courseId);
				ResultSet resultSet = statement.executeQuery();
				assertFalse(resultSet.next(), "The student should be removed from the course");
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void getEnrolledCoursesForStudent_shouldGetCoursesForStudentWithId1() {
		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao();

		List<String> expectedCourses = Arrays.asList("Mathematics");

		try {
			// Call the method to get the enrolled courses for the student with id 1
			List<String> enrolledCourses = schoolDatabaseInteractionDao.getEnrolledCoursesForStudent(connection, 1);

			// Verify that the correct courses are returned
			assertEquals(expectedCourses, enrolledCourses, "The student should be enrolled in the Mathematics course");

		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@Test
	void studentExistsTest_shouldVerifyIfStudentsWithIdExists() {
		// Create a new instance of SchoolDatabaseInteractionDao
		schoolDatabaseInteractionDao = new SchoolDatabaseInteractionDao();

		try {
			// Call the method for an existing student
			boolean exists = schoolDatabaseInteractionDao.studentExists(connection, 1);
			// Verify that the student exists
			assertTrue(exists, "The student with ID 1 should exist");

			// Call the method for a non-existing student
			exists = schoolDatabaseInteractionDao.studentExists(connection, 9999);
			// Verify that the student does not exist
			assertFalse(exists, "The student with ID 9999 should not exist");

		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

}
