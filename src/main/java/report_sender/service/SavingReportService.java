package report_sender.service;

import report_sender.entity.Report;
import report_sender.service.exception.ServiceException;

public interface SavingReportService {
    void saveReportAsTasks(Report report) throws ServiceException;
}
