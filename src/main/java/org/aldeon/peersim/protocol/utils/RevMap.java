package org.aldeon.peersim.protocol.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RevMap<K, V> {

    private final Map<K, V> values = new HashMap<>();
    private final Multimap<V, K> index = ArrayListMultimap.create();

    public void put(K key, V value) {
        remove(key);
        values.put(key, value);
        index.put(value, key);
    }

    public V get(K key) {
        return values.get(key);
    }

    public V remove(K key) {
        V previous = values.remove(key);
        if (previous != null) index.remove(key, previous);
        return previous;
    }

    public boolean containsKey(K key) {
        return values.containsKey(key);
    }

    public int size() {
        return values.size();
    }

    public Set<K> find(V value) {
        return new HashSet<>(index.get(value));
    }

    public void putAll(RevMap<K, V> map) {
        map.values.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
    }
}
