package ua.foxminded.javaspring.school_console_app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.regex.Pattern;

import ua.foxminded.javaspring.school_console_app.dao.GroupDao;
import ua.foxminded.javaspring.school_console_app.model.Group;
import org.mockito.Mockito;

public class GroupServiceTest {

    private GroupService groupService;
    private GroupDao groupDao;

    @BeforeEach
    public void setup() {
        groupDao = Mockito.mock(GroupDao.class);
        groupService = new GroupService(groupDao);
    }

    @Test
    void generateGroups_shouldGenerateCorrectNumberOfGroups() {
        List<Group> generatedGroups = groupService.generateGroups();

        assertNotNull(generatedGroups);
        assertEquals(10, generatedGroups.size());

        Pattern groupNamePattern = Pattern.compile("[A-Z]{2}-\\d{2}");

        for (int i = 0; i < generatedGroups.size(); i++) {
            Group group = generatedGroups.get(i);
            assertEquals(i + 1, group.getId());
            assertTrue(groupNamePattern.matcher(group.getName()).matches(), "Group name format is incorrect");
        }
    }
}
