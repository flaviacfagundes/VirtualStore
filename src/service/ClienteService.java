package service;

import java.util.List;

import jakarta.persistence.*;
import model.Cliente;
import utils.Logger;

public class ClienteService {
	private EntityManagerFactory emf() { return PersistenceManager.getEntityManagerFactory(); }
	private Logger log = new Logger(true, "LOGS/cliente_log.txt", "INFO");
	
	public void create(Cliente cliente) {
		EntityManagerFactory factory = emf();
		if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Create abortado."); return; }
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(cliente);
			em.getTransaction().commit();
			log.logSuccess("Cliente cadastrado: " + cliente.getNome());
		} catch (Exception e){
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			log.logError("Erro ao cadastrar cliente: " + e.getMessage());
		} finally {
			em.close();
		}
	}
	
	public List<Cliente> read() {
		EntityManagerFactory factory = emf();
		if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Read retorna lista vazia."); return java.util.Collections.emptyList(); }
		EntityManager em = factory.createEntityManager();
		List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
		em.close();
		log.logInfo("Listagem de clientes executada. Total de clientes: " + clientes.size());
		return clientes;
	}

	public Cliente buscarPorEmail(String email) {
	    EntityManagerFactory factory = emf();
	    if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Busca abortada."); return null; }
	    EntityManager em = factory.createEntityManager();
	    Cliente cliente = null;

	    try {
	        TypedQuery<Cliente> query = em.createQuery(
	            "SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
	        query.setParameter("email", email);

	        cliente = query.getSingleResult();
	        log.logInfo("Cliente encontrado: " + cliente.getNome());
	    } 
	    catch (NoResultException e) {
	        log.logWarning("Cliente com email '" + email + "' não encontrado!");
	    } 
	    catch (Exception e) {
	        log.logError("Erro ao buscar cliente por email: " + e.getMessage());
	    } 
	    finally {
	        em.close();
	    }

	    return cliente;
	}
	
	public void update(Cliente cliente) {
		EntityManagerFactory factory = emf();
		if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Update abortado."); return; }
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(cliente);
			em.getTransaction().commit();
			log.logSuccess("Cliente atualizado: " + cliente.getNome());
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			log.logError("Erro ao atualizar cliente: " + e.getMessage());
		} finally {
			em.close();
		}
	}
	
	public void delete(String email) {
	    EntityManagerFactory factory = emf();
	    if (factory == null) { log.logError("Nenhum provedor de persistência disponível. Delete abortado."); return; }
	    EntityManager em = factory.createEntityManager();
	    try {
	        em.getTransaction().begin();

	        TypedQuery<Cliente> query = em.createQuery(
	            "SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
	        query.setParameter("email", email);

	        Cliente cliente = null;
	        try {
	            cliente = query.getSingleResult();
	        } catch (NoResultException e) {
	            log.logWarning("Tentativa de remover cliente inexistente (Email: " + email + ")");
	        }

	        if (cliente != null) {
	            em.remove(em.merge(cliente));
	            em.getTransaction().commit();
	            log.logSuccess("Cliente removido com sucesso: " + cliente.getNome());
	        } else {
	            em.getTransaction().rollback();
	        }
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) em.getTransaction().rollback();
	        log.logError("Erro ao remover cliente por email (" + email + "): " + e.getMessage());
	    } finally {
	        em.close();
	    }
	}

}
