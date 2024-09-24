package filehandler;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

/**
 * Decompresses gzip files.
 */
public class GZDecompressor {

    /**
     * Decompress and read contents of file into a byte array.
     *
     * @param path The path to a `gzip` file.
     * @return A decompressed byte array
     */
    public static byte[] read(Path path) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int length;
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(path.toFile()))) {
            while ((length = in.read(buff)) > 0) {
                out.write(buff, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }
}
