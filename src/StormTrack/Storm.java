package StormTrack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew Markley on 11/13/16.
 */
public class Storm {
    private List<DatePosition> list;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String stormID;

    /**
     * Creates a storm with a supplied stormID and a List of String arrays containing information in the format
     * { Date in the form YYYYMMDDHH, Latitude in the form 123N or 456S, Longitude in the form 123E or 456W }
     * @param stormID the identifier of a storm
     * @param stormTracks a List of String arrays
     */
    public Storm(String stormID, List<String[]> stormTracks) {
        list = new ArrayList<>();
        this.stormID = stormID;

        //Populate list of DatePosition with contents of array
        stormTracks.forEach( row ->
            list.add( new DatePosition( convertDate(row[1]), row[2], row[3])));

        Collections.sort(list);

        startDate = list.get(0).getDate();
        endDate = list.get( list.size()-1 ).getDate();
    }


    private static LocalDateTime convertDate(String s) {
        //Formatted with YYYYMMDDHH
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("uuuuMMddHH"));
    }


    public List<DatePosition> getHistory() { return list; }

    /**
     * Method that provides the first
     * @return
     */
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getStormID() { return stormID; }

    public String toString() {
        return stormID + " : From " + startDate + " to " + endDate;
    }
}
