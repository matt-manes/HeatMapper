package filehandler;

import com.garmin.fit.FitDecoder;
import com.garmin.fit.FitMessages;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.util.SemicirclesConverter;
import models.Activity;
import models.Coordinate;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Read FIT and gzipped FIT files.
 */
public class FITReader {
    /**
     * Decode the given FIT file and return the messages it contains.
     *
     * @param path The path to a `.fit` or a `.fit.gz` file.
     * @return The fit messages contained in the file.
     */
    public static FitMessages getFitMessages(Path path) {
        if (path == null) throw new NullPointerException();
        if (!(FileType.isFITOrFITGZ(path))) {
            throw new IllegalArgumentException("File ext must be `.fit` not " + path);
        }

        if (FileType.isFITGZ(path)) {
            return readFromGZip(path);
        }
        return readFromFile(path);
    }

    /**
     * Decompress the file then decode.
     *
     * @param path The path to a gzipped fit file.
     * @return The fit messages contained in the file.
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
     * @param path The path to a FIT file.
     * @return The fit messages contained in the file.
     */
    private static FitMessages readFromFile(Path path) {
        FitDecoder decoder = new FitDecoder();
        try (FileInputStream in = new FileInputStream(path.toFile())) {
            return decoder.decode(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the date the activity was recorded.
     *
     * @param messages The fit messages for an activity.
     * @return The recording date of the activity.
     */
    private static Date getDate(FitMessages messages) {
        return messages.getFileIdMesgs().getFirst().getTimeCreated().getDate();
    }

    /**
     * Returns the list of lattitude and longitude coordinates from the activity.
     *
     * @param messages The fit messages for an activity.
     * @return A list of coordinates recorded during the activity.
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
            } catch (NullPointerException _) {
            }
        }
        return coordinates;
    }

    /**
     * Reads a `.fit` of `.fit.gz` file and returns the corresponding {@link Activity} record.
     *
     * @param path A `.fit` or `.fit.gz` file
     * @return An {@link Activity} record representing the activity recorded in the given file.
     */
    public static Activity parseToActivity(Path path) {
        FitMessages messages = getFitMessages(path);
        return new Activity(getDate(messages), getCoordinates((messages)));
    }
}
