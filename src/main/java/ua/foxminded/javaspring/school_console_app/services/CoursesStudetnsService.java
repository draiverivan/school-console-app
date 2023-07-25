package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import ua.foxminded.javaspring.school_console_app.dao.CoursesStudetnsDao;

public class CoursesStudetnsService {

	private static final Random RANDOM = new Random();

	public void loadCoursesStudetnsToDatabase(Connection connection) {
		CoursesStudetnsDao coursesStudetnsDao = new CoursesStudetnsDao();
		coursesStudetnsDao.insertCoursesStudents(connection);
	}

	// generate random element
	public <T> T generateRandomElement(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}

}
