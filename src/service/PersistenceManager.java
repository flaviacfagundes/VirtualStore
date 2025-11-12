package service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try { emf = Persistence.createEntityManagerFactory("virtualStorePU"); }
            catch (Exception e) {
                System.err.println("[PersistenceManager] Could not create EntityManagerFactory for 'virtualStorePU': " + e.getMessage());
                emf = null;
            }
        }
        return emf;
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}


