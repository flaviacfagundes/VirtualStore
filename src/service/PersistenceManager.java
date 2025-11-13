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
            // Força uma conexão inicial que irá criar as tabelas se necessário
            // Ao criar o EntityManager e acessar o metamodel, o EclipseLink
            // irá criar as tabelas automaticamente conforme configurado no persistence.xml
            em.getMetamodel();
            
            // Opcional: Força a criação executando uma query simples
            // Isso garante que as tabelas sejam realmente criadas no banco
            em.getTransaction().begin();
            // Não precisa fazer nada, apenas iniciar uma transação
            em.getTransaction().commit();
            
            System.out.println("[PersistenceManager] ✓ Banco de dados inicializado. Tabelas criadas/verificadas automaticamente.");
        } catch (Exception e) {
            System.err.println("[PersistenceManager] ✗ Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                try {
                    em.getTransaction().rollback();
                } catch (Exception ex) {
                    // Ignora erro no rollback
                }
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Inicializa explicitamente o banco de dados e cria as tabelas.
     * Este método pode ser chamado no início da aplicação para garantir
     * que as tabelas sejam criadas antes de qualquer operação.
     */
    public static synchronized void initialize() {
        getEntityManagerFactory(); // Isso vai chamar initializeDatabase()
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }
}


