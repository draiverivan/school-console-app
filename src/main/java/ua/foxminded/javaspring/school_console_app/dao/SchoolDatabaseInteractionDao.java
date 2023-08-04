package ua.foxminded.javaspring.school_console_app.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.services.GroupService;

public class SchoolDatabaseInteractionDao {

	private static final Logger logger = LoggerFactory.getLogger(SchoolDatabaseInteractionDao.class.getName());
	private Scanner scanner;

	public SchoolDatabaseInteractionDao() {
		this(System.in);
	}

	public SchoolDatabaseInteractionDao(InputStream in) {
		this.scanner = new Scanner(in);
	}

	public void findGroupsWithLessOrEqualStudents(Connection connection) throws SQLException {
		// Write your SQL query to find all groups with less or equal students' number
		String sql = "SELECT g.group_name, COUNT(s.student_id) AS num_students " + "FROM school.groups g "
				+ "LEFT JOIN school.students s ON g.group_id = s.group_id " + "GROUP BY g.group_name "
				+ "HAVING COUNT(s.student_id) <= ?";

		// Prompt the user for the maximum number of students
		logger.info("Enter the maximum number of students:");
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

	public void findStudentsByCourseName(Connection connection) throws SQLException {
		// Write your SQL query to find all students related to the course with the
		// given name
		String sql = "SELECT s.first_name, s.last_name " + "FROM school.students s "
				+ "JOIN school.course_student cs ON s.student_id = cs.student_id "
				+ "JOIN school.courses c ON cs.course_id = c.course_id " + "WHERE c.course_name = ?";

		// Prompt the user for the course name
		logger.info("Enter the course name:");
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

	public void addNewStudent(Connection connection) throws SQLException {
		// Prompt the user for the student details
		logger.info("Enter the first name of the student:");
		String firstName = scanner.nextLine();

		logger.info("Enter the last name of the student:");
		String lastName = scanner.nextLine();

		// Write your SQL query to add a new student
		String sql = "INSERT INTO school.students (first_name, last_name, group_id) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			GroupDao groupDao = new GroupDao();
			GroupService groupService = new GroupService(groupDao);
			List<Integer> groupIds = groupDao.getGroupIds(connection);
			int groupId = groupService.generateRandomElement(groupIds);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setInt(3, groupId);
			statement.executeUpdate();
			connection.commit(); // Commit the transaction
			logger.info("New student added successfully.");
		}
	}

	public void deleteStudentById(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student to delete:");
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Write your SQL query to delete a student by the STUDENT_ID
		String sql = "DELETE FROM school.students WHERE student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			int rowsAffected = statement.executeUpdate();
			connection.commit(); // Commit the transaction
			if (rowsAffected > 0) {
				logger.info("Student with ID {} deleted successfully.", studentId);
			} else {
				logger.info("No student found with ID {}.", studentId);
			}
		}
	}

	public List<String> getAvailableCoursesForStudent(Connection connection, int studentId) throws SQLException {
		// Write your SQL query to get the available courses for the student
		String sql = "SELECT c.course_name " + "FROM school.courses c "
				+ "WHERE c.course_id NOT IN (SELECT cs.course_id FROM school.course_student cs WHERE cs.student_id = ?)";

		List<String> availableCourses = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String courseName = resultSet.getString("course_name");
				availableCourses.add(courseName);
			}
		}

		return availableCourses;
	}

	public int getCourseIdByName(Connection connection, String courseName) throws SQLException {
		// Write your SQL query to get the course ID by name
		String sql = "SELECT course_id FROM school.courses WHERE course_name = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, courseName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("course_id");
			}
		}

		return -1; // Course not found
	}

	public void addStudentToCourse(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student:");
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		if (!studentExists(connection, studentId)) {
			logger.info("No student found with ID {}.", studentId);
			return; // Exit the method if the student does not exist
		}

		// Show available courses for the student
		logger.info("Available courses for the student:");
		List<String> availableCourses = getAvailableCoursesForStudent(connection, studentId);
		for (String course : availableCourses) {
			logger.info(course);
		}

		// Prompt the user for the course name
		logger.info("Enter the name of the course:");
		String courseName = scanner.nextLine();

		// Get the course ID based on the course name
		int courseId = getCourseIdByName(connection, courseName);

		// Add the student to the course
		if (courseId != -1) {
			addStudentToCourse(connection, studentId, courseId);
			logger.info("Student with ID {} added to course '{}'.", studentId, courseName);
		} else {
			logger.info("Course '{}' not found.", courseName);
		}
	}

	public void addStudentToCourse(Connection connection, int studentId, int courseId) throws SQLException {
		// Write your SQL query to add a student to the course
		String sql = "INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, courseId);
			statement.setInt(2, studentId);
			statement.executeUpdate();
			connection.commit(); // Commit the transaction
		}
	}

	public void removeStudentFromCourse(Connection connection) throws SQLException {
		// Prompt the user for the student ID
		logger.info("Enter the ID of the student:");
		int studentId = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		// Show courses for the student
		logger.info("Courses for the student:");
		List<String> enrolledCourses = getEnrolledCoursesForStudent(connection, studentId);
		for (String course : enrolledCourses) {
			logger.info(course);
		}

		// Prompt the user for the course name
		logger.info("Enter the name of the course to remove the student from:");
		String courseName = scanner.nextLine();

		// Remove the student from the course
		removeStudentFromCourse(connection, studentId, courseName);
	}

	public void removeStudentFromCourse(Connection connection, int studentId, String courseName) throws SQLException {
		// Get the course ID based on the course name
		int courseId = getCourseIdByName(connection, courseName);

		if (courseId != -1) {
			// Write your SQL query to remove the student from the course
			String sql = "DELETE FROM school.course_student WHERE course_id = ? AND student_id = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, courseId);
				statement.setInt(2, studentId);
				int rowsAffected = statement.executeUpdate();
				connection.commit(); // Commit the transaction
				if (rowsAffected > 0) {
					logger.info("Student with ID {} removed from course '{}'.", studentId, courseName);
				} else {
					logger.info("No enrollment found for student with ID {} in course {}.", studentId, courseName);
				}
			}
		} else {
			logger.info("Course '{}' not found.", courseName);
		}
	}

	public List<String> getEnrolledCoursesForStudent(Connection connection, int studentId) throws SQLException {
		// Write your SQL query to get the enrolled courses for the student
		String sql = "SELECT c.course_name " + "FROM school.courses c "
				+ "JOIN school.course_student cs ON c.course_id = cs.course_id " + "WHERE cs.student_id = ?";

		List<String> enrolledCourses = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String courseName = resultSet.getString("course_name");
				enrolledCourses.add(courseName);
			}
		}

		return enrolledCourses;
	}

	public boolean studentExists(Connection connection, int studentId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM school.students WHERE student_id = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentId);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt(1) > 0;
			}
		}

		return false; // Return false if student not found
	}
}