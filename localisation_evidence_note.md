Localisation Evidence Note

Project: Campus Shuttle & Maintenance Service Operations Optimizer
Prepared by: Anang Emmanuel (Data & DB squad), Phase 2

1. Source of the location data

- Institution: [University of Ghana/ campus]
- How locations were identified: [e.g. walked/mapped the campus, used
  the official campus map, cross-checked with the university website's
  hall/department listing]
- Location categories captured (per `locations` table):
  - Hostels / halls of residence — Legon Hall , Akuafo Hall, Commonwealth Hall , Volta Hall
  - Lecture halls / departments — Jones Quartey Building , K. A Busia Lecture Theater ,Classic Theater Room , Annex

2. Source of the road/route data

- How edges were derived: [the estimated walking/driving distance
  between each pair of connected locations using Google Maps / a campus
  map, converted to metres]
- Weighting scheme:
  - `distance` — [unit, how measured]
  - `travel_time` — [unit, how estimated]
  - `road_condition_weight` — [how derived]
- Total roads seeded: [N] (see `roads.csv`)

 3. Service request categories

- Shuttle ride requests are modelled on [describe real shuttle usage pattern]
- Maintenance requests use four categories (electrical, plumbing, IT,
  structural) chosen because [reasoning]

 4. Resources

- Shuttle vehicles: [how many modelled, capacity assumptions, and why]
- Maintenance staff/technicians: [specialties modelled and why]

 5. Index-number-derived parameters (AI-resistance requirement)

| Parameter | Derivation | Raw derived value | Value actually used | Why scaled (if applicable) |
|---|---|---|---|---|
| Priority weight | Sum of digits of [member]'s index number | [value] | [value] | — |
| Route penalty | Last two digits of [member]'s index number | [value] | [value] | — |
| Hash-table size | Nearest prime above the average of all 16 members' index numbers | 22,249,613 | [scaled value] | Raw value too large for a bucket count at this dataset size — scaled down. |


- `locations.csv`, `roads.csv`, `resources.csv` — seed data
- Screenshots/photos used to identify real locations: [link or filenames]
- This note itself, committed alongside the seed data
