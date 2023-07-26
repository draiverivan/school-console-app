package ua.foxminded.javaspring.school_console_app.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSchemaCreatorService {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaCreatorService.class.getName());

	public void createSchemaOfDatabase(Connection connection) {

		try {
			logger.info("Connected to database.");
			ScriptRunner sr = new ScriptRunner(connection);
			Reader reader = new BufferedReader(new FileReader("school.sql"));
			sr.runScript(reader);
			logger.info("Database schema created.");
		} catch (FileNotFoundException e) {
			logger.error("An error occurred: {}", e.getMessage(), e);
		}
	}
}
