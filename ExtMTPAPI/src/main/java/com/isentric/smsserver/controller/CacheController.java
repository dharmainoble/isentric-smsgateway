package com.isentric.smsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/UpdateCacheServlet")
@Slf4j
@RequiredArgsConstructor
public class CacheController {
    
    private final CacheManager cacheManager;
    
    /**
     * Clear all caches
     */
    @GetMapping
    public ResponseEntity<String> clearCache(@RequestParam(required = false) String cacheName) {
        
        if (cacheName != null && !cacheName.isEmpty()) {
            // Clear specific cache
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cache cleared: {}", cacheName);
                return ResponseEntity.ok("Cache " + cacheName + " cleared successfully");
            } else {
                return ResponseEntity.badRequest().body("Cache " + cacheName + " not found");
            }
        } else {
            // Clear all caches
            cacheManager.getCacheNames().forEach(name -> {
                var cache = cacheManager.getCache(name);
                if (cache != null) {
                    cache.clear();
                    log.info("Cache cleared: {}", name);
                }
            });
            return ResponseEntity.ok("All caches cleared successfully");
        }
    }
    
    /**
     * Get cache statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<String> getCacheStats() {
        StringBuilder stats = new StringBuilder("Cache Statistics:\n");
        
        cacheManager.getCacheNames().forEach(name -> {
            stats.append("Cache: ").append(name).append("\n");
        });
        
        return ResponseEntity.ok(stats.toString());
    }
}

