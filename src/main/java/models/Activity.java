package models;


import java.util.ArrayList;
import java.util.Date;

public record Activity(Date date, ArrayList<Coordinate> coordinates) {

    /**
     * Round all the activity coordinates to the given number of places.
     *
     * @param places The number of decimal places to round to
     * @return A new `Activity` record with the coordinates rounded.
     */
    public Activity withRoundedCoordinates(int places) {
        ArrayList<Coordinate> rounded = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            rounded.add(coordinate.round(places));
        }
        return new Activity(date, rounded);
    }

    public Activity withoutPauses() {
        ArrayList<Coordinate> unpaused = new ArrayList<>();
        unpaused.add(coordinates.getFirst());
        for (int i = 1; i < coordinates.size(); ++i) {
            // If the coordinate is equal to the previous one, call it paused
            if (coordinates.get(i - 1).equals(coordinates.get(i))) continue;
            unpaused.add(coordinates.get(i));
        }
        return new Activity(date, unpaused);
    }
}
