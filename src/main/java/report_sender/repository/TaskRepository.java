package report_sender.repository;

import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.repository.exception.RepositoryException;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository {
    void saveTasks(List<Task> tasks) throws RepositoryException;
    List<Report> getTasksForPeriodAsReport(LocalDateTime from, LocalDateTime to) throws RepositoryException;
}
