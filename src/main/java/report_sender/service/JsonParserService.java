package report_sender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import report_sender.entity.Report;
import report_sender.entity.User;

public interface JsonParserService {
    User getUserFromJson(String jsonUser) throws JsonProcessingException;

    Report getReportFromJson(String jsonReport);
}
