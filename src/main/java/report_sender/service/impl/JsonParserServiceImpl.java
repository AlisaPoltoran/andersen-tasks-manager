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
import java.time.format.DateTimeFormatter;
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            JSONObject reportJsonObject = (JSONObject) new JSONParser().parse(jsonReport);
            long userId = (Long) reportJsonObject.get("user_id");
            JSONArray tasksArr = (JSONArray) reportJsonObject.get("tasks");

            Report report = new Report();

            Iterator tasksItr = tasksArr.iterator();
            List<Task> tasks = new ArrayList<>();
            while (tasksItr.hasNext()) {
                JSONObject test = (JSONObject) tasksItr.next();
                Task task = new Task();
                task.setJob((String) test.get("job"));
                task.setTimeBegin(LocalDateTime.parse((String) test.get("timeBegin"), dateTimeFormatter));
                task.setTimeEnd(LocalDateTime.parse((String) test.get("timeEnd"), dateTimeFormatter));
                task.setUserId(userId);
                tasks.add(task);
            }
            System.out.println(tasks);




            report.setTaskList(tasks);
            return report;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
