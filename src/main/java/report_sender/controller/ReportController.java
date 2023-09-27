package report_sender.controller;

import report_sender.entity.Report;
import report_sender.service.JsonParserService;
import report_sender.service.ReportService;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.JsonParserServiceImpl;
import report_sender.service.impl.ReportServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/report")
public class ReportController extends HttpServlet {
    JsonParserService jsonParserService = new JsonParserServiceImpl();
    ReportService reportService = new ReportServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String reportFromBody = request.getReader()
                .lines().collect(Collectors.joining(System.lineSeparator()));
        Report report = jsonParserService.getReportFromJson(reportFromBody);
        try {
            reportService.saveReportAsTasks(report);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
