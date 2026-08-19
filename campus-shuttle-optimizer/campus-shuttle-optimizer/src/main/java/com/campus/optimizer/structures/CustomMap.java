package com.campus.optimizer.structures;

public class CustomMap<K, V> {
    private final HashTable<K, V> entries = new HashTable<>();

    public V put(K key, V value) {
        return entries.put(key, value);
    }

    public V get(K key) {
        return entries.get(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = entries.get(key);
        return value == null ? defaultValue : value;
    }

    public V remove(K key) {
        return entries.remove(key);
    }

    public boolean containsKey(K key) {
        return entries.containsKey(key);
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void clear() {
        entries.clear();
    }

    public DynamicArray<K> keySet() {
        return entries.keys();
    }
}