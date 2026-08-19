# Dataset Construction & Evidence Note

## Purpose
The project models campus shuttle transport and maintenance-request handling using campus-style locations, roads, requests, and resources.

## Dataset construction
The seed dataset uses representative campus entities:
- Hostels
- Lecture halls
- Laboratories
- Shuttle stops
- Maintenance office
- Workshop

Road records contain distance, travel time, and road-condition penalty.

Service requests cover both:
- SHUTTLE
- MAINTENANCE

Resources cover:
- SHUTTLE vehicles
- TECHNICIAN staff

## Evidence to capture
Before submission, add screenshots showing:
1. The six database tables.
2. Sample rows in each table.
3. CSV seed files.
4. Successful JDBC connection.
5. JDBC insert and SELECT/read result.
6. A test showing invalid/edge input handling.
7. Hash table demonstration.
8. Kruskal demonstration.

## Testing / edge cases
At minimum, test:
- Empty table/query result.
- Valid insert.
- Duplicate key.
- Invalid foreign key.
- Invalid urgency outside 1–5.
- Missing JDBC/database connection.
- Hash-table collision.
- Kruskal edge that would create a cycle.

Record expected result, actual result, and pass/fail in the team's trace table.
