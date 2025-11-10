package service;

import jakarta.persistence.*;
import model.Produto;
import utils.Logger;
import java.util.List;

public class ProdutoService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("virtualStorePU");
    private Logger log = new Logger(true, "LOGS/produto_log.txt", "INFO");

    public void create(Produto produto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(produto);
            em.getTransaction().commit();
            log.logSuccess("Produto cadastrado: " + produto.getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Produto> read() {
        EntityManager em = emf.createEntityManager();
        List<Produto> produtos = em.createQuery("SELECT p FROM produto p", Produto.class).getResultList();
        em.close();
        log.logInfo("Listagem de produtos executada. Total: " + produtos.size());
        return produtos;
    }

    public Produto buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Produto produto = em.find(Produto.class, id);
        em.close();
        if (produto != null)
            log.logInfo("Produto encontrado: " + produto.getNome());
        else
            log.logWarning("Produto com ID " + id + " não encontrado.");
        return produto;
    }

    public void update(Produto produto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(produto);
            em.getTransaction().commit();
            log.logSuccess("Produto atualizado: " + produto.getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
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
            em.getTransaction().rollback();
            log.logError("Erro ao remover produto: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
