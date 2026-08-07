# Campus Shuttle & Maintenance Service Operations Optimizer

**DCIT 204/308 — Joint DSA Semester Project**
Team size: 16 · Duration: ~2.5 weeks (compressed)

---

## 1. Chosen Local Context

We are building a Smart Service Operations Optimizer for the university campus, modelling shuttle transport and maintenance-request handling as our operational domain. This satisfies the localisation requirement: our locations, routes, and service categories are drawn from real campus life (hostels, lecture halls, labs, shuttle stops, and maintenance offices).

### Locations (network nodes)
- Hostels / halls of residence
- Lecture halls and departments
- Laboratories and workshops
- Shuttle stops / pick-up points
- Maintenance office / facilities depot

### Roads / edges
- Campus roads and footpaths connecting the above locations, weighted by distance, travel time, and a road-condition weight (e.g. potholes, traffic at peak hours).

### Service requests (two categories)
- **Shuttle ride requests** — source stop, destination stop, urgency (e.g. lecture start time), time submitted
- **Maintenance requests** — location, category (electrical, plumbing, IT, structural), urgency, deadline

### Resources
- **Shuttle vehicles** — homeLocation, capacity, availabilityStatus
- **Maintenance staff/technicians** — type/specialty, homeLocation, availabilityStatus

---

## 2. Team Structure — 6 Squads

Every squad owns its own unit tests, trace tables, and edge cases for the parts it builds — testing is not a separate end-of-project task.

| Squad | Members | Owns |
|---|---|---|
| **Data & DB** | Fredrick Karedzi Tsievor, Emmanuel Jerry Kuake, Anang Emmanuel | Schema (6 tables), CSV seed data, JDBC read/write, dataset construction & evidence note |
| **Linear structures** | Joel Yaw Adom Opoku, Melchizedek Sensemore D.A | Dynamic array, linked list, stack, queue/circular queue, deque |
| **Tree/hash structures** | Didemudo Peter-Paul, Shadrack Tweneboah Koduah Addofo, Freeman Kwadzo Agbelefe Wagba | BST, red-black tree, B-tree, hash table, set/map, disjoint set |
| **Search/Sort engine** | Etornam Kobla Gbamidoh Quashigah, Shadrack Dorkenoo | Linear/binary search, selection/insertion/merge/quicksort, benchmarking harness |
| **Graph engine** | Wilfred Adu Otabil, Richard Selorm Alato, Kelvin Ntiamoah | Adjacency list/matrix, BFS, DFS, Dijkstra, Prim, Kruskal |
| **Optimisation & integration** | Sylvester Daniel Okantah, Bright Gyan Boapea, Michael Da-Akpe Morkli | Greedy + DP algorithms, console menu, system integration, report assembly |

---

## 3. Compressed Timeline — ~17 Days

With roughly two and a half weeks left, the four-week plan has to compress into three overlapping phases. The key change: **squads work in parallel from Day 1** instead of waiting on the DB squad to finish first — everyone else builds against mock/stub data until real data is ready, then swaps it in.

| Phase | Dates | What happens |
|---|---|---|
| **Phase 1: Foundations (parallel)** | Thu 6 – Sun 9 Aug | Data & DB: finalise schema, seed CSV data, get JDBC connection working. **All other squads:** build core structures/algorithm skeletons in parallel using mock data — do not wait on the DB squad. |
| **Phase 2: Build-out & wiring** | Mon 10 – Sun 16 Aug | Algorithms fully implemented against real DB data. Integration happens continuously, not saved for the end. Every squad writes unit tests and trace tables as they go, not after. |
| **Phase 3: Test, measure, defend** | Mon 17 – Wed 19 Aug | Performance benchmarking, report writing, final bug-fixing, and oral defense rehearsal. No new features after Phase 2 ends. |

> If your actual deadline date differs from Wed 19 Aug, shift these ranges but keep the same day-counts per phase — Phase 1 is the tightest and most important to hit on time, since everything else depends on the schema and stub interfaces being locked early.

---

## 4. Individual Defense Assignments

Every member must defend one data structure and one algorithm during the oral demonstration.

| # | Member (Index No.) | Data Structure | Algorithm |
|---|---|---|---|
| 1 | Shadrack Dorkenoo (22027811) | Dynamic array | Linear search |
| 2 | Emmanuel Jerry Kuake (22020691) | Linked list | Binary search |
| 3 | Joel Yaw Adom Opoku (22117567) | Stack | Selection sort |
| 4 | Melchizedek Sensemore D.A (22234596) | Queue | Insertion sort |
| 5 | Wilfred Adu Otabil (22045896) | Circular queue | Merge sort |
| 6 | Didemudo Peter-Paul (22046391) | Deque | Quicksort |
| 7 | Shadrack Tweneboah Koduah Addofo (22270457) | Priority queue / heap | Greedy algorithm |
| 8 | Freeman Kwadzo Agbelefe Wagba (22409281) | BST | Dynamic programming |
| 9 | Etornam Kobla Gbamidoh Quashigah (22372127) | Red-black tree | BFS |
| 10 | Fredrick Karedzi Tsievor (22383280) | B-tree | DFS |
| 11 | Richard Selorm Alato (22392938) | Hash table | Dijkstra |
| 12 | Kelvin Ntiamoah (22399787) | Set / Map | Prim |
| 13 | Sylvester Daniel Okantah (22303503) | Disjoint set | Kruskal |
| 14 | Bright Gyan Boapea (22327548) | Graph (adjacency list) | Dijkstra (2nd defender) |
| 15 | Michael Da-Akpe Morkli (22400190) | Graph (adjacency matrix) | Greedy algorithm (2nd defender) |
| 16 | Anang Emmanuel (22241153) | Hash table (2nd defender) | Kruskal (2nd defender) |

---

## 5. Database Schema (Campus Shuttle & Maintenance)

| Table | Purpose |
|---|---|
| `locations` | Hostels, lecture halls, labs, shuttle stops, maintenance office |
| `roads` | Weighted campus routes between locations |
| `service_requests` | Shuttle ride requests and maintenance requests |
| `resources` | Shuttle vehicles and maintenance staff |
| `algorithm_runs` | Runtime/memory measurements per algorithm, per input size |
| `audit_events` | Stack-based undo/audit log of system actions |

---

## 6. Three Index-Number-Derived Parameters

Required by the AI-resistance rules — derive at least three algorithm parameters from member index numbers. Suggested approach:

- **Priority weight** — derived from the sum of digits of a chosen member's index number (used to weight urgency in the priority queue)
- **Route penalty** — derived from the last two digits of an index number (added to road weights to simulate traffic/condition penalties)
- **Hash-table size** — nearest prime above the average of all 16 members' index numbers (used as the base table size for the hash table)

### Reference: Team Index Numbers

| Member | Index No. |
|---|---|
| Shadrack Dorkenoo | 22027811 |
| Emmanuel Jerry Kuake | 22020691 |
| Joel Yaw Adom Opoku | 22117567 |
| Melchizedek Sensemore D.A | 22234596 |
| Wilfred Adu Otabil | 22045896 |
| Didemudo Peter-Paul | 22046391 |
| Shadrack Tweneboah Koduah Addofo | 22270457 |
| Freeman Kwadzo Agbelefe Wagba | 22409281 |
| Etornam Kobla Gbamidoh Quashigah | 22372127 |
| Fredrick Karedzi Tsievor | 22383280 |
| Richard Selorm Alato | 22392938 |
| Kelvin Ntiamoah | 22399787 |
| Sylvester Daniel Okantah | 22303503 |
| Bright Gyan Boapea | 22327548 |
| Michael Da-Akpe Morkli | 22400190 |
| Anang Emmanuel | 22241153 |

---

## 7. Next Steps

- [ ] **Today (Thu):** agree on the mock-data format so non-DB squads can start immediately without waiting on the schema
- [ ] **By Sun 9 Aug:** schema finalised, DB loader working, all squads have skeleton code running against mock data
- [ ] Write the localisation evidence note (how the campus dataset was constructed) — do this alongside Phase 1, not later
- [ ] Set up the shared Git repository with branch-per-squad workflow now, if not already done
