package com.campus.optimizer.db;

import com.campus.optimizer.model.ServiceRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRequestDAO {
    private final DatabaseConnection db;

    public ServiceRequestDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(ServiceRequest request) {
        String sql = "INSERT INTO service_requests (requestId, source, destination, category, urgency, timeSubmitted, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (request.getRequestId() > 0) {
                stmt.setInt(1, request.getRequestId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, request.getSource());
            stmt.setString(3, request.getDestination());
            stmt.setString(4, request.getCategory());
            stmt.setInt(5, request.getUrgency());
            stmt.setString(6, request.getTimeSubmitted());
            stmt.setString(7, request.getDeadline());
            stmt.setString(8, request.getStatus());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    request.setRequestId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert service request", e);
        }
    }

    public List<ServiceRequest> findAll() {
        String sql = "SELECT requestId, source, destination, category, urgency, timeSubmitted, deadline, status FROM service_requests";
        List<ServiceRequest> requests = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all service requests", e);
        }
        return requests;
    }

    public Optional<ServiceRequest> findById(int id) {
        String sql = "SELECT requestId, source, destination, category, urgency, timeSubmitted, deadline, status FROM service_requests WHERE requestId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch service request by id", e);
        }
        return Optional.empty();
    }

    public List<ServiceRequest> findByStatus(String status) {
        String sql = "SELECT requestId, source, destination, category, urgency, timeSubmitted, deadline, status FROM service_requests WHERE status = ?";
        List<ServiceRequest> requests = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch service requests by status", e);
        }
        return requests;
    }

    public List<ServiceRequest> findByUrgency(int urgency) {
        String sql = "SELECT requestId, source, destination, category, urgency, timeSubmitted, deadline, status FROM service_requests WHERE urgency = ?";
        List<ServiceRequest> requests = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, urgency);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch service requests by urgency", e);
        }
        return requests;
    }

    public void update(ServiceRequest request) {
        String sql = "UPDATE service_requests SET source = ?, destination = ?, category = ?, urgency = ?, timeSubmitted = ?, deadline = ?, status = ? WHERE requestId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, request.getSource());
            stmt.setString(2, request.getDestination());
            stmt.setString(3, request.getCategory());
            stmt.setInt(4, request.getUrgency());
            stmt.setString(5, request.getTimeSubmitted());
            stmt.setString(6, request.getDeadline());
            stmt.setString(7, request.getStatus());
            stmt.setInt(8, request.getRequestId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update service request", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM service_requests WHERE requestId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete service request", e);
        }
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        ServiceRequest req = new ServiceRequest();
        req.setRequestId(rs.getInt("requestId"));
        req.setSource(rs.getString("source"));
        req.setDestination(rs.getString("destination"));
        req.setCategory(rs.getString("category"));
        req.setUrgency(rs.getInt("urgency"));
        req.setTimeSubmitted(rs.getString("timeSubmitted"));
        req.setDeadline(rs.getString("deadline"));
        req.setStatus(rs.getString("status"));
        return req;
    }
}
