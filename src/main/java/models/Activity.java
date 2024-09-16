package models;

import java.util.ArrayList;
import java.util.Date;

public record Activity(Date date, ArrayList<Coordinate> coordinates) {
}
