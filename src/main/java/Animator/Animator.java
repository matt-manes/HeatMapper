package Animator;

import heatmap.HeatMapper;
import models.Pixel;
import settings.Settings;
import stdlib.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

class RedComparator implements Comparator<Pixel> {
    @Override
    public int compare(Pixel a, Pixel b) {
        return a.color().getRed() - b.color().getRed();
    }
}

public class Animator {

    private static int sleepTime = 0;
    private static int frameCount = 0;

    public static void animate(HeatMapper heatmaps) throws InterruptedException {
        Dimension screen = Settings.getCanvasSize();
        StdDraw.setCanvasSize(screen.width, screen.height);
        FrameGenerator frames = new FrameGenerator(heatmaps);
        StdDraw.show(sleepTime);
        StdDraw.setPenRadius(0.001);
        for (ArrayList<Pixel> frame : frames) {
            ++frameCount;
            StdDraw.clear(Color.BLACK);
            drawFrame(frame);
            StdDraw.show(sleepTime);
        }
    }

    private static void drawFrame(ArrayList<Pixel> pixels) {
        // Sort so that blue points are drawn first and red are drawn last.
        // This way red is more visible when there's overlap.
        pixels.sort(new RedComparator());
        for (Pixel pixel : pixels) {
            StdDraw.setPenColor(pixel.color());
            StdDraw.point(pixel.coordinate().x(), pixel.coordinate().y());
        }
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(1, 0, String.valueOf(frameCount));
    }
}
