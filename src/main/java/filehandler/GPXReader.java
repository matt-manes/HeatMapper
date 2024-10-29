package filehandler;

import models.Activity;
import models.Coordinate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * Read '.gpx' files and extract the activity date and GPS coordinates.
 * '.gpx' is the file type exported by Strava when an activity was recorded using their app
 * rather than a dedicated device that uses the FIT protocol.
 */
public class GPXReader {

    /**
     * Returns an xml Document object representing the file.
     *
     * @param path
     * @return The xml document.
     */
    public static Document read(Path path) {
        if (path == null) throw new NullPointerException();
        if (!FileType.isGPX(path)) {
            throw new IllegalArgumentException("File ext must be `.gpx` not " + path);
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(path.toString());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the date the activity was recorded.
     *
     * @param doc The document to extract the recording date from.
     * @return The date this activity was recorded.
     */
    private static Date getDate(Document doc) {
        try {
            String date = doc.getElementsByTagName("metadata").item(0).getTextContent();
            return Date.from(Instant.parse(date.strip()));
        } catch (DOMException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the list of lattitude and longitude coordinates from the activity.
     *
     * @param doc The docuement to extract the gps data from.
     * @return The list of coordinates recorded during the activity.
     */
    private static ArrayList<Coordinate> getCoordinates(Document doc) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("trkpt");
        double x, y;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            NamedNodeMap attrs = nodeList.item(i).getAttributes();
            x = Double.parseDouble(attrs.getNamedItem("lon").getTextContent());
            y = Double.parseDouble(attrs.getNamedItem("lat").getTextContent());
            coordinates.add(new Coordinate(x, y));
        }
        return coordinates;
    }

    /**
     * Reads a `.gpx` file and returns the corresponding `Activity` record.
     *
     * @param path A valid `.gpx` file
     * @return An {@link Activity} record representing the recorded activity.
     */
    public static Activity parseToActivity(Path path) {
        Document doc = read(path);
        return new Activity(getDate(doc), getCoordinates(doc));
    }
}
