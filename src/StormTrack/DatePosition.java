package StormTrack;

import com.vividsolutions.jts.geom.Coordinate;

import java.time.LocalDateTime;

/**
 * Created by Andrew Markley on 11/13/16.
 */
class DatePosition implements Comparable<DatePosition>{
    private LocalDateTime date;
    private Coordinate coords;

    /**
     * Constructor foa DatePosition object which encapsulates a storm's position in the world at a particular
     * LocalDateTime.
     * @param date The time of the storm's position
     * @param latCoord The N/S coordinate of the storm which accepts the format "123N" or "456S" - this first example
     *                 would be translated into a coordinate of 12.3º North
     * @param longCoord The E/W coordinate of the storm which accepts the format "123E" or "456W"
     */
    public DatePosition(LocalDateTime date, String latCoord, String longCoord ) {
        this.date = date;
        double latitude = Double.parseDouble(latCoord.substring(0, latCoord.length() - 1)) / 10.0;
        double longitude = Double.parseDouble(longCoord.substring(0, longCoord.length() - 1)) / 10.0;

        if ( latitude > 90 || longitude > 180 )
            throw new IllegalArgumentException("Invalid lat/long supplied, input provided was: "
                + latCoord + " Latitude, " + longCoord + " Longitude");

        //Check for whether Latitude is North or South, throw an exception if it is neither
        //N or S.
        char direction = latCoord.charAt(latCoord.length()-1);

        if ( direction == 'S' || direction =='s' )
            latitude *= -1;
        else if ( direction != 'N' && direction != 'n' )
            throw new IllegalArgumentException("Latitude must be either N or S");

        //Check for whether Longitude is East or West, throw an exception if it is neither
        //E or W.
        direction = longCoord.charAt(longCoord.length()-1);
        if ( direction == 'W' || direction == 'w' )
            longitude *= -1;
        else if ( direction != 'E' && direction != 'e' )
            throw new IllegalArgumentException(direction + "Longitude must be either E or W");

        coords = new Coordinate(longitude, latitude);
    }

    /**
     * Provides the date and time of the recorded position of a storm.
     * @return a LocalDateTime marking the date and time corresponding to the coordinates of a storm
     */
    public LocalDateTime getDate() { return date; }

    /**
     * Provides the N/S E/W coordinates of a storm.
     * @return a Coordinate object with the location data for a storm at a particular point in time.
     */
    public Coordinate getCoordinates() { return coords; }

    @Override
    public int compareTo(DatePosition o) {
        return this.getDate().compareTo(o.getDate());
    }

    public String toString() {
        return date + " : " + coords + "\n";
    }
}
