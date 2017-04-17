import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by astrg on 15.02.2017.
 */
public class PostRequest {

    private HttpsURLConnection conn;


    String send(){
        try {
            URL url = new URL("https://asr.yandex.net/asr_xml?uuid=015613cb744628b58fb536d496daa1e6&key=4af3be91-3618-4b1c-ac91-143ca2cc16ea&topic=queries");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "audio/x-wav");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Path path = Paths.get("D:\\speech.wav");
            OutputStream os = conn.getOutputStream();
            os.write(Files.readAllBytes(path));

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            os.close();
            return new String(response.toString().getBytes(),"UTF8");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }

}
