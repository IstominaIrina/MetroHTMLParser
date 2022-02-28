import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.HashMap;

public class MetroParser {
    String path;

    MetroParser(String path) {
        this.path = path;
    }

    public void startParse() {

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(this.path)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray linesJsonArray = (JSONArray) jsonObject.get("lines");
            JSONObject jsonObjectSt = (JSONObject) jsonObject.get("stations");

            HashMap<String, Integer> map = new HashMap<>();

            for (Object it : linesJsonArray) {
                JSONObject linesJsonObject = (JSONObject) it;
                String nameLines = (String) linesJsonObject.get("name");
                String numberLines = (String) linesJsonObject.get("number");
                JSONArray stationJsonArray = (JSONArray) jsonObjectSt.get(numberLines);

                System.out.println(nameLines + " - " + "номер " + numberLines + " (количество станций " + stationJsonArray.size()  + ").");
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}

