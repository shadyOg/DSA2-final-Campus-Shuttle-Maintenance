package com.campus.optimizer.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private final Path dbFile;
    private final String url;
    private Connection connection;

    private DatabaseConnection() {
        this.dbFile = resolveDbFile();
        this.url = "jdbc:sqlite:" + dbFile;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    /**
     * Resolves the SQLite file at {@code <module root>/src/main/resources/campus.db},
     * where the module root is found by walking up from wherever this class was loaded
     * from until a pom.xml turns up. This keeps the path correct regardless of the
     * process's working directory (IDE run configs, CI, or a plain terminal all differ),
     * instead of relying on a relative path that only resolves correctly by accident.
     */
    private static Path resolveDbFile() {
        return moduleRoot().resolve("src/main/resources/campus.db").toAbsolutePath();
    }

    private static Path moduleRoot() {
        try {
            Path codeLocation = Paths.get(
                    DatabaseConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path dir = Files.isDirectory(codeLocation) ? codeLocation : codeLocation.getParent();
            while (dir != null && !Files.exists(dir.resolve("pom.xml"))) {
                dir = dir.getParent();
            }
            if (dir != null) {
                return dir;
            }
        } catch (Exception ignored) {
            // Fall back to the working directory below.
        }
        return Paths.get("").toAbsolutePath();
    }

    /** Absolute path to the SQLite file backing this connection. */
    public Path getDbFile() {
        return dbFile;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    public void initializeDatabase() {
        String schema = loadSchema();
        if (schema == null || schema.isBlank()) {
            return;
        }
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(schema);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    private String loadSchema() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
            if (is == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read schema.sql", e);
        }
    }
}
