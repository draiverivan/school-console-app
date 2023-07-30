package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import ua.foxminded.javaspring.school_console_app.dao.CoursesStudentsDao;

public class CoursesStudetnsService {

	private static final Random RANDOM = new Random();
	private final CoursesStudentsDao coursesStudetnsDao;

	public CoursesStudetnsService(CoursesStudentsDao coursesStudetnsDao) {
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
