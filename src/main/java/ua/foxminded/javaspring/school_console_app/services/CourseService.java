package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ua.foxminded.javaspring.school_console_app.dao.CourseDao;
import ua.foxminded.javaspring.school_console_app.model.Course;

public class CourseService {

	private static final String[] COURSES = { "Math", "Biology", "Physics", "Chemistry", "History", "English", "Art",
			"Music", "Computer Science", "Geography" };
	private static final String[] COURSES_DESCRIPTION = { "learning Mathematics", "learning Biology",
			"learning Physics", "learning Chemistry", "learning History", "learning English", "learning Art",
			"learning Music", "learning Computer Science", "learning Geography" };
	private final CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void loadCoursesToDatabase(Connection connection) {
		courseDao.insertCourses(connection, generateCourses());
	}

	// generate list of Courses
	public List<Course> generateCourses() {
		List<Course> coursesList = new ArrayList<>();
		for (int i = 0; i < COURSES.length; i++) {
			int courseId = i + 1;
			Course course = new Course(courseId, COURSES[i], COURSES_DESCRIPTION[i]);
			coursesList.add(course);
		}
		return coursesList;
	}
}
