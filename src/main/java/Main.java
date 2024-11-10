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

    private static ArrayList<Activity> doPreprocessing(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Preprocessing data...");
        activities = Preprocessor.process(activities);
        System.out.println("There are " + activities.size() + " valid activities.");
        timer.lap();
        return activities;
    }

    private static ArrayList<Activity> sort(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Sorting activites by date...");
        activities = new ActivitySorter().sort(activities);
        System.out.println("Sorting complete.");
        timer.lap();
        return activities;
    }

    private static void animate(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Beginning animation...");
        try {
            Animator.animate(new HeatMapper(activities));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        timer.lap();
    }

    public static void main(String[] args) {
        Timer mainTimer = new Timer();
        Settings.load();
        System.out.println();
        Path dataDir = args.length > 0 ? Path.of(args[0]) : Settings.dataPath;

        ArrayList<Activity> activities = load(dataDir);
        if (activities == null) {
            mainTimer.lap();
            return;
        }
        activities = doPreprocessing(activities);
        if (activities.isEmpty()) {
            System.out.println("There are no valid activities in the given directory.");
            mainTimer.lap();
            return;
        }
        animate(sort(activities));
        mainTimer.lap();
    }
}
