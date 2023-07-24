package ua.foxminded.javaspring.school_console_app.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.dao.ManagingData;

public class ShowingConsoleMenu {

  private static final Logger logger = LoggerFactory.getLogger(ShowingConsoleMenu.class.getName());

  public void runConsoleMenu(Connection connection) throws SQLException {
    Scanner scanner = new Scanner(System.in);
    String choice = "";
    ManagingData managingData = new ManagingData();

    while (!choice.equalsIgnoreCase("exit")) {
      displayMenu();
      choice = scanner.nextLine();
      switch (choice) {
        case "a":
          managingData.findGroupsWithLessOrEqualStudents(connection); 
          break;
        case "b":
          managingData.findStudentsByCourseName(connection); 
          break;
        case "c":
          managingData.addNewStudent(connection); 
          break;
        case "d":
          managingData.deleteStudentById(connection); 
          break;
        case "e":
          managingData.addStudentToCourse(connection); 
          break;
        case "f":
          managingData.removeStudentFromCourse(connection); 
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
}