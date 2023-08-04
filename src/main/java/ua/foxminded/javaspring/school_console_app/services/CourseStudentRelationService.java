package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import ua.foxminded.javaspring.school_console_app.dao.CourseStudentRelationDao;

public class CourseStudentRelationService {

	private final CourseStudentRelationDao coursesStudetnsDao;

	public CourseStudentRelationService(CourseStudentRelationDao coursesStudetnsDao) {
		this.coursesStudetnsDao = coursesStudetnsDao;
	}

	public void loadCoursesStudetnsToDatabase(Connection connection) {
		coursesStudetnsDao.insertCoursesStudents(connection);
	}

}
