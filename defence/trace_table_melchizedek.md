# Individual Defense Trace Tables

**Student Name:** Melchizedek Sensemore D.A.
**Index Number:** 22234596
**Assigned Data Structure:** Queue
**Assigned Algorithm:** Insertion Sort

---

## 1. Data Structure: Queue Trace Table

### Operations: `enqueue()`, `dequeue()`, and `peek()`
* **Initial State:** Empty Queue `[]`
* **Underlying Implementation:** FIFO (First-In, First-Out) via Singly Linked List

| Step | Operation | Element | Internal Queue State (Front -> Rear) | Return Value | Size | Description / State Changes |
|---|---|---|---|---|---|---|
| 1 | `enqueue` | `"Req-101"` | `["Req-101"]` | `void` | 1 | Head and tail both point to node `"Req-101"`. |
| 2 | `enqueue` | `"Req-102"` | `["Req-101", "Req-102"]` | `void` | 2 | New node appended to tail. Tail updated to `"Req-102"`. |
| 3 | `peek` | - | `["Req-101", "Req-102"]` | `"Req-101"` | 2 | Returns head data without modifying pointers or size. |
| 4 | `dequeue` | - | `["Req-102"]` | `"Req-101"` | 1 | Head pointer advances to next node. Size decrements. |
| 5 | `enqueue` | `"Req-103"` | `["Req-102", "Req-103"]` | `void` | 2 | New node appended to tail. |
| 6 | `dequeue` | - | `["Req-103"]` | `"Req-102"` | 1 | Head advances to `"Req-103"`. |
| 7 | `dequeue` | - | `[]` | `"Req-103"` | 0 | Last element removed. Head and tail set to `null`. |
| 8 | `dequeue` | - | `[]` | `NoSuchElementException` | 0 | Underflow condition caught on empty queue. |

---

## 2. Algorithm: Insertion Sort Trace Table

### Input Array: `[45, 12, 85, 32, 10]`
* **Time Complexity:** $O(N^2)$ worst/average, $O(N)$ best
* **Space Complexity:** $O(1)$ auxiliary

| Pass ($i$) | Key ($A[i]$) | Comparisons / Shifts ($j$ index traversal) | Array State After Pass | Notes |
|---|---|---|---|---|
| Initial | - | - | `[45, 12, 85, 32, 10]` | Sub-array `[45]` at index 0 considered sorted. |
| $i = 1$ | `12` | $45 > 12 \rightarrow$ Shift 45 right to index 1 | `[12, 45, 85, 32, 10]` | Insert 12 at index 0. |
| $i = 2$ | `85` | $45 < 85 \rightarrow$ 0 shifts needed | `[12, 45, 85, 32, 10]` | 85 remains at index 2. |
| $i = 3$ | `32` | $85 > 32 \rightarrow$ Shift 85 right<br>$45 > 32 \rightarrow$ Shift 45 right | `[12, 32, 45, 85, 10]` | Insert 32 at index 1. |
| $i = 4$ | `10` | $85, 45, 32, 12 > 10 \rightarrow$ Shift all elements right | `[10, 12, 32, 45, 85]` | Insert 10 at index 0. Array fully sorted. |
