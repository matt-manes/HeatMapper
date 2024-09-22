package heatmap;

import models.Activity;
import models.Coordinate;
import utilities.Range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Creates a list of cumulative heat maps from a list of Activities.
 */
public class HeatMapper {

    public HeatMapper() {}

    /**
     * Generate heat maps at instantiation.
     *
     * @param activities A list of activities to generate heat maps from.
     */
    public HeatMapper(ArrayList<Activity> activities) {map(activities);}

    // The list of heatmaps
    private final ArrayList<HashMap<Coordinate, Integer>> maps = new ArrayList<>();

    /**
     * @param index The index of the heat map to return.
     * @return A specific heat map from the list.
     */
    public HashMap<Coordinate, Integer> getMap(int index) {return maps.get(index);}

    /**
     * @return The number of heat maps.
     */
    public int size() {return maps.size();}

    /**
     * Generate heat maps from a list of activities.
     * <p>
     * Note: If this `HeatMapper` has previous heat map data,
     * the new heat maps will be cumulative with respect to that data.
     *
     * @param activities The list of activities to generate heat maps from.
     */
    public void map(ArrayList<Activity> activities) {
        for (Activity activity : activities) {
            HashMap<Coordinate, Integer> map = getCoordinateHeatmap(activity.coordinates());
            if (!maps.isEmpty()) {
                map = addMaps(maps.getLast(), map);
            }
            maps.add(map);
        }
    }

    /**
     * @param coordinates A list of `Coordinate` objects to generate a hasmap with.
     * @return A hashmap conveying how many times a given coordinate appeared in `coordinates`.
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
     * Add the contents of two maps together.
     *
     * @param map1 A map to add to the other.
     * @param map2 A map to add to the other.
     * @return The sum of `map1` and `map2`.
     */
    private HashMap<Coordinate, Integer> addMaps(HashMap<Coordinate, Integer> map1,
            HashMap<Coordinate, Integer> map2) {
        HashMap<Coordinate, Integer> comboMap = new HashMap<>(map1);
        map2.forEach((k, v) -> comboMap.merge(k, v, Integer::sum));
        return comboMap;
    }

    /**
     * @return The count of the most frequent coordinate in the last frame.
     */
    public Integer getMaxCount() {
        return maps.getLast().values().stream().max(Comparator.naturalOrder()).get();
    }

    /**
     * @return The count of the least frequent coordinate in the last frame.
     */
    public Integer getMinCount() {
        return maps.getLast().values().stream().min(Comparator.naturalOrder()).get();
    }

    /**
     * @return A `Range` containing the least frequenct and most frequent coordinate counts
     * in the last frame.
     */
    public Range getCountRange() {
        return new Range(getMinCount(), getMaxCount());
    }


}
