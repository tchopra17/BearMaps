package bearmaps.proj2c;

import bearmaps.hw4.NewTrieSet;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    List<Node> nodes;
    HashMap<Point, Node> map;
    List<Point> points;
    NewTrieSet tset;
    Map<String, List> nameMap = new HashMap<>();
    Map<String, List<Node>> locationMap = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        nodes = this.getNodes();
        map = new HashMap<>();
        points = new ArrayList<>();
        tset = new NewTrieSet();
        for (Node n : nodes) {
            if (!neighbors(n.id()).isEmpty()) {
                Point p = new Point(n.lon(), n.lat());
                map.put(p, n);
                points.add(p);
            }
            if (n.name() != null) {
                String s = cleanString(n.name());
                if (s.length() >= 1) {
                    tset.add(s);
                    if (!nameMap.containsKey(s) && !locationMap.containsKey(s)) {
                        List<String> l = new LinkedList<>();
                        l.add(n.name());
                        nameMap.put(s, l);
                        List<Node> l2 = new LinkedList<>();
                        l2.add(n);
                        locationMap.put(s, l2);
                    } else {
                        nameMap.get(s).add(n.name());
                        locationMap.get(s).add(n);
                    }
                } else {

                }
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        WeirdPointSet wspt = new WeirdPointSet(points);
        Point p = wspt.nearest(lon, lat);
        Node n = map.get(p);
        long vertex = n.id();

        return vertex;
    }

    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleaned = cleanString(prefix);
        Iterator iter = tset.keysWithPrefix(cleaned).iterator();
        LinkedList names = new LinkedList();
        while (iter.hasNext()){
            String name = (String) iter.next();
            List<String> l = nameMap.get(name);
            for (String s : l){
                names.add(s);
            }
        }
        return names;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleaned = cleanString(locationName);
        List<Map<String, Object>> names = new LinkedList();
        if (locationMap.containsKey(cleaned)) {
            if (cleaned.equals("")) {
                if (locationMap.get(cleaned) != null) {
                    List<Node> l = locationMap.get(cleaned);
                    for (Node s : l) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("lat", s.lat());
                        m.put("lon", s.lon());
                        m.put("name", s.name());
                        m.put("id", s.id());
                        names.add(m);
                    }

                }
            } else {
                Iterator iter = tset.keysThatMatch(cleaned).iterator();
                while (iter.hasNext()) {
                    String name = (String) iter.next();
                    List<Node> l = locationMap.get(name);
                    for (Node s : l) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("lat", s.lat());
                        m.put("lon", s.lon());
                        m.put("name", s.name());
                        m.put("id", s.id());
                        names.add(m);
                    }

                }
            }
        }
        return names;
    }

    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
