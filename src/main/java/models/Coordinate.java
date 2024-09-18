package models;

import utilities.Round;

public record Coordinate(double x, double y) {

    public Coordinate round(int places) {
        return new Coordinate(Round.round(x, places), Round.round(y, places));
    }
}
