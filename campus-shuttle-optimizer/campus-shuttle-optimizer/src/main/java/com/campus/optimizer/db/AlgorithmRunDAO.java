package com.campus.optimizer.db;

import com.campus.optimizer.model.AlgorithmRun;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmRunDAO {
    private final DatabaseConnection db;

    public AlgorithmRunDAO() {
        this.db = DatabaseConnection.getInstance();
    }

    public void insert(AlgorithmRun run) {
        String sql = "INSERT INTO algorithm_runs (algorithmName, inputSize, timeNs, memoryKb, dateRun) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, run.getAlgorithmName());
            stmt.setInt(2, run.getInputSize());
            stmt.setDouble(3, run.getTimeNs());
            stmt.setDouble(4, run.getMemoryKb());
            stmt.setString(5, run.getDateRun());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    run.setRunId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert algorithm run", e);
        }
    }

    public List<AlgorithmRun> findAll() {
        String sql = "SELECT runId, algorithmName, inputSize, timeNs, memoryKb, dateRun FROM algorithm_runs";
        List<AlgorithmRun> runs = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                runs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all algorithm runs", e);
        }
        return runs;
    }

    public List<AlgorithmRun> findByAlgorithm(String algorithmName) {
        String sql = "SELECT runId, algorithmName, inputSize, timeNs, memoryKb, dateRun FROM algorithm_runs WHERE algorithmName = ?";
        List<AlgorithmRun> runs = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, algorithmName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    runs.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch algorithm runs by name", e);
        }
        return runs;
    }

    public List<AlgorithmRun> findLatest(int limit) {
        String sql = "SELECT runId, algorithmName, inputSize, timeNs, memoryKb, dateRun FROM algorithm_runs ORDER BY dateRun DESC LIMIT ?";
        List<AlgorithmRun> runs = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    runs.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch latest algorithm runs", e);
        }
        return runs;
    }

    public void delete(int id) {
        String sql = "DELETE FROM algorithm_runs WHERE runId = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete algorithm run", e);
        }
    }

    private AlgorithmRun mapRow(ResultSet rs) throws SQLException {
        AlgorithmRun run = new AlgorithmRun();
        run.setRunId(rs.getInt("runId"));
        run.setAlgorithmName(rs.getString("algorithmName"));
        run.setInputSize(rs.getInt("inputSize"));
        run.setTimeNs(rs.getDouble("timeNs"));
        run.setMemoryKb(rs.getDouble("memoryKb"));
        run.setDateRun(rs.getString("dateRun"));
        return run;
    }
}
