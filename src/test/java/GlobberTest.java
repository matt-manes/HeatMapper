import filehandler.Globber;

import java.nio.file.Path;
import java.util.List;

public class GlobberTest {
    public static void main(String[] args) {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        assert paths.size() > 0;
        for (Path path : paths) {
            assert !path.toString().endsWith(".txt");
        }
        System.out.println(paths.get(0));
    }
}
