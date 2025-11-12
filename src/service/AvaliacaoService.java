package service;

import jakarta.persistence.*;
import model.Avaliacao;
import utils.Logger;
import java.util.List;

public class AvaliacaoService {

    private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
    private Logger log = new Logger(true, "LOGS/avaliacao_log.txt", "INFO");

    public void create(Avaliacao avaliacao) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(avaliacao);
            em.getTransaction().commit();
            log.logSuccess("Avaliação criada para o produto: " + avaliacao.getProduto().getNome());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao cadastrar avaliação: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Avaliacao> read() {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
        EntityManager em = factory.createEntityManager();
        List<Avaliacao> avaliacoes = em.createQuery("SELECT a FROM Avaliacao a", Avaliacao.class).getResultList();
        em.close();
        log.logInfo("Listagem de avaliações executada. Total: " + avaliacoes.size());
        return avaliacoes;
    }

    public Avaliacao buscarPorId(Long id) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
        EntityManager em = factory.createEntityManager();
        Avaliacao avaliacao = em.find(Avaliacao.class, id);
        em.close();
        if (avaliacao != null)
            log.logInfo("Avaliação encontrada (Produto: " + avaliacao.getProduto().getNome() + ")");
        else
            log.logWarning("Avaliação com ID " + id + " não encontrada.");
        return avaliacao;
    }

    public void update(Avaliacao avaliacao) {
        EntityManagerFactory factory = emf();
        if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(avaliacao);
            em.getTransaction().commit();
            log.logSuccess("Avaliação atualizada para o produto: " + avaliacao.getProduto().getNome());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao atualizar avaliação: " + e.getMessage());
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
            Avaliacao avaliacao = em.find(Avaliacao.class, id);
            if (avaliacao != null) {
                em.remove(avaliacao);
                em.getTransaction().commit();
                log.logSuccess("Avaliação removida (Produto: " + avaliacao.getProduto().getNome() + ")");
            } else {
                log.logWarning("Tentativa de remover avaliação inexistente (ID: " + id + ")");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.logError("Erro ao remover avaliação: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
