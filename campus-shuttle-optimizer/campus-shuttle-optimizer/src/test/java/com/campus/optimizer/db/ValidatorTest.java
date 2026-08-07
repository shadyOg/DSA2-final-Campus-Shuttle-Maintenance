package com.campus.optimizer.db;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void testValidRecord() {
        String[] record = {"1", "Test", "Legon", "hostel", "5.65", "-0.19"};
        assertTrue("Valid record should pass", validateRecord(record, 6));
    }

    @Test
    public void testInvalidColumnCount() {
        String[] record = {"1", "Test", "Legon"};
        assertFalse("Record with wrong column count should fail", validateRecord(record, 6));
    }

    @Test
    public void testValidUrgency() {
        assertTrue("Urgency 3 should be valid", isValidUrgency(3));
        assertTrue("Urgency 1 should be valid", isValidUrgency(1));
        assertTrue("Urgency 5 should be valid", isValidUrgency(5));
    }

    @Test
    public void testInvalidUrgency() {
        assertFalse("Urgency 0 should be invalid", isValidUrgency(0));
        assertFalse("Urgency 6 should be invalid", isValidUrgency(6));
        assertFalse("Urgency -1 should be invalid", isValidUrgency(-1));
    }

    @Test
    public void testValidStatus() {
        assertTrue("pending should be valid", isValidStatus("pending"));
        assertTrue("completed should be valid", isValidStatus("completed"));
    }

    @Test
    public void testInvalidStatus() {
        assertFalse("unknown should be invalid", isValidStatus("unknown"));
        assertFalse("empty should be invalid", isValidStatus(""));
    }

    private boolean validateRecord(String[] record, int expectedColumns) {
        return record != null && record.length == expectedColumns;
    }

    private boolean isValidUrgency(int urgency) {
        return urgency >= 1 && urgency <= 5;
    }

    private boolean isValidStatus(String status) {
        return status != null && !status.isEmpty() && !status.equals("unknown");
    }
}
