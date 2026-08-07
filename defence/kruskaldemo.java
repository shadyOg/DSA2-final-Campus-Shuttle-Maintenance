import java.util.*;

public class KruskalDemo {
    record Edge(int u, int v, int weight) {}

    static class DSU {
        int[] parent, rank;
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false; // would create a cycle
            if (rank[ra] < rank[rb]) parent[ra] = rb;
            else if (rank[ra] > rank[rb]) parent[rb] = ra;
            else { parent[rb] = ra; rank[ra]++; }
            return true;
        }
    }

    static List<Edge> kruskal(int vertices, List<Edge> edges) {
        edges.sort(Comparator.comparingInt(Edge::weight));
        DSU dsu = new DSU(vertices);
        List<Edge> mst = new ArrayList<>();

        for (Edge e : edges) {
            if (dsu.union(e.u(), e.v())) {
                mst.add(e);
                if (mst.size() == vertices - 1) break;
            }
        }
        return mst;
    }

    public static void main(String[] args) {
        // 0=Legon Hall, 1=Shuttle Stop A, 2=JQB,
        // 3=Computer Lab, 4=Maintenance Office
        List<Edge> roads = new ArrayList<>(List.of(
            new Edge(0,1,8),
            new Edge(1,2,11),
            new Edge(2,3,6),
            new Edge(3,4,14),
            new Edge(1,4,10),
            new Edge(0,2,13)
        ));

        List<Edge> mst = kruskal(5, roads);
        int total = 0;
        for (Edge e : mst) {
            System.out.println(e.u() + " -- " + e.v() + " weight=" + e.weight());
            total += e.weight();
        }
        System.out.println("MST total weight = " + total);
    }
}
