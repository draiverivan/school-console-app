package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchoolDatabaseAccessor {
	
	private static final Logger logger = LoggerFactory.getLogger(SchoolDatabaseAccessor.class.getName());
	  private static final String ERROR = "An error occurred: {}";

	public List<Integer> getGroupIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT group_id FROM school.groups";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("group_id"));
			}
		} catch (SQLException e) {
	        logger.error(ERROR, e.getMessage(), e);
	      }
		return ids;
	}

	public List<Integer> getStudentIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT student_id FROM school.students";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("student_id"));
			}
		} catch (SQLException e) {
	        logger.error(ERROR, e.getMessage(), e);
	      }
		return ids;
	}

	public List<Integer> getCourseIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT course_id FROM school.courses";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("course_id"));
			}
		} catch (SQLException e) {
	        logger.error(ERROR, e.getMessage(), e);
	      }
		return ids;
	}
}
