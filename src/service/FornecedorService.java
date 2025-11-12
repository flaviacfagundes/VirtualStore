package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.Fornecedor;
import utils.Logger;

import java.util.List;

public class FornecedorService {

    private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
    private Logger LOG = new Logger(true, "LOGS/pedido_log.txt", "INFO");

    public void create(Fornecedor fornecedor) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor cadastrado: " + fornecedor.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao criar fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

    public List<Fornecedor> read() {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
        EntityManager EM = factory.createEntityManager();
        List<Fornecedor> fornecedores = EM.createQuery("SELECT f FROM Fornecedor f", Fornecedor.class).getResultList();
        EM.close();
        LOG.logSuccess("Listagem de fornecedores executada!\n Total: " + fornecedores.size());
        return fornecedores;
    }

    public Fornecedor buscarPorId(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
        EntityManager EM = factory.createEntityManager();
        Fornecedor fornecedor = EM.find(Fornecedor.class, id);
        EM.close();
        if (fornecedor == null) { LOG.logWarning("Fornecedor com ID " + id + " não encontrado!"); return null; }
        LOG.logInfo("Fornecedor encontrado: " + fornecedor.getId());
        return fornecedor;
    }

    public void update(Fornecedor fornecedor) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor atualizado: " + fornecedor.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao atualizar fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

    public void delete(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Delete abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            Fornecedor fornecedor = EM.find(Fornecedor.class, id);
            if (fornecedor == null) { LOG.logWarning("Tentativa de remover fornecedor inexistente (ID: " + id + ")"); return; }
            EM.remove(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor removido: " + id);
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao remover fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

}
