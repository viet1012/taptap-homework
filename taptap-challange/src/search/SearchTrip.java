package search;


import model.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SearchTrip implements Function<String, List<Trip>> {
    private static final String IN = "IN";

    @Override
    public List<Trip> apply(String cardId) {
        //Find row ids by input card id
        Map<String, List<Integer>> rowIndexes = findRowIndexByCardId(cardId);

        //Find direction(IN/OUT) and timestamp by rowIndexes to collect trips
        List<String> matchedDirections = findDirectionByMatchedIndex(rowIndexes);
        List<String> matchedTimestamp =  findTimestampByMatchedIndex(rowIndexes);

        //Collect trips
        List<Trip> trips = new ArrayList<>();
        Trip latestTrip = null;
        for (int index = 0; index < matchedDirections.size(); index++) {
            if (matchedDirections.get(index).equals(IN)) {
                latestTrip = new Trip(matchedTimestamp.get(index));
            } else {
                try {
                    latestTrip.setArrivalTimeStr(matchedTimestamp.get(index));
                    trips.add(latestTrip);
                    latestTrip = null;
                }catch (Exception e){
                    //todo handle bad simulated data
                }
            }
        }
        return trips;
    }

    private Map<String, List<Integer>> findRowIndexByCardId(String cardId) {
        try {
            return SearchUtil.searchCard(cardId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_MAP;
    }

    private List<String> findDirectionByMatchedIndex(Map<String, List<Integer>> matchedIndex) {
        try {
            return SearchUtil.searchDirection(matchedIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private List<String> findTimestampByMatchedIndex(Map<String, List<Integer>> matchedIndex) {
        try {
            return SearchUtil.searchTimestamp(matchedIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
