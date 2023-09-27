package report_sender.repository.impl;

import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.entity.User;
import report_sender.repository.ReportRepository;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportRepositoryImpl implements ReportRepository {
    private final String SAVE_TASK = "INSERT INTO tasks (toDo, timeBegin, timeEnd, userId) VALUES (?,?,?,?)";
    private final String GET_TASKS_BY_PERIOD = "SELECT * FROM tasks JOIN users ON users.id=tasks.user_id" +
            "WHERE timeBegin > ? AND timeEnd < ? ORDER BY timeBegin";
    private final static String GET_ALL_USERS = "SELECT * from users";
    private TransactionalRepository transactionalRepository = new TransactionalRepositoryImpl();


    @Override
    public void saveTasks(List<Task> tasks) throws RepositoryException {
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
        for (Task task : tasks) {
            try (PreparedStatement ps = connection.prepareStatement(SAVE_TASK)) {
                ps.setString(1, task.getToDo());
                ps.setTimestamp(2, Timestamp.valueOf(task.getTimeBegin()));
                ps.setTimestamp(3, Timestamp.valueOf(task.getTimeEnd()));
                ps.setLong(4, task.getUserId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RepositoryException("Error while saving a task: " + e);
            }
        }
        try {
            transactionalRepository.commitTransaction(connection);
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Report> getTasksForPeriod(LocalDateTime from, LocalDateTime to) throws RepositoryException {
        List<User> users = getAllUsers();
        List<Report> reports = new ArrayList<>();
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
        for (User user : users) {
            try (PreparedStatement ps = connection.prepareStatement(GET_TASKS_BY_PERIOD)) {
                ps.setTimestamp(1, Timestamp.valueOf(from));
                ps.setTimestamp(2, Timestamp.valueOf(to));
                List<Task> tasks = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String toDo = rs.getString("toDo");
                    LocalDateTime timeBegin = new Date(rs.getDate("timeBegin").getTime())
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime timeEnd = new Date(rs.getDate("timeEnd").getTime())
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int user_id = rs.getInt("user_id");
                    tasks.add(new Task(id, toDo, timeBegin, timeEnd, user_id));
                }
                reports.add(new Report(user.getId(), tasks));
            } catch (SQLException e) {
                throw new RepositoryException(e);
            }
        }
        try {
            transactionalRepository.commitTransaction(connection);
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
        return reports;
    }


    public List<User> getAllUsers() throws RepositoryException {
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }

        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_USERS)) {
            List<User> users = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("id");
                String name = rs.getString("name");
                User user = new User();
                user.setId(userId);
                user.setName(name);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Error while getting all users: " + e);
        }
    }
}

