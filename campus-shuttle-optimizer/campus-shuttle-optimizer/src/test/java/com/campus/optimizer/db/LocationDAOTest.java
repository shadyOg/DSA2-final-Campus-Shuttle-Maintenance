package com.campus.optimizer.db;

import com.campus.optimizer.model.Location;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class LocationDAOTest {

    private LocationDAO dao;

    private void deleteDatabaseFile() {
        String dbPath = "src/main/resources/campus.db";
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testInsertAndFindById() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        dao = new LocationDAO();

        Location loc = new Location();
        loc.setName("Test Hostel");
        loc.setArea("Legon");
        loc.setType("hostel");
        loc.setLatitude(5.6500);
        loc.setLongitude(-0.1850);

        dao.insert(loc);
        assertTrue("Location ID should be generated", loc.getLocationId() > 0);

        Optional<Location> found = dao.findById(loc.getLocationId());
        assertTrue("Location should be found by ID", found.isPresent());
        assertEquals("Test Hostel", found.get().getName());
    }

    @Test
    public void testFindAll() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        DatabaseLoader.loadLocations();
        dao = new LocationDAO();

        List<Location> locations = dao.findAll();
        assertTrue("Should have at least 20 locations from seed data", locations.size() >= 20);
    }

    @Test
    public void testFindByName() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        DatabaseLoader.loadLocations();
        dao = new LocationDAO();

        Optional<Location> found = dao.findByName("Balme Library");
        assertTrue("Balme Library should exist", found.isPresent());
        assertEquals("Balme Library", found.get().getName());
    }

    @Test
    public void testUpdate() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        DatabaseLoader.loadLocations();
        dao = new LocationDAO();

        Optional<Location> found = dao.findById(1);
        assertTrue("Location 1 should exist", found.isPresent());
        Location loc = found.get();
        loc.setArea("Test Area");
        dao.update(loc);

        Optional<Location> updated = dao.findById(1);
        assertTrue("Updated location should exist", updated.isPresent());
        assertEquals("Test Area", updated.get().getArea());
    }

    @Test
    public void testDelete() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        dao = new LocationDAO();

        Location loc = new Location();
        loc.setName("ToDelete");
        loc.setArea("Legon");
        loc.setType("hostel");
        loc.setLatitude(5.6500);
        loc.setLongitude(-0.1850);
        dao.insert(loc);
        int id = loc.getLocationId();
        dao.delete(id);
        assertFalse("Deleted location should not exist", dao.findById(id).isPresent());
    }
}
