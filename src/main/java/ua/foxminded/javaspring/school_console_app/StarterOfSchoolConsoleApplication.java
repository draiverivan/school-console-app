package ua.foxminded.javaspring.school_console_app;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.foxminded.javaspring.school_console_app.services.RunningScript;

public class StarterOfSchoolConsoleApplication {

  private static final Logger logger = LoggerFactory.getLogger(StarterOfSchoolConsoleApplication.class.getName());

  public static void main(String[] args) {
    RunningScript runningScript = new RunningScript();
    try {
      runningScript.runScript();
    } catch (FileNotFoundException e) {
      logger.error("An error occurred: {}", e.getMessage(), e);
    }
  }

}
