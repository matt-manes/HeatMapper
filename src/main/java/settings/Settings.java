package settings;

public class Settings {
    // GPS values will be rounded to this many decimal places.
    public static final int gpsPrecision = 4;

    public static final String[] fileTypes = {".fit", ".fit.gz", ".gpx"};

    // Limits to Illinois near chicago and a tiny bit of Indiana
    public static final ActivityBoundaries gpsBoundaries =
            new ActivityBoundaries(42.408318, 41.427542, -87.365932, -88.340524);

    /*
    // Same as above except southeastern corner doesn't exceed state line
    public final ActivityBoundaries gpsBoundaries =
            new ActivityBoundaries(42.408318, 41.707924, -87.524541, -88.340524);
     */
}
