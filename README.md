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

### User:

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

### Developer:

HeatMapper can be opened as an Intellij Idea project with the source code located in `src/main/java`
and a junit test suite in `src/test/java`.

To build the project, click `build` on the top menu bar and select `build project`.

The application can be run from source by selecting the `Main` configuration in the
`run/debug configurations` drop down menu and clicking the run button.

The test suite can be run the same way, but by using the `alltests` configuration.

#### Processing flow

![diagram](diagram.png)
