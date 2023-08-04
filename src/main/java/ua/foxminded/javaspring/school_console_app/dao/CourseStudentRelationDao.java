package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.exceptions.CourseStudentRelationDataAccessException;
import ua.foxminded.javaspring.school_console_app.model.Group;

public class CourseStudentRelationDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseStudentRelationDao.class.getName());
	private static final String ERROR = "An error occurred: {}";
	public static final Random RANDOM = new Random();
	List<Group> groupsList = new ArrayList<>();
	public static final String INSERT_COURSE_STUDENT_SQL = "INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?)";
	private final CourseDao courseDao;
	private final StudentDao studentDao;

	public CourseStudentRelationDao(CourseDao courseDao, StudentDao studentDao) {
		this.courseDao = courseDao;
		this.studentDao = studentDao;
	}

	public void insertRecord(Connection connection, String sql, Object... params) {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
			throw new CourseStudentRelationDataAccessException("Error occurred during insert record operation.", e);
		}
	}

	// Insert courses_students
	public void insertCoursesStudents(Connection connection) {
		// Using the instances passed in constructor instead of creating new ones
		List<Integer> courseIds = courseDao.getCourseIds(connection);
		for (int studentId : studentDao.getStudentIds(connection)) {
			int numCourses = RANDOM.nextInt(3) + 1;
			List<Integer> chosenCourseIds = new ArrayList<>();
			for (int i = 0; i < numCourses; i++) {
				Integer courseId = generateRandomElement(courseIds);
				if (courseId != null && !chosenCourseIds.contains(courseId)) {
					insertRecord(connection, INSERT_COURSE_STUDENT_SQL, courseId, studentId);
					chosenCourseIds.add(courseId);
				}
			}
		}
	}

	// generate random element
	public <T> T generateRandomElement(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}

}
