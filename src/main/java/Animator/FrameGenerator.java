package Animator;

import heatmap.HeatMapper;
import models.Coordinate;
import models.Pixel;
import settings.ActivityBoundaries;
import settings.Settings;
import utilities.Blend;
import utilities.Curve;
import utilities.Range;
import utilities.Scale;

import java.awt.*;
import java.util.*;

/**
 * Compares the red channel of two `Pixel` objects.
 */
class RedComparator implements Comparator<Pixel> {
    @Override
    public int compare(Pixel a, Pixel b) {
        return a.color().getRed() - b.color().getRed();
    }
}

public class FrameGenerator implements Iterable<ArrayList<Pixel>> {

    /**
     * @param heatmaps The `HeatMapper` object to use for generating frames.
     */
    public FrameGenerator(HeatMapper heatmaps) {
        this.heatmaps = heatmaps;
        initParams();
    }

    // Scales to map gps coordinates and counts to pixel coordinates and color
    private Scale xScaler, yScaler, redScaler;

    private final HeatMapper heatmaps;
    private Curve colorCurve;
    private final RedComparator redComparator = new RedComparator();

    /**
     * Initialize scalers and color curve from heatmaps.
     */
    private void initParams() {
        Range heatRange = heatmaps.getHeatRange();
        ActivityBoundaries bounds = Settings.gpsBoundaries;
        Range westEastRange = new Range(bounds.west, bounds.east);
        Range southNorthRange = new Range(bounds.south, bounds.north);
        // `StdDraw` default expects a number between 0 and 1
        xScaler = new Scale(westEastRange, new Range(0, 1));
        yScaler = new Scale(southNorthRange, new Range(0, 1));

        redScaler = new Scale(heatRange, new Range(0, 255));
        //colorCurve = new Curve(0.995, new Range(0, 255));
        colorCurve = new Curve(0.999, new Range(0, 1));
    }

    /**
     * Generate a color from a coordinate's count.
     *
     * @param heat An int between 0 and 255, inclusive.
     * @return A `Color` between blue and red.
     */
    private Color getColor(int heat) {
        //int red = (int) redScaler.fromAToB(heat);
        double control = redScaler.fromAToB(heat);
        // Curve is applied to push colors toward the red end
        // b/c the way heat is determined leaves a pretty sparse middle ground.
        // Without curving, map would be mostly blue.
        control = colorCurve.apply(control);
        Color base = new Color(0, 0, 100);
        Color red = new Color(255, 0, 0);
        return new Color((int) Blend.blend(base.getRed(), red.getRed(), control),
                (int) Blend.blend(base.getGreen(), red.getGreen(), control),
                (int) Blend.blend(base.getBlue(), red.getBlue(), control));
        //return new Color(red, 0, Math.max(0, 100 - red));
    }

    /**
     * Convert a gps x coordinate to a pixel coordinate.
     *
     * @param x The gps coordinate to convert.
     * @return A pixel location.
     */
    private double getXPixel(double x) {return xScaler.fromAToB(x);}

    /**
     * Convert a gps y coordinate to a pixel coordinate.
     *
     * @param y The gps coordinate to convert.
     * @return A pixel location.
     */
    private double getYPixel(double y) {return yScaler.fromAToB(y);}

    /**
     * @param coordinate The coordinate to determine pixel coordinates for.
     * @return Pixel coordinates for `StdDraw`.
     */
    private Coordinate getPixelCoordinate(Coordinate coordinate) {
        return new Coordinate(getXPixel(coordinate.x()), getYPixel(coordinate.y()));
    }

    /**
     * Convert a heatmap entry into a pixel with coordinates and a color.
     *
     * @param hotSpot A heatmap entry
     * @return A pixel
     */
    private Pixel hotSpotToPixel(Map.Entry<Coordinate, Integer> hotSpot) {
        return new Pixel(getPixelCoordinate(hotSpot.getKey()), getColor(hotSpot.getValue()));
    }


    /**
     * Convert a heatmap to a list of `Pixels` to drawn.
     *
     * @param map The heatmap to convert into pixels
     * @return A list of `Pixel` objects that can be drawn.
     */
    private ArrayList<Pixel> mapToPixels(HashMap<Coordinate, Integer> map) {
        ArrayList<Pixel> pixels = new ArrayList<>();
        //redScaler = new Scale(heatmaps.getHeatRange(map), new Range(0, 255));
        redScaler = new Scale(heatmaps.getHeatRange(map), new Range(0, 1));
        for (Map.Entry<Coordinate, Integer> hotspot : map.entrySet()) {
            pixels.add(hotSpotToPixel(hotspot));
        }
        // Sort so that blue points are drawn first and red are drawn last.
        // This way red is more visible when there's overlap.
        pixels.sort(redComparator);
        return pixels;
    }

    @Override
    public Iterator<ArrayList<Pixel>> iterator() {
        return new Iterator<>() {
            private int frameCount = 0;
            private final Iterator<HashMap<Coordinate, Integer>> mapIterator = heatmaps.iterator();

            @Override
            public boolean hasNext() {
                return frameCount < heatmaps.size();
            }

            @Override
            public ArrayList<Pixel> next() {
                frameCount++;
                return mapToPixels(mapIterator.next());
            }
        };
    }
}
