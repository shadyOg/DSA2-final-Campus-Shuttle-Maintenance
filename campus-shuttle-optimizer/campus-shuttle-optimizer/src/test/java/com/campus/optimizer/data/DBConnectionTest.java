package com.campus.optimizer.data;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DBConnectionTest {

    @Test
    public void canOpenDatabaseConnection() throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }
}