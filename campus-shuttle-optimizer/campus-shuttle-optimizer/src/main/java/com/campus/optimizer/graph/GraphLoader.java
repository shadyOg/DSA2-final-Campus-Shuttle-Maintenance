package com.campus.optimizer.graph;

import com.campus.optimizer.db.LocationDAO;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.Road;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link Graph} from the real SQLite-backed campus data (locations + roads),
 * replacing the hardcoded demo graph used in Phase 1. Vertices are location names
 * (matching {@link Graph}'s String-keyed API); edge weight is road distance.
 */
public final class GraphLoader {

    private GraphLoader() {
    }

    public static Graph loadFromDatabase() {
        List<Location> locations = new LocationDAO().findAll();
        List<Road> roads = new RoadDAO().findAll();
        return buildGraph(locations, roads);
    }

    static Graph buildGraph(List<Location> locations, List<Road> roads) {
        Graph graph = new Graph(Math.max(locations.size(), 1));

        Map<Integer, String> idToName = new HashMap<>(Math.max(locations.size() * 2, 16));
        for (Location loc : locations) {
            graph.addVertex(loc.getName());
            idToName.put(loc.getLocationId(), loc.getName());
        }

        for (Road road : roads) {
            String from = idToName.get(road.getFromLocationId());
            String to = idToName.get(road.getToLocationId());
            if (from == null || to == null) {
                System.err.println("Skipping road " + road.getRoadId()
                        + ": references unknown location id(s) " + road.getFromLocationId()
                        + " -> " + road.getToLocationId());
                continue;
            }
            graph.addEdge(from, to, road.getDistance());
        }

        return graph;
    }
}
