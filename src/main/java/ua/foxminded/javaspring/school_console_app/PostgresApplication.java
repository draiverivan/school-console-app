package ua.foxminded.javaspring.school_console_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PostgresApplication {

	// @formatter:off
	private static final String CREATE_TABLES_SQL = "CREATE TABLE IF NOT EXISTS school.groups ("+ "group_id SERIAL PRIMARY KEY," + "group_name VARCHAR(5) UNIQUE NOT NULL);"
			+ "CREATE TABLE IF NOT EXISTS school.courses (" + "course_id SERIAL PRIMARY KEY," + "course_name VARCHAR(50) UNIQUE NOT NULL," + "course_description VARCHAR(250) NOT NULL);"
			+ "CREATE TABLE IF NOT EXISTS school.students (" + "student_id SERIAL PRIMARY KEY," + "group_id INTEGER," + "FOREIGN KEY (group_id) REFERENCES school.groups(group_id)," + "first_name VARCHAR(50) NOT NULL," + "last_name VARCHAR(50) NOT NULL);"
			+ "CREATE TABLE IF NOT EXISTS school.course_student (" + "junction_id SERIAL PRIMARY KEY," + "course_id INTEGER," + "student_id INTEGER," + "FOREIGN KEY (course_id) REFERENCES school.courses(course_id)," + "FOREIGN KEY (student_id) REFERENCES school.students(student_id)," + "UNIQUE (course_id, student_id));";
	// @formatter:on

	// @formatter:off
	private static final String DROP_TABLES_SQL = "DROP TABLE IF EXISTS school.groups CASCADE;"
			+ "DROP TABLE IF EXISTS school.courses CASCADE;"
			+ "DROP TABLE IF EXISTS school.students CASCADE;"
			+ "DROP TABLE IF EXISTS school.course_student;";
	// @formatter:on

	private static final String INSERT_GROUP_SQL = "INSERT INTO school.groups (group_name) VALUES (?)";
	private static final String INSERT_COURSE_SQL = "INSERT INTO school.courses (course_name, course_description) VALUES (?, ?)";
	private static final String INSERT_STUDENT_SQL = "INSERT INTO school.students (first_name, last_name, group_id) VALUES (?, ?, ?)";
	private static final String INSERT_COURSE_STUDENT_SQL = "INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?)";

	private static final String[] FIRST_NAMES = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi",
			"Ivan", "Judy", "Kevin", "Laura", "Mike", "Nancy", "Olivia", "Peter", "Quincy", "Rita", "Steve", "Tina" };
	private static final String[] LAST_NAMES = { "Adams", "Brown", "Clark", "Davis", "Evans", "Foster", "Garcia",
			"Harris", "Irwin", "Johnson", "Kim", "Lee", "Martinez", "Nguyen", "O'Connor", "Patel", "Quinn", "Rodriguez",
			"Smith", "Taylor" };

	private static final Random RANDOM = new Random();

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/school";
		String username = "postgres";
		String password = "1234";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			System.out.println("Connected to database.");

			// Drop tables if they already exist
			executeSql(connection, DROP_TABLES_SQL);

			// Create tables
			executeSql(connection, CREATE_TABLES_SQL);

			// Insert groups
			for (int i = 0; i < 10; i++) {
				String groupName = generateGroupName();
				insertRecord(connection, INSERT_GROUP_SQL, groupName);
			}

			// Insert courses
			Map<String, String> coursesMap = new LinkedHashMap<>();

			String[] courses = { "Math", "Biology", "Physics", "Chemistry", "History", "English", "Art", "Music",
					"Computer Science", "Geography" };
			String[] coursesDescription = { "learning Mathematics", "learning Biology", "learning Physics",
					"learning Chemistry", "learning History", "learning English", "learning Art", "learning Music",
					"learning Computer Science", "learning Geography" };
			for (int i = 0; i < courses.length; i++) {
				coursesMap.put(courses[i], coursesDescription[i]);
			}

			for (Map.Entry<String, String> entry : coursesMap.entrySet()) {
				String course = entry.getKey();
				String courseDescription = entry.getValue();
				insertRecord(connection, INSERT_COURSE_SQL, course, courseDescription);
			}

			// Insert students
			List<Integer> groupIds = getGroupIds(connection);
			for (int i = 0; i < 200; i++) {
				String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
				String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
				Integer groupId = getRandomElement(groupIds);
				insertRecord(connection, INSERT_STUDENT_SQL, firstName, lastName, groupId);
			}

			// Randomly assign students to groups
			List<Integer> studentIds = getStudentIds(connection);
			for (int groupId : groupIds) {
				int numStudents = RANDOM.nextInt(21) + 10;
				for (int i = 0; i < numStudents; i++) {
					Integer studentId = getRandomElement(studentIds);
					if (studentId != null) {
						updateRecord(connection, "UPDATE school.students SET group_id = ? WHERE student_id = ?",
								groupId, studentId);
						studentIds.remove(studentId);
					}
				}
			}

			// Randomly assign courses to students
			List<Integer> courseIds = getCourseIds(connection);
			for (int studentId : getStudentIds(connection)) {
				int numCourses = RANDOM.nextInt(3) + 1;
				List<Integer> chosenCourseIds = new ArrayList<>();
				for (int i = 0; i < numCourses; i++) {
					Integer courseId = getRandomElement(courseIds);
					if (courseId != null && !chosenCourseIds.contains(courseId)) {
						insertRecord(connection, INSERT_COURSE_STUDENT_SQL, courseId, studentId);
						chosenCourseIds.add(courseId);
					}
				}
			}

			System.out.println("Test data generated successfully.");

		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	private static void executeSql(Connection connection, String sql) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.execute(sql);
		}
	}

	private static void insertRecord(Connection connection, String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		}
	}

	private static void updateRecord(Connection connection, String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		}
	}

	private static String generateGroupName() {
		int firstPart = RANDOM.nextInt(26) + 'A';
		int secondPart = RANDOM.nextInt(26) + 'A';
		int numPart = RANDOM.nextInt(100);
		return "" + (char) firstPart + (char) secondPart + "-" + String.format("%02d", numPart);
	}

	private static <T> T getRandomElement(List<T> list) {
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(RANDOM.nextInt(list.size()));
		}
	}

	private static List<Integer> getGroupIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();

		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT group_id FROM school.groups")) {

			while (resultSet.next()) {
				ids.add(resultSet.getInt("group_id"));
			}
			return ids;
		}
	}

	private static List<Integer> getStudentIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT student_id FROM school.students")) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("student_id"));
			}
			return ids;
		}
	}

	private static List<Integer> getCourseIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT course_id FROM school.courses")) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("course_id"));
			}
			return ids;
		}
	}
}
