package org.apache.jcs.access;
import java.util.HashMap;
import java.util.Map;
public class CacheAccess<V, P> {
    private final String cacheName;
    private final Map<String, V> cache = new HashMap<>();
    public CacheAccess(String cacheName) {
        this.cacheName = cacheName;
    }
    public V get(String key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }
    public void put(String key, V value) {
        synchronized (cache) {
            cache.put(key, value);
        }
    }
    public void remove(String key) {
        synchronized (cache) {
            cache.remove(key);
        }
    }
    public void remove() {
        synchronized (cache) {
            cache.clear();
        }
    }
}
