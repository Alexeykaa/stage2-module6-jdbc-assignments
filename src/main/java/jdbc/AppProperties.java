package jdbc;

import java.io.IOException;
import java.util.Properties;

public class AppProperties {

    private String fileName = "app.properties";
    private Properties prop;

    public AppProperties() {
    }

    public AppProperties(String fileName) {
        this.fileName = fileName;
    }

    public void load() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream(fileName));
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