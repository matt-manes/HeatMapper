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
     * @return
     */
    public static Document read(Path path) {
        if (path == null) throw new NullPointerException();
        if (!path.toString().endsWith(".gpx")) {
            throw new IllegalArgumentException("File ext must be `.gpx` not " + path);
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(path.toString());
            return document;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the date the activity was recorded.
     *
     * @param doc
     * @return
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
     * @param doc
     * @return
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
     * @return
     */
    public static Activity parse(Path path) {
        Document doc = read(path);
        return new Activity(getDate(doc), getCoordinates(doc));
    }
}
