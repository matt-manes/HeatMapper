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
 * Compares two `Pixel` objects as being closer or farther away from the `Settings.hot` color.
 */
class ColorComparator implements Comparator<Pixel> {
    private final int redDiff = Math.abs(Settings.hot.getRed() - Settings.cold.getRed());
    private final int greenDiff = Math.abs(Settings.hot.getGreen() - Settings.cold.getGreen());
    private final int blueDiff = Math.abs(Settings.hot.getBlue() - Settings.cold.getBlue());

    @Override
    public int compare(Pixel a, Pixel b) {
        // Hacky way of picking which channel qualifies
        // a color as closer to cold or closer to hot
        if (redDiff > greenDiff && redDiff > blueDiff) {
            return a.color().getRed() - b.color().getRed();
        } else if (greenDiff > redDiff && greenDiff > blueDiff) {
            return a.color().getGreen() - b.color().getGreen();
        } else {return a.color().getBlue() - b.color().getBlue();}
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
    private Scale xScaler, yScaler, controlScaler;

    private final HeatMapper heatmaps;
    private Curve colorCurve;
    private final ColorComparator colorComparator = new ColorComparator();
    private final Range controlRange = new Range(0, 1);
    // `StdDraw` default expects a number between 0 and 1
    private final Range canvasRange = new Range(0, 1);
    Color cold = Settings.cold;
    Color hot = Settings.hot;

    /**
     * Initialize scalers and color curve from heatmaps.
     */
    private void initParams() {
        ActivityBoundaries bounds = Settings.gpsBoundaries;
        Range westEastRange = new Range(bounds.west, bounds.east);
        Range southNorthRange = new Range(bounds.south, bounds.north);
        xScaler = new Scale(westEastRange, canvasRange);
        yScaler = new Scale(southNorthRange, canvasRange);

        Range heatRange = heatmaps.getHeatRange();
        controlScaler = new Scale(heatRange, controlRange);
        colorCurve = new Curve(Settings.colorCompression, controlRange);
    }

    /**
     * Generate a color from a coordinate's count.
     *
     * @param heat The coordinate's count.
     * @return A `Color` between blue and red.
     */
    private Color getColor(int heat) {
        double control = controlScaler.fromAToB(heat);
        // Curve is applied to push color toward the red end
        // b/c the way heat is determined tends to leave a pretty sparse middle ground.
        // Without curving, map would be mostly blue.
        control = colorCurve.apply(control);
        int r = (int) Blend.blend(cold.getRed(), hot.getRed(), control);
        int g = (int) Blend.blend(cold.getGreen(), hot.getGreen(), control);
        int b = (int) Blend.blend(cold.getBlue(), hot.getBlue(), control);
        return new Color(r, g, b);
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
        controlScaler.a = heatmaps.getHeatRange(map);
        for (Map.Entry<Coordinate, Integer> hotspot : map.entrySet()) {
            pixels.add(hotSpotToPixel(hotspot));
        }
        // Sort so that blue points are drawn first and red are drawn last.
        // This way red is more visible when there's overlap.
        pixels.sort(colorComparator);
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
