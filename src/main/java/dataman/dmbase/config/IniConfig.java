package dataman.dmbase.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import dataman.dmbase.exception.IncorrectParameters;
import lombok.Data;

@Data
public class IniConfig {

    private Properties properties = new Properties();

    public IniConfig(String configPath) {
    	
    	if(configPath == null) {
    		throw new IncorrectParameters("Given ConfigFile Path is Null(configFilePath = NULL)");
    	}
    	
        try (FileInputStream input = new FileInputStream(configPath)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading properties file: " + configPath);
            ex.printStackTrace();
        }
    }                                                                                                                                  

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}


