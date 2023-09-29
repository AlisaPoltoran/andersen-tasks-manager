package report_sender.listener.scheduler;

import com.itextpdf.text.DocumentException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import report_sender.entity.Report;
import report_sender.service.SendingFinalReportService;
import report_sender.service.ServiceProvider;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.SendingFinalReportServiceImpl;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SendJob implements Job {
    private ServiceProvider serviceProvider = ServiceProvider.getInstance();
    private SendingFinalReportService sendingFinalReportService = serviceProvider.getSendingFinalReportService();

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            sendingFinalReportService.sendFinalReport("", "");

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }
}
