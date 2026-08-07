PRAGMA foreign_keys = ON;

CREATE TABLE locations (
    location_id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    location_type TEXT NOT NULL,
    description TEXT
);

CREATE TABLE roads (
    road_id INTEGER PRIMARY KEY,
    from_location_id INTEGER NOT NULL,
    to_location_id INTEGER NOT NULL,
    distance_km REAL NOT NULL CHECK(distance_km >= 0),
    travel_time_min INTEGER NOT NULL CHECK(travel_time_min >= 0),
    road_condition_penalty INTEGER NOT NULL DEFAULT 0 CHECK(road_condition_penalty >= 0),
    FOREIGN KEY(from_location_id) REFERENCES locations(location_id),
    FOREIGN KEY(to_location_id) REFERENCES locations(location_id)
);

CREATE TABLE service_requests (
    request_id INTEGER PRIMARY KEY,
    request_type TEXT NOT NULL CHECK(request_type IN ('SHUTTLE','MAINTENANCE')),
    source_location_id INTEGER,
    destination_location_id INTEGER,
    maintenance_category TEXT,
    urgency INTEGER NOT NULL CHECK(urgency BETWEEN 1 AND 5),
    submitted_at TEXT NOT NULL,
    deadline TEXT,
    status TEXT NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY(source_location_id) REFERENCES locations(location_id),
    FOREIGN KEY(destination_location_id) REFERENCES locations(location_id)
);

CREATE TABLE resources (
    resource_id INTEGER PRIMARY KEY,
    resource_type TEXT NOT NULL CHECK(resource_type IN ('SHUTTLE','TECHNICIAN')),
    name TEXT NOT NULL,
    specialty TEXT,
    home_location_id INTEGER NOT NULL,
    capacity INTEGER,
    availability_status TEXT NOT NULL,
    FOREIGN KEY(home_location_id) REFERENCES locations(location_id)
);

CREATE TABLE algorithm_runs (
    run_id INTEGER PRIMARY KEY,
    algorithm_name TEXT NOT NULL,
    input_size INTEGER NOT NULL,
    runtime_ms REAL NOT NULL,
    memory_bytes INTEGER,
    run_at TEXT NOT NULL
);

CREATE TABLE audit_events (
    event_id INTEGER PRIMARY KEY,
    action_type TEXT NOT NULL,
    entity_type TEXT NOT NULL,
    entity_id INTEGER,
    action_details TEXT,
    performed_at TEXT NOT NULL,
    stack_position INTEGER NOT NULL
);
