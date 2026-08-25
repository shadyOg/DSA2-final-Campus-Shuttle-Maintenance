package com.campus.optimizer.graph;

import com.campus.optimizer.db.LocationDAO;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.Road;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link Graph} from the seeded database instead of hand-typed
 * locations, so graph algorithms run against the real campus dataset
 * (every location and road) rather than a handful of hardcoded edges.
 */
public final class CampusGraphLoader {

    private CampusGraphLoader() {
    }

    public static Graph loadFromDatabase() {
        List<Location> locations = new LocationDAO().findAll();
        List<Road> roads = new RoadDAO().findAll();

        Graph graph = new Graph(Math.max(20, locations.size()));

        Map<Integer, String> nameById = new HashMap<>();
        for (Location location : locations) {
            nameById.put(location.getLocationId(), location.getName());
            graph.addVertex(location.getName());
        }

        for (Road road : roads) {
            String from = nameById.get(road.getFromLocationId());
            String to = nameById.get(road.getToLocationId());
            if (from == null || to == null) {
                System.err.println("Skipping road " + road.getRoadId()
                        + ": references a locationId not present in the locations table");
                continue;
            }
            graph.addEdge(from, to, road.getDistance());
        }

        return graph;
    }
}
