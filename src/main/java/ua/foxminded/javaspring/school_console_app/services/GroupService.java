package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.foxminded.javaspring.school_console_app.dao.GroupDao;
import ua.foxminded.javaspring.school_console_app.model.Group;

public class GroupService {

	private static final Random RANDOM = new Random();

	public void loadGroupsToDatabase(Connection connection) {
		GroupDao groupDao = new GroupDao();
		groupDao.insertGroups(connection, generateGroups());
	}

	// generate list of Groups
	public List<Group> generateGroups() {
		List<Group> groupsList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int groupId = i + 1;
			int firstPart = RANDOM.nextInt(26) + 'A';
			int secondPart = RANDOM.nextInt(26) + 'A';
			int numPart = RANDOM.nextInt(100);
			String groupName = "" + (char) firstPart + (char) secondPart + "-" + String.format("%02d", numPart);
			Group group = new Group(groupId, groupName);
			groupsList.add(group);
		}
		return groupsList;
	}
	
	// generate random element
		public <T> T generateRandomElement(List<T> list) {
			return list.get(RANDOM.nextInt(list.size()));
		}

}
