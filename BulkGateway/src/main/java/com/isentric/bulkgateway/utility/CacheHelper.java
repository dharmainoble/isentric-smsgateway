package com.isentric.bulkgateway.utility;

import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

/**
 * Small helper to centralize unchecked casts when obtaining JCS caches.
 */
public final class CacheHelper {

    private CacheHelper() {}

    @SuppressWarnings("unchecked")
    public static <V> CacheAccess<V, Object> getCache(String cacheName) throws CacheException {
        return (CacheAccess<V, Object>) JCS.getInstance(cacheName);
    }
}

