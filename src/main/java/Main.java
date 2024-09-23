import Animator.Animator;
import activityloader.ActivityLoader;
import activityloader.ActivitySorter;
import activityloader.Preprocessor;
import heatmap.HeatMapper;
import models.Activity;
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

    public static void main(String[] args) {
        System.out.println();
        Path defaultData = Path.of(System.getProperty("user.dir") + Path.of("/data/activities"));
        Path data = args.length > 0 ? Path.of(args[0]) : defaultData;

        Timer timer = new Timer();

        System.out.println("Loading data from '" + data + "'...");
        ArrayList<Activity> activities = ActivityLoader.loadAll(data);
        System.out.println("Data loaded successfully.");
        System.out.println("Loaded " + activities.size() + " activities.");
        timer.lap();

        System.out.println("Preprocessing data...");
        activities = Preprocessor.process(activities);
        System.out.println("Data set now has " + activities.size() + " valid activities.");
        timer.lap();

        System.out.println("Sorting activites by date...");
        activities = new ActivitySorter().sort(activities);
        System.out.println("Sorting complete.");
        timer.lap();

        System.out.println("Generating heat maps...");
        HeatMapper heatmaps = new HeatMapper(activities);
        System.out.println("Generation complete.");
        timer.lap();

        System.out.println("Beginning animation...");
        try {
            Animator.animate(heatmaps);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        timer.lap();
    }
}
