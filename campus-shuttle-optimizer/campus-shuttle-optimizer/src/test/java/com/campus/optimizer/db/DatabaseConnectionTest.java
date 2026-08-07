package com.campus.optimizer.db;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    private void deleteDatabaseFile() {
        String dbPath = "src/main/resources/campus.db";
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testConnectionNotNull() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection db = DatabaseConnection.getInstance();
        try (Connection conn = db.getConnection()) {
            assertNotNull("Connection should not be null", conn);
        }
    }

    @Test
    public void testDatabaseInitialization() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.initializeDatabase();
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'")) {
            int tableCount = 0;
            while (rs.next()) {
                tableCount++;
            }
            assertTrue("Database should have at least 6 tables", tableCount >= 6);
        }
    }

    @Test
    public void testExpectedTablesExist() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.initializeDatabase();
        String[] expectedTables = {"locations", "roads", "service_requests", "resources", "algorithm_runs", "audit_events"};
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String table : expectedTables) {
                ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'");
                assertTrue("Table " + table + " should exist", rs.next());
                rs.close();
            }
        }
    }
}
