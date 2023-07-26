package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.foxminded.javaspring.school_console_app.dao.StudentDao;
import ua.foxminded.javaspring.school_console_app.model.Student;

public class StudentService {

	private static final String[] FIRST_NAMES = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi",
			"Ivan", "Judy", "Kevin", "Laura", "Mike", "Nancy", "Olivia", "Peter", "Quincy", "Rita", "Steve", "Tina" };
	private static final String[] LAST_NAMES = { "Adams", "Brown", "Clark", "Davis", "Evans", "Foster", "Garcia",
			"Harris", "Irwin", "Johnson", "Kim", "Lee", "Martinez", "Nguyen", "O'Connor", "Patel", "Quinn", "Rodriguez",
			"Smith", "Taylor" };
	private static final Random RANDOM = new Random();
	private final StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void loadStudentsToDatabase(Connection connection) {
		studentDao.insertStudents(connection, generateStudents());
	}

	// generate list of Students
	public List<Student> generateStudents() {
		List<Student> studentsList = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
			String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
			int studentId = i + 1;
			Student student = new Student(studentId, null, firstName, lastName);
			studentsList.add(student);
		}
		return studentsList;
	}

	// generate random element
	public <T> T generateRandomElement(List<T> list) {
		return list.get(RANDOM.nextInt(list.size()));
	}
}
