package activityloader;

import filehandler.FITReader;
import filehandler.FileType;
import filehandler.GPXReader;
import filehandler.Globber;
import models.Activity;
import settings.Settings;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ActivityLoader {

    public static Activity load(Path path) {
        String name = path.toString();
        if (FileType.isFITOrFITGZ(name)) {
            return FITReader.parse(path);
        } else if (FileType.isGPX(name)) {
            return GPXReader.parse(path);
        }
        throw new IllegalArgumentException("No reader for " + name + " found.");
    }

    public static ArrayList<Activity> loadAll(Path dir) {
        ArrayList<Activity> activities = new ArrayList<>();
        List<Path> paths = Globber.glob(dir);
        for (Path path : paths) {
            activities.add(load(path).withRoundedCoordinates(Settings.gpsPrecision));
        }
        // TODO: Add gps coordinate rounding
        return activities;
    }


}
