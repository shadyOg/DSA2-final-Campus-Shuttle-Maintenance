package com.campus.optimizer.data;

import com.campus.optimizer.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class MockDataSource implements DataSource {

    private final List<Location> locations = new ArrayList<>();
    private final List<Road> roads = new ArrayList<>();
    private final List<ServiceRequest> serviceRequests = new ArrayList<>();
    private final List<Resource> resources = new ArrayList<>();

    public MockDataSource() {
        //locations
        locations.add(new Location(1, "Balme Library"));
        locations.add(new Location(2, "UGCS"));
        locations.add(new Location(3, "Commonwealth Hall"));
        locations.add(new Location(4, "Legon Hall"));
        locations.add(new Location(5, "GCB"));
        locations.add(new Location(6, "JQB"));
        locations.add(new Location(7, "N Block"));
        locations.add(new Location(8, "New N block"));

        //roads
        roads.add(new Road(1, 2, 0.8));
        roads.add(new Road(2, 3, 0.6));
        roads.add(new Road(3, 4, 0.5));
        roads.add(new Road(4, 5, 0.7));
        roads.add(new Road(5, 6, 0.4));
        roads.add(new Road(6, 7, 0.9));
        roads.add(new Road(7, 8, 0.3));

        roads.add(new Road(2, 5, 1.0));
        roads.add(new Road(3, 6, 1.2));
        roads.add(new Road(1, 4, 1.1));

        //serviceRequests
        serviceRequests.add(new ServiceRequest(1, 1, "SHUTTLE", "Student pickup request at Balme Library"));

        serviceRequests.add(new ServiceRequest(2, 3, "MAINTENANCE", "Broken street light near Commonwealth Hall"));

        serviceRequests.add(new ServiceRequest(3, 5, "SHUTTLE", "Shuttle required at GCB"));

        serviceRequests.add(new ServiceRequest(4, 7, "MAINTENANCE", "Damaged road section near N Block"));

        serviceRequests.add(new ServiceRequest(5, 8, "SHUTTLE", "Student pickup request at New N Block"));

        resources.add(new Resource(1, "Shuttle Bus", "Balme Library", 20, "available"));
        resources.add(new Resource(2, "Technician-Electrical", "Maintenance Depot", 1, "available"));
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public List<Road> getRoads() {
        return roads;
    }

    @Override
    public List<ServiceRequest> getServiceRequests() {
        return serviceRequests;
    }

    @Override
    public List<Resource> getResources() {
        return resources;
    }
}