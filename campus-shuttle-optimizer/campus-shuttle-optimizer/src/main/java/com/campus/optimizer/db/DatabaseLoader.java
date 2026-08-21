package com.campus.optimizer.db;

import com.campus.optimizer.model.AlgorithmRun;
import com.campus.optimizer.model.AuditEvent;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.Resource;
import com.campus.optimizer.model.Road;
import com.campus.optimizer.model.ServiceRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLoader {

    /**
     * Prepares the database for use: creates the schema if needed, seeds it from the
     * bundled CSVs on a first run, and skips reseeding on every subsequent run so the
     * same jar/IDE run configuration can be re-executed on any machine without piling
     * up duplicate rows or crashing on unique-key conflicts.
     */
    public static void initDatabase() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.initializeDatabase();
        System.out.println("Using database: " + db.getDbFile());

        if (isSeeded()) {
            System.out.println("Database already contains data - skipping seed load.");
        } else {
            loadAllData();
        }

        verifySchemaIntegrity();
    }

    /** True if the database already has location rows, i.e. a previous run seeded it. */
    private static boolean isSeeded() {
        String sql = "SELECT COUNT(*) FROM locations";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check whether the database is already seeded", e);
        }
    }

    /**
     * Checks the seeded database end-to-end: every table has rows, and every
     * name/id reference between tables (roads -&gt; locations, resources -&gt;
     * locations, service_requests -&gt; locations) actually resolves. Nothing here
     * is enforced by a FOREIGN KEY constraint at the schema level, so this is the
     * only thing standing between a bad CSV edit and a silently broken dataset.
     */
    public static boolean verifySchemaIntegrity() {
        List<String> issues = new ArrayList<>();
        String[] tables = {
                "locations", "roads", "service_requests",
                "resources", "algorithm_runs", "audit_events"
        };

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            for (String table : tables) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        issues.add("Table '" + table + "' is empty.");
                    }
                }
            }

            issues.addAll(countIssue(stmt,
                    "SELECT COUNT(*) FROM roads r WHERE "
                            + "NOT EXISTS (SELECT 1 FROM locations l WHERE l.locationId = r.fromLocationId) "
                            + "OR NOT EXISTS (SELECT 1 FROM locations l WHERE l.locationId = r.toLocationId)",
                    "road(s) reference a locationId that does not exist"));

            issues.addAll(countIssue(stmt,
                    "SELECT COUNT(*) FROM resources res WHERE "
                            + "NOT EXISTS (SELECT 1 FROM locations l WHERE l.name = res.homeLocation)",
                    "resource(s) reference a homeLocation that does not exist"));

            issues.addAll(countIssue(stmt,
                    "SELECT COUNT(*) FROM service_requests sr WHERE "
                            + "NOT EXISTS (SELECT 1 FROM locations l WHERE l.name = sr.source) "
                            + "OR NOT EXISTS (SELECT 1 FROM locations l WHERE l.name = sr.destination)",
                    "service request(s) reference a source/destination that does not exist"));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to verify schema integrity", e);
        }

        if (issues.isEmpty()) {
            System.out.println("Schema integrity check passed: all tables populated, all references resolve.");
            return true;
        }

        System.err.println("Schema integrity check found " + issues.size() + " issue(s):");
        for (String issue : issues) {
            System.err.println("  - " + issue);
        }
        return false;
    }

    private static List<String> countIssue(Statement stmt, String sql, String message) throws SQLException {
        List<String> issues = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                issues.add(count + " " + message + ".");
            }
        }
        return issues;
    }

    private static void loadAllData() {
        loadLocations();
        loadRoads();
        loadResources();
        loadServiceRequests();
        loadAlgorithmRuns();
        loadAuditEvents();
        System.out.println("All seed data loaded successfully.");
    }

    public static void loadLocations() {
        List<Location> locations = readCsv("data/locations.csv", row -> {
            if (row.length < 6) return null;
            Location loc = new Location();
            loc.setLocationId(Integer.parseInt(row[0].trim()));
            loc.setName(row[1].trim());
            loc.setArea(row[2].trim());
            loc.setType(row[3].trim());
            loc.setLatitude(Double.parseDouble(row[4].trim()));
            loc.setLongitude(Double.parseDouble(row[5].trim()));
            return loc;
        });
        LocationDAO dao = new LocationDAO();
        for (Location loc : locations) {
            try {
                dao.insert(loc);
            } catch (Exception e) {
                System.err.println("Failed to insert location: " + loc.getName() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + locations.size() + " locations.");
    }

    public static void loadRoads() {
        List<Road> roads = readCsv("data/roads.csv", row -> {
            if (row.length < 6) return null;
            Road road = new Road();
            road.setRoadId(Integer.parseInt(row[0].trim()));
            road.setFromLocationId(Integer.parseInt(row[1].trim()));
            road.setToLocationId(Integer.parseInt(row[2].trim()));
            road.setDistance(Double.parseDouble(row[3].trim()));
            road.setTravelTime(Double.parseDouble(row[4].trim()));
            road.setRoadConditionWeight(Double.parseDouble(row[5].trim()));
            return road;
        });
        RoadDAO dao = new RoadDAO();
        for (Road road : roads) {
            try {
                dao.insert(road);
            } catch (Exception e) {
                System.err.println("Failed to insert road: " + road.getRoadId() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + roads.size() + " roads.");
    }

    public static void loadResources() {
        List<Resource> resources = readCsv("data/resources.csv", row -> {
            if (row.length < 5) return null;
            Resource res = new Resource();
            res.setResourceId(Integer.parseInt(row[0].trim()));
            res.setType(row[1].trim());
            res.setHomeLocation(row[2].trim());
            res.setCapacity(Integer.parseInt(row[3].trim()));
            res.setAvailabilityStatus(row[4].trim());
            return res;
        });
        ResourceDAO dao = new ResourceDAO();
        for (Resource res : resources) {
            try {
                dao.insert(res);
            } catch (Exception e) {
                System.err.println("Failed to insert resource: " + res.getResourceId() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + resources.size() + " resources.");
    }

    public static void loadServiceRequests() {
        List<ServiceRequest> requests = readCsv("data/service_requests.csv", row -> {
            if (row.length < 8) return null;
            ServiceRequest req = new ServiceRequest();
            req.setRequestId(Integer.parseInt(row[0].trim()));
            req.setSource(row[1].trim());
            req.setDestination(row[2].trim());
            req.setCategory(row[3].trim());
            req.setUrgency(Integer.parseInt(row[4].trim()));
            req.setTimeSubmitted(row[5].trim());
            req.setDeadline(row[6].trim());
            req.setStatus(row[7].trim());
            return req;
        });
        ServiceRequestDAO dao = new ServiceRequestDAO();
        for (ServiceRequest req : requests) {
            try {
                dao.insert(req);
            } catch (Exception e) {
                System.err.println("Failed to insert request: " + req.getRequestId() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + requests.size() + " service requests.");
    }

    public static void loadAlgorithmRuns() {
        List<AlgorithmRun> runs = readCsv("data/algorithm_runs.csv", row -> {
            if (row.length < 6) return null;
            AlgorithmRun run = new AlgorithmRun();
            run.setRunId(Integer.parseInt(row[0].trim()));
            run.setAlgorithmName(row[1].trim());
            run.setInputSize(Integer.parseInt(row[2].trim()));
            run.setTimeNs(Double.parseDouble(row[3].trim()));
            run.setMemoryKb(Double.parseDouble(row[4].trim()));
            run.setDateRun(row[5].trim());
            return run;
        });
        AlgorithmRunDAO dao = new AlgorithmRunDAO();
        for (AlgorithmRun run : runs) {
            try {
                dao.insert(run);
            } catch (Exception e) {
                System.err.println("Failed to insert algorithm run: " + run.getRunId() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + runs.size() + " algorithm runs.");
    }

    public static void loadAuditEvents() {
        List<AuditEvent> events = readCsv("data/audit_events.csv", row -> {
            if (row.length < 4) return null;
            AuditEvent ev = new AuditEvent();
            ev.setEventId(Integer.parseInt(row[0].trim()));
            ev.setAction(row[1].trim());
            ev.setDetails(row[2].trim());
            ev.setTimestamp(row[3].trim());
            return ev;
        });
        AuditEventDAO dao = new AuditEventDAO();
        for (AuditEvent ev : events) {
            try {
                dao.insert(ev);
            } catch (Exception e) {
                System.err.println("Failed to insert audit event: " + ev.getEventId() + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + events.size() + " audit events.");
    }

    private static <T> List<T> readCsv(String resourcePath, CsvRowMapper<T> mapper) {
        List<T> result = new ArrayList<>();
        try (InputStream is = DatabaseLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Resource not found: " + resourcePath);
                return result;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] row = parseCsvLine(line);
                    if (row != null && row.length > 0 && !row[0].isEmpty()) {
                        T obj = mapper.mapRow(row);
                        if (obj != null) {
                            result.add(obj);
                        } else {
                            System.err.println("Skipping malformed row in " + resourcePath + ": " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV: " + resourcePath, e);
        }
        return result;
    }

    private static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    @FunctionalInterface
    interface CsvRowMapper<T> {
        T mapRow(String[] row);
    }
}
