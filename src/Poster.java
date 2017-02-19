import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Poster {

    /**
     * 'POST' requests via HTTP
     */
    public String postit(String reqURL, Document doc) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) new URL(reqURL).openConnection();
        httpCon.setRequestMethod("POST");
        httpCon.setDoInput(true);
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");

        new XMLOutputter(Format.getPrettyFormat()).output(doc, httpCon.getOutputStream());

        BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
