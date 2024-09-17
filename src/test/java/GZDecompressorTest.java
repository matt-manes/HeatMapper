import filehandler.FileType;
import filehandler.GZDecompressor;
import filehandler.Globber;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GZDecompressorTest {
    public static void main(String[] args) {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        Path zipped = null;
        // Find one that's a `.gz`
        for (Path path : paths) {
            if (FileType.isFITGZ(path)) {
                zipped = path;
                break;
            }
        }
        assert !(zipped == null);
        byte[] unzipped = GZDecompressor.read(zipped);
        assert unzipped.length > 0;

        // Write to file to that can be checked with FIT SDK.
        Path outpath = Path.of("decompressTest.fit");
        try (FileOutputStream out = new FileOutputStream(outpath.toFile())) {
            out.write(unzipped);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
