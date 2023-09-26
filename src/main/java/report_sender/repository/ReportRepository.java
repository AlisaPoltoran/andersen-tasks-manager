package report_sender.repository;

import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository {
    void saveTasks(List<Task> tasks) throws RepositoryException;
    List<Report> getTasksForPeriod(LocalDateTime from, LocalDateTime to) throws RepositoryException;

}
