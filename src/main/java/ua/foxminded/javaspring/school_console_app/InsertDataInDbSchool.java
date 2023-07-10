package ua.foxminded.javaspring.school_console_app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InsertDataInDbSchool {

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
		}
	}

	// Insert groups
	public void insertGroups(Connection connection, List<Group> groups) {
		groups.stream().forEach(group -> {
			String groupName = group.getGroupName();
			try {
				insertRecord(connection, INSERT_GROUP_SQL, groupName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// Insert courses
	public void insertCourses(Connection connection, List<Course> courses) {
		courses.stream().forEach(course -> {
			String nameCourse = course.getNameCourse();
			String decriptionCourse = course.getDecriptionCourse();
			try {
				insertRecord(connection, INSERT_COURSE_SQL, nameCourse, decriptionCourse);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// Insert students
	public void insertStudents(Connection connection, List<Student> students) {
		GetDataFromDbSchool getDataFromDbSchool = new GetDataFromDbSchool();
		GenerateData generateData = new GenerateData();
		List<Integer> groupIds = new ArrayList<>();
		try {
			groupIds = getDataFromDbSchool.getGroupIds(connection);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Integer> finalGroupIds = groupIds;
		students.forEach(student -> {
				String firstNameStudent = student.getFirstNameStudent();
				String lastNameStudent = student.getLastNameStudent();
				Integer groupId = generateData.generateRandomElement(finalGroupIds);
				try {
					insertRecord(connection, INSERT_STUDENT_SQL, groupId, firstNameStudent, lastNameStudent);
				} catch (SQLException e) {
					e.printStackTrace();
				}		
		});
	}
	
	// Insert courses_students
	public void insertCoursesStudents(Connection connection) {
		GetDataFromDbSchool getDataFromDbSchool = new GetDataFromDbSchool();
		GenerateData generateData = new GenerateData();
		List<Integer> courseIds = new ArrayList<>();
		try {
			courseIds = getDataFromDbSchool.getCourseIds(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}