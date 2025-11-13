package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try { 
                emf = Persistence.createEntityManagerFactory("virtualStorePU");
                // Força a criação das tabelas ao inicializar
                initializeDatabase();
            }
            catch (Exception e) {
                System.err.println("[PersistenceManager] Could not create EntityManagerFactory for 'virtualStorePU': " + e.getMessage());
                e.printStackTrace();
                emf = null;
            }
        }
        return emf;
    }

    /**
     * Inicializa o banco de dados e cria as tabelas automaticamente
     * se elas não existirem. A criação de tabelas acontece automaticamente
     * quando o EntityManagerFactory é criado com as propriedades de 
     * schema-generation no persistence.xml.
     * Este método força uma conexão inicial para garantir que as tabelas sejam criadas.
     */
    private static void initializeDatabase() {
        if (emf == null) return;
        
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getMetamodel();
            em.getTransaction().begin();
            em.getTransaction().commit();
            
            System.out.println("[PersistenceManager] ✓ Banco de dados inicializado. Tabelas criadas/verificadas automaticamente.");
        } catch (Exception e) {
            System.err.println("[PersistenceManager] ✗ Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                try { em.getTransaction().rollback(); }
                catch (Exception ex) { /* Ignorar */ }
            }
        } finally { if (em != null && em.isOpen()) { em.close(); } }
    }

    /**
     * Inicializa explicitamente o banco de dados e cria as tabelas.
     * Este método pode ser chamado no início da aplicação para garantir
     * que as tabelas sejam criadas antes de qualquer operação.
     */
    public static synchronized void initialize() { getEntityManagerFactory(); }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}


