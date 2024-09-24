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
     * @param path The path to check.
     * @param ext  The extension to check for.
     * @return Whether `path` has the given extension.
     */
    public static boolean hasExt(Path path, String ext) {
        return path.toString().toLowerCase().endsWith(ext);
    }

    /**
     * Check if the given path contains the given extension.
     *
     * @param path The path to check.
     * @param ext  The extension to check for.
     * @return Whether `path` has the given extension.
     */
    public static boolean hasExt(String path, String ext) {
        return path.toLowerCase().endsWith(ext);
    }

    /**
     * Check if the given path contains any extension from the list.
     * Returns on the first matching extension.
     *
     * @param path The path to check.
     * @param exts A list of extensions to check for.
     * @return `true` on the first matching extension.
     */
    public static boolean hasExt(Path path, String[] exts) {
        String p = path.toString();
        return hasExt(p, exts);
    }

    /**
     * Check if the given path contains any extension from the list.
     * Returns on the first matching extension.
     *
     * @param path The path to check.
     * @param exts A list of extensions to check for.
     * @return `true` on the first matching extension.
     */
    public static boolean hasExt(String path, String[] exts) {
        for (String ext : exts) {
            if (hasExt(path, ext)) return true;
        }
        return false;
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit` file.
     */
    public static boolean isFIT(Path path) {
        return hasExt(path, ".fit");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit` file.
     */
    public static boolean isFIT(String path) {
        return hasExt(path, ".fit");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit.gz` file.
     */
    public static boolean isFITGZ(Path path) {
        return hasExt(path, ".fit.gz");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit.gz` file.
     */
    public static boolean isFITGZ(String path) {
        return hasExt(path, ".fit.gz");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.gpx` file.
     */
    public static boolean isGPX(Path path) {
        return hasExt(path, ".gpx");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.gpx` file.
     */
    public static boolean isGPX(String path) {
        return hasExt(path, ".gpx");
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit` or a `.fit.gz` file.
     */
    public static boolean isFITOrFITGZ(Path path) {
        String p = path.toString();
        return isFIT(p) || isFITGZ(p);
    }

    /**
     * @param path The path to check.
     * @return Whether `path` is a `.fit` or a `.fit.gz` file.
     */
    public static boolean isFITOrFITGZ(String path) {
        return isFIT(path) || isFITGZ(path);
    }
}
