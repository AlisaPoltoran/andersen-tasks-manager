package report_sender.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import report_sender.ConfigService;
import report_sender.entity.Report;
import report_sender.entity.Task;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TaskRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.service.SendingFinalReportService;
import report_sender.service.ServiceProvider;
import report_sender.service.exception.ServiceException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class SendingFinalReportServiceImpl implements SendingFinalReportService {
    private static final SendingFinalReportServiceImpl INSTANCE = new SendingFinalReportServiceImpl();
    private SendingFinalReportServiceImpl() {
    }
    public static SendingFinalReportServiceImpl getInstance() {
        return INSTANCE;
    }
    private ConfigService configService = ServiceProvider.getInstance().getConfigReaderService();

    private final RepositoryProvider repositoryProvider = RepositoryProvider.getInstance();
    private final TaskRepository taskRepository = repositoryProvider.getTaskRepository();
    private final DateTimeFormatter todayFormatterPoint = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter todayFormatterDash = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void sendFinalReport() throws ServiceException {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Report> reports = createFinalReportFromTasks(localDateTime.toLocalDate().atTime(LocalTime.MIN),
                        localDateTime.toLocalDate().atTime(LocalTime.MAX));
        String file = null;
        try {
            file = createPDFFinalReport(reports);
        } catch (FileNotFoundException | DocumentException e) {
            throw new ServiceException(e);
        }
        sendFinalReportToEmail(configService.getProperty("recipients.emails"), file);
        sendFinalReportToTelegram(configService.getProperty("recipients.telegrams"));
    }

    private List<Report> createFinalReportFromTasks(LocalDateTime from, LocalDateTime to) throws ServiceException {
        try {
            return taskRepository.getTasksForPeriodAsReport(from, to);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    private String createPDFFinalReport(List<Report> reports) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        String fileToWrite = "WhiteTeam-" + LocalDateTime.now().format(todayFormatterDash) + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(fileToWrite));
        DateTimeFormatter hoursMinutesFormatter = DateTimeFormatter.ofPattern("HH:mm");

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        String title = "White Team\nReport for " + LocalDateTime.now().format(todayFormatterPoint) + "\n\n" +
                "****************************************************";
        Paragraph paragraph = new Paragraph(title);
        document.add(paragraph);

        for (Report report : reports) {
            paragraph = new Paragraph();
            StringBuilder builder = new StringBuilder();
            builder.append("" + report.getUser().getName().toUpperCase() +
                    " has done the following tasks" + "\n\n");
            int i = 1;
            for (Task task : report.getTaskList()) {
                builder.append("Task #" + i++ + "\n");
                builder.append("Time: " + task.getTimeBegin().format(hoursMinutesFormatter) + " - ");
                builder.append(task.getTimeEnd().format(hoursMinutesFormatter) + "\n\n");
                builder.append(task.getJob() + "\n");
                builder.append("-----------------------------------------------\n");
            }
            builder.append("****************************************************");
            paragraph.add(builder.toString());
            document.add(paragraph);
        }
        document.close();
        return fileToWrite;
    }


    private void sendFinalReportToEmail(String recipientsEmails, String file) throws ServiceException {

        final String senderUsername = configService.getProperty("sender.email");
        final String senderPassword = configService.getProperty("sender.password");

//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS
//        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", configService.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.socketFactory.port", configService.getProperty("mail.smtp.socketFactory.port"));
        properties.put("mail.smtp.socketFactory.class", configService.getProperty("mail.smtp.socketFactory.class"));
        properties.put("mail.smtp.auth", configService.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.port", configService.getProperty("mail.smtp.port"));
        properties.put("mail.smtp.ssl.trust", configService.getProperty("mail.smtp.ssl.trust"));

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderUsername, senderPassword);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderUsername));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientsEmails));

            message.setSubject("White Team Report for " + LocalDateTime.now().format(todayFormatterPoint));

            BodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText("Report of a white team is in the attached pdf file.");
            Multipart multipart = new MimeMultipart();
            DataSource source = new FileDataSource(file);
            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file);
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendFinalReportToTelegram(String telegram) {

    }
}
