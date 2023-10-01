package report_sender;

import report_sender.service.exception.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {
    private static final ConfigService INSTANCE = new ConfigService();
    private Properties properties;
    private ConfigService() {
    }

    public static ConfigService getInstance() {
        return INSTANCE;
    }

    public void loadPropertiesFromFile() throws ServiceException {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new ServiceException("Can not read properties file");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
