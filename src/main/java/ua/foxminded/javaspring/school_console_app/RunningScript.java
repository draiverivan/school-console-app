package ua.foxminded.javaspring.school_console_app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;

public class RunningScript {
	public static void main(String args[]) throws Exception {

		// Getting the connection
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "1234");
		System.out.println("Connection established......");
		// Initialize the script runner
		ScriptRunner sr = new ScriptRunner(con);
		// Creating a reader object
		Reader reader = new BufferedReader(new FileReader("school.sql"));
		// Running the script
		sr.runScript(reader);
		/* PostgresApplication.main(args); */
		con.close();
	}
}