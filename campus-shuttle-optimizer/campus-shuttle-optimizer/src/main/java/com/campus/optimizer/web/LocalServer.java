package com.campus.optimizer.web;

import com.campus.optimizer.db.DatabaseLoader;
import com.campus.optimizer.db.LocationDAO;
import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.AuditEventDAO;
import com.campus.optimizer.db.ResourceDAO;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.db.ServiceRequestDAO;
import com.campus.optimizer.graph.Graph;
import com.campus.optimizer.graph.GraphLoader;
import com.campus.optimizer.graph.Kruskal;
import com.campus.optimizer.graph.MstEdge;
import com.campus.optimizer.graph.Prim;
import com.campus.optimizer.graph.RouteResult;
import com.campus.optimizer.model.AuditEvent;
import com.campus.optimizer.model.Resource;
import com.campus.optimizer.model.ServiceRequest;
import com.campus.optimizer.optimization.CampusServiceOptimizer;
import com.campus.optimizer.optimization.OptimizationResult;
import com.campus.optimizer.integration.GreedyMaintenanceAllocatorService;
import com.campus.optimizer.sort.BenchmarkHarness;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class LocalServer {
    private static final int DEFAULT_PORT = 8080;
    private static final Pattern CAPACITY_PATTERN = Pattern.compile("\\\"capacity\\\"\\s*:\\s*(\\d+)");
    private final HttpServer server;

    private LocalServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/api/locations", this::locations);
        server.createContext("/api/requests", this::requests);
        server.createContext("/api/resources", this::resources);
        server.createContext("/api/route", this::route);
        server.createContext("/api/mst", this::mst);
        server.createContext("/api/optimize", this::optimize);
        server.createContext("/api/benchmark", this::benchmark);
        server.createContext("/api/audit", this::audit);
        server.createContext("/api/audit/undo", this::undoAudit);
        server.createContext("/", this::frontend);
    }

    public static void main(String[] args) throws IOException {
        DatabaseLoader.initDatabase();
        int port = args.length == 0 ? DEFAULT_PORT : Integer.parseInt(args[0]);
        LocalServer localServer = new LocalServer(port);
        localServer.server.start();
        System.out.println("Local UI available at http://localhost:" + port);
    }

    private void locations(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) {
            return;
        }
        List<String> locations = new LocationDAO().findAll().stream()
                .map(location -> location.getName())
                .collect(Collectors.toList());
        sendJson(exchange, 200, toJsonArray(locations));
    }

    private void requests(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) return;
        List<ServiceRequest> requests = new ServiceRequestDAO().findByStatus("pending");
        sendJson(exchange, 200, requests.stream().map(request -> "{\"id\":" + request.getRequestId()
                + ",\"category\":" + quote(request.getCategory())
                + ",\"source\":" + quote(request.getSource())
                + ",\"destination\":" + quote(request.getDestination())
                + ",\"urgency\":" + request.getUrgency() + "}").collect(Collectors.joining(",", "[", "]")));
    }

    private void resources(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) return;
        List<Resource> resources = new ResourceDAO().findByStatus("available");
        sendJson(exchange, 200, resources.stream().map(resource -> "{\"id\":" + resource.getResourceId()
                + ",\"type\":" + quote(resource.getType())
                + ",\"location\":" + quote(resource.getHomeLocation())
                + ",\"capacity\":" + resource.getCapacity() + "}").collect(Collectors.joining(",", "[", "]")));
    }

    private void route(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) {
            return;
        }
        Map<String, String> query = parseQuery(exchange.getRequestURI().getRawQuery());
        String source = query.get("from");
        String destination = query.get("to");
        if (source == null || destination == null) {
            sendJson(exchange, 400, "{\"error\":\"from and to are required\"}");
            return;
        }

        Graph graph = GraphLoader.loadFromDatabase();
        RouteResult result = graph.shortestRoute(source, destination);
        if (!result.isReachable()) {
            recordAudit("ROUTE_NOT_FOUND", source + " -> " + destination);
            sendJson(exchange, 404, "{\"error\":\"No route found\"}");
            return;
        }
        recordAudit("ROUTE_SEARCH", source + " -> " + destination + " (" + result.getDistance() + ")");
        sendJson(exchange, 200, "{\"source\":" + quote(result.getSource())
                + ",\"destination\":" + quote(result.getDestination())
                + ",\"path\":" + toJsonArray(result.getPath())
                + ",\"distance\":" + result.getDistance() + "}");
    }

    private void mst(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) return;
        Graph graph = GraphLoader.loadFromDatabase();
        List<MstEdge> prim = Prim.minimumSpanningTree(graph);
        List<MstEdge> kruskal = Kruskal.minimumSpanningTree(graph);
        recordAudit("MST_CALCULATION", "Prim and Kruskal calculated for " + graph.getNodeCount() + " locations");
        sendJson(exchange, 200, "{\"prim\":" + edgesJson(prim)
            + ",\"kruskal\":" + edgesJson(kruskal) + "}");
    }

    private void optimize(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Matcher capacityMatcher = CAPACITY_PATTERN.matcher(body);
        if (!capacityMatcher.find()) {
            sendJson(exchange, 400, "{\"error\":\"capacity is required\"}");
            return;
        }
        int capacity = Integer.parseInt(capacityMatcher.group(1));
        if (capacity <= 0) {
            sendJson(exchange, 400, "{\"error\":\"capacity must be positive\"}");
            return;
        }
        OptimizationResult knapsack = new CampusServiceOptimizer().optimizePendingRequests(capacity);
        OptimizationResult greedy = new GreedyMaintenanceAllocatorService().allocateMaintenanceRequests(capacity);
        recordAudit("OPTIMIZATION_RUN", "Compared plans with " + capacity + " working minutes");
        sendJson(exchange, 200, "{\"knapsack\":" + optimizationJson(knapsack)
                + ",\"greedy\":" + optimizationJson(greedy) + "}");
    }

    private void benchmark(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        BenchmarkHarness.runAndPersistDefaultSuite();
        recordAudit("BENCHMARK_RUN", "Search and sort benchmark suite completed");
        sendJson(exchange, 200, "{\"runs\":" + new AlgorithmRunDAO().findLatest(24).size() + "}");
    }

    private void audit(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) return;
        List<AuditEvent> events = new AuditEventDAO().findRecent(12);
        sendJson(exchange, 200, events.stream().map(event -> "{\"action\":" + quote(event.getAction())
                + ",\"details\":" + quote(event.getDetails())
                + ",\"timestamp\":" + quote(event.getTimestamp()) + "}")
                .collect(Collectors.joining(",", "[", "]")));
    }

    private void undoAudit(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        AuditEventDAO dao = new AuditEventDAO();
        if (dao.liveAuditCount() == 0) {
            sendJson(exchange, 409, "{\"error\":\"No live session action to undo\"}");
            return;
        }
        AuditEvent undone = dao.undoLast();
        sendJson(exchange, 200, "{\"action\":" + quote(undone.getAction())
                + ",\"details\":" + quote(undone.getDetails()) + "}");
    }

    private void frontend(HttpExchange exchange) throws IOException {
        if (!allowGet(exchange)) {
            return;
        }
        String resource = exchange.getRequestURI().getPath().equals("/")
                ? "index.html" : exchange.getRequestURI().getPath().substring(1);
        Path file = Path.of("src/main/resources/web").resolve(resource).normalize();
        if (!file.startsWith(Path.of("src/main/resources/web")) || !Files.isRegularFile(file)) {
            sendText(exchange, 404, "Not found", "text/plain");
            return;
        }
        String contentType = resource.endsWith(".css") ? "text/css"
            : resource.endsWith(".js") ? "application/javascript"
            : "text/html; charset=utf-8";
        sendText(exchange, 200, Files.readString(file), contentType);
    }

    private boolean allowGet(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return false;
        }
        return true;
    }

    private Map<String, String> parseQuery(String rawQuery) {
        if (rawQuery == null || rawQuery.isBlank()) {
            return Map.of();
        }
        return java.util.Arrays.stream(rawQuery.split("&"))
                .map(pair -> pair.split("=", 2))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        pair -> decode(pair[0]), pair -> decode(pair[1]), (first, second) -> second));
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private String toJsonArray(List<String> values) {
        return values.stream().map(this::quote).collect(Collectors.joining(",", "[", "]"));
    }

    private String quote(String value) {
        if (value == null) return "null";
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private String edgesJson(List<MstEdge> edges) {
        return edges.stream().map(edge -> "{\"from\":" + quote(edge.getSource())
                + ",\"to\":" + quote(edge.getDestination())
                + ",\"weight\":" + edge.getWeight() + "}")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String optimizationJson(OptimizationResult result) {
        return "{\"selected\":" + toJsonArray(result.getSelectedRequestIds())
                + ",\"benefit\":" + result.getTotalBenefit()
                + ",\"duration\":" + result.getTotalDuration() + "}";
    }

    private void recordAudit(String action, String details) {
        new AuditEventDAO().insert(new AuditEvent(0, action, details, Instant.now().toString()));
    }

    private void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        sendText(exchange, status, body, "application/json; charset=utf-8");
    }

    private void sendText(HttpExchange exchange, int status, String body, String contentType)
            throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, response.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }
}