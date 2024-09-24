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
    static private ActivityBoundaries bounds = Settings.gpsBoundaries;
    static private int precision = Settings.gpsPrecision;

    /**
     * Remove coordinates that are outside of `gpsBoundaries` in the settings
     * and reduce GPS coordinate precision according to the settings.
     * If an activity has no coordinates, it will be removed from the list entirely.
     *
     * @param activities
     * @return
     */
    public static ArrayList<Activity> process(ArrayList<Activity> activities) {
        ArrayList<Activity> processedActivities = new ArrayList<>();
        for (Activity activity : activities) {
            ArrayList<Coordinate> validCoordinates = filterCoordinates(activity.coordinates());
            // If no coordinates are valid, skip the whole activity
            if (validCoordinates.isEmpty()) continue;
            // Add the activity with the coordinates rounded
            processedActivities.add(
                    new Activity(activity.date(), validCoordinates).withRoundedCoordinates(
                            precision).withoutPauses());
        }
        return processedActivities;
    }

    /**
     * Returns wether `coordinate` is within the bounds defined in `Settings`.
     *
     * @param coordinate
     * @return
     */
    private static boolean isInBounds(Coordinate coordinate) {
        return (bounds.west < coordinate.x() && coordinate.x() < bounds.east) &&
                (bounds.south < coordinate.y() && coordinate.y() < bounds.north);
    }

    /**
     * Filters out coordinates that are outside the boundaries defined in `Settings`.
     *
     * @param coordinates
     * @return
     */
    private static ArrayList<Coordinate> filterCoordinates(ArrayList<Coordinate> coordinates) {
        ArrayList<Coordinate> validCoordinates = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            if (isInBounds(coordinate)) validCoordinates.add(coordinate);
        }
        return validCoordinates;
    }
}
