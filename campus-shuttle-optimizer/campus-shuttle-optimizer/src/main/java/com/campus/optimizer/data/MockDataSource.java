package com.campus.optimizer.data;

import com.campus.optimizer.db.LocationDAO;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.db.ServiceRequestDAO;
import com.campus.optimizer.db.ResourceDAO;

import java.util.List;
import com.campus.optimizer.model.Resource;

public class MockDataSource implements DataSource {

    private final LocationDAO locationDAO;
    private final RoadDAO roadDAO;
    private final ServiceRequestDAO serviceRequestDAO;
    private final ResourceDAO resourceDAO;

    public MockDataSource() {
        locationDAO = new LocationDAO();
        roadDAO = new RoadDAO();
        serviceRequestDAO = new ServiceRequestDAO();
        resourceDAO = new ResourceDAO();
    }

    @Override
    public List<Location> getLocations() {
        return locationDAO.findAll();
    }

    @Override
    public List<Road> getRoads() {
        return roadDAO.findAll();
    }

    @Override
    public List<ServiceRequest> getServiceRequests() {
        return serviceRequestDAO.findAll();
    }

    @Override
    public List<Resource> getResources() {
        return resourceDAO.findAll();
    }
}
