package com.campus.optimizer;

import com.campus.optimizer.graph.Graph;
import com.campus.optimizer.graph.Kruskal;
import com.campus.optimizer.graph.Prim;
import com.campus.optimizer.optimization.CampusServiceOptimizer;
import com.campus.optimizer.optimization.GreedyMaintenanceAllocator;
import com.campus.optimizer.optimization.OptimizationResult;
import com.campus.optimizer.optimization.ServiceRequest;
import com.campus.optimizer.structures.DynamicArray;
import com.campus.optimizer.structures.HashTable;
import com.campus.optimizer.structures.MinHeap;
import com.campus.optimizer.structures.Stack;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        System.out.println("Campus Shuttle & Maintenance Optimizer");
        demonstrateStructures();
        demonstrateGraphAlgorithms();
        demonstrateOptimisation();
        System.out.println("Phase 1 smoke run completed successfully.");
    }

    private static void demonstrateStructures() {
        DynamicArray<String> locations = new DynamicArray<>();
        locations.add("Balme Library");
        locations.add("Science Block");

        Stack<String> audit = new Stack<>();
        audit.push("CREATE route");

        HashTable<String, Integer> requests = new HashTable<>();
        requests.put("pending", 4);

        MinHeap<Integer> priorities = new MinHeap<>();
        priorities.offer(3);
        priorities.offer(1);

        System.out.println("Structures: locations=" + locations.size()
                + ", auditTop=" + audit.peek()
                + ", pendingRequests=" + requests.get("pending")
                + ", nextPriority=" + priorities.peek());
    }

    private static void demonstrateGraphAlgorithms() {
        Graph graph = new Graph();
        graph.addEdge("Main Gate", "Balme Library", 2.0);
        graph.addEdge("Balme Library", "Science Block", 1.5);
        graph.addEdge("Main Gate", "Science Block", 4.0);
        graph.addEdge("Science Block", "Maintenance Depot", 2.5);

        System.out.println("BFS: " + graph.bfs("Main Gate"));
        System.out.println("DFS: " + graph.dfs("Main Gate"));
        System.out.println("Dijkstra: " + graph.dijkstra("Main Gate"));
        System.out.println("Prim weight: " + Prim.totalWeight(graph));
        System.out.println("Kruskal weight: " + Kruskal.totalWeight(graph));
    }

    private static void demonstrateOptimisation() {
        ServiceRequest[] requestArray = {
                new ServiceRequest("SH-101", "SHUTTLE", "Hostel A", 9, 3, 18),
                new ServiceRequest("MN-204", "MAINTENANCE", "Electrical Lab", 8, 4, 24),
                new ServiceRequest("SH-220", "SHUTTLE", "Lecture Hall 5", 7, 2, 12),
                new ServiceRequest("MN-315", "MAINTENANCE", "Water Plant", 10, 5, 30)
        };

        CampusServiceOptimizer optimizer = new CampusServiceOptimizer();
        OptimizationResult dynamicResult = optimizer.optimize(Arrays.asList(requestArray), 7);
        OptimizationResult greedyResult = new GreedyMaintenanceAllocator()
                .allocate(Arrays.asList(requestArray), 7);

        System.out.println("Dynamic programming: " + dynamicResult.getSelectedRequestIds());
        System.out.println("Greedy maintenance: " + greedyResult.getSelectedRequestIds());
    }
}
