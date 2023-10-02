package report_sender.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.entity.User;

import java.time.LocalDateTime;

class JsonParserServiceImplTest {

    @Test
    public void testGetUserFromValidJson() throws JsonProcessingException {
        // Given
        String validJson = "{\"id\":1,\"name\":\"alisa\",\"password\":\"alisa\"}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // When
        User user = parser.getUserFromJson(validJson);

        // Then
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("alisa", user.getName());
        Assertions.assertEquals("alisa", user.getPassword());
    }

    @Test
    public void testGetUserFromInvalidJson() {
        // Given
        String invalidJson = "{\"name\":\"John\",\"age\":invalid,\"city\":\"New York\"}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // Then
        Assertions.assertThrows(JsonProcessingException.class, () -> {
            // When
            parser.getUserFromJson(invalidJson);
        });
    }

    @Test
    public void testGetReportFromValidJson() {
        // Given
        String validJson = "{\"user_id\":1,\"tasks\":[{\"job\":\"ToDo\",\"timeBegin\":\"2023-10-02 09:00\",\"timeEnd\":\"2023-10-02 17:00\"}]}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // When
        Report report = parser.getReportFromJson(validJson);

        // Then
        Assertions.assertNotNull(report);
        Assertions.assertNotNull(report.getTaskList());
        Assertions.assertEquals(1, report.getTaskList().size());

        Task task = report.getTaskList().get(0);
        Assertions.assertEquals("ToDo", task.getJob());
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 2, 9, 0), task.getTimeBegin());
        Assertions.assertEquals(LocalDateTime.of(2023, 10, 2, 17, 0), task.getTimeEnd());

        Assertions.assertEquals(1, task.getUserId());
    }

    @Test
    public void testGetReportFromInvalidJson() {
        // Given
        String invalidJson = "{\"user_id\":\"invalid\",\"tasks\":[]}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // Then
        Assertions.assertThrows(RuntimeException.class, () -> {
            // When
            parser.getReportFromJson(invalidJson);
        });
    }

    @Test
    public void testGetReportFromJsonWithEmptyTasksArray() {
        // Given
        String jsonWithEmptyTasks = "{\"user_id\":1,\"tasks\":[]}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // When
        Report report = parser.getReportFromJson(jsonWithEmptyTasks);

        // Then
        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.getTaskList().isEmpty());
    }

    @Test
    public void testGetReportFromJsonWithMissingTasksField() {
        // Given
        String jsonWithoutTasks = "{\"user_id\":1}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // Then
        Assertions.assertThrows(RuntimeException.class, () -> {
            // When
            parser.getReportFromJson(jsonWithoutTasks);
        });
    }

    @Test
    public void testGetReportFromJsonWithMissingUserIdField() {
        // Given
        String jsonWithoutUserId = "{\"tasks\":[{\"job\":\"ToDo\",\"timeBegin\":\"2023-10-02 09:00\",\"timeEnd\":\"2023-10-02 17:00\"}]}";
        JsonParserServiceImpl parser = JsonParserServiceImpl.getInstance();

        // Then
        Assertions.assertThrows(RuntimeException.class, () -> {
            // When
            parser.getReportFromJson(jsonWithoutUserId);
        });
    }

}