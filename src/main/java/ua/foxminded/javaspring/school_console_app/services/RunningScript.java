package ua.foxminded.javaspring.school_console_app.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.dao.DataInsertion;
import ua.foxminded.javaspring.school_console_app.model.Course;
import ua.foxminded.javaspring.school_console_app.model.Group;
import ua.foxminded.javaspring.school_console_app.model.Student;

public class RunningScript {

  private static final Logger logger = LoggerFactory.getLogger(RunningScript.class.getName());

  public void runScript() throws FileNotFoundException {
    String url = getProperty("db.url");
    String username = getProperty("db.username");
    String password = getProperty("db.password");

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
      logger.info("Connected to database.");

      ScriptRunner sr = new ScriptRunner(connection);
      Reader reader = new BufferedReader(new FileReader("school.sql"));
      sr.runScript(reader);

      DataGenerator generateData = new DataGenerator();
      List<Group> groupsList = generateData.generateGroups();
      List<Course> coursesList = generateData.generateCourses();
      List<Student> studentsList = generateData.generateStudents();

      DataInsertion insertDataInDbSchool = new DataInsertion();
      insertDataInDbSchool.insertGroups(connection, groupsList);
      insertDataInDbSchool.insertCourses(connection, coursesList);
      insertDataInDbSchool.insertStudents(connection, studentsList);
      insertDataInDbSchool.insertCoursesStudents(connection);

      logger.info("Test data generated successfully.");

      ShowingConsoleMenu showingConsoleMenu = new ShowingConsoleMenu();
      showingConsoleMenu.runConsoleMenu(connection);
    } catch (SQLException e) {
      logger.error("An error occurred: {}", e.getMessage(), e);
    }
  }

  private String getProperty(String key) {
    return System.getenv(key);
  }
}