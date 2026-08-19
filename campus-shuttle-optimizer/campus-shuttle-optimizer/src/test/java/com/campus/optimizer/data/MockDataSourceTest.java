package com.campus.optimizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockDataSourceTest {

    @Test
    void mockDataSourceContainsLocations() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getLocations().isEmpty());
    }

    @Test
    void mockDataSourceContainsRoads() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getRoads().isEmpty());
    }

    @Test
    void mockDataSourceContainsServiceRequests() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getServiceRequests().isEmpty());
    }
}