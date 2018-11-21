package StormTrack;

import java.util.*;

/**
 * Created by Andrew Markley on 11/13/16.
 */
public class StormData {

    private Map<String,Storm> stormIDMap;
    private Map<Integer,List<Storm>> yearMap;

    public StormData() {
        stormIDMap = new HashMap<>();
        yearMap = new HashMap<>();
    }

    public void addStorm(String stormID, Storm storm) {
        if (!stormIDMap.containsKey(stormID))
            stormIDMap.put(stormID, storm);
        else
            throw new UnsupportedOperationException("Cannot add two storms with the same name.");

        int year = Integer.parseInt(stormID.substring(0, 4));

        if (!yearMap.containsKey(year)) {
            List<Storm> stormList = new ArrayList<>();
            stormList.add(storm);
            yearMap.put(year, stormList);
        }
        else
            yearMap.get(year).add(storm);
    }

    /**
     * Returns a storm by its ID
     * @param stormID
     * @return
     */
    public Storm getStorm(String stormID) { return stormIDMap.get(stormID); }
    public List<Storm> getYear(int year ) { return yearMap.get(year); }
    public List<Storm> getYear(String year) { return yearMap.get(Integer.parseInt(year)); }
    public Set<Integer> getYearList() { return yearMap.keySet(); }
    public Set<String> getStormIDList() { return stormIDMap.keySet(); }

    @SuppressWarnings("unchecked")
    public ArrayList<Storm> getAllStorms() { return new ArrayList(stormIDMap.values()); }


}
