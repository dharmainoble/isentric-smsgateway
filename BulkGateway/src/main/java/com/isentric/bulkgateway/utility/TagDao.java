package com.isentric.bulkgateway.utility;

import java.util.List;

/**
 * Minimal DAO helper used by legacy code. It delegates native SQL execution to
 * EntityManagerFactoryProvider. It picks a persistence unit heuristically
 * when not provided explicitly.
 */
public class TagDao {

    /**
     * Execute the SQL using a best-effort persistence unit choice.
     */
    @SuppressWarnings("unchecked")
    public List<Object> query(String sql) {
        String pu = detectPersistenceUnit(sql);
        List<?> res = EntityManagerFactoryProvider.executeNativeQuery(pu, sql);
        return (List<Object>) res;
    }

    /**
     * Execute the SQL against the given persistence unit name.
     */
    @SuppressWarnings("unchecked")
    public List<Object> query(String persistenceUnitName, String sql) {
        List<?> res = EntityManagerFactoryProvider.executeNativeQuery(persistenceUnitName, sql);
        return (List<Object>) res;
    }

    /**
     * Simple existence check: returns true when the query returns at least one row.
     */
    public boolean check(String sql, String persistenceUnitName) {
        List<Object> res = query(persistenceUnitName, sql);
        return res != null && !res.isEmpty();
    }

    private String detectPersistenceUnit(String sql) {
        if (sql == null) return "bulkgateway";
        String lower = sql.toLowerCase();
        if (lower.contains("bulk_config") || lower.contains("bulk_config.")) return "bulk_config";
        if (lower.contains("extmt") || lower.contains("extmt.")) return "bulkgateway"; // adjust if extmt belongs elsewhere
        // default fallback
        return "bulkgateway";
    }
}

