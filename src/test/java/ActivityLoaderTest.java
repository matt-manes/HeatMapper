import activityloader.ActivityLoader;
import models.Activity;

import java.util.ArrayList;

public class ActivityLoaderTest {
    public static void main(String[] args) {
        ArrayList<Activity> activities = ActivityLoader.loadAll(ActivitiesDir.dir);
        assert activities.size() > 0;
        System.out.println(activities.size());
    }
}
