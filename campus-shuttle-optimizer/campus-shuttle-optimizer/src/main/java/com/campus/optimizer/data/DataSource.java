package com.campus.optimizer.data;

import java.util.List;

public interface DataSource {

    List<Location> getLocations();

    List<Road> getRoads();

    List<ServiceRequest> getServiceRequests();
}