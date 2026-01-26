package com.isentric.bulkgateway.utility;

import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Small helper to centralize unchecked casts when obtaining JCS caches.
 */
public final class CacheHelper {

    private CacheHelper() {}

    @SuppressWarnings("unchecked")
    public static <V> CacheAccess<V, Object> getCache(String cacheName) throws CacheException {
        try {
            return (CacheAccess<V, Object>) JCS.getInstance(cacheName);
        } catch (CacheException ce) {
            // Attempt to bootstrap JCS from classpath resource '/cache.ccf'
            try (InputStream is = CacheHelper.class.getResourceAsStream("/cache.ccf")) {
                if (is != null) {
                    // write to temp file and set system property for JCS config
                    File tmp = File.createTempFile("cache-", ".ccf");
                    try (FileOutputStream fos = new FileOutputStream(tmp)) {
                        byte[] buf = new byte[4096];
                        int r;
                        while ((r = is.read(buf)) != -1) {
                            fos.write(buf, 0, r);
                        }
                    }
                    System.setProperty("jcs.config", tmp.getAbsolutePath());
                    // Retry obtaining the cache
                    return (CacheAccess<V, Object>) JCS.getInstance(cacheName);
                }
            } catch (Exception ex) {
                // ignore and rethrow original exception below
            }
            throw ce;
        }
    }
}
