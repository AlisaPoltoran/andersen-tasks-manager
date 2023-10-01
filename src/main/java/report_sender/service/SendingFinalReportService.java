package report_sender.service;

import com.itextpdf.text.DocumentException;
import report_sender.entity.Report;
import report_sender.service.exception.ServiceException;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface SendingFinalReportService {

    void sendFinalReport() throws ServiceException;
}