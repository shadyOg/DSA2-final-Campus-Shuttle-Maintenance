package com.campus.optimizer.db;

import com.campus.optimizer.model.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourceDAO {
    private final DatabaseConnection db;

    public ResourceDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(Resource resource) {
        String sql = "INSERT INTO resources (type, homeLocation, capacity, availabilityStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, resource.getType());
            stmt.setString(2, resource.getHomeLocation());
            stmt.setInt(3, resource.getCapacity());
            stmt.setString(4, resource.getAvailabilityStatus());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    resource.setResourceId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert resource", e);
        }
    }

    public List<Resource> findAll() {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources";
        List<Resource> resources = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all resources", e);
        }
        return resources;
    }

    public Optional<Resource> findById(int id) {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources WHERE resourceId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch resource by id", e);
        }
        return Optional.empty();
    }

    public List<Resource> findByStatus(String status) {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources WHERE availabilityStatus = ?";
        List<Resource> resources = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch resources by status", e);
        }
        return resources;
    }

    public List<Resource> findByType(String type) {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources WHERE type = ?";
        List<Resource> resources = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch resources by type", e);
        }
        return resources;
    }

    public void update(Resource resource) {
        String sql = "UPDATE resources SET type = ?, homeLocation = ?, capacity = ?, availabilityStatus = ? WHERE resourceId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getType());
            stmt.setString(2, resource.getHomeLocation());
            stmt.setInt(3, resource.getCapacity());
            stmt.setString(4, resource.getAvailabilityStatus());
            stmt.setInt(5, resource.getResourceId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update resource", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM resources WHERE resourceId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete resource", e);
        }
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        Resource res = new Resource();
        res.setResourceId(rs.getInt("resourceId"));
        res.setType(rs.getString("type"));
        res.setHomeLocation(rs.getString("homeLocation"));
        res.setCapacity(rs.getInt("capacity"));
        res.setAvailabilityStatus(rs.getString("availabilityStatus"));
        return res;
    }
}
