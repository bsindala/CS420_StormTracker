package StormTrack;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Andrew Markley on 11/13/16.
 */
public class StormController {

    private StormData data;

    public StormController() {
        data = new StormData();

        try {
            loadFiles("Info/" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFiles( String f ) throws IOException {
        File folder = new File(this.getClass().getClassLoader().getResource(f).getFile());
        File[] yearFiles = folder.listFiles();

        for ( File year : yearFiles ) {
            CSVReader reader = new CSVReader(new FileReader(year));
            List<String[]> input = reader.readAll();
            removeSpaces(input);

            //Organize storms by storm number
            Map<String,List<String[]>> storms = new HashMap<>();
            for ( String[] row : input ) {
                String stormName = row[0];

                if ( !storms.containsKey(stormName)) {
                    List<String[]> value = new ArrayList<>();
                    value.add(row);
                    storms.put(row[0], value);
                }
                else {
                    storms.get(row[0]).add(row);
                }
            }

            //Create a storm object for each key and add it to StormData object
            for ( String key : storms.keySet() ) {
                List<String[]> rows = storms.get(key);

                String stormID =  rows.get(0)[1] + "" + key;
                Storm storm = new Storm( stormID, rows);

                data.addStorm(stormID, storm);
            }
        }
    }

    private static void removeSpaces(List<String[]> data) {
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < 4; j++) {
                data.get(i)[j] = data.get(i)[j].replaceAll("\\s+", "");
            }
        }
    }

    //TODO: Design a format for input strings.
    public String loadString( String input ) { return null; }

    private StormData getData() { return data; }
    public Set<Integer> getYears() { return data.getYearList(); }
    public Set<String> getStormIDs() { return data.getStormIDList(); }
    public List<Storm> getStormsInYear(int year) { return data.getYear(year); }
    public Storm getStormByID(String stormID) { return data.getStorm(stormID); }
    public List<Storm> getAllStorms() { return data.getAllStorms(); }
}
