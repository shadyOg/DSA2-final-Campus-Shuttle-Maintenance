package com.campus.optimizer.model;

import com.campus.optimizer.model.Resource;

import java.util.List;

public interface DataSource {

    List<Location> getLocations();

    List<Road> getRoads();

    List<ServiceRequest> getServiceRequests();

    List<Resource> getResources();
}
