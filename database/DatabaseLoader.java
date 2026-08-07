```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLoader {

    private static final String DB_URL = "jdbc:sqlite:campus_shuttle.db";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            System.out.println("Connected to database successfully.");

            loadLocations(connection);
            loadRoads(connection);
            loadServiceRequests(connection);
            loadResources(connection);
            loadAlgorithmRuns(connection);
            loadAuditEvents(connection);

            System.out.println("All CSV data loaded successfully.");

        } catch (SQLException e) {
            System.err.println("Database error:");
            e.printStackTrace();
        }
    }

    private static void loadLocations(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO locations
                (location_id, name, location_type, description)
                VALUES (?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/locations.csv",
                sql,
                4
        );
    }

    private static void loadRoads(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO roads
                (road_id, from_location_id, to_location_id,
                 distance_km, travel_time_min, road_condition_penalty)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/roads.csv",
                sql,
                6
        );
    }

    private static void loadServiceRequests(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO service_requests
                (request_id, request_type, source_location_id,
                 destination_location_id, maintenance_category,
                 urgency, submitted_at, deadline, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/service_requests.csv",
                sql,
                9
        );
    }

    private static void loadResources(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO resources
                (resource_id, resource_type, name, specialty,
                 home_location_id, capacity, availability_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/resources.csv",
                sql,
                7
        );
    }

    private static void loadAlgorithmRuns(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO algorithm_runs
                (run_id, algorithm_name, input_size,
                 runtime_ms, memory_bytes, run_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/algorithm_runs.csv",
                sql,
                6
        );
    }

    private static void loadAuditEvents(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO audit_events
                (event_id, action_type, entity_type, entity_id,
                 action_details, performed_at, stack_position)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        loadCsv(
                connection,
                "database/csv/audit_events.csv",
                sql,
                7
        );
    }

    private static void loadCsv(
            Connection connection,
            String filePath,
            String sql,
            int expectedColumns) throws SQLException {

        try (
                BufferedReader reader =
                        new BufferedReader(new FileReader(filePath));
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            // Skip the CSV header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length != expectedColumns) {
                    System.err.println(
                            "Skipping invalid row in "
                            + filePath + ": " + line
                    );
                    continue;
                }

                for (int i = 0; i < values.length; i++) {

                    String value = values[i].trim();

                    if (value.isEmpty()) {
                        statement.setObject(i + 1, null);
                    } else {
                        statement.setString(i + 1, value);
                    }
                }

                statement.executeUpdate();
            }

            System.out.println("Loaded: " + filePath);

        } catch (IOException e) {
            throw new SQLException(
                    "Could not read CSV file: " + filePath,
                    e
            );
        }
    }
}
```
