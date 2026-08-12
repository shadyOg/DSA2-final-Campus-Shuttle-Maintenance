# Oral Defense Study Guide

**Student**: Didemudo Peter-Paul (Index Number: 22046391)  
**Assignments**: Deque (Data Structure) & Quicksort (Algorithm)

---

## 1. Deque (Double-Ended Queue) Trace Table

This trace table shows a sequence of operations on our custom `Deque<Integer>` implemented via a Doubly Linked List. 
Each node is represented as `(prev <- Data -> next)`.

| Step | Operation | Element | Front Pointer | Rear Pointer | Size | Deque State (Front to Rear) | Node Connections |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 0 | Initial State | - | `null` | `null` | 0 | Empty | - |
| 1 | `insertFront(10)` | 10 | Node(10) | Node(10) | 1 | `[10]` | `(null <- 10 -> null)` |
| 2 | `insertRear(20)` | 20 | Node(10) | Node(20) | 2 | `[10, 20]` | `(null <- 10 -> Node(20)) <-> (Node(10) <- 20 -> null)` |
| 3 | `insertFront(5)` | 5 | Node(5) | Node(20) | 3 | `[5, 10, 20]` | `(null <- 5 -> Node(10)) <-> (Node(5) <- 10 -> Node(20)) <-> (Node(10) <- 20 -> null)` |
| 4 | `removeFront()` | - | Node(10) | Node(20) | 2 | `[10, 20]` (returns 5) | `(null <- 10 -> Node(20)) <-> (Node(10) <- 20 -> null)` |
| 5 | `removeRear()` | - | Node(10) | Node(10) | 1 | `[10]` (returns 20) | `(null <- 10 -> null)` |
| 6 | `removeFront()` | - | `null` | `null` | 0 | Empty (returns 10) | - |

---

## 2. Quicksort Partitioning Trace Table

Trace of partitioning a sub-array `[15, 3, 9, 21, 5, 8, 12, 1, 6]` with **Median-of-Three** pivot selection.

### Step 1: Median-of-Three Selection
- **Low index**: 0 (value = 15)
- **Mid index**: 4 (value = 5)
- **High index**: 8 (value = 6)
- Sort the values: `5, 6, 15`
  - Swapping values at low and mid: `low = 5` (was 15), `mid = 15` (was 5). Array becomes `[5, 3, 9, 21, 15, 8, 12, 1, 6]`
  - Swapping values at mid and high: `mid = 6` (was 15), `high = 15` (was 6). Array becomes `[5, 3, 9, 21, 6, 8, 12, 1, 15]`
- The **median** is `6` (located at index `mid = 4`).
- Swap pivot (`6` at mid) to `high - 1` (index 7, value = 1) for partitioning.
- Array becomes: `[5, 3, 9, 21, 1, 8, 12, 6, 15]`
- **Pivot** = `6`.

### Step 2: Partitioning Process
Partition range: `low = 0` to `high - 1 = 7`. (Index 8 is `15`, which is guaranteed to be larger than pivot `6` due to Median-of-Three).
Initialize `i = -1`.

| Iteration | Pointer `k` | `array[k]` | Comparison (`array[k] <= pivot`) | Action taken | Array State | Pointer `i` |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| Start | - | - | - | - | `[5, 3, 9, 21, 1, 8, 12, 6, 15]` | -1 |
| 1 | 0 | 5 | `5 <= 6` (True) | `i++` (0), swap `array[0]` and `array[0]` | `[5, 3, 9, 21, 1, 8, 12, 6, 15]` | 0 |
| 2 | 1 | 3 | `3 <= 6` (True) | `i++` (1), swap `array[1]` and `array[1]` | `[5, 3, 9, 21, 1, 8, 12, 6, 15]` | 1 |
| 3 | 2 | 9 | `9 <= 6` (False) | No swap | `[5, 3, 9, 21, 1, 8, 12, 6, 15]` | 1 |
| 4 | 3 | 21 | `21 <= 6` (False) | No swap | `[5, 3, 9, 21, 1, 8, 12, 6, 15]` | 1 |
| 5 | 4 | 1 | `1 <= 6` (True) | `i++` (2), swap `array[2]` (9) and `array[4]` (1) | `[5, 3, 1, 21, 9, 8, 12, 6, 15]` | 2 |
| 6 | 5 | 8 | `8 <= 6` (False) | No swap | `[5, 3, 1, 21, 9, 8, 12, 6, 15]` | 2 |
| 7 | 6 | 12 | `12 <= 6` (False) | No swap | `[5, 3, 1, 21, 9, 8, 12, 6, 15]` | 2 |
| End Loop | - | - | Swap Pivot: | Swap `array[i + 1]` (index 3, value 21) with Pivot (index 7, value 6) | `[5, 3, 1, 6, 9, 8, 12, 21, 15]` | **Pivot Index = 3** |

*Result*: Element `6` is at its correct sorted position (index 3). Everything to its left is `<= 6` and to its right is `>= 6`.

---

## 3. Complexity Analysis

### Deque (Doubly Linked List Implementation)
- **Time Complexities**:
  - `insertFront()`: $O(1)$ — Only updates head references.
  - `insertRear()`: $O(1)$ — Only updates tail references.
  - `removeFront()`: $O(1)$ — Updates head references and garbage collects old head.
  - `removeRear()`: $O(1)$ — Updates tail references and garbage collects old tail.
  - `peekFront() / peekRear()`: $O(1)$ — Direct reference access.
  - `size() / isEmpty()`: $O(1)$ — Constant-time integer variable check.
- **Space Complexity**: $O(n)$ — Linear space where $n$ is the number of active elements in the Deque.

### Quicksort (Median-of-Three Partitioning)
- **Time Complexities**:
  - **Best Case**: $O(n \log n)$ — Partition splits the array into roughly equal halves.
  - **Average Case**: $O(n \log n)$ — Random order array sorting.
  - **Worst Case**: $O(n \log n)$ (with Median-of-Three) or $O(n^2)$ (without). Median-of-Three prevents the worst-case for sorted or reversed arrays by picking a stable pivot close to the actual median.
- **Space Complexity**: $O(\log n)$ — Call stack space due to recursion.

---

## 4. Anticipated Oral Defense Questions & Answers

### Q1: Why did you choose a Doubly Linked List for your Deque instead of an Array?
**Answer**:  
A Doubly Linked List is ideal for a Deque because it provides true $O(1)$ time complexity for insertions and removals at both ends without requiring any element shifting or array resizing. 
In contrast, a static array-based Deque would require shifting elements (taking $O(n)$ time) or implementing a circular buffer which requires complex index wrapping calculations and expensive memory copy allocations during resizing.

### Q2: What is the Median-of-Three pivot selection and why is it important?
**Answer**:  
Median-of-Three is a pivot selection strategy where we look at the first (low), middle (mid), and last (high) elements of the sub-array, sort them, and select the median of these three values as our pivot.
This is important because it prevents the worst-case $O(n^2)$ time complexity of Quicksort. In standard Quicksort, choosing the first or last element as the pivot results in highly unbalanced partitions (e.g. $0$ and $n-1$ splits) when sorting pre-sorted or reversed arrays, degrading performance to $O(n^2)$. Median-of-three guarantees balanced partitions and preserves the $O(n \log n)$ speed.

### Q3: How did you implement Quicksort in a generic way?
**Answer**:  
I implemented Quicksort using Java Generics with two overloads:
1. `public static <T extends Comparable<T>> void sort(T[] array)` which uses the natural ordering of objects implementing `Comparable`.
2. `public static <T> void sort(T[] array, Comparator<? super T> comparator)` which allows sorting custom objects using a custom comparator strategy.
In our application, this enables us to sort `ServiceRequest` objects dynamically based on priority (urgency and time).

### Q4: Explain how your index number (22046391) was used to parameterize the system.
**Answer**:  
Following the AI-resistance instructions, I derived parameters from my index number (`22046391`):
1. **Priority Urgency Base**: $2 + 2 + 0 + 4 + 6 + 3 + 9 + 1 = 27$. This value is used to calibrate baseline urgencies for service requests.
2. **Route Penalty**: The last two digits are $91$. This is designed to act as an operational delay or traffic penalty added to road travel times during routing optimizations.
