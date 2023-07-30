package ua.foxminded.javaspring.school_console_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ua.foxminded.javaspring.school_console_app.dao.CourseDao;
import ua.foxminded.javaspring.school_console_app.model.Course;
import ua.foxminded.javaspring.school_console_app.services.CourseService;

import org.mockito.Mockito;

public class CourseServiceTest {

    private CourseService courseService;
    private CourseDao courseDao;

    @BeforeEach
    public void setup() {
        courseDao = Mockito.mock(CourseDao.class);
        courseService = new CourseService(courseDao);
    }

    @Test
    public void generateCourses_shouldReturnListOfCourses() {
        List<Course> generatedCourses = courseService.generateCourses();

        assertNotNull(generatedCourses);
        assertEquals(10, generatedCourses.size());

        for (int i = 0; i < generatedCourses.size(); i++) {
            Course course = generatedCourses.get(i);
            assertEquals(i + 1, course.getId());
            assertEquals(CourseService.COURSES[i], course.getName());
            assertEquals(CourseService.COURSES_DESCRIPTION[i], course.getDescription());
        }
    }
}
