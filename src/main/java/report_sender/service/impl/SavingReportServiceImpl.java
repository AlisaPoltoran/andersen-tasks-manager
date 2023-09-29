package report_sender.service.impl;

import report_sender.entity.Report;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TaskRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.service.SavingReportService;
import report_sender.service.exception.ServiceException;

public class SavingReportServiceImpl implements SavingReportService {
    private RepositoryProvider repositoryProvider = RepositoryProvider.getInstance();
    private TaskRepository taskRepository = repositoryProvider.getTaskRepository();
    private static final SavingReportServiceImpl INSTANCE = new SavingReportServiceImpl();
    private SavingReportServiceImpl() {
    }
    public static SavingReportServiceImpl getInstance() {
        return INSTANCE;
    }
    @Override
    public void saveReportAsTasks(Report report) throws ServiceException {
        try {
            taskRepository.saveTasks(report.getTaskList());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
