package filehandler;

import com.garmin.fit.FitDecoder;
import com.garmin.fit.FitMessages;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.util.SemicirclesConverter;
import models.Activity;
import models.Coordinate;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FITReader {
    /**
     * Decode the given FIT file and return the messages it contains.
     *
     * @param path
     * @return
     */
    public static FitMessages read(Path path) {
        if (path == null) throw new NullPointerException();
        String filepath = path.toString();
        if (!(filepath.endsWith(".fit") || filepath.endsWith(".fit.gz"))) {
            throw new IllegalArgumentException("File ext must be `.fit` not " + path);
        }

        if (filepath.endsWith(".fit.gz")) {
            return readFromGZip(path);
        }
        return readFromFile(path);
    }

    /**
     * Decompress the file then decode.
     *
     * @param path
     * @return
     */
    private static FitMessages readFromGZip(Path path) {
        FitDecoder decoder = new FitDecoder();
        try (ByteArrayInputStream in = new ByteArrayInputStream(GZDecompressor.read(path))) {
            return decoder.decode(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decode from file.
     *
     * @param path
     * @return
     */
    private static FitMessages readFromFile(Path path) {
        FitDecoder decoder = new FitDecoder();
        try (FileInputStream in = new FileInputStream(path.toFile())) {
            return decoder.decode(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the date the activity was recorded.
     *
     * @param messages
     * @return
     */
    private static Date getDate(FitMessages messages) {
        return messages.getFileIdMesgs().getFirst().getTimeCreated().getDate();
    }

    /**
     * Returns the list of lattitude and longitude coordinates from the activity.
     *
     * @param messages
     * @return
     */
    private static ArrayList<Coordinate> getCoordinates(FitMessages messages) {
        List<RecordMesg> records = messages.getRecordMesgs();
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        double x, y;
        for (RecordMesg record : records) {
            try {
                x = SemicirclesConverter.semicirclesToDegrees(record.getPositionLong());
                y = SemicirclesConverter.semicirclesToDegrees(record.getPositionLat());
                coordinates.add(new Coordinate(x, y));
            } catch (NullPointerException e) {
            }
        }
        return coordinates;
    }

    /**
     * Reads a `.fit` of `.fit.gz` file and returns the corresponding `Activity` record.
     *
     * @param path A `.fit` or `.fit.gz` file
     * @return
     */
    public static Activity parse(Path path) {
        FitMessages messages = read(path);
        return new Activity(getDate(messages), getCoordinates((messages)));
    }
}
