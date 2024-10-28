package filehandler;

import settings.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Recursively searches a given directory for `.fit`, `.fit.gz`, and `.gpx` files.
 */
public class Globber {
    /**
     * Recursively searches `dir` and returns a list of files matching extensions `.fit`, `.fit
     * .gz`, or `.gpx`.
     *
     * @param dir The directory to search.
     * @return A {@link List} of matching {@link Path} objects.
     */
    public static List<Path> glob(Path dir) {
        try (Stream<Path> entries = Files.walk(dir)) {
            return entries.filter(path -> FileType.hasExts(path, Settings.fileTypes)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Overload of {@link Globber#glob(Path)}
     *
     * @see Globber#glob(Path)
     */
    public static List<Path> glob(String dir) {
        return glob(Path.of(dir));
    }
}
