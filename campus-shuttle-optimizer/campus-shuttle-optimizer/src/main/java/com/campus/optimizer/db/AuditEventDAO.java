package com.campus.optimizer.db;

import com.campus.optimizer.model.AuditEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditEventDAO {
    private final DatabaseConnection db;

    public AuditEventDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(AuditEvent event) {
        String sql = "INSERT INTO audit_events (eventId, action, details, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (event.getEventId() > 0) {
                stmt.setInt(1, event.getEventId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, event.getAction());
            stmt.setString(3, event.getDetails());
            stmt.setString(4, event.getTimestamp());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    event.setEventId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert audit event", e);
        }
    }

    public List<AuditEvent> findAll() {
        String sql = "SELECT eventId, action, details, timestamp FROM audit_events";
        List<AuditEvent> events = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all audit events", e);
        }
        return events;
    }

    public List<AuditEvent> findRecent(int limit) {
        String sql = "SELECT eventId, action, details, timestamp FROM audit_events ORDER BY timestamp DESC LIMIT ?";
        List<AuditEvent> events = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch recent audit events", e);
        }
        return events;
    }

    public List<AuditEvent> findByAction(String action) {
        String sql = "SELECT eventId, action, details, timestamp FROM audit_events WHERE action = ?";
        List<AuditEvent> events = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, action);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch audit events by action", e);
        }
        return events;
    }

    private AuditEvent mapRow(ResultSet rs) throws SQLException {
        AuditEvent event = new AuditEvent();
        event.setEventId(rs.getInt("eventId"));
        event.setAction(rs.getString("action"));
        event.setDetails(rs.getString("details"));
        event.setTimestamp(rs.getString("timestamp"));
        return event;
    }
}
