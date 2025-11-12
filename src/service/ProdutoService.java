package service;

import jakarta.persistence.*;
import model.Produto;
import utils.Logger;
import java.util.List;

public class ProdutoService {

    private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
    private Logger log = new Logger(true, "LOGS/produto_log.txt", "INFO");

    public void create(Produto produto) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(produto);
            em.getTransaction().commit();
            log.logSuccess("Produto cadastrado: " + produto.getNome());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Produto> read() {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
        EntityManager em = factory.createEntityManager();
        List<Produto> produtos = em.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
        em.close();
        log.logInfo("Listagem de produtos executada. Total: " + produtos.size());
        return produtos;
    }

    public Produto buscarPorId(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
        EntityManager em = factory.createEntityManager();
        Produto produto = em.find(Produto.class, id);
        em.close();
        if (produto != null)
            log.logInfo("Produto encontrado: " + produto.getNome());
        else
            log.logWarning("Produto com ID " + id + " não encontrado.");
        return produto;
    }

    public void update(Produto produto) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(produto);
            em.getTransaction().commit();
            log.logSuccess("Produto atualizado: " + produto.getNome());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Delete abortado."); return; }
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            Produto produto = em.find(Produto.class, id);
            if (produto != null) {
                em.remove(produto);
                em.getTransaction().commit();
                log.logSuccess("Produto removido: " + produto.getNome());
            } else {
                log.logWarning("Tentativa de remover produto inexistente (ID: " + id + ")");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao remover produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
