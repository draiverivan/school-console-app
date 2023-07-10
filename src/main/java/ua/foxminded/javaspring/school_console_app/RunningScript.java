package ua.foxminded.javaspring.school_console_app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;

public class RunningScript {
	public static void main(String args[]) throws Exception {

		// Getting the connection

		String url = "jdbc:postgresql://localhost:5432/school";
		String username = "postgres";
		String password = "1234";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			System.out.println("Connected to database.");

			// Initialize the script runner
			ScriptRunner sr = new ScriptRunner(connection);
			// Creating a reader object
			Reader reader = new BufferedReader(new FileReader("school.sql"));
			// Running the script
			sr.runScript(reader);

			GenerateData generateData = new GenerateData();
			List<Group> groupsList = generateData.generateGroups();
			List<Course> coursesList = generateData.generateCourses();

			InsertDataInDbSchool insertDataInDbSchool = new InsertDataInDbSchool();
			insertDataInDbSchool.insertGroups(connection, groupsList);
			insertDataInDbSchool.insertCourses(connection, coursesList);

			System.out.println("Test data generated successfully.");

		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}