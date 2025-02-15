package dataman.dmbase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FilePathService {

    @Value("${filePath}") // Injecting VM argument
    private String filePath;

    public void printFilePath() {
        System.out.println("File Path from VM Argument: " + filePath);
    }
}
