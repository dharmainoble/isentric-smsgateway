package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.manager.LoggerManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public class TagRepository {

    private static final Logger logger = LoggerManager.createLoggerPattern(TagRepository.class);

    private static volatile EntityManagerFactory entityManagerFactory = null;

    public TagRepository() {
    }

    public static void setEntityManagerFactory(EntityManagerFactory emf) {
        entityManagerFactory = emf;
        logger.info("TagRepository registered EntityManagerFactory: " + (emf != null));
    }

    /**
     * Returns true if a row exists for the given custid with status = '0'.
     */
    public boolean existsByCustId(String custid) {
        if (entityManagerFactory == null) {
            logger.warn("EntityManagerFactory not set - cannot query bulk_skip_autoresend");
            return false;
        }

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String sql = "select custid from bulk_config.bulk_skip_autoresend where custid = :custid and status = '0'";
            Query q = em.createNativeQuery(sql);
            q.setParameter("custid", custid);
            Object res = q.getSingleResult();
            return res != null;
        } catch (NoResultException nre) {
            return false;
        } catch (Exception e) {
            logger.error("Error querying bulk_skip_autoresend for custid=" + custid, e);
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Execute a native SQL query and return results as ArrayList.
     * Each row is represented as Object[] array.
     * @param sqlQuery the SQL query to execute
     * @param datasource the datasource name (currently unused - uses entityManagerFactory)
     * @return ArrayList of Object[] rows or empty list if no results
     */
    public Object query(String sqlQuery, String datasource) {
        if (entityManagerFactory == null) {
            logger.warn("EntityManagerFactory not set for datasource '" + datasource + "' - cannot execute query");
            return new ArrayList<>();
        }

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            Query q = em.createNativeQuery(sqlQuery);
            java.util.List<?> resultList = q.getResultList();
            return new ArrayList<>(resultList);
        } catch (Exception e) {
            logger.error("Error executing query on datasource '" + datasource + "': " + sqlQuery, e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Execute a native SQL update/insert/delete query.
     * @param sqlQuery the SQL update query to execute
     * @param datasource the datasource name (currently unused - uses entityManagerFactory)
     * @return number of rows affected
     */
    public int update(String sqlQuery, String datasource) {
        if (entityManagerFactory == null) {
            logger.warn("EntityManagerFactory not set for datasource '" + datasource + "' - cannot execute update");
            return 0;
        }

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            Query q = em.createNativeQuery(sqlQuery);
            int result = q.executeUpdate();
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            logger.error("Error executing update on datasource '" + datasource + "': " + sqlQuery, e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return 0;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

}
