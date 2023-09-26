package report_sender.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.entity.User;
import report_sender.service.JsonParserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParserServiceImpl implements JsonParserService {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public User getUserFromJson(String jsonUser) throws JsonProcessingException {
        User user = objectMapper.readValue(jsonUser, User.class);
        return user;
    }

    @Override
    public Report getReportFromJson(String jsonReport) {
        try {
            JSONObject reportJsonObject = (JSONObject) new JSONParser().parse(jsonReport);
            long id = (Long) reportJsonObject.get("id");

            JSONArray tasksArr = (JSONArray) reportJsonObject.get("tasks");
            Report report = new Report();
            report.setId(id);

            Iterator tasksItr = tasksArr.iterator();
            List<Task> tasks = new ArrayList<>();
            while (tasksItr.hasNext()) {
                JSONObject test = (JSONObject) tasksItr.next();
                Task task = new Task();
                task.setId((long) test.get("id"));
                task.setToDo((String) test.get("toDo"));
                task.setTimeBegin(LocalDateTime.parse((String) test.get("timeBegin")));
                task.setTimeEnd(LocalDateTime.parse((String) test.get("timeEnd")));
                task.setUserId((long) test.get("userId"));
                tasks.add(task);
            }
            report.setTaskList(tasks);
            return report;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
