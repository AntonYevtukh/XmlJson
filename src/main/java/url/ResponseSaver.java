package url;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResponseSaver {

    public void saveResponse(String urlString, String fileName) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(urlString).openConnection();

        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                printWriter.write(line);
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
