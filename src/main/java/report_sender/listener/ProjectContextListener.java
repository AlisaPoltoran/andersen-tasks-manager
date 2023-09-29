package report_sender.listener;

import report_sender.service.ServiceProvider;
import report_sender.service.UserService;
import report_sender.service.exception.ServiceException;
import report_sender.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectContextListener implements ServletContextListener {
    private ServiceProvider serviceProvider = ServiceProvider.getInstance();
    private UserService userService = serviceProvider.getUserService();

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
    }
}

