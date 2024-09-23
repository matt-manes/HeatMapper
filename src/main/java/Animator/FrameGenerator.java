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
import java.util.Map;

public class FrameGenerator {

    public FrameGenerator(HeatMapper heatmaps) {
        this.heatmaps = heatmaps;
        initParams();
    }

    private Range heatRange, westEastRange, southNorthRange;
    private Scale xScaler, yScaler, redScaler;
    private Dimension canvas;
    private HeatMapper heatmaps;
    private int i = 0;
    private Curve colorCurve;

    public boolean hasNext() {return i < heatmaps.size();}

    private void initParams() {
        heatRange = heatmaps.getCountRange();
        ActivityBoundaries bounds = Settings.gpsBoundaries;
        westEastRange = new Range(bounds.west, bounds.east);
        southNorthRange = new Range(bounds.south, bounds.north);
        canvas = Settings.getCanvasSize();
        xScaler = new Scale(westEastRange, new Range(0, 1));
        yScaler = new Scale(southNorthRange, new Range(0, 1));
        redScaler = new Scale(heatRange, new Range(0, 255));
        colorCurve = new Curve(0.999999, new Range(0, 255));
    }

    private Color getColor(int heat) {
        int red = (int) redScaler.fromAToB(heat);
        red = (int) colorCurve.apply(red);
        // Least heat is blue, most is red
        return new Color(red, 0, Math.min(100, 255 - red));
        //return new Color(red, 0, 255 - red);
    }

    private double getXPixel(double x) {return xScaler.fromAToB(x);}

    private double getYPixel(double y) {
        // Drawing Y coordinates are reversed from what is sensible.
        return yScaler.fromAToB(y);//canvas.height - yScaler.fromAToB(y);
    }

    private Coordinate getPixelCoordinate(Coordinate coordinate) {
        return new Coordinate(getXPixel(coordinate.x()), getYPixel(coordinate.y()));
    }

    private Pixel hotSpotToPixel(Map.Entry<Coordinate, Integer> hotSpot) {
        return new Pixel(getPixelCoordinate(hotSpot.getKey()), getColor(hotSpot.getValue()));
    }

    private ArrayList<Pixel> mapToPixels(HashMap<Coordinate, Integer> map) {
        ArrayList<Pixel> pixels = new ArrayList<>();
        for (Map.Entry<Coordinate, Integer> hotspot : map.entrySet()) {
            pixels.add(hotSpotToPixel(hotspot));
        }
        return pixels;
    }

    public ArrayList<Pixel> next() {

        if (hasNext()) return mapToPixels(heatmaps.getMap(i++));
        return null;
    }
}
