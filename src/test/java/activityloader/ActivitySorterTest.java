package activityloader;

import models.Activity;
import org.junit.jupiter.api.Test;
import settings.ActivitiesDir;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ActivitySorterTest {

    @Test
    void sort() {
        long x = 1_000_000_000;
        // Reverse ordered dates
        Date[] dates =
                {new Date(1000 * x), new Date(500 * x), new Date(300 * x), new Date(100 * x)};

        ArrayList<Activity> activities = new ArrayList<>();
        for (Date date : dates) activities.add(new Activity(date, null));
        activities = new ActivitySorter().sort(activities);
        for (int i = 0; i < activities.size(); ++i) {
            assertEquals(activities.get(i).date(), dates[dates.length - 1 - i]);
        }
    }

    @Test
    void sortActivities() {
        ArrayList<Activity> activities =
                new ActivitySorter().sort(ActivityLoader.loadAll(ActivitiesDir.dir));
        for (int i = 0; i < activities.size() - 1; ++i) {
            assertTrue(activities.get(i).date().before(activities.get(i + 1).date()));
        }
    }
}