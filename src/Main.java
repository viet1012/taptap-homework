package src;

import model.Trip;
import search.SearchTrip;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String cardId = "CID000224";

        //Search and monitor performance
        long start = System.currentTimeMillis();
        SearchTrip searchTrip = new SearchTrip();
        List<Trip> trips = searchTrip.apply(cardId);
        long end = System.currentTimeMillis();

        //Print result to console
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            System.out.printf("Trip #%d, departure:%d, arrival:%d take %d ms\n",i+1,trip.getDepartureTime(),trip.getArrivalTime(),(trip.getArrivalTime()-trip.getDepartureTime()));
        }
        System.out.printf("Customer %s have %d trips\n", cardId, trips.size());
        System.out.printf("Search Api take %d ms\n", (end - start));

    }
}