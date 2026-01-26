package org.apache.jcs;

import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

import java.util.HashMap;
import java.util.Map;

public class JCS {
    // store caches with wildcard generics; actual type parameters are provided at call site
    private static final Map<String, CacheAccess<?, ?>> caches = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <K, V> CacheAccess<K, V> getInstance(String cacheName) throws CacheException {
        synchronized (caches) {
            CacheAccess<?, ?> cache = caches.get(cacheName);
            if (cache == null) {
                cache = new CacheAccess<K, V>(cacheName);
                caches.put(cacheName, cache);
            }
            return (CacheAccess<K, V>) cache;
        }
    }
}
