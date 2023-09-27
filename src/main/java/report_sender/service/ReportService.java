package report_sender.service;

import com.itextpdf.text.DocumentException;
import report_sender.entity.Report;
import report_sender.service.exception.ServiceException;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    void saveReportAsTasks(Report report) throws ServiceException;

    List<Report> createFinalReportFromTasks(LocalDateTime from, LocalDateTime to) throws ServiceException;

    String createPDFFinalReport(List<Report> reports) throws FileNotFoundException, DocumentException;

    void sendFinalReportToEmail(String email, String fil) throws ServiceException;

    void sendFinalReportToTelegram(String telegram);


}
