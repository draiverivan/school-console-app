package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.model.Group;
import ua.foxminded.javaspring.school_console_app.model.Student;
import ua.foxminded.javaspring.school_console_app.services.StudentService;

public class StudentDao {

	private static final Logger logger = LoggerFactory.getLogger(StudentDao.class.getName());
	private static final String ERROR = "An error occurred: {}";
	public static final String INSERT_STUDENT_SQL = "INSERT INTO school.students (first_name, last_name, group_id) VALUES (?, ?, ?)";
	public static final String GET_STUDENT_IDS = "SELECT student_id FROM school.students";
	List<Group> groupsList = new ArrayList<>();

	public void insertRecord(Connection connection, String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
			connection.commit();
		}
	}

	// Insert students
	public void insertStudents(Connection connection, List<Student> students) {
		GroupDao groupDao = new GroupDao();
		StudentService studentService = new StudentService();
		List<Integer> groupIds = groupDao.getGroupIds(connection);
		List<Integer> finalGroupIds = groupIds;
		students.forEach(student -> {
			String firstNameStudent = student.getFirstName();
			String lastNameStudent = student.getLastName();
			int groupId = studentService.generateRandomElement(finalGroupIds);
			try {
				insertRecord(connection, INSERT_STUDENT_SQL, firstNameStudent, lastNameStudent, groupId);
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
		});
	}

	// Get students IDs
	public List<Integer> getStudentIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(GET_STUDENT_IDS);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("student_id"));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
		return ids;
	}

}
