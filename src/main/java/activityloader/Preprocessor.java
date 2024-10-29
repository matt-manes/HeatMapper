package activityloader;

import models.Activity;
import models.Coordinate;
import settings.ActivityBoundaries;
import settings.Settings;

import java.util.ArrayList;

/**
 * Used for prepping activity data before sorting and frame generation.
 */
public class Preprocessor {

    static private final ActivityBoundaries bounds = Settings.gpsBoundaries;

    /**
     * Remove coordinates that are outside of `gpsBoundaries` in the settings
     * and reduce GPS coordinate precision according to the settings.
     * If an activity has no coordinates, it will be removed from the list entirely.
     *
     * @param activities The list of activities to process.
     * @return A list of activities with rounded gps coordinates and no "paused" coordinates.
     */
    public static ArrayList<Activity> process(ArrayList<Activity> activities) {
        ArrayList<Activity> processedActivities = new ArrayList<>();
        int precision = Settings.gpsPrecision;
        for (Activity activity : activities) {
            // No recorded coordinates, likely an indoor activity
            if (activity.coordinates().isEmpty()) continue;
        
            activity = activity.withoutPauses().withRoundedCoordinates(precision);
            ArrayList<Coordinate> validCoordinates = filterCoordinates(activity.coordinates());
            // If no coordinates are valid, skip the whole activity
            if (validCoordinates.isEmpty()) continue;
            processedActivities.add(new Activity(activity.date(), validCoordinates));
        }
        return processedActivities;
    }

    /**
     * @param coordinate The coordinate to check.
     * @return Whether the coordinate is within the bounds defined in `Settings`.
     */
    private static boolean isInBounds(Coordinate coordinate) {
        return (bounds.west < coordinate.x() && coordinate.x() < bounds.east) &&
                (bounds.south < coordinate.y() && coordinate.y() < bounds.north);
    }

    /**
     * Filters out coordinates that are outside the boundaries defined in `Settings`.
     *
     * @param coordinates A list of coordinates to filter.
     * @return The filtered list of coordinates.
     */
    private static ArrayList<Coordinate> filterCoordinates(ArrayList<Coordinate> coordinates) {
        ArrayList<Coordinate> validCoordinates = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            if (isInBounds(coordinate)) validCoordinates.add(coordinate);
        }
        return validCoordinates;
    }
}
