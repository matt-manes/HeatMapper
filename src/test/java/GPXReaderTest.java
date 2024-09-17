import filehandler.FileType;
import filehandler.GPXReader;
import filehandler.Globber;
import models.Activity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GPXReaderTest {
    public static void main(String[] args) {
        List<Path> paths = Globber.glob(ActivitiesDir.dir);
        ArrayList<Activity> activities = new ArrayList<>();
        for (Path path : paths) {
            if (FileType.isGPX(path)) {
                activities.add(GPXReader.parse(path));
            }
        }
        assert activities.size() > 0;
    }
}
