package com.campus.optimizer.db;

import com.campus.optimizer.model.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDAO {
    private final DatabaseConnection db;

    public LocationDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(Location location) {
        String sql = "INSERT INTO locations (locationId, name, area, type, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (location.getLocationId() > 0) {
                stmt.setInt(1, location.getLocationId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, location.getName());
            stmt.setString(3, location.getArea());
            stmt.setString(4, location.getType());
            stmt.setDouble(5, location.getLatitude());
            stmt.setDouble(6, location.getLongitude());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    location.setLocationId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert location", e);
        }
    }

    public List<Location> findAll() {
        String sql = "SELECT locationId, name, area, type, latitude, longitude FROM locations";
        List<Location> locations = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all locations", e);
        }
        return locations;
    }

    public Optional<Location> findById(int id) {
        String sql = "SELECT locationId, name, area, type, latitude, longitude FROM locations WHERE locationId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch location by id", e);
        }
        return Optional.empty();
    }

    public Optional<Location> findByName(String name) {
        String sql = "SELECT locationId, name, area, type, latitude, longitude FROM locations WHERE name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch location by name", e);
        }
        return Optional.empty();
    }

    public void update(Location location) {
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, latitude = ?, longitude = ? WHERE locationId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location.getName());
            stmt.setString(2, location.getArea());
            stmt.setString(3, location.getType());
            stmt.setDouble(4, location.getLatitude());
            stmt.setDouble(5, location.getLongitude());
            stmt.setInt(6, location.getLocationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update location", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM locations WHERE locationId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete location", e);
        }
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        Location loc = new Location();
        loc.setLocationId(rs.getInt("locationId"));
        loc.setName(rs.getString("name"));
        loc.setArea(rs.getString("area"));
        loc.setType(rs.getString("type"));
        loc.setLatitude(rs.getDouble("latitude"));
        loc.setLongitude(rs.getDouble("longitude"));
        return loc;
    }
}
