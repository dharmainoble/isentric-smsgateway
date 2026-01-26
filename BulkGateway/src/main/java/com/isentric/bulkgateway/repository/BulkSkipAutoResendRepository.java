package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.manager.LoggerManager;
import org.apache.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

/**
 * Repository to check bulk_skip_autoresend entries using an EntityManagerFactory provided by configuration.
 */
public class BulkSkipAutoResendRepository {
    private static final Logger logger = LoggerManager.createLoggerPattern(BulkSkipAutoResendRepository.class);

    private static volatile EntityManagerFactory entityManagerFactory = null;

    public BulkSkipAutoResendRepository() {
    }

    public static void setEntityManagerFactory(EntityManagerFactory emf) {
        entityManagerFactory = emf;
        logger.info("BulkSkipAutoResendRepository registered EntityManagerFactory: " + (emf != null));
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
}

