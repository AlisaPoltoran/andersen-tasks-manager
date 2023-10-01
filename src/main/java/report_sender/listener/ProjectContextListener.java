package report_sender.listener;

import report_sender.ConfigService;
import report_sender.service.ServiceProvider;
import report_sender.service.UserService;
import report_sender.service.exception.ServiceException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectContextListener implements ServletContextListener {
    private ServiceProvider serviceProvider = ServiceProvider.getInstance();
    private UserService userService = serviceProvider.getUserService();

    private ConfigService configService = serviceProvider.getConfigReaderService();

    public void contextInitialized(ServletContextEvent sce) {
        try {
            userService.createTable();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        try {
            userService.createUsers();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            configService.loadPropertiesFromFile();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}

