package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Fornecedor;
import utils.Logger;

import java.util.List;

public class FornecedorService {

    private EntityManagerFactory EMF = Persistence.createEntityManagerFactory("VirtualStorePU");
    private Logger LOG = new Logger(true, "LOGS/pedido_log.txt", "INFO");

    public void create(Fornecedor fornecedor) {
        EntityManager EM = EMF.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor cadastrado: " + fornecedor.getId());
        } catch (Exception e) {
            EM.getTransaction().rollback();
            LOG.logError("Erro ao criar fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

    public List<Fornecedor> read() {
        EntityManager EM = EMF.createEntityManager();
        List<Fornecedor> fornecedores = EM.createQuery("SELECT f FROM fornecedor f", Fornecedor.class).getResultList();
        EM.close();
        LOG.logSuccess("Listagem de fornecedores executada!\n Total: " + fornecedores.size());
        return fornecedores;
    }

    public Fornecedor buscarPorId(Long id) {
        EntityManager EM = EMF.createEntityManager();
        Fornecedor fornecedor = EM.find(Fornecedor.class, id);
        EM.close();
        if (fornecedor == null) { LOG.logWarning("Fornecedor com ID " + id + " não encontrado!"); return null; }
        LOG.logInfo("Fornecedor encontrado: " + fornecedor.getId());
        return fornecedor;
    }

    public void update(Fornecedor fornecedor) {
        EntityManager EM = EMF.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor atualizado: " + fornecedor.getId());
        } catch (Exception e) {
            EM.getTransaction().rollback();
            LOG.logError("Erro ao atualizar fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

    public void delete(Long id) {
        EntityManager EM = EMF.createEntityManager();
        try {
            EM.getTransaction().begin();
            Fornecedor fornecedor = EM.find(Fornecedor.class, id);
            if (fornecedor == null) { LOG.logWarning("Tentativa de remover fornecedor inexistente (ID: " + id + ")"); return; }
            EM.remove(fornecedor);
            EM.getTransaction().commit();
            LOG.logSuccess("Fornecedor removido: " + id);
        } catch (Exception e) {
            EM.getTransaction().rollback();
            LOG.logError("Erro ao remover fornecedor: " + e.getMessage());
        } finally { EM.close(); }
    }

}
