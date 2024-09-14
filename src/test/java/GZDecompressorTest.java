import filehandler.Globber;

import java.nio.file.Path;
import java.util.List;

public class GZDecompressorTest {
    public static void main(String[] args) {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
    }
}
