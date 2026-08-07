package com.campus.optimizer.db;

import com.campus.optimizer.model.Road;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoadDAO {
    private final DatabaseConnection db;

    public RoadDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(Road road) {
        String sql = "INSERT INTO roads (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, road.getFromLocationId());
            stmt.setInt(2, road.getToLocationId());
            stmt.setDouble(3, road.getDistance());
            stmt.setDouble(4, road.getTravelTime());
            stmt.setDouble(5, road.getRoadConditionWeight());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    road.setRoadId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert road", e);
        }
    }

    public List<Road> findAll() {
        String sql = "SELECT roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight FROM roads";
        List<Road> roads = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roads.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all roads", e);
        }
        return roads;
    }

    public Optional<Road> findById(int id) {
        String sql = "SELECT roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight FROM roads WHERE roadId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch road by id", e);
        }
        return Optional.empty();
    }

    public List<Road> findByLocation(int locationId) {
        String sql = "SELECT roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight FROM roads WHERE fromLocationId = ? OR toLocationId = ?";
        List<Road> roads = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.setInt(2, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roads.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch roads by location", e);
        }
        return roads;
    }

    public void update(Road road) {
        String sql = "UPDATE roads SET fromLocationId = ?, toLocationId = ?, distance = ?, travelTime = ?, roadConditionWeight = ? WHERE roadId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, road.getFromLocationId());
            stmt.setInt(2, road.getToLocationId());
            stmt.setDouble(3, road.getDistance());
            stmt.setDouble(4, road.getTravelTime());
            stmt.setDouble(5, road.getRoadConditionWeight());
            stmt.setInt(6, road.getRoadId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update road", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM roads WHERE roadId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete road", e);
        }
    }

    private Road mapRow(ResultSet rs) throws SQLException {
        Road road = new Road();
        road.setRoadId(rs.getInt("roadId"));
        road.setFromLocationId(rs.getInt("fromLocationId"));
        road.setToLocationId(rs.getInt("toLocationId"));
        road.setDistance(rs.getDouble("distance"));
        road.setTravelTime(rs.getDouble("travelTime"));
        road.setRoadConditionWeight(rs.getDouble("roadConditionWeight"));
        return road;
    }
}
