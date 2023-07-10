package ua.foxminded.javaspring.school_console_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateData {

	private static final String[] COURSES = { "Math", "Biology", "Physics", "Chemistry", "History", "English", "Art",
			"Music", "Computer Science", "Geography" };
	private static final String[] COURSES_DESCRIPTION = { "learning Mathematics", "learning Biology",
			"learning Physics", "learning Chemistry", "learning History", "learning English", "learning Art",
			"learning Music", "learning Computer Science", "learning Geography" };

	private static final String[] FIRST_NAMES = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi",
			"Ivan", "Judy", "Kevin", "Laura", "Mike", "Nancy", "Olivia", "Peter", "Quincy", "Rita", "Steve", "Tina" };
	private static final String[] LAST_NAMES = { "Adams", "Brown", "Clark", "Davis", "Evans", "Foster", "Garcia",
			"Harris", "Irwin", "Johnson", "Kim", "Lee", "Martinez", "Nguyen", "O'Connor", "Patel", "Quinn", "Rodriguez",
			"Smith", "Taylor" };

	private static final Random RANDOM = new Random();

	// generate list of Courses
	public List<Course> generateCourses() {

		List<Course> coursesList = new ArrayList<>();
		for (int i = 0; i < COURSES.length; i++) {
			Course course = new Course(COURSES[i], COURSES_DESCRIPTION[i]);
			coursesList.add(course);
		}
		return coursesList;
	}

	// generate list of Students
	public List<Student> generateStudents() {

		List<Student> studentsList = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
			String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
			Student student = new Student(firstName, lastName);
			studentsList.add(student);
		}
		return studentsList;
	}

	// generate list of Groups
	public List<Group> generateGroups() {

		List<Group> groupsList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int firstPart = RANDOM.nextInt(26) + 'A';
			int secondPart = RANDOM.nextInt(26) + 'A';
			int numPart = RANDOM.nextInt(100);
			String groupName = "" + (char) firstPart + (char) secondPart + "-" + String.format("%02d", numPart);
			Group group = new Group(groupName);
			groupsList.add(group);
		}
		return groupsList;
	}

	// generate random element
	public <T> T generateRandomElement(List<T> list) {
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(RANDOM.nextInt(list.size()));
		}
	}

}
