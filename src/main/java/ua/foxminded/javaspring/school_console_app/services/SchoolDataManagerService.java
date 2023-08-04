package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;

import ua.foxminded.javaspring.school_console_app.dao.CourseDao;
import ua.foxminded.javaspring.school_console_app.dao.CourseStudentRelationDao;
import ua.foxminded.javaspring.school_console_app.dao.GroupDao;
import ua.foxminded.javaspring.school_console_app.dao.StudentDao;

public class SchoolDataManagerService {

	public void manageSchoolApplication(Connection connection) {

		DatabaseSchemaCreatorService creatorSchemaOfDatabaseService = new DatabaseSchemaCreatorService();
		creatorSchemaOfDatabaseService.createSchemaOfDatabase(connection);
		CourseDao courseDao = new CourseDao();
		CourseService courseService = new CourseService(courseDao);
		courseService.loadCoursesToDatabase(connection);
		GroupDao groupDao = new GroupDao();
		GroupService groupService = new GroupService(groupDao);
		groupService.loadGroupsToDatabase(connection);
		StudentDao studentDao = new StudentDao();
		StudentService studentService = new StudentService(studentDao);
		studentService.loadStudentsToDatabase(connection);
		CourseStudentRelationDao courseStudentRelationDao = new CourseStudentRelationDao(courseDao, studentDao);
		CourseStudentRelationService courseStudentRelationService = new CourseStudentRelationService(
				courseStudentRelationDao);
		courseStudentRelationService.loadCoursesStudetnsToDatabase(connection);

	}

}
