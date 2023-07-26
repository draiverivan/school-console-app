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

public class GroupDao {

	private static final Logger logger = LoggerFactory.getLogger(GroupDao.class.getName());
	private static final String ERROR = "An error occurred: {}";
	private static final String INSERT_GROUP_SQL = "INSERT INTO school.groups (group_name) VALUES (?)";
	private static final String INSERT_GROUP_IDS_SQL = "SELECT group_id FROM school.groups";
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

	// Insert groups
	public void insertGroups(Connection connection, List<Group> groups) {
		groups.forEach(group -> {
			String groupName = group.getName();
			try {
				insertRecord(connection, INSERT_GROUP_SQL, groupName);
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
		});
	}

	// Get groups IDs
	public List<Integer> getGroupIds(Connection connection) {
		List<Integer> ids = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(INSERT_GROUP_IDS_SQL);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				ids.add(resultSet.getInt("group_id"));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
		return ids;
	}

}
