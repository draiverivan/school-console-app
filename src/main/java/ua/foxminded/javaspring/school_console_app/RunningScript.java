package ua.foxminded.javaspring.school_console_app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunningScript {

	private static final Logger logger = LoggerFactory.getLogger(RunningScript.class.getName());

	public static void main(String args[]) throws Exception {
		String url = "jdbc:postgresql://localhost:5432/school";
		String username = "postgres";
		String password = "1234";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			logger.info("Connected to database.");

			ScriptRunner sr = new ScriptRunner(connection);
			Reader reader = new BufferedReader(new FileReader("school.sql"));
			sr.runScript(reader);

			GenerateData generateData = new GenerateData();
			List<Group> groupsList = generateData.generateGroups();
			List<Course> coursesList = generateData.generateCourses();
			List<Student> studentsList = generateData.generateStudents();

			InsertDataInDbSchool insertDataInDbSchool = new InsertDataInDbSchool();
			insertDataInDbSchool.insertGroups(connection, groupsList);
			insertDataInDbSchool.insertCourses(connection, coursesList);
			insertDataInDbSchool.insertStudents(connection, studentsList);
			insertDataInDbSchool.insertCoursesStudents(connection);

			logger.info("Test data generated successfully.");

			runConsoleMenu(connection);
		} catch (SQLException e) {
			logger.error("An error occurred: {}", e.getMessage(), e);
		}
	}

	private static void runConsoleMenu(Connection connection) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		String choice = "";
		while (!choice.equalsIgnoreCase("exit")) {
			displayMenu();
			choice = scanner.nextLine();
			switch (choice) {
			case "a":
				findGroupsWithLessOrEqualStudents(connection);
				break;
			case "b":
				findStudentsByCourseName(connection);
				break;
			case "c":
				addNewStudent(connection);
				break;
			case "d":
				deleteStudentById(connection);
				break;
			case "e":
				addStudentToCourse(connection);
				break;
			case "f":
				removeStudentFromCourse(connection);
				break;
			case "exit":
				logger.info("Exiting the program.");
				break;
			default:
				logger.info("Invalid choice. Please try again.");
				break;
			}
		}
		scanner.close();
	}

	private static void displayMenu() {
		logger.info("======= Console Menu =======");
		logger.info("a. Find all groups with less or equal students’ number");
		logger.info("b. Find all students related to the course with the given name");
		logger.info("c. Add a new student");
		logger.info("d. Delete a student by the STUDENT_ID");
		logger.info("e. Add a student to the course (from a list)");
		logger.info("f. Remove the student from one of their courses");
		logger.info("exit. Exit the program");
		logger.info("============================");
		logger.info("Enter your choice:");
	}

	private static void findGroupsWithLessOrEqualStudents(Connection connection) throws SQLException {
		// Write your SQL query to find all groups with less or equal students' number
		String sql = "SELECT g.group_name, COUNT(s.student_id) AS num_students " + "FROM school.groups g "
				+ "LEFT JOIN school.students s ON g.group_id = s.group_id " + "GROUP BY g.group_name "
				+ "HAVING COUNT(s.student_id) <= ?";

		// Prompt the user for the maximum number of students
		logger.info("Enter the maximum number of students:");
		Scanner scanner = new Scanner(System.in);
		int maxStudents = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, maxStudents);
			ResultSet resultSet = statement.executeQuery();
			logger.info("Groups with less or equal students' number:");
			while (resultSet.next()) {
				String groupName = resultSet.getString("group_name");
				int numStudents = resultSet.getInt("num_students");
				logger.info("Group: {} | Number of Students: {}", groupName, numStudents);
			}
		}
	}

	private static void findStudentsByCourseName(Connection connection) throws SQLException {
		// Write your SQL query to find all students related to the course with the
		// given name
		String sql = "SELECT s.first_name, s.last_name " + "FROM school.students s "
				+ "JOIN school.course_student cs ON s.student_id = cs.student_id "
				+ "JOIN school.courses c ON cs.course_id = c.course_id " + "WHERE c.course_name = ?";

		// Prompt the user for the course name
		logger.info("Enter the course name:");
		Scanner scanner = new Scanner(System.in);
		String courseName = scanner.nextLine();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, courseName);
			ResultSet resultSet = statement.executeQuery();
			logger.info("Students related to the course '{}':", courseName);
			while (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				logger.info("Student: {} {}", firstName, lastName);
			}
		}
	}

	private static void addNewStudent(Connection connection) throws SQLException {
		// Prompt the user for the student details
		logger.info("Enter the first name of the student:");
		Scanner scanner = new Scanner(System.in);
		String firstName = scanner.nextLine();

		logger.info("Enter the last name of the student:");
		String lastName = scanner.nextLine();

		// Write your SQL query to add a new student
		String sql = "INSERT INTO school.students (first_name, last_name) VALUES (?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.executeUpdate();
			logger.info("New student added successfully.");
		}
	}

	private static void deleteStudentById(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student to delete:");
		Scanner scanner = new Scanner(System.in);
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Write your SQL query to delete a student by the STUDENT_ID
		String sql = "DELETE FROM school.students WHERE student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
				logger.info("Student with ID {} deleted successfully.", studentId);
			} else {
				logger.info("No student found with ID {}.", studentId);
			}
		}
	}

	private static void addStudentToCourse(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student:");
		Scanner scanner = new Scanner(System.in);
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Prompt the user for the course ID
		logger.info("Enter the ID of the course:");
		int courseId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Write your SQL query to add a student to the course
		String sql = "INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, courseId);
			statement.setInt(2, studentId);
			statement.executeUpdate();
			logger.info("Student with ID {} added to course with ID {}.", studentId, courseId);
		}
	}

	private static void removeStudentFromCourse(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student:");
		Scanner scanner = new Scanner(System.in);
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Prompt the user for the course ID
		logger.info("Enter the ID of the course:");
		int courseId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Write your SQL query to remove the student from the course
		String sql = "DELETE FROM school.course_student WHERE course_id = ? AND student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, courseId);
			statement.setInt(2, studentId);
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
				logger.info("Student with ID {} removed from course with ID {}.", studentId, courseId);
			} else {
				logger.info("No enrollment found for student with ID {} in course with ID {}.", studentId, courseId);
			}
		}
	}
}
