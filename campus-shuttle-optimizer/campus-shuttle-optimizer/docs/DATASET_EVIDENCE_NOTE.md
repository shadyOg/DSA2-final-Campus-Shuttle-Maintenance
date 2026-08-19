# Dataset Construction Evidence Note

## Local Context
This dataset models the University of Ghana, Legon campus — a real tertiary institution in Accra, Ghana. The campus hosts approximately 30,000 students and staff across 6 halls of residence, multiple lecture halls, laboratories, administrative blocks, and support facilities.

## Data Sources and Construction
All location names, road names, and service categories were derived from publicly known campus landmarks and operational patterns. No personal data, staff records, or student identifiers were used.

### Locations (55 nodes)
- **Halls of residence**: Commonwealth Hall, Volta Hall, Mensah Sarbah Hall, Akuafo Hall, Legon Hall, Presbyterian Hall, Jean Nelson Hall, Alexander Kwapong Hostel, Claver Robinson Hostel, Mizpah Hostel, Bani Hostel, Legacy Hall, Amafi Hostel
- **Academic buildings**: Great Hall, Balme Library, N Block, Science Block, Engineering Block, School of Pharmacy, School of Business, Faculty of Law, Faculty of Education, School of Arts, School of Social Sciences, School of Biological Sciences, School of Physical Sciences, Central Administration
- **Facilities**: Central Cafeteria, Night Market, Sarbah Park, Fire Station, Legon Stadium, Basketball Court, Botanical Garden
- **Institutes**: ISSER, Noguchi Memorial Institute, WACCI, RIPS, UGBS, Graduate School, Veterinary School, Medical School
- **Transport nodes**: Legon Campus Main Gate, Admin Gate, Night Gate, South Gate, Shuttle Stop A/B/C, Legon Airport Road, Ring Road West, Gimbidi Road
- **Maintenance**: Maintenance Depot

Latitude and longitude coordinates were approximated from publicly available campus maps, centered around 5.65°N, 0.19°W.

### Roads/Edges (110 edges)
Roads were generated as a connected graph where edge existence is determined by geographic proximity (Euclidean distance < 0.25 degrees ≈ 27 km). This ensures the campus network is traversable while maintaining realistic routing. Weights include:
- `distance`: proportional to geographic distance in kilometres
- `travelTime`: derived from distance with random variation (0.8× to 1.5×) to simulate walking vs. shuttle vs. traffic
- `roadConditionWeight`: randomly assigned 1.0–2.5 to simulate potholes, construction, or pedestrian-only paths

### Service Requests (320 records)
Two categories were modelled:
1. **Shuttle rides**: Source and destination are real shuttle stops, halls, or academic blocks. Times are distributed across typical campus hours (07:00–20:00) during the January–February semester period.
2. **Maintenance requests**: Electrical, plumbing, IT, and structural issues reported from real locations, routed to the Maintenance Depot or relevant technical offices. Urgency levels 1–5 reflect operational priority.

### Resources (35 records)
- **Shuttle buses (10)**: 14–30 seat capacity, home-located at high-traffic stops (Main Gate, Cafeteria, Library, Engineering Block, Business School, Stadium)
- **Maintenance technicians (25)**: Electrical, plumbing, IT, and structural specialists, based at the Maintenance Depot or workshop locations

### Algorithm Runs (35 records)
Synthetic benchmark results covering the required algorithms: search, sort, graph traversal, shortest path, and MST algorithms at varying input sizes.

### Audit Events (25 records)
Simulated operational log entries for INSERT, UPDATE, DELETE, LOGIN, EXPORT, and UNDO actions.

## Index-Number-Derived Parameters
Three algorithm parameters were derived from team member index numbers to satisfy AI-resistance requirements:

1. **Hash-table base size**: Nearest prime above the average of all 16 members' index numbers
   - Average index ≈ 2229xxxx
   - Derived prime: 22321171 (used as base table size in hash table implementation)

2. **Route penalty multiplier**: Sum of digits of member 22020691 (Emmanuel Jerry Kuake)
   - Sum = 2+2+0+2+0+6+9+1 = 22
   - Applied as an additive penalty to roadConditionWeight during peak hours

3. **Priority weight**: Last two digits of member 22046391 (Didemudo Peter-Paul)
   - Value = 91
   - Used to weight urgency in the priority queue dispatch formula

## Localisation Evidence
- Location names and types reflect actual University of Ghana campus geography
- Service request patterns mirror real student and staff behaviour (morning shuttle demand, evening maintenance windows)
- Resource counts and types are consistent with a mid-sized Ghanaian university (10 shuttles, 25 technicians)
- No external proprietary datasets were used; all data was constructed from public campus knowledge
