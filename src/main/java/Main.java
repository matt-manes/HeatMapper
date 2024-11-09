import Animator.Animator;
import activityloader.ActivityLoader;
import activityloader.ActivitySorter;
import activityloader.Preprocessor;
import heatmap.HeatMapper;
import models.Activity;
import settings.ActivitiesDir;
import stdlib.Stopwatch;

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

    private static ArrayList<Activity> load(Path data) {
        timer.reset();
        System.out.println("Loading data from '" + data + "'...");
        ArrayList<Activity> activities = ActivityLoader.loadAll(data);
        System.out.println("Data loaded successfully.");
        System.out.println("Loaded " + activities.size() + " activities.");
        timer.lap();
        return activities;
    }

    private static ArrayList<Activity> doPreprocessing(ArrayList<Activity> activities) {
        timer.reset();
        System.out.println("Preprocessing data...");
        activities = Preprocessor.process(activities);
        System.out.println("Data set now has " + activities.size() + " valid activities.");
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
        System.out.println();
        Path defaultData = ActivitiesDir.dir;
        Path data = args.length > 0 ? Path.of(args[0]) : defaultData;
        animate(sort(doPreprocessing(load(data))));
        mainTimer.lap();
    }
}
