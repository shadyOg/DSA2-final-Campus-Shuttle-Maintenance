import java.util.*;

public class HashTableDemo {
    static class Entry {
        String key, value;
        Entry(String k, String v) { key = k; value = v; }
    }

    private final List<Entry>[] buckets;

    @SuppressWarnings("unchecked")
    HashTableDemo(int capacity) {
        buckets = new List[capacity];
        for (int i = 0; i < capacity; i++) buckets[i] = new ArrayList<>();
    }

    private int index(String key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    void put(String key, String value) {
        int i = index(key);
        for (Entry e : buckets[i]) {
            if (e.key.equals(key)) { e.value = value; return; }
        }
        buckets[i].add(new Entry(key, value));
    }

    String get(String key) {
        int i = index(key);
        for (Entry e : buckets[i])
            if (e.key.equals(key)) return e.value;
        return null;
    }

    void print() {
        for (int i = 0; i < buckets.length; i++)
            if (!buckets[i].isEmpty())
                System.out.println(i + " -> " + buckets[i].stream()
                        .map(e -> e.key + "=" + e.value).toList());
    }

    public static void main(String[] args) {
        // Small capacity keeps the oral demonstration easy to see.
        // The assignment's team-level derived base size is documented in the notes.
        HashTableDemo table = new HashTableDemo(7);
        table.put("Shuttle Stop A", "4");
        table.put("JQB", "2");
        table.put("Legon Hall", "1");

        table.print();
        System.out.println("Search JQB -> " + table.get("JQB"));
        System.out.println("Search Missing -> " + table.get("Missing"));
    }
}
