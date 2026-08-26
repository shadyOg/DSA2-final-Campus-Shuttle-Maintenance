package com.campus.optimizer.graph;

import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.Road;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GraphLoaderTest {

    @Test
    public void databaseRoadWeightIncludesConditionPenalty() {
        Location first = new Location(1, "Gate", "North", "entrance", 0.0, 0.0);
        Location second = new Location(2, "Depot", "South", "facility", 0.0, 0.0);
        Road road = new Road(1, 1, 2, 10.0, 5.0, 2.5);

        Graph graph = GraphLoader.buildGraph(Arrays.asList(first, second), Arrays.asList(road));

        assertEquals(25.0, graph.getWeight("Gate", "Depot"), 0.000001);
        assertEquals(25.0, graph.dijkstra("Gate").get("Depot"), 0.000001);
        assertEquals(25.0, Prim.totalWeight(graph), 0.000001);
        assertEquals(25.0, Kruskal.totalWeight(graph), 0.000001);
    }
}