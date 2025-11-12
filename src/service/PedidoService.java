package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.Pedido;
import utils.Logger;

import java.util.List;

public class PedidoService {

    private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
    private Logger LOG = new Logger(true, "LOGS/pedido_log.txt", "INFO");

    public void create(Pedido pedido) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(pedido);
            EM.getTransaction().commit();
            LOG.logSuccess("Pedido cadastrado: " + pedido.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao criar pedido: " + e.getMessage());
        } finally { EM.close(); }
    }

    public List<Pedido> read() {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
        EntityManager EM = factory.createEntityManager();
        List<Pedido> pagamentos = EM.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        EM.close();
        LOG.logSuccess("Listagem de pedidos executada!\n Total: " + pagamentos.size());
        return pagamentos;
    }

    public Pedido buscarPorId(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
        EntityManager EM = factory.createEntityManager();
        Pedido pedido = EM.find(Pedido.class, id);
        EM.close();
        if (pedido == null) { LOG.logWarning("Pedido com ID " + id + " não encontrado!"); return null; }
        LOG.logInfo("Pedido encontrado: " + pedido.getId());
        return pedido;
    }

    public void update(Pedido pedido) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            EM.persist(pedido);
            EM.getTransaction().commit();
            LOG.logSuccess("Pedido atualizado: " + pedido.getId());
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao atualizar pedido: " + e.getMessage());
        } finally { EM.close(); }
    }

    public void delete(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { LOG.logError("Nenhum provedor de persistência disponível. Delete abortado."); return; }
        EntityManager EM = factory.createEntityManager();
        try {
            EM.getTransaction().begin();
            Pedido pedido = EM.find(Pedido.class, id);
            if (pedido == null) { LOG.logWarning("Tentativa de remover pedido inexistente (ID: " + id + ")"); return; }
            EM.remove(pedido);
            EM.getTransaction().commit();
            LOG.logSuccess("Pedido removido: " + id);
        } catch (Exception e) {
            if (EM.getTransaction().isActive()) EM.getTransaction().rollback();
            LOG.logError("Erro ao remover pedido: " + e.getMessage());
        } finally { EM.close(); }
    }

}
