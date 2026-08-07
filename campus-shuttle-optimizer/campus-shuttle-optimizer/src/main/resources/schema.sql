-- Schema for Campus Shuttle & Maintenance Service Operations Optimizer
-- SQLite DDL

CREATE TABLE IF NOT EXISTS locations (
    locationId INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    area TEXT NOT NULL,
    type TEXT NOT NULL,
    latitude REAL,
    longitude REAL
);

CREATE TABLE IF NOT EXISTS roads (
    roadId INTEGER PRIMARY KEY,
    fromLocationId INTEGER NOT NULL,
    toLocationId INTEGER NOT NULL,
    distance REAL NOT NULL,
    travelTime REAL NOT NULL,
    roadConditionWeight REAL NOT NULL DEFAULT 1.0,
    FOREIGN KEY (fromLocationId) REFERENCES locations(locationId),
    FOREIGN KEY (toLocationId) REFERENCES locations(locationId)
);

CREATE TABLE IF NOT EXISTS service_requests (
    requestId INTEGER PRIMARY KEY,
    source TEXT NOT NULL,
    destination TEXT NOT NULL,
    category TEXT NOT NULL,
    urgency INTEGER NOT NULL CHECK (urgency BETWEEN 1 AND 5),
    timeSubmitted TEXT NOT NULL,
    deadline TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'pending'
);

CREATE TABLE IF NOT EXISTS resources (
    resourceId INTEGER PRIMARY KEY,
    type TEXT NOT NULL,
    homeLocation TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    availabilityStatus TEXT NOT NULL DEFAULT 'available'
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    runId INTEGER PRIMARY KEY,
    algorithmName TEXT NOT NULL,
    inputSize INTEGER NOT NULL,
    timeNs REAL NOT NULL,
    memoryKb REAL NOT NULL,
    dateRun TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_events (
    eventId INTEGER PRIMARY KEY,
    action TEXT NOT NULL,
    details TEXT,
    timestamp TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_service_requests_status ON service_requests(status);
CREATE INDEX IF NOT EXISTS idx_service_requests_urgency ON service_requests(urgency);
CREATE INDEX IF NOT EXISTS idx_roads_from_to ON roads(fromLocationId, toLocationId);
