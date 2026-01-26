package com.isentric.bulkgateway.utility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight provider for EntityManagerFactory instances keyed by persistence unit name.
 * Provides a convenience method to execute native SQL queries and return results.
 *
 * Usage:
 *   EntityManagerFactoryProvider.executeNativeQuery("bulkgateway", "SELECT ...");
 */
public final class EntityManagerFactoryProvider {

    private static final Map<String, EntityManagerFactory> EMF_MAP = new ConcurrentHashMap<>();

    private EntityManagerFactoryProvider() {
    }

    public static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {
        return EMF_MAP.computeIfAbsent(persistenceUnitName, pn -> Persistence.createEntityManagerFactory(pn));
    }

    public static EntityManager getEntityManager(String persistenceUnitName) {
        EntityManagerFactory emf = getEntityManagerFactory(persistenceUnitName);
        return emf.createEntityManager();
    }

    /**
     * Execute a native SQL query and return the result list.
     * Caller is responsible for any casting of returned elements.
     */
    public static List<?> executeNativeQuery(String persistenceUnitName, String sql) {
        EntityManager em = getEntityManager(persistenceUnitName);
        try {
            Query q = em.createNativeQuery(sql);
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Execute a native SQL query with positional parameters and return the raw result list.
     * Example: executeNativeQuery("pu", "select * from t where id = ?", 123);
     */
    public static List<?> executeNativeQuery(String persistenceUnitName, String sql, Object... params) {
        EntityManager em = getEntityManager(persistenceUnitName);
        try {
            Query q = em.createNativeQuery(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    // JPA positional parameters are 1-based
                    q.setParameter(i + 1, params[i]);
                }
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Convenience variant that always returns List<Object[]> for multi-column selects.
     */
    @SuppressWarnings("unchecked")
    public static List<Object[]> executeNativeQueryAsArray(String persistenceUnitName, String sql, Object... params) {
        List<?> raw = executeNativeQuery(persistenceUnitName, sql, params);
        return (List<Object[]>) raw;
    }

    /**
     * Execute the SQL and return a list of maps (column->value). This uses a JDBC Connection
     * obtained from the EntityManager by unwrapping. Useful when you want column names.
     */
    public static List<java.util.Map<String, Object>> executeNativeQueryAsMap(String persistenceUnitName, String sql, Object... params) {
        EntityManager em = getEntityManager(persistenceUnitName);
        java.sql.Connection conn = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        try {
            conn = em.unwrap(java.sql.Connection.class);
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            java.sql.ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                for (int i = 1; i <= cols; i++) {
                    String colName = md.getColumnLabel(i);
                    if (colName == null || colName.isEmpty()) colName = md.getColumnName(i);
                    map.put(colName, rs.getObject(i));
                }
                rows.add(map);
            }
            return rows;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            if (em != null && em.isOpen()) em.close();
        }
    }

}
