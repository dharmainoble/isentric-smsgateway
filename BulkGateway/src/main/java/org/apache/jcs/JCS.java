package org.apache.jcs;

import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

import java.util.HashMap;
import java.util.Map;
public class JCS {
    private static final Map<String, CacheAccess<?>> caches = new HashMap<>();
    public static <K, V> CacheAccess<V> getInstance(String cacheName) throws CacheException {
        synchronized (caches) {
            CacheAccess<?> cache = caches.get(cacheName);
            if (cache == null) {
                cache = new CacheAccess<V>(cacheName);
                caches.put(cacheName, cache);
            }
            return (CacheAccess<V>) cache;
        }
    }
}
