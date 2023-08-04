package ua.foxminded.javaspring.school_console_app;

import java.util.Scanner;

import ua.foxminded.javaspring.school_console_app.controllers.ConsoleMenuController;
import ua.foxminded.javaspring.school_console_app.dao.SchoolDatabaseInteractionDao;
import ua.foxminded.javaspring.school_console_app.services.SchoolDataManagerService;

public class StartSchoolConsoleApp {

	public static void main(String[] args) {

		SchoolDataManagerService schoolDataManagerService = new SchoolDataManagerService();
		SchoolDatabaseInteractionDao schoolDatabaseInteractionService = new SchoolDatabaseInteractionDao();
		Scanner scanner = new Scanner(System.in);

		ConsoleMenuController consoleMenuControllerService = new ConsoleMenuController(
				schoolDataManagerService, schoolDatabaseInteractionService, scanner);
		consoleMenuControllerService.runSchoolDataManagerService();
	}

}