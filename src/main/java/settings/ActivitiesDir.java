package settings;

import java.nio.file.Path;

/**
 * Convenience class pointing to a default directory.
 */
public class ActivitiesDir {
    public static final Path dir =
            Path.of(System.getProperty("user.dir") + Path.of("/data/activities"));
}
