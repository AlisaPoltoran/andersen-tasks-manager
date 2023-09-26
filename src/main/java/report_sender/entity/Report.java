package report_sender.entity;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private long id;
    private List<Task> taskList;
    private User user;

    public Report(long id, List<Task> taskList) {
        this.id = id;
        this.taskList = taskList;
    }

    public Report() {
        taskList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", taskList=" + taskList +
                ", user=" + user +
                '}';
    }
}
