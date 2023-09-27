package report_sender.entity;

import java.time.LocalDateTime;

public class Task {
    private long id;
    private String job;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
    private long userId;

    public Task() {
    }

    public Task(long id, String job, LocalDateTime timeBegin, LocalDateTime timeEnd, long userId) {
        this.id = id;
        this.job = job;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDateTime getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(LocalDateTime timeBegin) {
        this.timeBegin = timeBegin;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", job='" + job + '\'' +
                ", timeBegin=" + timeBegin +
                ", timeEnd=" + timeEnd +
                ", userId=" + userId +
                '}';
    }
}
