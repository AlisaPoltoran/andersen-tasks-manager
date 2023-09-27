package report_sender.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.repository.ReportRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.impl.ReportRepositoryImpl;
import report_sender.service.ReportService;
import report_sender.service.exception.ServiceException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

public class ReportServiceImpl implements ReportService {
    ReportRepository reportRepository = new ReportRepositoryImpl();

    @Override
    public void saveReportAsTasks(Report report) throws ServiceException {
        try {
            reportRepository.saveTasks(report.getTaskList());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Report> createFinalReportFromTasks(LocalDateTime from, LocalDateTime to) throws ServiceException {
        try {
            return reportRepository.getTasksForPeriod(from, to);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String createPDFFinalReport(List<Report> reports) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        for (Report report : reports) {
            Chunk chunk = new Chunk("Trainee: " + report.getUser().getName() + "has executed tasks for today\n\n", font);
            document.add(chunk);
            for (Task task : report.getTaskList()) {
                document.add(new Chunk("Task #" + task.getId() + "\n", font));
                document.add(new Chunk("Begin time: " + task.getTimeBegin() + " --> ", font));
                document.add(new Chunk("End time: " + task.getTimeEnd() + "\n", font));
                document.add(new Chunk(task.getToDo() + "\n", font));
                document.add(new Chunk("-----------------------------------------------\n", font));
            }
        }
        document.close();
        return "test.pdf";
    }

    @Override
    public void sendFinalReportToEmail(String email, String file) throws ServiceException {


        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        String from = "shaturko.maksim@gmail.com";
        String to = "i.am.masy@gmail.com";


        Session session = Session.getDefaultInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("shaturko.maksim@gmail.com", "***********");
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject("This is the Subject Line!");


            BodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText("This is message body");
            Multipart multipart = new MimeMultipart();
            DataSource source = new FileDataSource(file);
            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file);
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    @Override
    public void sendFinalReportToTelegram(String telegram) {

    }


}
