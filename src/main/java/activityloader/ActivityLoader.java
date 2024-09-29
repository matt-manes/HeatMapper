package activityloader;

import filehandler.FITReader;
import filehandler.FileType;
import filehandler.GPXReader;
import filehandler.Globber;
import models.Activity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ActivityLoader {

    /**
     * Load an activity.
     *
     * @param path The activity file to load.
     * @return The loaded {@link Activity} record.
     */
    public static Activity load(Path path) {
        String name = path.toString();
        if (FileType.isFITOrFITGZ(name)) {
            return FITReader.parse(path);
        } else if (FileType.isGPX(name)) {
            return GPXReader.parse(path);
        }
        throw new IllegalArgumentException("No reader for " + name + " found.");
    }

    /**
     * Multithread activity file parsing.
     *
     * @param files The files to be loaded.
     * @return A list of Futures that return a loaded {@link Activity} record.
     */
    private static ArrayList<Future<Activity>> readAll(List<Path> files) {
        ArrayList<Future<Activity>> futures;
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            futures = new ArrayList<>();
            for (Path file : files) {
                futures.add(executor.submit(() -> load(file)));
            }
        }
        return futures;
    }

    /**
     * Load all activity files in the given directory, recursively.
     *
     * @param dir The directory to search for file in.
     * @return A list of loaded {@link Activity} records.
     */
    public static ArrayList<Activity> loadAll(Path dir) {
        ArrayList<Activity> activities = new ArrayList<>();
        List<Path> paths = Globber.glob(dir);
        ArrayList<Future<Activity>> futures = readAll(paths);
        for (Future<Activity> future : futures) {
            try {
                activities.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return activities;
    }
}
