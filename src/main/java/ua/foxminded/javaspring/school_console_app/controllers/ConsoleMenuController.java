package ua.foxminded.javaspring.school_console_app.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.dao.SchoolDatabaseInteractionDao;
import ua.foxminded.javaspring.school_console_app.services.SchoolDataManagerService;

public class ConsoleMenuController {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleMenuController.class.getName());
	private static final String ERROR = "An error occurred: {}";
	private String url = getProperty("db.url");
	private String username = getProperty("db.username");
	private String password = getProperty("db.password");
	private final SchoolDataManagerService schoolDataManagerService;
	private final SchoolDatabaseInteractionDao schoolDatabaseInteractionService;
	private final Scanner scanner;

	public ConsoleMenuController(SchoolDataManagerService schoolDataManagerService,
			SchoolDatabaseInteractionDao schoolDatabaseInteractionService, Scanner scanner) {

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
			handleMenuChoice(choice, connection);
		}
		scanner.close();
	}

	private void handleMenuChoice(String choice, Connection connection) {
		switch (choice) {
			case "a":
				handleFindGroupsWithLessOrEqualStudents(connection);
				break;
			case "b":
				handleFindStudentsByCourseName(connection);
				break;
			case "c":
				handleAddNewStudent(connection);
				break;
			case "d":
				handleDeleteStudentById(connection);
				break;
			case "e":
				handleAddStudentToCourse(connection);
				break;
			case "f":
				handleRemoveStudentFromCourse(connection);
				break;
			case "exit":
				logger.info("Exiting the program.");
				break;
			default:
				logger.info("Invalid choice. Please try again.");
				break;
		}
	}

	private void handleFindGroupsWithLessOrEqualStudents(Connection connection) {
		try {
			schoolDatabaseInteractionService.findGroupsWithLessOrEqualStudents(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void handleFindStudentsByCourseName(Connection connection) {
		try {
			schoolDatabaseInteractionService.findStudentsByCourseName(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void handleAddNewStudent(Connection connection) {
		try {
			schoolDatabaseInteractionService.addNewStudent(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void handleDeleteStudentById(Connection connection) {
		try {
			schoolDatabaseInteractionService.deleteStudentById(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void handleAddStudentToCourse(Connection connection) {
		try {
			schoolDatabaseInteractionService.addStudentToCourse(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
	}

	private void handleRemoveStudentFromCourse(Connection connection) {
		try {
			schoolDatabaseInteractionService.removeStudentFromCourse(connection);
		} catch (SQLException e) {
			logger.error(ERROR, e.getMessage(), e);
		}
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
