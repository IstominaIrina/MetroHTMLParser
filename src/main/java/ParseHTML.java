import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

public class ParseHTML {

    public void parse() throws IOException {

        Document doc = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();

        Elements metroElements = doc.select("#metrodata");
        Elements numberLineElements = metroElements.select(".js-metro-line");

        JSONObject lines = new JSONObject();
        JSONArray infoLines = new JSONArray();
        JSONObject station = new JSONObject();

        for (Element element : numberLineElements) {
            String numberLine = element.attr("data-line");
            String nameLine = element.text();

            JSONObject linesTest = new JSONObject();

            linesTest.put("name", nameLine);
            linesTest.put("number", numberLine);
            infoLines.add(linesTest);

            Elements nameStationElements = metroElements.select(".js-metro-stations[data-line=" + numberLine + "]");
            Elements nameStation = nameStationElements.select(".name");

            JSONArray infoStation = new JSONArray();


            for (Element element1 : nameStation) {
                infoStation.add(element1.text());
            }
            station.put(numberLine, infoStation);

            for (Element element2 : nameStationElements) {
                for (Element elementCon : element2.select("p")) {
                    JSONArray infoConStatLine = new JSONArray();
                    boolean flag = true;

                    Elements infoConnect = elementCon.select(".t-icon-metroln");
                    for (Element connect : infoConnect) {
                        String infoConnectStat = connect.select(".t-icon-metroln").attr("title");
                        int indexA = infoConnectStat.indexOf("«");
                        int indexB = infoConnectStat.lastIndexOf("»");
                        String nameConnectStat = infoConnectStat.substring(indexA + 1, indexB);

                        String text = connect.className();
                        int indexC = text.lastIndexOf("-");
                        String numberLineCon = text.substring(indexC + 1);

                        String nameStation2 = elementCon.select(".name").text();

                        JSONObject сonnectStatLine1 = new JSONObject();
                        JSONObject сonnectStatLine2 = new JSONObject();


                        сonnectStatLine1.put("line", numberLine);
                        сonnectStatLine1.put("station", nameStation2);

                        сonnectStatLine2.put("line", numberLineCon);
                        сonnectStatLine2.put("station", nameConnectStat);

                        if (flag) infoConStatLine.add(сonnectStatLine1);

                        infoConStatLine.add(сonnectStatLine2);
                        flag = false;

                    }
                }
            }
        }
        lines.put("lines", infoLines);
        lines.put("stations", station);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(String.valueOf(lines));
        String prettyJsonString = gson.toJson(je);

        FileWriter file = new FileWriter("src/main/resources/map.json");
        file.write(prettyJsonString);
        file.flush();
        file.close();
    }
}
