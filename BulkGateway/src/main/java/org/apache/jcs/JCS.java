package org.apache.jcs;

import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

import java.util.HashMap;
import java.util.Map;
public class JCS {
    private static final Map<String, CacheAccess<?, P>> caches = new HashMap<>();
    public static <K, V> CacheAccess<V, P> getInstance(String cacheName) throws CacheException {
        synchronized (caches) {
            CacheAccess<?, P> cache = caches.get(cacheName);
            if (cache == null) {
                cache = new CacheAccess<V, P>(cacheName);
                caches.put(cacheName, cache);
            }
            return (CacheAccess<V, P>) cache;
        }
    }
}
