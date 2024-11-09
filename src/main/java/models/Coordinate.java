package models;

import utilities.Round;

public record Coordinate(double x, double y) {

    /**
     * Round this coordinate to the given number of places.
     *
     * @param places The number of decimal places to round to.
     * @return A new `Coordinate` object rounded to the given precision.
     */
    public Coordinate round(int places) {
        return new Coordinate(Round.round(x, places), Round.round(y, places));
    }
}
