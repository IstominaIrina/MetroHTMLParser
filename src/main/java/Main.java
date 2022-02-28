import java.io.IOException;

public class Main {

    private static String dataFile = "src/main/resources/map.json";

    public static void main(String[] args) throws IOException {

        ParseHTML test = new ParseHTML();
        test.parse();

        MetroParser parser = new MetroParser(dataFile);
        parser.startParse();
    }
}








