package com.campus.optimizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StackAndDynamicArrayTest {

    @Test
    public void dynamicArrayShouldGrowAndTrackValues() {
        DynamicArray<Integer> values = new DynamicArray<>();

        values.add(10);
        values.add(20);
        values.add(30);

        assertEquals(3, values.size());
        assertEquals(Integer.valueOf(10), values.get(0));
        assertEquals(Integer.valueOf(20), values.get(1));
        assertEquals(Integer.valueOf(30), values.get(2));

        values.add(40);
        values.add(50);
        values.add(60);
        values.add(70);

        assertEquals(7, values.size());
        assertEquals(Integer.valueOf(70), values.get(6));
    }

    @Test
    public void dynamicArrayShouldReplaceAndRemoveItems() {
        DynamicArray<String> names = new DynamicArray<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        names.set(1, "Bobby");
        assertEquals("Bobby", names.get(1));

        String removed = names.remove(0);
        assertEquals("Alice", removed);
        assertEquals(2, names.size());
        assertEquals("Bobby", names.get(0));
        assertEquals("Charlie", names.get(1));
    }

    @Test
    public void stackShouldSupportLifoOperations() {
        Stack<String> auditTrail = new Stack<>();

        auditTrail.push("INSERT INTO audit_events VALUES (1)");
        auditTrail.push("UPDATE shuttle_status SET active = 0");
        auditTrail.push("DELETE FROM maintenance_requests WHERE id = 7");

        assertEquals(3, auditTrail.size());
        assertEquals("DELETE FROM maintenance_requests WHERE id = 7", auditTrail.peek());

        String lastAction = auditTrail.pop();
        assertEquals("DELETE FROM maintenance_requests WHERE id = 7", lastAction);
        assertEquals("UPDATE shuttle_status SET active = 0", auditTrail.peek());

        assertFalse(auditTrail.isEmpty());
        assertEquals("UPDATE shuttle_status SET active = 0", auditTrail.pop());
        assertEquals("INSERT INTO audit_events VALUES (1)", auditTrail.pop());
        assertTrue(auditTrail.isEmpty());
    }

    @Test
    public void stackShouldSupportAuditUndoScenario() {
        Stack<String> auditEvents = new Stack<>();

        auditEvents.push("CREATE shuttle route");
        auditEvents.push("RESCHEDULE maintenance task");
        auditEvents.push("CANCEL vehicle dispatch");

        assertEquals("CANCEL vehicle dispatch", auditEvents.pop());
        assertEquals("RESCHEDULE maintenance task", auditEvents.pop());
        assertEquals("CREATE shuttle route", auditEvents.pop());
        assertTrue(auditEvents.isEmpty());
    }
}
