package Animator;

import heatmap.HeatMapper;
import models.Coordinate;
import models.Pixel;
import settings.ActivityBoundaries;
import settings.Settings;
import utilities.Curve;
import utilities.Range;
import utilities.Scale;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        colorCurve = new Curve(0.9999999, new Range(0, 255));
    }

    /**
     * Generate a color from a coordinate's count.
     *
     * @param heat An int between 0 and 255, inclusive.
     * @return A `Color` between blue and red.
     */
    private Color getColor(int heat) {
        int red = (int) redScaler.fromAToB(heat);
        // Curve is applied to push colors toward the red end
        // b/c the way heat is determined leaves a pretty sparse middle ground.
        // Without curving, map would be mostly blue.
        red = (int) colorCurve.apply(red);
        // Least heat is blue, most is red
        return new Color(red, 0, Math.min(100, 255 - red));
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
        for (Map.Entry<Coordinate, Integer> hotspot : map.entrySet()) {
            pixels.add(hotSpotToPixel(hotspot));
        }
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
