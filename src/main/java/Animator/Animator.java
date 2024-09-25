package Animator;

import heatmap.HeatMapper;
import models.Pixel;
import settings.Settings;
import stdlib.StdDraw;

import java.awt.*;
import java.util.ArrayList;

public class Animator {

    private static final int sleepTime = Settings.animationFrameWait;
    private static int frameCount = 0;

    public static void animate(HeatMapper heatmaps) throws InterruptedException {
        setCanvasSize();
        // Calling this to set `defer` to `true`, can't set it directly :/
        show();
        StdDraw.setPenRadius(0.001);
        // Draw all frames
        for (ArrayList<Pixel> frame : new FrameGenerator(heatmaps)) {
            ++frameCount;
            render(frame);
        }
    }

    /**
     * Clear the canvas, draw the frame, and then show it.
     *
     * @param frame The frame to render.
     */
    private static void render(ArrayList<Pixel> frame) {
        clear();
        drawFrame(frame);
        show();
    }


    /**
     * Show the current contents and set `defer` to `true`.
     * <p>
     * If `defer` is `false`, `StdDraw` will try to show each pixel as it is drawn,
     * instead of the whole frame at once.
     */
    private static void show() {StdDraw.show(sleepTime);}

    /**
     * Set canvas size according to `Settings`.
     */
    private static void setCanvasSize() {
        Dimension screen = Settings.getCanvasSize();
        StdDraw.setCanvasSize(screen.width, screen.height);
    }

    /**
     * Clear the canvas.
     */
    private static void clear() {StdDraw.clear(Color.BLACK);}

    /**
     * Draw a frame from a list of pixels.
     *
     * @param pixels The pixels to draw.
     */
    private static void drawFrame(ArrayList<Pixel> pixels) {
        for (Pixel pixel : pixels) {
            StdDraw.setPenColor(pixel.color());
            StdDraw.point(pixel.coordinate().x(), pixel.coordinate().y());
        }
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(1, 0, String.valueOf(frameCount));
    }
}
