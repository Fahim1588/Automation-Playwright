package playwrightPractice.utilities;

import java.util.Properties;

public class Config {
    Properties configFile;

    public Config() {
        configFile = new Properties();
        try {
            configFile.load(this.getClass().getResourceAsStream("/config/config.properties"));
        } catch (Exception eta) {
            eta.printStackTrace();
        }
    }

    public String getProperty(String key) {
        String value = this.configFile.getProperty(key);
        return value;
    }

    public String setProperty(String key, String value) {
        configFile.setProperty(key, value);
        return configFile.getProperty(key);
    }
}
