import filehandler.FileType;

import java.nio.file.Path;

public class FileTypeTest {
    public static void main(String[] args) {
        Path path = Path.of("somefilepath.fit");
        assert FileType.hasExt(path, ".fit");
        assert FileType.isFIT(path);
        assert !FileType.isFITGZ(path);
        assert !FileType.isGPX(path);
        assert FileType.isFITOrFITGZ(path);

        path = Path.of("somefilepath.fit.gz");
        assert !FileType.isFIT(path);
        assert FileType.isFITGZ(path);
        assert !FileType.isGPX(path);
        assert FileType.isFITOrFITGZ(path);

        path = Path.of("somefilepath.gpx");
        assert !FileType.isFIT(path);
        assert !FileType.isFITGZ(path);
        assert FileType.isGPX(path);
        assert !FileType.isFITOrFITGZ(path);

        assert FileType.hasExts(path, new String[]{".fit", ".gpx"});
        assert !FileType.hasExts(path, new String[]{".fit", ".txt", ".html"});

    }
}
