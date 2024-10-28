package heatmap;

import models.Activity;
import models.Coordinate;
import utilities.Range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Lazily generate a series of heat maps where each map is cumulative with the previous maps.
 */
public class HeatMapper implements Iterable<HashMap<Coordinate, Integer>> {

    /**
     * @param activities The list of activities to generate heat maps with.
     */
    public HeatMapper(ArrayList<Activity> activities) {this.activities = activities;}

    private final ArrayList<Activity> activities;

    // min and max coordinate counts from all coordinates in activities
    private Range heatRange = null;

    /**
     * @return The number of maps that can be generated.
     */
    public int size() {return activities.size();}

    /**
     * @return A flattened list of all coordinates in the activities list.
     */
    private ArrayList<Coordinate> getAllCoordinates() {
        return new ArrayList<>(activities.subList(0, activities.size()).stream()
                .flatMap(a -> a.coordinates().stream()).toList());
    }

    /**
     * Generates a hash map of coordinates and their frequency.
     *
     * @param coordinates A list of coordinates to generate the map from.
     * @return A mapping of coordinates and how many times they appeared.
     */
    private HashMap<Coordinate, Integer> getCoordinateHeatmap(ArrayList<Coordinate> coordinates) {
        HashMap<Coordinate, Integer> map = new HashMap<>();
        for (Coordinate coordinate : coordinates) {
            Integer val = map.putIfAbsent(coordinate, 1);
            if (val != null) map.put(coordinate, ++val);
        }
        return map;
    }

    /**
     * Get the minimum and maximum counts for a given map.
     *
     * @param map The map to get min and max from.
     * @return A `Range` object containing the min and max counts.
     */
    public Range getHeatRange(HashMap<Coordinate, Integer> map) {
        int min = map.values().stream().min(Comparator.naturalOrder()).get();
        int max = map.values().stream().max(Comparator.naturalOrder()).get();
        return new Range(min, max);
    }

    /**
     * Determine the min and max coordinate counts for all activities combined.
     *
     * @return The min and max coordinate counts as a `Range`.
     */
    public Range getHeatRange() {
        // Cache heatRange
        if (heatRange == null) {
            HashMap<Coordinate, Integer> map = getCoordinateHeatmap(getAllCoordinates());
            int min = map.values().stream().min(Comparator.naturalOrder()).get();
            int max = map.values().stream().max(Comparator.naturalOrder()).get();
            heatRange = new Range(min, max);
        }
        return heatRange;
    }

    @Override
    public Iterator<HashMap<Coordinate, Integer>> iterator() {
        return new Iterator<>() {
            int count = 0;
            final HashMap<Coordinate, Integer> map = new HashMap<>();

            private void addToMap(ArrayList<Coordinate> coordinates) {
                for (Coordinate coordinate : coordinates) {
                    Integer val = map.putIfAbsent(coordinate, 1);
                    if (val != null) map.put(coordinate, ++val);
                }
            }

            @Override
            public boolean hasNext() {
                return count < activities.size();
            }

            @Override
            public HashMap<Coordinate, Integer> next() {
                addToMap(activities.get(count++).coordinates());
                return map;
            }
        };
    }
}
