package report_sender.service;

import report_sender.ConfigService;
import report_sender.service.impl.JsonParserServiceImpl;
import report_sender.service.impl.SavingReportServiceImpl;
import report_sender.service.impl.SendingFinalReportServiceImpl;
import report_sender.service.impl.UserServiceImpl;

public class ServiceProvider {
    private final static ServiceProvider INSTANCE= new ServiceProvider();
    private ServiceProvider() {
    }

    public static ServiceProvider getInstance() {
        return INSTANCE;
    }

    public SavingReportService getSavingReportService() {
        return SavingReportServiceImpl.getInstance();
    }

    public JsonParserService getJsonParserService() {
        return JsonParserServiceImpl.getInstance();
    }

    public SendingFinalReportService getSendingFinalReportService() {
        return SendingFinalReportServiceImpl.getInstance();
    }

    public UserService getUserService() {
        return UserServiceImpl.getInstance();
    }

    public ConfigService getConfigReaderService() { return ConfigService.getInstance(); }
}
