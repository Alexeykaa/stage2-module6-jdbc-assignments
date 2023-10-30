package jdbc;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
    private static final String POSTGRES_DRIVER_PROPERTY = "postgres.driver";
    private static final String POSTGRES_URL_PROPERTY = "postgres.url";
    private static final String POSTGRES_NAME_PROPERTY = "postgres.name";
    private static final String POSTGRES_PASSWORD_PROPERTY = "postgres.password";
    public static final String APP_PROPERTIES_FILE_NAME = "app.properties";

    private String fileName;
    private Properties prop;

    public AppProperties() {
        fileName = APP_PROPERTIES_FILE_NAME;
    }

    public void load() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            logger.error("Cannot load file property: {}", fileName, e);
        }
        this.prop = prop;
    }

    public String getDriver() {
        return prop.getProperty(POSTGRES_DRIVER_PROPERTY);
    }

    public String getUrl() {
        return prop.getProperty(POSTGRES_URL_PROPERTY);
    }

    public String getName() {
        return prop.getProperty(POSTGRES_NAME_PROPERTY);
    }

    public String getPassword() {
        return prop.getProperty(POSTGRES_PASSWORD_PROPERTY);
    }
}