package settings;

import org.json.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Settings {

    public static final Path settingsPath =
            Path.of(System.getProperty("user.dir") + "/settings.json");

    public static int gpsPrecision = 4;

    public static ArrayList<String> fileTypes =
            new ArrayList<>(Arrays.asList(".fit", ".fit.gz", ".gpx"));

    public static ActivityBoundaries gpsBoundaries =
            new ActivityBoundaries(42.153288, 41.791163, -87.570721, -87.815);

    private static double canvasHeight = 0.91;
    private static double canvasWidth = 0.99;

    public static Dimension getCanvasSize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.height = (int) (screen.height * canvasHeight);
        screen.width = (int) (screen.width * canvasWidth);
        return screen;
    }

    public static int animationFrameWait = 0;

    public static Color cold = new Color(0, 0, 125);
    public static Color hot = new Color(255, 0, 0);
    public static Color background = new Color(0, 0, 0);
    public static double colorCompression = 0.999;
    public static double penRadius = 0.001;
    public static Path dataPath = Path.of(System.getProperty("user.dir") + "/data/activities");

    /**
     * Returns a json object from a nested structure where each nested key is given by `keychain`.
     * Replaces typing `data.getJSONObject(key1).getJSONObject(key2).getJSONObject(key3)` with
     * `getSubObject(data, key1, key2, key3)`
     *
     * @param data     The json object to extract the subobject from.
     * @param keychain One or more keys of a nested structure.
     * @return The subobject.
     */
    private static JSONObject getSubObject(JSONObject data, String... keychain) {
        JSONObject subobj = data.getJSONObject(keychain[0]);
        for (int i = 1; i < keychain.length; ++i) {
            subobj = subobj.getJSONObject(keychain[i]);
        }
        return subobj;
    }

    private static void parsePrecision(JSONObject data) {
        if (data.has("gpsPrecision")) gpsPrecision = data.getInt("gpsPrecision");
    }

    private static void parseFileTypes(JSONObject data) {
        if (data.has("fileTypes")) {
            fileTypes.clear();
            for (Object ext : data.getJSONArray("fileTypes")) {
                fileTypes.add((String) ext);
            }
        }
    }

    private static void parseCoordinateBounds(JSONObject data) {
        if (data.has("coordinateBounds")) {
            JSONObject bounds = getSubObject(data, "coordinateBounds");
            gpsBoundaries =
                    new ActivityBoundaries(bounds.getDouble("north"), bounds.getDouble("south"),
                            bounds.getDouble("east"), bounds.getDouble("west"));
        }
    }

    private static void parseCanvasSize(JSONObject data) {
        if (data.has("canvasSize")) {
            JSONObject canvas = getSubObject(data, "canvasSize");
            canvasHeight = canvas.getDouble("height");
            canvasWidth = canvas.getDouble("width");
        }
    }

    private static void parseAnimationFrameWait(JSONObject data) {
        if (data.has("animationFrameWaitMS")) {
            animationFrameWait = data.getInt("animationFrameWaitMS");
        }
    }

    private static void parseColors(JSONObject data) {
        if (data.has("colors")) {
            JSONObject color = getSubObject(data, "colors", "cold");
            cold = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"));
            color = getSubObject(data, "colors", "hot");
            hot = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"));
            color = getSubObject(data, "colors", "background");
            background = new Color(color.getInt("r"), color.getInt("g"), color.getInt("b"));
        }
    }

    private static void lightModeIsIllegal() {
        // If all 3 channels are 175+, you're going to jail
        if (background.getRed() >= 175 && background.getGreen() >= 175 &&
                background.getBlue() >= 175) {background = Color.BLACK;}
    }

    private static void parseColorCompression(JSONObject data) {
        if (data.has("colorCompression")) colorCompression = data.getDouble("colorCompression");
    }

    private static void parsePenRadius(JSONObject data) {
        if (data.has("penRadius")) penRadius = data.getDouble("penRadius");
    }

    private static void parseDataPath(JSONObject data) {
        if (data.has("dataPath")) dataPath = Path.of(data.getString("dataPath"));
    }

    private static void parseJson(String json) {
        JSONObject data = new JSONObject(json);
        parsePrecision(data);
        parseFileTypes(data);
        parseCoordinateBounds(data);
        parseCanvasSize(data);
        parseAnimationFrameWait(data);
        parseColors(data);
        parseColorCompression(data);
        parsePenRadius(data);
        parseDataPath(data);
        lightModeIsIllegal();
    }

    /**
     * Load settings from `settings.json`.
     * If no settings file is found, one will be created with default values.
     */
    public static void load() {
        try {
            String json = Files.readString(settingsPath);
            parseJson(json);
        } catch (FileNotFoundException | NoSuchFileException e) {
            System.out.println("No settings file found.");
            System.out.println("Creating new 'settings.json' file with default values.");
            dump();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a `Color` object to a hash map.
     *
     * @param color The color to convert.
     * @return A hash map with the keys "r", "g", and "b".
     */
    private static HashMap<String, Integer> colorToMap(Color color) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("r", color.getRed());
        map.put("g", color.getGreen());
        map.put("b", color.getBlue());
        return map;
    }

    /**
     * Dump current settings values to `settings.json`.
     */
    public static void dump() {
        JSONObject data = new JSONObject();
        data.put("gpsPrecision", gpsPrecision);
        data.put("fileTypes", fileTypes);

        HashMap<String, Double> map = new HashMap<>();
        map.put("north", gpsBoundaries.north);
        map.put("south", gpsBoundaries.south);
        map.put("east", gpsBoundaries.east);
        map.put("west", gpsBoundaries.west);
        data.put("coordinateBounds", map);

        map.clear();
        map.put("height", canvasHeight);
        map.put("width", canvasWidth);
        data.put("canvasSize", map);

        data.put("animationFrameWaitMS", animationFrameWait);

        HashMap<String, HashMap<String, Integer>> colors = new HashMap<>();
        colors.put("cold", colorToMap(cold));
        colors.put("hot", colorToMap(hot));
        colors.put("background", colorToMap(background));
        data.put("colors", colors);

        data.put("colorCompression", colorCompression);
        data.put("penRadius", penRadius);
        data.put("dataPath", dataPath.toString());

        try {
            Files.writeString(settingsPath, data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
