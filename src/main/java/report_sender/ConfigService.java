package report_sender;

import report_sender.service.exception.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {
    private static Properties properties;
    private ConfigService() {
    }

    static {
        try {
            loadPropertiesFromFile();
        } catch (ServiceException e) {
            throw new RuntimeException("Can not read properties file");
        }
    }

    public static void loadPropertiesFromFile() throws ServiceException {
        properties = new Properties();
        try (InputStream input = ConfigService.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new ServiceException("Can not read properties file");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
