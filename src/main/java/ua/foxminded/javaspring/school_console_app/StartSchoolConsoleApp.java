package ua.foxminded.javaspring.school_console_app;

import java.util.Scanner;

import ua.foxminded.javaspring.school_console_app.services.ConsoleMenuControllerService;
import ua.foxminded.javaspring.school_console_app.services.SchoolDataManagerService;
import ua.foxminded.javaspring.school_console_app.services.SchoolDatabaseInteractionService;

public class StartSchoolConsoleApp {

	public static void main(String[] args) {

		SchoolDataManagerService schoolDataManagerService = new SchoolDataManagerService();
		SchoolDatabaseInteractionService schoolDatabaseInteractionService = new SchoolDatabaseInteractionService();
		Scanner scanner = new Scanner(System.in);

		ConsoleMenuControllerService consoleMenuControllerService = new ConsoleMenuControllerService(
				schoolDataManagerService, schoolDatabaseInteractionService, scanner);
		consoleMenuControllerService.runSchoolDataManagerService();
	}

}