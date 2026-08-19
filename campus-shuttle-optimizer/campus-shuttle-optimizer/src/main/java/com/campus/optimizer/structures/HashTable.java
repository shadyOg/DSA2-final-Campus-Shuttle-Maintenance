package com.campus.optimizer.structures;

public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 17;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        buckets = (Entry<K, V>[]) new Entry[capacity];
    }

    public V put(K key, V value) {
        requireKey(key);
        int index = indexFor(key, buckets.length);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                V previous = current.value;
                current.value = value;
                return previous;
            }
            current = current.next;
        }

        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        if ((double) size / buckets.length > MAX_LOAD_FACTOR) {
            resize(buckets.length * 2);
        }
        return null;
    }

    public V get(K key) {
        Entry<K, V> entry = find(key);
        return entry == null ? null : entry.value;
    }

    public V remove(K key) {
        requireKey(key);
        int index = indexFor(key, buckets.length);
        Entry<K, V> previous = null;
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return find(key) != null;
    }

    public DynamicArray<K> keys() {
        DynamicArray<K> keys = new DynamicArray<>();
        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        buckets = (Entry<K, V>[]) new Entry[buckets.length];
        size = 0;
    }

    private Entry<K, V> find(K key) {
        requireKey(key);
        Entry<K, V> current = buckets[indexFor(key, buckets.length)];
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private int indexFor(K key, int capacity) {
        int hash = key.hashCode();
        hash ^= hash >>> 16;
        hash *= 0x45d9f3b;
        hash ^= hash >>> 16;
        return Math.floorMod(hash, capacity);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[newCapacity];
        for (Entry<K, V> entry : oldBuckets) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = indexFor(entry.key, newCapacity);
                entry.next = buckets[index];
                buckets[index] = entry;
                entry = next;
            }
        }
    }

    private void requireKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Hash table keys cannot be null");
        }
    }

    private static final class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}