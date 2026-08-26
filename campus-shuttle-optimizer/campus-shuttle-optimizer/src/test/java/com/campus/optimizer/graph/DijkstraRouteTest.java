package com.campus.optimizer.graph;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DijkstraRouteTest {

    @Test
    public void shortestRouteReturnsOrderedPathAndDistance() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 2.0);
        graph.addEdge("B", "C", 3.0);
        graph.addEdge("A", "C", 10.0);

        RouteResult result = graph.shortestRoute("A", "C");

        assertTrue(result.isReachable());
        assertEquals(Arrays.asList("A", "B", "C"), result.getPath());
        assertEquals(5.0, result.getDistance(), 0.000001);
    }

    @Test
    public void shortestRouteHandlesSameInvalidAndUnreachableLocations() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1.0);
        graph.addVertex("C");

        RouteResult same = graph.shortestRoute("A", "A");
        assertEquals(Arrays.asList("A"), same.getPath());
        assertEquals(0.0, same.getDistance(), 0.000001);

        RouteResult invalid = graph.shortestRoute("missing", "A");
        assertFalse(invalid.isReachable());
        assertTrue(invalid.getDistance() == Double.POSITIVE_INFINITY);

        RouteResult unreachable = graph.shortestRoute("A", "C");
        assertFalse(unreachable.isReachable());
        assertTrue(unreachable.getDistance() == Double.POSITIVE_INFINITY);
    }
}