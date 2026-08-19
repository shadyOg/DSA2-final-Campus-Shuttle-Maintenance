package com.campus.optimizer.data;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class MockDataSourceTest {

    @Test
    public void mockDataSourceContainsLocations() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getLocations().isEmpty());
    }

    @Test
    public void mockDataSourceContainsRoads() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getRoads().isEmpty());
    }

    @Test
    public void mockDataSourceContainsServiceRequests() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getServiceRequests().isEmpty());
    }

    @Test
    public void mockDataSourceContainsResources() {
        MockDataSource dataSource = new MockDataSource();

        assertFalse(dataSource.getResources().isEmpty());
    }
}