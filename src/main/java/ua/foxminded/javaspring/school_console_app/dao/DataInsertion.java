package ua.foxminded.javaspring.school_console_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.model.Course;
import ua.foxminded.javaspring.school_console_app.model.Group;
import ua.foxminded.javaspring.school_console_app.model.Student;
import ua.foxminded.javaspring.school_console_app.services.DataGenerator;

public class DataInsertion {

	private static final Logger logger = LoggerFactory.getLogger(DataInsertion.class.getName());
	private static final String ERROR = "An error occurred: {}";

	List<Group> groupsList = new ArrayList<>();

	public static final String INSERT_GROUP_SQL = "INSERT INTO school.groups (group_name) VALUES (?)";
	public static final String INSERT_COURSE_SQL = "INSERT INTO school.courses (course_name, course_description) VALUES (?, ?)";
	public static final String INSERT_STUDENT_SQL = "INSERT INTO school.students (first_name, last_name, group_id) VALUES (?, ?, ?)";
	public static final String INSERT_COURSE_STUDENT_SQL = "INSERT INTO school.course_student (course_id, student_id) VALUES (?, ?)";

	public static final Random RANDOM = new Random();

	public void insertRecord(Connection connection, String sql, Object... params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
			connection.commit();
		}
	}

//Insert groups
	public void insertGroups(Connection connection, List<Group> groups) {
		groups.stream().forEach(group -> {
			String groupName = group.getName();
			try {
				insertRecord(connection, INSERT_GROUP_SQL, groupName);
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
		});
	}

//Insert courses
	public void insertCourses(Connection connection, List<Course> courses) {
		courses.stream().forEach(course -> {
			String nameCourse = course.getName();
			String decriptionCourse = course.getDescription();
			try {
				insertRecord(connection, INSERT_COURSE_SQL, nameCourse, decriptionCourse);
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
		});
	}

//Insert students
	public void insertStudents(Connection connection, List<Student> students) {
		SchoolDatabaseAccessor getDataFromDbSchool = new SchoolDatabaseAccessor();
		DataGenerator generateData = new DataGenerator();
		List<Integer> groupIds = getDataFromDbSchool.getGroupIds(connection);
		List<Integer> finalGroupIds = groupIds;
		students.forEach(student -> {
			String firstNameStudent = student.getFirstName();
			String lastNameStudent = student.getLastName();
			int groupId = generateData.generateRandomElement(finalGroupIds);
			try {
				insertRecord(connection, INSERT_STUDENT_SQL, firstNameStudent, lastNameStudent, groupId);
			} catch (SQLException e) {
				logger.error(ERROR, e.getMessage(), e);
			}
		});
	}

	// Insert courses_students
	public void insertCoursesStudents(Connection connection) {
		SchoolDatabaseAccessor getDataFromDbSchool = new SchoolDatabaseAccessor();
		DataGenerator generateData = new DataGenerator();
		List<Integer> courseIds = getDataFromDbSchool.getCourseIds(connection);
		try {
			for (int studentId : getDataFromDbSchool.getStudentIds(connection)) {
				int numCourses = RANDOM.nextInt(3) + 1;
				List<Integer> chosenCourseIds = new ArrayList<>();
				for (int i = 0; i < numCourses; i++) {
					Integer courseId = generateData.generateRandomElement(courseIds);
					if (courseId != null && !chosenCourseIds.contains(courseId)) {
						insertRecord(connection, INSERT_COURSE_STUDENT_SQL, courseId, studentId);
						chosenCourseIds.add(courseId);
					}
				}
			}
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

}
