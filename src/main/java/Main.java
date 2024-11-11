import Animator.Animator;
import activityloader.ActivityLoader;
import activityloader.ActivitySorter;
import activityloader.Preprocessor;
import heatmap.HeatMapper;
import models.Activity;
import settings.Settings;
import stdlib.Stopwatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class Timer {
    Stopwatch timer = new Stopwatch();

    public void reset() {
        timer = new Stopwatch();
    }

    public double elapsedTime() {return timer.elapsedTime();}

    public double lap() {
        double elapsed = elapsedTime();
        System.out.println("Elapsed time: " + elapsed + "s\n");
        reset();
        return elapsed;
    }
}

public class Main {

    private static final Timer timer = new Timer();

    /**
     * Find and load any files in `dataDir` that have extensions matching `Settings.fileTypes`.
     *
     * @param dataDir The directory to search (non-recursive).
     * @return A list of `Activity` objects.
     */
    private static ArrayList<Activity> load(Path dataDir) {
        timer.reset();
        if (Files.notExists(dataDir)) {
            System.out.println("The given path does not exist.");
            return null;
        }
        System.out.println("Loading data from '" + dataDir + "'...");
        ArrayList<Activity> activities = ActivityLoader.loadAll(dataDir);
        System.out.println("Data loaded successfully.");
        System.out.println("Loaded " + activities.size() + " activities.");
        timer.lap();
        return activities;
    }

    /**
     * Does pre-mapping processing to `activities`:
     * - round all coordinates to `Settings.gpsPrecision` number of places
     * - remove "pauses" (if successive coordinates are the same, keep only the first)
     * - remove coordinates outside of the box defined by `Settings.gpsBoundaries`
     * - toss any activities that are empty after the above
     *
     * @param activities The activity list to process.
     * @return An activity list ready for mapping.
     */
    private static ArrayList<Activity> doPreprocessing(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Preprocessing data...");
        activities = Preprocessor.process(activities);
        System.out.println("There are " + activities.size() + " valid activities.");
        timer.lap();
        return activities;
    }

    /**
     * Sort activities from oldest to newest.
     *
     * @param activities The activities to sort.
     * @return Date-sorted activities.
     */
    private static ArrayList<Activity> sort(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Sorting activites by date...");
        activities = new ActivitySorter().sort(activities);
        System.out.println("Sorting complete.");
        timer.lap();
        return activities;
    }

    /**
     * Uses `activities` to instantiate a `HeatMapper` object, which is then given to the
     * `Animator.animate` function.
     *
     * @param activities The list of activities to use for drawing heat maps.
     */
    private static void animate(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Beginning animation...");
        try {
            // The `animate` function will iterate over the `HeatMapper` instance
            // and draw each frame
            Animator.animate(new HeatMapper(activities));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        timer.lap();
    }

    /**
     * The entry point of HeatMapper
     *
     * @param args Any command line arguments. The only expected one is an optional path to a
     *             directory containing activity files.
     */
    public static void main(String[] args) {
        Timer mainTimer = new Timer();
        Settings.load();
        System.out.println();
        // If no path provided on command line, use the path from settings
        Path dataDir = args.length > 0 ? Path.of(args[0]) : Settings.dataPath;

        // Load files into a list of `Activity` objects
        ArrayList<Activity> activities = load(dataDir);
        if (activities == null) {
            System.out.println("No activity files were found in " + dataDir);
            mainTimer.lap();
            return;
        }
        // Clean and modify data
        activities = doPreprocessing(activities);
        if (activities.isEmpty()) {
            System.out.println("There are no valid activities in " + dataDir);
            mainTimer.lap();
            return;
        }
        // Pass date-sorted activity list to animate function
        animate(sort(activities));
        mainTimer.lap();
    }
}
