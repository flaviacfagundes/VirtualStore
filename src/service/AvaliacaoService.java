package service;

import jakarta.persistence.*;
import model.Avaliacao;
import utils.Logger;
import java.util.List;

public class AvaliacaoService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("virtualStorePU");
    private Logger log = new Logger(true, "LOGS/avaliacao_log.txt", "INFO");

    public void create(Avaliacao avaliacao) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(avaliacao);
            em.getTransaction().commit();
            log.logSuccess("Avaliação criada para o produto: " + avaliacao.getProduto().getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao cadastrar avaliação: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Avaliacao> read() {
        EntityManager em = emf.createEntityManager();
        List<Avaliacao> avaliacoes = em.createQuery("SELECT a FROM Avaliacao a", Avaliacao.class).getResultList();
        em.close();
        log.logInfo("Listagem de avaliações executada. Total: " + avaliacoes.size());
        return avaliacoes;
    }

    public Avaliacao buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Avaliacao avaliacao = em.find(Avaliacao.class, id);
        em.close();
        if (avaliacao != null)
            log.logInfo("Avaliação encontrada (Produto: " + avaliacao.getProduto().getNome() + ")");
        else
            log.logWarning("Avaliação com ID " + id + " não encontrada.");
        return avaliacao;
    }

    public void update(Avaliacao avaliacao) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(avaliacao);
            em.getTransaction().commit();
            log.logSuccess("Avaliação atualizada para o produto: " + avaliacao.getProduto().getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao atualizar avaliação: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
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
            em.getTransaction().rollback();
            log.logError("Erro ao remover avaliação: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
