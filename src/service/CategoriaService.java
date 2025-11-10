package service;

import jakarta.persistence.*;
import model.Categoria;
import utils.Logger;
import java.util.List;

public class CategoriaService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("virtualStorePU");
    private Logger log = new Logger(true, "LOGS/categoria_log.txt", "INFO");

    public void create(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
            log.logSuccess("Categoria cadastrada: " + categoria.getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao cadastrar categoria: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Categoria> read() {
        EntityManager em = emf.createEntityManager();
        List<Categoria> categorias = em.createQuery("SELECT c FROM categoria c", Categoria.class).getResultList();
        em.close();
        log.logInfo("Listagem de categorias executada. Total: " + categorias.size());
        return categorias;
    }

	public Categoria buscarPorNome(String nome) {
	    EntityManager em = emf.createEntityManager();
	    Categoria categoria = null;

	    try {
	        TypedQuery<Categoria> query = em.createQuery(
	            "SELECT c FROM categoria c WHERE c.nome_categoria = :nome", Categoria.class);
	        query.setParameter("nome", nome);

	        categoria = query.getSingleResult();
	        log.logInfo("Categoria encontrada: " + categoria.getNome());
	    } 
	    catch (NoResultException e) {
	        log.logWarning("Categoria com o nome: '" + nome + "' não encontrada!");
	    } 
	    catch (Exception e) {
	        log.logError("Erro ao buscar categoria: " + e.getMessage());
	    } 
	    finally {
	        em.close();
	    }

	    return categoria;
	}

    public void update(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();
            log.logSuccess("Categoria atualizada: " + categoria.getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao atualizar categoria: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void delete(String nome) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            TypedQuery<Categoria> query = em.createQuery(
                "SELECT c FROM categoria c WHERE c.nome = :nome", Categoria.class);
            query.setParameter("nome", nome);

            Categoria categoria = null;
            try {
                categoria = query.getSingleResult();
            } catch (NoResultException e) {
                log.logWarning("Tentativa de remover categoria inexistente [Nome: " + nome + "]");
            }

            if (categoria != null) {
                em.remove(em.merge(categoria));
                em.getTransaction().commit();
                log.logSuccess("Categoria removida com sucesso: " + categoria.getNome());
            } else {
                em.getTransaction().rollback();
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            log.logError("Erro ao remover categoria por nome (" + nome + "): " + e.getMessage());
        } finally {
            em.close();
        }
    }

}
