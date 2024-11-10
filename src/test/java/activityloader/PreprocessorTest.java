package activityloader;

import models.Activity;
import models.Coordinate;
import org.junit.jupiter.api.Test;
import settings.ActivityBoundaries;
import settings.Settings;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PreprocessorTest {
    ActivityBoundaries bounds = Settings.gpsBoundaries;
    int gpsPrecision = Settings.gpsPrecision;

    int numDecimals(double n) {
        // Convert to a string and count the number of characters after the decimal.
        return String.valueOf(n).split("\\.")[1].length();
    }

    boolean hasReducedPrecision(Coordinate coordinate) {
        return (numDecimals(coordinate.x()) <= gpsPrecision) &&
                (numDecimals(coordinate.x()) <= gpsPrecision);
    }

    boolean coordinateInBounds(Coordinate coordinate) {
        return (bounds.west <= coordinate.x() && coordinate.x() <= bounds.east) &&
                (bounds.south <= coordinate.y() && coordinate.y() <= bounds.north);
    }

    boolean coordinateIsValid(Coordinate coordinate) {
        return hasReducedPrecision(coordinate) && coordinateInBounds(coordinate);
    }

    void testCoordinates(Activity activity) {
        for (Coordinate coordinate : activity.coordinates()) {
            assertTrue(coordinateIsValid(coordinate), coordinate.toString());
        }
    }

    void testDate(Activity activity) {
        assertAll(() -> assertNotNull(activity.date()),
                () -> assertTrue(activity.date().after(new Date(0))),
                () -> assertTrue(activity.date().before(Date.from(Instant.now()))));
    }

    @Test
    void process() {
        ArrayList<Activity> activities = ActivityLoader.loadAll(Settings.dataPath);
        ArrayList<Activity> processedActivities = Preprocessor.process(activities);
        assertTrue(processedActivities.size() <= activities.size());
        for (Activity activity : processedActivities) {
            assertFalse(activity.coordinates().isEmpty());
            testDate(activity);
            testCoordinates(activity);
        }
    }
}