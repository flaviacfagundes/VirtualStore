package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.Pagamento;
import utils.Logger;

import java.util.List;

public class PagamentoService {

    private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
    private Logger LOG = new Logger(true, "LOGS/pagamento_log.txt", "INFO");

    public void create(Pagamento pagamento) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(pagamento);
            EM.getTransaction().commit();
            LOG.logSuccess("Pagamento cadastrado: " + pagamento.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao criar Pagamento: " + e.getMessage());
        } finally { EM.close(); }
    }

    public List<Pagamento> read() {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
        EntityManager EM = factory.createEntityManager();
        List<Pagamento> pagamentos = EM.createQuery("SELECT p FROM Pagamento p", Pagamento.class).getResultList();
        EM.close();
        LOG.logSuccess("Listagem de pagamentos executada!\n Total: " + pagamentos.size());
        return pagamentos;
    }

    public Pagamento buscarPorId(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
        EntityManager EM = factory.createEntityManager();
        Pagamento pagamento = EM.find(Pagamento.class, id);
        EM.close();
        if (pagamento == null) { LOG.logWarning("Produto com ID " + id + " não encontrado!"); return null; }
        LOG.logInfo("Pagamento encontrado: " + pagamento.getId());
        return pagamento;
    }

    public void update(Pagamento pagamento) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(pagamento);
            EM.getTransaction().commit();
            LOG.logSuccess("Pagamento atualizado: " + pagamento.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao atualizar Pagamento: " + e.getMessage());
        } finally { EM.close(); }
    }

    public void delete(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Delete abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            Pagamento pagamento = EM.find(Pagamento.class, id);
            if (pagamento == null) { LOG.logWarning("Tentativa de remover produto inexistente (ID: " + id + ")"); return; }
            EM.remove(pagamento);
            EM.getTransaction().commit();
            LOG.logSuccess("Pagamento removido: " + id);
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao remover Pagamento: " + e.getMessage());
        } finally { EM.close(); }
    }

}
