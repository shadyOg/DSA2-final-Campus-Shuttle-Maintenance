# Custom Structure Usage Audit

## Scope

This audit covers the active Maven application under `src/main/java`. It records Java collection usage before replacing project-facing data structures with the implementations in `com.campus.optimizer.structures`.

## Must Replace or Review

These are the assignment-facing collection uses and should be handled during integration:

| Area | Files | Java structures currently used | Replacement direction |
|---|---|---|---|
| Mock data | `data/DataSource.java`, `data/MockDataSource.java` | `List`, `ArrayList` | Use a project collection or introduce a project-owned collection interface. |
| Database read results | `db/AlgorithmRunDAO.java`, `db/AuditEventDAO.java`, `db/LocationDAO.java`, `db/ResourceDAO.java`, `db/RoadDAO.java`, `db/ServiceRequestDAO.java` | `List`, `ArrayList` | Return a project-owned dynamic array/list abstraction, or keep Java `List` only at the JDBC boundary and convert immediately. |
| Dataset generation/loading | `db/DatabaseLoader.java`, `db/MockDataGenerator.java` | `List`, `ArrayList` | Use `DynamicArray` or a project-owned list abstraction. |
| Graph storage | `graph/Graph.java` | `List`, `ArrayList`, `Map` at public boundaries | Internal vertex and adjacency storage now uses `CustomMap`; public traversal/accessor adapters still return Java collections. |
| Graph traversal | `graph/Graph.java` | Java `Deque`, `ArrayDeque` | Use the custom `Deque` or `Stack` after its API is aligned with BFS/DFS needs. |
| Shortest path | `graph/Dijkstra.java` | `Map`, `LinkedHashMap` at public boundary | Priority queue usage now uses `MinHeap`; returned distance map remains a Java compatibility adapter. |
| Minimum spanning tree | `graph/Prim.java` | `List`, `ArrayList` at public boundary | Visited tracking uses `CustomSet`, edge ordering uses `MinHeap`, and returned MST data remains a Java compatibility adapter. |
| Optimisation | `optimization/CampusServiceOptimizer.java`, `optimization/GreedyMaintenanceAllocator.java`, `optimization/OptimizationResult.java` | `List` at public boundaries | Algorithm working storage now uses `DynamicArray`; Java lists remain for existing public callers and result adapters. |
| Integration demo | `integration/CampusOperationsIntegration.java` | `List`, `Arrays.asList` | Replace the demo input with project-owned structures once optimizer signatures are migrated. |

## Direct Compliance Problems in Current Custom Structures

These files were initially named as custom structures but depended on Java collection implementations:

- `structures/CustomMap.java` previously wrapped `java.util.HashMap`; it now uses the custom `HashTable`.
- `structures/CustomSet.java` previously wrapped `java.util.HashSet`; it now uses the custom `HashTable`.
- `structures/BTree.java` still uses Java `List`/`ArrayList` internally for keys and children.

The B-tree needs a custom dynamic-array-backed node representation if the requirement is strictly “from scratch.”

## Reasonable Java Utility Uses

These are not data-structure substitutions by themselves:

- `DynamicArray.java`: `Arrays.copyOf` and `Arrays.fill` support the custom array implementation.
- DAO classes: `Optional` represents a possibly absent single database result.
- `DatabaseConnection.java`: `Collectors.joining` assembles schema text.
- Sort/optimisation code: `Comparator` supports ordering decisions.
- `BenchmarkHarness.java`: `Arrays.copyOf` supports benchmark isolation.
- Linear structure classes: `NoSuchElementException` communicates empty-structure access.
- Tree traversal result methods: Java `List` is currently an output adapter, though this can be changed later if strict compliance requires it.

## Missing Custom Structures or APIs

The following gaps prevent a safe wholesale replacement today:

1. There is no custom priority queue/heap for Dijkstra and Prim.
2. `CustomMap` and `CustomSet` do not implement Java `Map`/`Set` and have limited iteration APIs.
3. Custom collections do not consistently provide iteration, indexed access, entries, or conversion operations.
4. DAO and optimizer public methods expose Java `List`, so changing them requires coordinated API changes across callers and tests.
5. The custom linear-structure source files under `com/campus` should be checked for package/path consistency before reuse.

## Recommended Replacement Order

1. Replace graph and optimisation public collection APIs if strict zero-`java.util` usage is required.
2. Replace mock-data and DAO collection internals.
3. Change database/API boundaries last, preserving Java collections only at JDBC/test adapters if needed.
4. Rework B-tree internals and tree traversal output types if the marking requirement forbids all Java `List` usage.

## Legacy/Out-of-Scope Material

The repository also contains root-level demos and an older database loader outside the nested Maven application. They use Java collections in places, but they are not currently compiled by the nested Maven build. They should be reviewed separately before deletion or integration.