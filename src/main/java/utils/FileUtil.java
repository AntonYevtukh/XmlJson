package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public void writeToFile(String fileBody, String fileName) {
        try(Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(fileBody);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String readFromFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String tempString;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while ((tempString = bufferedReader.readLine()) != null) {
                stringBuilder.append(tempString);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return stringBuilder.toString();
    }
}
