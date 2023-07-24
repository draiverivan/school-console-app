package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolDatabaseAccessor {

	public List<Integer> getGroupIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT group_id FROM school.groups";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("group_id"));
			}
		}
		return ids;
	}

	public List<Integer> getStudentIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT student_id FROM school.students";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("student_id"));
			}
		}
		return ids;
	}

	public List<Integer> getCourseIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();

		String sql = "SELECT course_id FROM school.courses";

		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("course_id"));
			}
		}
		return ids;
	}
}
