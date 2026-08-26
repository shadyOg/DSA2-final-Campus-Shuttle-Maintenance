package com.campus.optimizer;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.AuditEventDAO;
import com.campus.optimizer.db.DatabaseLoader;
import com.campus.optimizer.db.LocationDAO;
import com.campus.optimizer.db.ResourceDAO;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.db.ServiceRequestDAO;
import com.campus.optimizer.graph.Graph;
import com.campus.optimizer.graph.GraphLoader;
import com.campus.optimizer.graph.Kruskal;
import com.campus.optimizer.graph.MstEdge;
import com.campus.optimizer.graph.Prim;
import com.campus.optimizer.graph.RouteResult;
import com.campus.optimizer.integration.GreedyMaintenanceAllocatorService;
import com.campus.optimizer.model.AuditEvent;
import com.campus.optimizer.model.Resource;
import com.campus.optimizer.model.ServiceRequest;
import com.campus.optimizer.optimization.CampusServiceOptimizer;
import com.campus.optimizer.optimization.OptimizationResult;
import com.campus.optimizer.sort.BenchmarkHarness;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Campus Shuttle & Maintenance Optimizer");
        DatabaseLoader.initDatabase();
        new App(new Scanner(System.in)).run();
    }

    private final Scanner scanner;
    private final LocationDAO locationDAO = new LocationDAO();
    private final RoadDAO roadDAO = new RoadDAO();
    private final ServiceRequestDAO serviceRequestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final AlgorithmRunDAO algorithmRunDAO = new AlgorithmRunDAO();
    private final AuditEventDAO auditEventDAO = new AuditEventDAO();
    private final CampusServiceOptimizer serviceOptimizer = new CampusServiceOptimizer();

    private App(Scanner scanner) {
        this.scanner = scanner;
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> showLocationsAndRoads();
                    case "2" -> showRequestsAndResources();
                    case "3" -> planRoute();
                    case "4" -> showSpanningTrees();
                    case "5" -> allocateCrew();
                    case "6" -> runBenchmarks();
                    case "7" -> showAuditAndUndo();
                    case "8" -> running = false;
                    default -> System.out.println("Choose an option from 1 to 8.");
                }
            } catch (RuntimeException exception) {
                System.out.println("Operation failed: " + exception.getMessage());
            }
            if (running) {
                System.out.println("Press Enter to return to the menu.");
                scanner.nextLine();
            }
        }
        System.out.println("Application closed.");
    }

    private void printMenu() {
        System.out.println("\nCampus Shuttle & Maintenance Optimizer");
        System.out.println("1. Manage campus locations and roads");
        System.out.println("2. Manage service requests and resources");
        System.out.println("3. Plan shortest route");
        System.out.println("4. Compute campus spanning trees");
        System.out.println("5. Allocate crew and optimize requests");
        System.out.println("6. Run benchmark suite");
        System.out.println("7. View audit trail and undo latest session action");
        System.out.println("8. Exit");
        System.out.print("Select an option: ");
    }

    private void showLocationsAndRoads() {
        System.out.println("Locations:");
        locationDAO.findAll().forEach(location -> System.out.println("  " + location));
        System.out.println("Roads: " + roadDAO.findAll().size());
        roadDAO.findAll().forEach(road -> System.out.println("  " + road));
    }

    private void showRequestsAndResources() {
        List<ServiceRequest> requests = serviceRequestDAO.findByStatus("pending");
        System.out.println("Pending service requests: " + requests.size());
        requests.forEach(request -> System.out.println("  " + request));

        List<Resource> resources = resourceDAO.findByStatus("available");
        System.out.println("Available resources: " + resources.size());
        resources.forEach(resource -> System.out.println("  " + resource));
    }

    private void planRoute() {
        Graph graph = GraphLoader.loadFromDatabase();
        System.out.print("Source location: ");
        String source = scanner.nextLine().trim();
        System.out.print("Destination location: ");
        String destination = scanner.nextLine().trim();
        RouteResult route = graph.shortestRoute(source, destination);
        if (!route.isReachable()) {
            System.out.println("No route found between those locations.");
            return;
        }
        System.out.println("Route: " + String.join(" -> ", route.getPath()));
        System.out.println("Shortest penalized route distance: " + route.getDistance());
    }

    private void showSpanningTrees() {
        Graph graph = GraphLoader.loadFromDatabase();
        printTree("Prim", Prim.minimumSpanningTree(graph));
        printTree("Kruskal", Kruskal.minimumSpanningTree(graph));
    }

    private void printTree(String name, List<MstEdge> edges) {
        System.out.println(name + " MST (" + edges.size() + " edges):");
        edges.forEach(edge -> System.out.println("  " + edge));
    }

    private void allocateCrew() {
        int capacity = readPositiveInt("Working capacity in minutes: ");
        OptimizationResult dynamicResult = serviceOptimizer.optimizePendingRequests(capacity);
        OptimizationResult greedyResult = new GreedyMaintenanceAllocatorService()
                .allocateMaintenanceRequests(capacity);
        printOptimization("0/1 Knapsack", dynamicResult);
        printOptimization("Greedy maintenance", greedyResult);
    }

    private void printOptimization(String name, OptimizationResult result) {
        System.out.println(name + ": selected=" + result.getSelectedRequestIds()
                + ", benefit=" + result.getTotalBenefit()
                + ", duration=" + result.getTotalDuration() + " minutes");
    }

    private void runBenchmarks() {
        BenchmarkHarness.runAndPersistDefaultSuite();
        algorithmRunDAO.findLatest(24).forEach(run -> System.out.println("  " + run));
    }

    private void showAuditAndUndo() {
        System.out.println("Recent audit events:");
        auditEventDAO.findRecent(10).forEach(event -> System.out.println("  " + event));
        System.out.println("Live session actions: " + auditEventDAO.liveAuditCount());
        if (auditEventDAO.liveAuditCount() == 0) {
            return;
        }
        System.out.print("Undo latest live action? (y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            AuditEvent undone = auditEventDAO.undoLast();
            System.out.println("Undid: " + undone.getAction() + " - " + undone.getDetails());
        }
    }

    private int readPositiveInt(String prompt) {
        System.out.print(prompt);
        int value = Integer.parseInt(scanner.nextLine().trim());
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        return value;
    }
}
