package filehandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Recursively searches a given directory for `.fit`, `.fit.gz`, and `.gpx` files.
 */
public class Globber {
    private static final String[] patterns = {".fit", ".fit.gz", ".gpx"};

    private static boolean matches(Path path) {
        String file = path.toString();
        for (String pattern : patterns) {
            if (file.endsWith(pattern)) return true;
        }
        return false;
    }

    /**
     * Recursively searches `dir` and returns a list of files matching extensions `.fit`, `.fit
     * .gz`, or `.gpx`.
     *
     * @param dir
     * @return A {@link List} of matching {@link Path} objects.
     */
    public static List<Path> glob(Path dir) {
        try (Stream<Path> entries = Files.walk(dir)) {
            return entries.filter(p -> matches(p)).toList();
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
