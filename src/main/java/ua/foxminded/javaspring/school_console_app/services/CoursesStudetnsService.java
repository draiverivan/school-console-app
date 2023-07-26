package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import ua.foxminded.javaspring.school_console_app.dao.CoursesStudetnsDao;

public class CoursesStudetnsService {

	private static final Random RANDOM = new Random();
	private final CoursesStudetnsDao coursesStudetnsDao;

	public CoursesStudetnsService(CoursesStudetnsDao coursesStudetnsDao) {
		this.coursesStudetnsDao = coursesStudetnsDao;
	}

	public void loadCoursesStudetnsToDatabase(Connection connection) {
		coursesStudetnsDao.insertCoursesStudents(connection);
	}

	// generate random element
	public <T> T generateRandomElement(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}

}
