package com.campus.optimizer.db;

import com.campus.optimizer.model.ServiceRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import java.io.File;

import static org.junit.Assert.*;

public class ServiceRequestDAOTest {

    private ServiceRequestDAO dao;

    @Before
    public void setUp() throws Exception {
        deleteDatabaseFile();
        DatabaseConnection.getInstance().initializeDatabase();
        DatabaseLoader.loadLocations();
        DatabaseLoader.loadServiceRequests();
        dao = new ServiceRequestDAO();
    }

    private void deleteDatabaseFile() {
        String dbPath = "src/main/resources/campus.db";
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @After
    public void tearDown() throws Exception {
        dao = null;
    }

    @Test
    public void testFindAll() {
        List<ServiceRequest> requests = dao.findAll();
        assertTrue("Should have service requests", requests.size() > 0);
    }

    @Test
    public void testFindByStatus() {
        List<ServiceRequest> pending = dao.findByStatus("pending");
        assertTrue("Should have pending requests", pending.size() > 0);
        for (ServiceRequest req : pending) {
            assertEquals("pending", req.getStatus());
        }
    }

    @Test
    public void testFindByUrgency() {
        List<ServiceRequest> urgent = dao.findByUrgency(5);
        assertTrue("Should have urgency 5 requests", urgent.size() > 0);
        for (ServiceRequest req : urgent) {
            assertEquals(5, req.getUrgency());
        }
    }

    @Test
    public void testInsertAndFindById() {
        ServiceRequest req = new ServiceRequest();
        req.setSource("Test Stop A");
        req.setDestination("Test Stop B");
        req.setCategory("shuttle_ride");
        req.setUrgency(3);
        req.setTimeSubmitted("2024-01-20 10:00");
        req.setDeadline("2024-01-20 11:00");
        req.setStatus("pending");
        dao.insert(req);
        assertTrue("Request ID should be generated", req.getRequestId() > 0);
    }
}
