# HeatMapper

![sample](sample.png)

A java app for making heat map animations from `.fit`, `.fit.gz`, and `.gpx` files.

## Requirements

- Java 21

## Build Requirements

- Java 21
- Intellij Idea

## Download

```console
git clone https://github.com/matt-manes/HeatMapper
```

## Running HeatMapper

### User

Run with included sample data:

```console
java -jar ./out/HeatMapper.jar
```

To use your own data (downloaded from strava or a device), provide the directory as a command line
argument:

```console
java -jar ./out/HeatMapper.jar some/path/containing/files
```

A different data directory can also be specified in `settings.json`.

The first time HeatMapper is run, a `settings.json` file will be generated and populated with
default values:

```json
{
  "coordinateBounds": {
    "west": -87.8,
    "east": -87.570721,
    "south": 41.791163,
    "north": 42.153288
  },
  "penRadius": 0.0001,
  "gpsPrecision": 4,
  "animationFrameWaitMS": 0,
  "canvasSize": {
    "width": 0.99,
    "height": 0.91
  },
  "colors": {
    "hot": {
      "r": 255,
      "b": 0,
      "g": 0
    },
    "background": {
      "r": 0,
      "b": 0,
      "g": 0
    },
    "cold": {
      "r": 0,
      "b": 125,
      "g": 0
    }
  },
  "fileTypes": [
    ".fit",
    ".fit.gz",
    ".gpx"
  ],
  "colorCompression": 0.995,
  "dataPath": "data\\activities"
}
```

### Developer

HeatMapper can be opened as an Intellij Idea project with the source code located in `src/main/java`
and a junit test suite in `src/test/java`.

To build the project, click `build` on the top menu bar and select `build project`.

The application can be run from source by selecting the `Main` configuration in the
`run/debug configurations` drop down menu and clicking the run button.

This will run the file `src/main/java/Main.java`.

The test suite can be run the same way, but by using the `alltests` configuration.

#### Processing flow

![diagram](diagram.png)

#### Implementation details

###### File handling

At the beginning of the program, [Globber](src/main/java/filehandler/Globber.java) is invoked to
gather all files
from a given directory (`data/activities` by default) that match the `fileTypes` field
in [Settings](src/main/java/settings/Settings.java).

The default file types (and only ones currently supported) are `.fit`, `.fit.gz`, and `.gpx`.

A `.fit` file is
a [binary file protocol developed by Garmin](https://developer.garmin.com/fit/overview) for
recording and transmitting physical activity data from a device such as a bike computer or smart
watch.

A `.fit.gz` file is simply a gzip compressed `.fit` file.

A `.gpx` file is an xml file containing activity data when the activity is recorded via
the [Strava](https://www.strava.com) app.

File type detection is done on the basis of the file extension by
the [FileType](src/main/java/filehandler/FileType.java) class.

Any `.fit` or `.fit.gz` files will be decoded and parsed into an `Activity` object by
the [FITReader](src/main/java/filehandler/FITReader.java) class, with `.fit.gz` files first
being decompressed by the [GZDecompressor](src/main/java/filehandler/GZDecompressor.java) class.

Similarly `.gpx` files will be parsed into `Activity` objects by
the [GPXReader](src/main/java/filehandler/GPXReader.java) class.

These steps are all handled by passing a path containing activity files to
the `loadAll` function of the [ActivityLoader](src/main/java/activityloader/ActivityLoader.java)
class,
which returns an `ArrayList` of `Activity` objects.

###### Models

There are three data models implemented as Java Records

- [Coordinate](src/main/java/models/Coordinate.java) to represent a recorded gps location
    - Consists of `x` and `y` fields
- [Activity](src/main/java/models/Activity.java) to represent a recorded activity
    - Consists of a `Date` field and an `ArrayList` of `Coordinate` objects
- [Pixel](src/main/java/models/Pixel.java) to represent a pixel on screen
    - Consists of a `Coordinate` field (gps coordinate mapped to a screen coordinate) and a `Color`
      field.

###### Preprocessing

Once all the `Activity` records have been loaded, they are processed by
the [Preprocessor](src/main/java/activityloader/Preprocessor.java) class using the `process` method.

The first step is to remove any activities that don't contain any coordinates (typically something
like biking on a stationary indoor trainer).

Then each activity has its coordinate list rounded to `Settings.gpsPrecision` number of decimal
places (default is 4).

Each activity will also have "pauses" removed, where a pause is defined by two or more consecutive
repeating coordinates.

In these instances, the first coordinate is kept and the following occurences are tossed in order to
unweight things like waiting at traffic lights.

Once coordinates have been rounded and pruned of pauses, they are then compared to the bounding box
defined by
`Settings.gpsBoundaries`.

Any coordinate outside this bounding box will be tossed and any `Activity` with no coordinates after
this will be removed from the list altogether.

###### Sorting

After processing, the `Activity` list is sorted according to its `Date` field by
the [ActivitySorter](src/main/java/activityloader/ActivitySorter.java) class.

This class is a straight forward min-heap implementation.

###### HeatMapper

The [HeatMapper](src/main/java/heatmap/HeatMapper.java) class is responsible for building heat maps
from a list of activities.

Each heat map is a hashmap of the form `HashMap<Coordinate, Integer>`.

The `Integer` value indicates how many times a given `Coordinate` has occured across however many
activities have been added to the map.

In order to save memory, the `HeatMapper` class implements the `Iterable` interface so that new heat
maps are lazily generated on demand.

The next map generated is the previous map with the next activity added to it.

Each `Coordinate` in the next activity will be added as a key with the value `1` if it doesn't exist
in the map,
but, if it does exist in the map, the current value for that `Coordinate` key will simply be
incremented.

In this way, each map can be looked at as a slice in time of how the final heat map evolves.

###### Frame Generation and Animation

Ultimately the goal is to draw each addition to the heat map as a frame on screen.

This is accomplished by passing a `HeatMapper` object to
the [Animator](src/main/java/Animator/Animator.java) class.

Within this class, the work of converting a heat map to something drawable is delegated to
a [FrameGenerator](src/main/java/Animator/FrameGenerator.java) instance.

Much like the `HeatMapper` class, the `FrameGenerator` class implements the `Iterable` interface to
lazily produce an `ArrayList` of `Pixel` objects from a heat map that can be drawn by the animator.

A heat map is converted to a pixel list by iterating over all `Coordinate` keys in the map and
scaling the `x` and `y` fields to the range `[0,1]`, where, as defined by
`Settings.gpsBoundaries`, the southwestern corner corresponds to pixel `(0,0)` and the northeastern
corner corresponds to `(1,1)`.

The `color` field of the `Pixel` object is calculated from the corresponding `Integer` value in the
heat map for the `Coordinate` key.

This value will be scaled to the range `[0,1]` to create a blending value.

The scaling is relative to the current maximum `Integer` value in the map, i.e. if coordinate being
converted has a corresponding value of `10` and the most frequent coordinate in the map has a value
of `100`, the blending value will be `0.1`.

Because the spread of values in the heat map tends to be rather sparse,
the [Curve](src/main/java/utilities/Curve.java) class is used to compress (or expand) the calculated
blending value.

The amount and shape of the curve is controlled by `Settings.colorCompression`, which should be
in the range `(0,1)`.

With a value of `0.9`, the blending value of `0.1` will be converted to `0.5`.

In this way, curve values in the range `(0, .49]` push colors toward the "cold" end of the color
spectrum and values in the range `[0.51, 1)` push colors toward the "hot" end of the color spectrum.

To obtain the actual `Color` object for the pixel, the curved blending value is used to blend each
of the `r`, `g`, and `b` channels of `Settings.cold` and `Settings.hot`.

Before the generated pixel list is returned to the `Animator`, the pixels are sorted from coldest
color to hottest color.

The `Animator` then clears the screen to the `Settings.background` color, draws each pixel according
to its position and color, waits `Settings.animationFrameWait` number of milliseconds, and then
requests the next frame from the generator until it's been exhausted.