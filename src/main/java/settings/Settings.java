package settings;

import java.awt.*;

public class Settings {
    /**
     * GPS values will be rounded to this many decimal places.
     */
    public static final int gpsPrecision = 4;

    public static final String[] fileTypes = {".fit", ".fit.gz", ".gpx"};

    // Limits to Illinois near chicago and a tiny bit of Indiana
    //public static final ActivityBoundaries gpsBoundaries =
    //        new ActivityBoundaries(42.408318, 41.427542, -87.365932, -88.340524);

    // Same as above except southeastern corner doesn't exceed state line
    //    public static final ActivityBoundaries gpsBoundaries =
    //            new ActivityBoundaries(42.408318, 41.707924, -87.524541, -88.340524);
    public static final ActivityBoundaries gpsBoundaries =
            new ActivityBoundaries(42.153288, 41.791163, -87.570721, -87.815);


    public static Dimension getCanvasSize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.height = (int) (screen.height * 0.91);
        screen.width = (int) (screen.width * 0.99);
        return screen;
    }

    /**
     * How long to sleep between animation frames.
     */
    public static int animationFrameWait = 0;
}
