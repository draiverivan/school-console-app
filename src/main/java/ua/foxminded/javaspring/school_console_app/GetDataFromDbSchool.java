package ua.foxminded.javaspring.school_console_app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetDataFromDbSchool {

	public List<Integer> getGroupIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();

		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT group_id FROM school.groups")) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("group_id"));
			}
			return ids;
		}
	}

	public List<Integer> getStudentIds(Connection connection) throws SQLException {
		List<Integer> ids = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT student_id FROM school.students")) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("student_id"));
			}
			return ids;
		}
	}

	public List<Integer> getCourseIds(Connection connection) throws SQLException {
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
