package util;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import static com.json.parsers.JsonParserFactory.getInstance;

public class Utils {


    public static final String wget(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line, result = "";
        while ((line = inputStreamReader.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public static final Map fromJson(String json) {
        Map map = null;
        try {
            JsonParserFactory factory = getInstance();
            JSONParser parser = factory.newJsonParser();
            map = parser.parseJson(json);

        } catch (RuntimeException e) {
            System.out.println("Json parsing failed for " + json);
            throw e;
        }
        return map;
    }

}
