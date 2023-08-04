package ua.foxminded.javaspring.school_console_app.dao;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.school_console_app.model.Group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class GroupDaoIT {

	private static final Logger logger = LoggerFactory.getLogger(GroupDaoIT.class.getName());

	@Container
	private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("school").withUsername("testuser").withPassword("testpassword");

	private Connection connection;
	private GroupDao groupDao;
	private static final String ERROR = "An error occurred: {}";

	@BeforeAll
	public static void setUpClass() {
		try (Connection connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
				postgresContainer.getUsername(), postgresContainer.getPassword());

				Statement statement = connection.createStatement()) {

			// Create the schema
			statement.executeUpdate("DROP SCHEMA IF EXISTS school cascade;" + "CREATE SCHEMA IF NOT EXISTS school");

			// Create the groups table
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS school.groups (" + "group_id SERIAL PRIMARY KEY,"
					+ "group_name VARCHAR(50) UNIQUE NOT NULL" + ")");
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void cleanUpGroupsTable() {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM school.groups");
			connection.commit();
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	@BeforeEach
	public void setUp() throws SQLException {
		String jdbcUrl = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();

		connection = DriverManager.getConnection(jdbcUrl, username, password);
		connection.setAutoCommit(false);
		groupDao = new GroupDao();

		cleanUpGroupsTable();
	}

	@AfterEach
	public void tearDown() throws SQLException {
		connection.rollback();
		connection.close();
	}

	@Test
	void insertGroups_shouldInsertGroupsWhenGivenGroupList() {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "Group 1"));
		groups.add(new Group(2, "Group 2"));

		groupDao.insertGroups(connection, groups);

		List<Group> insertedGroups = fetchGroupsFromDatabase();
		assertEquals(groups.size(), insertedGroups.size(), "Number of inserted groups should match");

		for (int i = 0; i < groups.size(); i++) {
			assertEquals(groups.get(i), insertedGroups.get(i), "Inserted group details should match");
		}
	}

	@Test
	void getGroupIds_shouldReturnGroupIdsWhenGroupsAreInserted() {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "Group 1"));
		groups.add(new Group(2, "Group 2"));

		groupDao.insertGroups(connection, groups);

		List<Integer> groupIds = groupDao.getGroupIds(connection);
		assertEquals(groups.size(), groupIds.size(), "Number of inserted group IDs should match");
	}

	private List<Group> fetchGroupsFromDatabase() {
		List<Group> groups = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM school.groups");
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				long groupId = resultSet.getLong("group_id");
				String groupName = resultSet.getString("group_name");
				groups.add(new Group(groupId, groupName));
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
		return groups;
	}

}
