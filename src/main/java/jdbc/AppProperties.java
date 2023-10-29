package jdbc;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

    private String fileName = "app.properties";
    private Properties prop;

    public AppProperties() {
    }

    public AppProperties(String fileName) {
        this.fileName = fileName;
    }

    public void load() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            logger.error("Cannot load file property: ", fileName, e);
        }
        this.prop = prop;
    }

    public String getDriver() {
        return prop.getProperty("postgres.driver");
    }

    public String getUrl() {
        return prop.getProperty("postgres.url");
    }

    public String getName() {
        return prop.getProperty("postgres.name");
    }

    public String getPassword() {
        return prop.getProperty("postgres.password");
    }
}