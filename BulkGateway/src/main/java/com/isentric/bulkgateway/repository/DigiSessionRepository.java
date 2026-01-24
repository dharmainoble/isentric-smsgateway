package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.manager.LoggerManager;
import org.apache.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

/**
 * Small repository to centralize lookup of digi_session_id by route name.
 * Uses the provided EntityManagerFactory (set via setEntityManagerFactory) to run a native query.
 * If the EMF is not set, the repository returns an empty session and logs a warning.
 */
public class DigiSessionRepository {
    private static final Logger logger = LoggerManager.createLoggerPattern(DigiSessionRepository.class);

    // EntityManagerFactory should be registered at startup by configuration
    private static volatile EntityManagerFactory entityManagerFactory = null;

    public DigiSessionRepository() {
    }

    public static void setEntityManagerFactory(EntityManagerFactory emf) {
        entityManagerFactory = emf;
        logger.info("DigiSessionRepository registered EntityManagerFactory: " + (emf != null));
    }

    public String findDigiSessionByRoute(String routeName) {
        if (entityManagerFactory == null) {
            logger.warn("EntityManagerFactory not set - DigiSessionRepository cannot perform JPA lookup for route " + routeName);
            return "";
        }

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            String sql = "select digi_session_id from bulk_config.digi_session_id where route_name = :route";
            Query q = em.createNativeQuery(sql);
            q.setParameter("route", routeName);
            Object res = q.getSingleResult();
            String session = res != null ? res.toString() : "";
            return session == null ? "" : session;
        } catch (NoResultException nre) {
            return "";
        } catch (Exception e) {
            logger.error("Error fetching digi_session_id via EntityManagerFactory for route " + routeName, e);
            return "";
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
