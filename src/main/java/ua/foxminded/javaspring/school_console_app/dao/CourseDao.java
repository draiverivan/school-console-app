package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.exceptions.CourseDataAccessException;
import ua.foxminded.javaspring.school_console_app.model.Course;

public class CourseDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDao.class.getName());
	private static final String ERROR = "An error occurred: {}";
	private static final String INSERT_COURSE_SQL = "INSERT INTO school.courses (course_name, course_description) VALUES (?, ?)";
	private static final String GET_COURSE_IDS_SQL = "SELECT course_id FROM school.courses";

	public void insertRecord(Connection connection, String sql, Object... params) {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
			throw new CourseDataAccessException("Error occurred during insert record operation.", e);
		}
	}

	// Insert courses
	public void insertCourses(Connection connection, List<Course> courses) {
		courses.forEach(course -> {
			String nameCourse = course.getName();
			String decriptionCourse = course.getDescription();
			insertRecord(connection, INSERT_COURSE_SQL, nameCourse, decriptionCourse);
		});
	}

	// Get courses IDs
	public List<Integer> getCourseIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(GET_COURSE_IDS_SQL);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("course_id"));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
			throw new CourseDataAccessException("Error occurred during get course IDs operation.", e);
		}
		return ids;
	}

}
