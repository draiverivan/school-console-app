package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMenuControllerService {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleMenuControllerService.class.getName());
	private static final String ERROR = "An error occurred: {}";
	private String url = getProperty("db.url");
	private String username = getProperty("db.username");
	private String password = getProperty("db.password");
	private final SchoolDataManagerService schoolDataManagerService;
	private final SchoolDatabaseInteractionService schoolDatabaseInteractionService;
	private final Scanner scanner;

	public ConsoleMenuControllerService(SchoolDataManagerService schoolDataManagerService,
			SchoolDatabaseInteractionService schoolDatabaseInteractionService, Scanner scanner) {

		this.schoolDataManagerService = schoolDataManagerService;
		this.schoolDatabaseInteractionService = schoolDatabaseInteractionService;
		this.scanner = scanner;
	}

	public void runSchoolDataManagerService() {

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			schoolDataManagerService.manageSchoolApplication(connection);
			runConsoleMenu(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	public void runConsoleMenu(Connection connection) {
		String choice = "";

		while (!choice.equalsIgnoreCase("exit")) {
			displayMenu();
			choice = scanner.nextLine();
			switch (choice) {
			case "a":
				try {
					schoolDatabaseInteractionService.findGroupsWithLessOrEqualStudents(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "b":
				try {
					schoolDatabaseInteractionService.findStudentsByCourseName(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "c":
				try {
					schoolDatabaseInteractionService.addNewStudent(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "d":
				try {
					schoolDatabaseInteractionService.deleteStudentById(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "e":
				try {
					schoolDatabaseInteractionService.addStudentToCourse(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "f":
				try {
					schoolDatabaseInteractionService.removeStudentFromCourse(connection);
				} catch (SQLException e) {
					logger.error(ERROR, e.getMessage(), e);
				}
				break;
			case "exit":
				logger.info("Exiting the program.");
				break;
			default:
				logger.info("Invalid choice. Please try again.");
				break;
			}
		}
		scanner.close();
	}

	private void displayMenu() {
		logger.info("======= Console Menu =======");
		logger.info("a. Find all groups with less or equal students’ number");
		logger.info("b. Find all students related to the course with the given name");
		logger.info("c. Add a new student");
		logger.info("d. Delete a student by the STUDENT_ID");
		logger.info("e. Add a student to the course (from a list)");
		logger.info("f. Remove the student from one of their courses");
		logger.info("exit. Exit the program");
		logger.info("============================");
		logger.info("Enter your choice:");
	}

	private String getProperty(String key) {
		return System.getenv(key);
	}

}