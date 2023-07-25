package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;

public class SchoolDataManagerService {

	public void manageSchoolApplication(Connection connection) {

		CreatorSchemaOfDatabaseService creatorSchemaOfDatabaseService = new CreatorSchemaOfDatabaseService();
		creatorSchemaOfDatabaseService.createSchemaOfDatabase(connection);
		CourseService courseService = new CourseService();
		courseService.loadCoursesToDatabase(connection);
		GroupService groupService = new GroupService();
		groupService.loadGroupsToDatabase(connection);
		StudentService studentService = new StudentService();
		studentService.loadStudentsToDatabase(connection);

	}

}
