package report_sender.controller;

import com.itextpdf.text.DocumentException;
import report_sender.entity.Report;
import report_sender.service.JsonParserService;
import report_sender.service.SavingReportService;
import report_sender.service.SendingFinalReportService;
import report_sender.service.ServiceProvider;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.JsonParserServiceImpl;
import report_sender.service.impl.SendingFinalReportServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/report")
public class ReportController extends HttpServlet {
    private ServiceProvider serviceProvider = ServiceProvider.getInstance();
    private JsonParserService jsonParserService = serviceProvider.getJsonParserService();
    private SendingFinalReportService sendingFinalReportService = serviceProvider.getSendingFinalReportService();
    private SavingReportService savingReportService = serviceProvider.getSavingReportService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportFromBody = request.getReader()
                .lines().collect(Collectors.joining(System.lineSeparator()));
        Report report = jsonParserService.getReportFromJson(reportFromBody);
        try {
            savingReportService.saveReportAsTasks(report);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            sendingFinalReportService.sendFinalReport();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
