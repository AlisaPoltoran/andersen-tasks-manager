package report_sender.controller;

import com.itextpdf.text.DocumentException;
import report_sender.entity.Report;
import report_sender.service.ReportService;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.ReportServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/send")
public class SendController extends HttpServlet {
    ReportService reportService = new ReportServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        LocalDateTime localDateTime = LocalDateTime.now();

//            List<Report> reports = reportService
//                    .createFinalReportFromTasks(localDateTime.toLocalDate().atTime(LocalTime.MIN),
//                            localDateTime.toLocalDate().atTime(LocalTime.MAX));

//            String filePath = reportService.createPDFFinalReport(new ArrayList<Report>());
        try {
            reportService.sendFinalReportToEmail("", "");
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
//            reportService.sendFinalReportToEmail("shaturko.maksim@gmail.com", filePath);

//        } catch (ServiceException e) {
//            throw new RuntimeException(e);
//        } catch (DocumentException e) {
//            throw new RuntimeException(e);
//        }
    }
}
