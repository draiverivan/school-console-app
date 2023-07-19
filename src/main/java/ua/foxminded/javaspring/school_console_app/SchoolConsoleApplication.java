package ua.foxminded.javaspring.school_console_app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchoolConsoleApplication {

	private static final Logger logger = LoggerFactory.getLogger(SchoolConsoleApplication.class.getName());

	public static void main(String[] args) {
		RunningScript scriptRunner = new RunningScript();
		try {
			scriptRunner.runScript();
		} catch (Exception e) {
			logger.error("An error occurred: {}", e.getMessage(), e);
		}
	}
}
