package models;


import java.util.ArrayList;
import java.util.Date;

public record Activity(Date date, ArrayList<Coordinate> coordinates) {

    public Activity withRoundedCoordinates(int places) {
        ArrayList<Coordinate> rounded = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            rounded.add(coordinate.round(places));
        }
        return new Activity(date, rounded);
    }
}
