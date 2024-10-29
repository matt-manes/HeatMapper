package heatmap;

import models.Activity;
import models.Coordinate;
import org.junit.jupiter.api.Test;
import utilities.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeatMapperTest {
    Coordinate newKey(double x, double y) {
        return new Coordinate(x, y);
    }

    @Test
    void heatTest() {
        Coordinate[][] coordinates = {{newKey(1, 1), newKey(2, 2), newKey(3, 3)},
                {newKey(1, 1), newKey(12, 12), newKey(13, 13)},
                {newKey(1, 1), newKey(2, 2), newKey(33, 33)},};
        /**
         *  Each map should be the new activity plus the previous heat map:
         *  first heat map should be
         *  {
         *      (1,1): 1,
         *      (2,2): 1,
         *      (3,3): 1
         *  }
         *  Second should be
         *  {
         *      (1,1): 2,
         *      (2,2): 1,
         *      (3,3): 1,
         *      (12,12): 1,
         *      (13,13): 1
         *  }
         *  Third should be
         *  {
         *      (1,1): 3,
         *      (2,2): 2,
         *      (3,3): 1,
         *      (12,12): 1,
         *      (13,13): 1,
         *      (33,33): 1
         *  }
         */

        // Construct activites list from coordinates
        ArrayList<Activity> activities = new ArrayList<>();
        for (Coordinate[] coordList : coordinates) {
            activities.add(new Activity(null, new ArrayList<>(List.of(coordList))));
        }

        // Invoke heat mapper
        HeatMapper mapper = new HeatMapper(activities);
        assertEquals(coordinates.length, mapper.size());

        Iterator<HashMap<Coordinate, Integer>> maperator = mapper.iterator();
        // Check first map
        HashMap<Coordinate, Integer> map = maperator.next();
        assertEquals(1, map.get(newKey(1, 1)));
        assertEquals(1, map.get(newKey(2, 2)));
        assertEquals(1, map.get(newKey(3, 3)));

        // Check second map
        map = maperator.next();
        assertEquals(2, map.get(newKey(1, 1)));
        assertEquals(1, map.get(newKey(2, 2)));
        assertEquals(1, map.get(newKey(3, 3)));
        assertEquals(1, map.get(newKey(12, 12)));
        assertEquals(1, map.get(newKey(13, 13)));

        // Check third map
        map = maperator.next();
        assertEquals(3, map.get(newKey(1, 1)));
        assertEquals(2, map.get(newKey(2, 2)));
        assertEquals(1, map.get(newKey(3, 3)));
        assertEquals(1, map.get(newKey(12, 12)));
        assertEquals(1, map.get(newKey(13, 13)));
        assertEquals(1, map.get(newKey(33, 33)));

        // Check min and max counts
        Range range = mapper.getHeatRange();
        assert range.a == 1;
        assert range.b == 3;
    }
}