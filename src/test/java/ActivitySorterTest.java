import activityloader.ActivityLoader;
import activityloader.ActivitySorter;
import models.Activity;

import java.util.ArrayList;
import java.util.Date;

public class ActivitySorterTest {
    public static void main(String[] args) {
        long x = 1_000_000_000;
        Date[] dates =
                {new Date(1000 * x), new Date(500 * x), new Date(300 * x), new Date(100 * x)};

        ArrayList<Activity> activities = new ArrayList<>();
        for (Date date : dates) activities.add(new Activity(date, null));
        activities = new ActivitySorter().sort(activities);
        for (int i = 0; i < activities.size(); ++i) {
            assert activities.get(i).date().equals(dates[dates.length - 1 - i]);
            System.out.println(activities.get(i).date());
        }

        // Do essentially the same test but on actual activity data.
        activities = new ActivitySorter().sort(ActivityLoader.loadAll(ActivitiesDir.dir));
        for (int i = 0; i < activities.size() - 1; ++i) {
            // Check that each date is earlier than the next
            assert activities.get(i).date().before(activities.get(i + 1).date());
        }
        System.out.println();
        System.out.println(activities.getFirst().date());
        System.out.println(activities.getLast().date());
    }
}
