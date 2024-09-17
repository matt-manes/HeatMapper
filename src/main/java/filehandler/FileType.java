package filehandler;

import java.nio.file.Path;

/**
 * Convenience methods for checking file type extensions since
 * {@link Path#endsWith(String)} is only valid for the whole last path part,
 * i.e. `/xxx.xxx`.
 * <p>
 * Overloads taking {@link String} arguments for `path` are intended to avoid repeated calls to
 * {@link Path#toString()} when the same file is being checked multiple times.
 * <p>
 * All checks are case-insensitive.
 */
public class FileType {
    /**
     * Check if the given path contains the given extension.
     *
     * @param path
     * @param ext
     * @return
     */
    public static boolean hasExt(Path path, String ext) {
        return path.toString().toLowerCase().endsWith(ext);
    }

    /**
     * Check if the given path contains the given extension.
     *
     * @param path
     * @param ext
     * @return
     */
    public static boolean hasExt(String path, String ext) {
        return path.toLowerCase().endsWith(ext);
    }

    /**
     * Check if the given path contains any extension from the list.
     * Returns on the first matching extension.
     *
     * @param path
     * @param exts
     * @return
     */
    public static boolean hasExt(Path path, String[] exts) {
        String p = path.toString();
        return hasExt(p, exts);
    }

    /**
     * Check if the given path contains any extension from the list.
     * Returns on the first matching extension.
     *
     * @param path
     * @param exts
     * @return
     */
    public static boolean hasExt(String path, String[] exts) {
        for (String ext : exts) {
            if (hasExt(path, ext)) return true;
        }
        return false;
    }

    /**
     * Is the given path a `.fit` file.
     *
     * @param path
     * @return
     */
    public static boolean isFIT(Path path) {
        return hasExt(path, ".fit");
    }

    /**
     * Is the given path a `.fit` file.
     *
     * @param path
     * @return
     */
    public static boolean isFIT(String path) {
        return hasExt(path, ".fit");
    }

    /**
     * Is the given path a `.fit.gz` file.
     *
     * @param path
     * @return
     */
    public static boolean isFITGZ(Path path) {
        return hasExt(path, ".fit.gz");
    }

    /**
     * Is the given path a `.fit.gz` file.
     *
     * @param path
     * @return
     */
    public static boolean isFITGZ(String path) {
        return hasExt(path, ".fit.gz");
    }

    /**
     * Is the given path a `.gpx` file.
     *
     * @param path
     * @return
     */
    public static boolean isGPX(Path path) {
        return hasExt(path, ".gpx");
    }

    /**
     * Is the given path a `.gpx` file.
     *
     * @param path
     * @return
     */
    public static boolean isGPX(String path) {
        return hasExt(path, ".gpx");
    }

    /**
     * Is the given path a `.fit` or a `.fit.gz` file.
     *
     * @param path
     * @return
     */
    public static boolean isFITOrFITGZ(Path path) {
        String p = path.toString();
        return isFIT(p) || isFITGZ(p);
    }

    /**
     * Is the given path a `.fit` or a `.fit.gz` file.
     *
     * @param path
     * @return
     */
    public static boolean isFITOrFITGZ(String path) {
        return isFIT(path) || isFITGZ(path);
    }
}
