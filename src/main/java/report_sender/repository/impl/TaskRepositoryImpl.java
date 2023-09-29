package report_sender.repository.impl;

import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.entity.User;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TaskRepository;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {
    private static final TaskRepositoryImpl INSTANCE = new TaskRepositoryImpl();
    private TaskRepositoryImpl() {
    }
    public static TaskRepositoryImpl getInstance() {
        return INSTANCE;
    }
    private RepositoryProvider repositoryProvider= RepositoryProvider.getInstance();
    private TransactionalRepository transactionalRepository = repositoryProvider.getTransactionalRepository();
    private final String SAVE_TASK = "INSERT INTO tasks (job, timeBegin, timeEnd, user_id) VALUES (?,?,?,?)";
    private final String GET_TASKS_BY_PERIOD = "SELECT * FROM tasks JOIN users ON (users.id=tasks.user_id)" +
            "WHERE timebegin > ? AND timeend < ? AND user_id = ? ORDER BY timebegin";
    private final static String GET_ALL_USERS = "SELECT * from users";

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
                ps.setString(1, task.getJob());
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
    public List<Report> getTasksForPeriodAsReport(LocalDateTime from, LocalDateTime to) throws RepositoryException {
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
                ps.setInt(3, user.getId());
                List<Task> tasks = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String toDo = rs.getString("job");
                    LocalDateTime timeBegin = rs.getTimestamp("timeBegin").toLocalDateTime();
                    LocalDateTime timeEnd = rs.getTimestamp("timeEnd").toLocalDateTime();
                    int user_id = rs.getInt("user_id");
                    tasks.add(new Task(id, toDo, timeBegin, timeEnd, user_id));
                }
                reports.add(new Report(user.getId(), tasks, user));
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


    private List<User> getAllUsers() throws RepositoryException {
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

