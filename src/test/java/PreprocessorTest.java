import activityloader.ActivityLoader;
import activityloader.Preprocessor;
import models.Activity;
import models.Coordinate;
import settings.Settings;

import java.util.ArrayList;

public class PreprocessorTest {
    public static void main(String[] args) {
        ArrayList<Activity> activities = ActivityLoader.loadAll(ActivitiesDir.dir);
        ArrayList<Activity> preprocessedActivities = Preprocessor.process(activities);
        // This assumed the boundaries in settings are small enough to remove whole activities
        assert preprocessedActivities.size() < activities.size();
        System.out.println(preprocessedActivities.size());
        // Check coordinates for proper precision
        for (Activity activity : preprocessedActivities) {
            for (Coordinate coordinate : activity.coordinates()) {
                assert hasReducedPrecision(coordinate.x());
                assert hasReducedPrecision(coordinate.y());
            }
        }
    }

    private static boolean hasReducedPrecision(double num) {
        // Convert to a string and count the number of characters after the decimal.
        return String.valueOf(num).split("\\.")[1].length() <= Settings.gpsPrecision;
    }
}
