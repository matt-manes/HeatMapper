package activityloader;

import models.Activity;
import org.junit.jupiter.api.Test;
import settings.Settings;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ActivityLoaderTest {

    @Test
    void loadAll() {
        ArrayList<Activity> activities = ActivityLoader.loadAll(Settings.dataPath);
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            assertAll(() -> assertNotNull(activity.date()),
                    () -> assertTrue(activity.date().after(new Date(0))),
                    () -> assertTrue(activity.date().before(Date.from(Instant.now()))));
        }
    }
}