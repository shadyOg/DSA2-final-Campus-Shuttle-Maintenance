# Anang Emmanuel — Oral Defense Notes

## Identity
- Index number: 22241153
- Data structure: Hash table (2nd defender)
- Algorithm: Kruskal (2nd defender)

## Hash table

A hash table stores key/value pairs by converting a key into an array index using a hash function.

### Core operations
- Insert: hash the key, go to the bucket, insert/update the entry.
- Search: hash the key and inspect the relevant bucket.
- Delete: hash the key and remove the matching entry.

### Collision
A collision occurs when different keys map to the same index.

This demonstration uses **separate chaining**: each table slot contains a list of entries. Multiple keys can therefore share one bucket.

### Complexity
With a good hash function and controlled load factor, average insert/search/delete are O(1). In the worst case, many keys can land in one bucket, giving O(n).

### Project connection
A hash table can provide fast lookup of campus entities such as locations/resources. The assignment also requires a hash-table-size parameter derived from team index numbers.

## Kruskal

Kruskal's algorithm constructs a Minimum Spanning Tree (MST) for a weighted, undirected graph.

### Steps
1. Put all edges into a list.
2. Sort edges by increasing weight.
3. Consider the cheapest edge first.
4. Add an edge only if it does not form a cycle.
5. Use Disjoint Set/Union-Find to detect cycles.
6. Stop after V-1 edges have been accepted for V vertices.

### Project connection
Campus locations are graph nodes and campus roads are weighted edges. Kruskal can build a minimum spanning tree connecting the locations with minimum total selected edge weight.

### Complexity
Sorting dominates: O(E log E). With efficient Union-Find, the set operations are close to constant amortized time.

## Index-derived values

Anang's index number: 22241153

Digit sum:
2+2+2+4+1+1+5+3 = 20

Therefore:
- Priority weight = 20
- Route penalty = 53

Team-level hash-table parameter:
- Average of all 16 supplied index numbers = 22,249,576
- Next prime above the average = 22,249,613

## Important defense points

1. **Oral Demo Size vs. Final Capacity:**  
   Do not claim that the small 7-bucket `HashTableDemo` is the team's required final hash-table size. It is only a compact oral demonstration.

2. **Memory Optimization Strategy for $22,249,613$:**  
   While $22,249,613$ is mathematically derived as the team's base prime parameter, allocating a 22-million-bucket array for a dataset of ~50 to 320 campus locations would waste over 88 MB of RAM on empty array references.  
   Therefore, the runtime implementation (`HashTable.java`) references `RAW_DERIVED_INDEX_PRIME = 22_249_613L` as the derived baseline, but uses a scaled initial prime capacity (`17` or `createWithDerivedIndexCapacity()`) with dynamic $O(1)$ resizing at a $0.75$ load factor.  
   *Defense Response if asked:*  
   *"$22,249,613$ is our derived base parameter. For real-world campus dataset scaling ($N \approx 50-320$), we apply a prime scaling factor to maintain memory efficiency while preserving $O(1)$ dynamic resizing."*
