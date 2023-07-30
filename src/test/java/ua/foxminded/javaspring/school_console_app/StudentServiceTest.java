package ua.foxminded.javaspring.school_console_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import ua.foxminded.javaspring.school_console_app.dao.StudentDao;
import ua.foxminded.javaspring.school_console_app.model.Student;
import ua.foxminded.javaspring.school_console_app.services.StudentService;

import org.mockito.Mockito;

public class StudentServiceTest {

    private StudentService studentService;
    private StudentDao studentDao;

    @BeforeEach
    public void setup() {
        studentDao = Mockito.mock(StudentDao.class);
        studentService = new StudentService(studentDao);
    }

    @Test
    public void generateStudents_shouldReturnListOfStudents() {
        List<Student> generatedStudents = studentService.generateStudents();

        assertNotNull(generatedStudents);
        assertEquals(200, generatedStudents.size());

        List<String> possibleFirstNames = Arrays.asList(StudentService.FIRST_NAMES);
        List<String> possibleLastNames = Arrays.asList(StudentService.LAST_NAMES);

        for (int i = 0; i < generatedStudents.size(); i++) {
            Student student = generatedStudents.get(i);
            assertEquals(i + 1, student.getId());
            assertTrue(possibleFirstNames.contains(student.getFirstName()), "Generated student first name is not in the expected list");
            assertTrue(possibleLastNames.contains(student.getLastName()), "Generated student last name is not in the expected list");
        }
    }
}
