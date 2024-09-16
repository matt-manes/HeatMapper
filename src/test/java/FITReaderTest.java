import filehandler.FITReader;
import filehandler.Globber;
import models.Activity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FITReaderTest {
    public static void main(String[] args) {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        ArrayList<Activity> activities = new ArrayList<>();
        for (Path path : paths) {
            String filepath = path.toString();
            if (filepath.endsWith(".fit") || filepath.endsWith(".fit.gz")) {
                activities.add(FITReader.parse(path));
            }
        }
        assert activities.size() > 0;
    }
}
