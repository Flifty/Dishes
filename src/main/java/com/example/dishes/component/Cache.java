package com.example.dishes.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Cache<K, V> {
    private static final int MAX_ENTRIES = 10;

    private final Map<K, Object> cache = new LinkedHashMap<>(MAX_ENTRIES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void putList(K key, List<V> valueList) {
        cache.put(key, valueList);
    }

    public V get(K key) {
        return (V) cache.get(key);
    }

    public List<V> getList(K key) {
        return (List<V>) cache.get(key);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}