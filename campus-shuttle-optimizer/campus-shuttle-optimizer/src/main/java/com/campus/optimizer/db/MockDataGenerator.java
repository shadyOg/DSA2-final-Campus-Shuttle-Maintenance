package com.campus.optimizer.db;

import com.campus.optimizer.model.AlgorithmRun;
import com.campus.optimizer.model.AuditEvent;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.Resource;
import com.campus.optimizer.model.Road;
import com.campus.optimizer.model.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {

    public static List<Location> generateLocations(int count) {
        List<Location> locations = new ArrayList<>();
        String[] names = {"Commonwealth Hall", "Great Hall", "Balme Library", "N Block", "Science Block",
                "Central Cafeteria", "Volta Hall", "Mensah Sarbah Hall", "Akuafo Hall", "Engineering Block"};
        String[] types = {"hall", "building", "building", "building", "building",
                "facility", "hall", "hall", "hall", "building"};
        for (int i = 0; i < count; i++) {
            Location loc = new Location();
            loc.setLocationId(i + 1);
            loc.setName(names[i % names.length] + (i >= names.length ? " " + (i / names.length + 1) : ""));
            loc.setArea("Legon");
            loc.setType(types[i % types.length]);
            loc.setLatitude(5.65 + (Math.random() * 0.02 - 0.01));
            loc.setLongitude(-0.19 + (Math.random() * 0.02 - 0.01));
            locations.add(loc);
        }
        return locations;
    }

    public static List<Road> generateRoads(int count, List<Location> locations) {
        List<Road> roads = new ArrayList<>();
        int locCount = locations.size();
        for (int i = 0; i < count; i++) {
            int from = (i % locCount) + 1;
            int to = ((i + 1) % locCount) + 1;
            if (from == to) to = (to % locCount) + 1;
            Road road = new Road();
            road.setRoadId(i + 1);
            road.setFromLocationId(from);
            road.setToLocationId(to);
            road.setDistance(0.1 + Math.random() * 1.0);
            road.setTravelTime(1.0 + Math.random() * 10.0);
            road.setRoadConditionWeight(1.0 + Math.random() * 2.0);
            roads.add(road);
        }
        return roads;
    }

    public static List<ServiceRequest> generateServiceRequests(int count) {
        List<ServiceRequest> requests = new ArrayList<>();
        String[] sources = {"Commonwealth Hall", "Great Hall", "Balme Library", "Engineering Block", "Akuafo Hall"};
        String[] categories = {"shuttle_ride", "maintenance_electrical", "maintenance_plumbing", "maintenance_IT"};
        for (int i = 0; i < count; i++) {
            ServiceRequest req = new ServiceRequest();
            req.setRequestId(i + 1);
            req.setSource(sources[i % sources.length]);
            req.setDestination(sources[(i + 1) % sources.length]);
            req.setCategory(categories[i % categories.length]);
            req.setUrgency(1 + (i % 5));
            req.setTimeSubmitted("2024-01-15 08:00");
            req.setDeadline("2024-01-15 12:00");
            req.setStatus(i % 3 == 0 ? "pending" : (i % 3 == 1 ? "assigned" : "completed"));
            requests.add(req);
        }
        return requests;
    }

    public static List<Resource> generateResources(int count) {
        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Resource res = new Resource();
            res.setResourceId(i + 1);
            if (i < count / 2) {
                res.setType("Shuttle Bus");
                res.setHomeLocation("Legon Campus Main Gate");
                res.setCapacity(20);
            } else {
                res.setType("Technician-Electrical");
                res.setHomeLocation("Maintenance Depot");
                res.setCapacity(1);
            }
            res.setAvailabilityStatus("available");
            resources.add(res);
        }
        return resources;
    }

    public static List<AlgorithmRun> generateAlgorithmRuns(int count) {
        List<AlgorithmRun> runs = new ArrayList<>();
        String[] algos = {"Linear Search", "Binary Search", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Dijkstra", "BFS"};
        for (int i = 0; i < count; i++) {
            AlgorithmRun run = new AlgorithmRun();
            run.setRunId(i + 1);
            run.setAlgorithmName(algos[i % algos.length]);
            run.setInputSize(100 * ((i % 10) + 1));
            run.setTimeNs(100000.0 + Math.random() * 1000000.0);
            run.setMemoryKb(1.0 + Math.random() * 50.0);
            run.setDateRun("2024-02-01");
            runs.add(run);
        }
        return runs;
    }

    public static List<AuditEvent> generateAuditEvents(int count) {
        List<AuditEvent> events = new ArrayList<>();
        String[] actions = {"INSERT", "UPDATE", "DELETE", "LOGIN", "EXPORT", "UNDO"};
        for (int i = 0; i < count; i++) {
            AuditEvent ev = new AuditEvent();
            ev.setEventId(i + 1);
            ev.setAction(actions[i % actions.length]);
            ev.setDetails("Event " + (i + 1));
            ev.setTimestamp("2024-02-10 10:00");
            events.add(ev);
        }
        return events;
    }
}
