/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Autocombustion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Terrestre;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class AutocombustionJpaController implements Serializable {

    public AutocombustionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public AutocombustionJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autocombustion autocombustion) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Terrestre terrestreOrphanCheck = autocombustion.getTerrestre();
        if (terrestreOrphanCheck != null) {
            Autocombustion oldAutocombustionOfTerrestre = terrestreOrphanCheck.getAutocombustion();
            if (oldAutocombustionOfTerrestre != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Terrestre " + terrestreOrphanCheck + " already has an item of type Autocombustion whose terrestre column cannot be null. Please make another selection for the terrestre field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terrestre terrestre = autocombustion.getTerrestre();
            if (terrestre != null) {
                terrestre = em.getReference(terrestre.getClass(), terrestre.getIdVehiculo());
                autocombustion.setTerrestre(terrestre);
            }
            em.persist(autocombustion);
            if (terrestre != null) {
                terrestre.setAutocombustion(autocombustion);
                terrestre = em.merge(terrestre);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAutocombustion(autocombustion.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Autocombustion " + autocombustion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autocombustion autocombustion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autocombustion persistentAutocombustion = em.find(Autocombustion.class, autocombustion.getIdVehiculo());
            Terrestre terrestreOld = persistentAutocombustion.getTerrestre();
            Terrestre terrestreNew = autocombustion.getTerrestre();
            List<String> illegalOrphanMessages = null;
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                Autocombustion oldAutocombustionOfTerrestre = terrestreNew.getAutocombustion();
                if (oldAutocombustionOfTerrestre != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Terrestre " + terrestreNew + " already has an item of type Autocombustion whose terrestre column cannot be null. Please make another selection for the terrestre field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (terrestreNew != null) {
                terrestreNew = em.getReference(terrestreNew.getClass(), terrestreNew.getIdVehiculo());
                autocombustion.setTerrestre(terrestreNew);
            }
            autocombustion = em.merge(autocombustion);
            if (terrestreOld != null && !terrestreOld.equals(terrestreNew)) {
                terrestreOld.setAutocombustion(null);
                terrestreOld = em.merge(terrestreOld);
            }
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                terrestreNew.setAutocombustion(autocombustion);
                terrestreNew = em.merge(terrestreNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autocombustion.getIdVehiculo();
                if (findAutocombustion(id) == null) {
                    throw new NonexistentEntityException("The autocombustion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autocombustion autocombustion;
            try {
                autocombustion = em.getReference(Autocombustion.class, id);
                autocombustion.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autocombustion with id " + id + " no longer exists.", enfe);
            }
            Terrestre terrestre = autocombustion.getTerrestre();
            if (terrestre != null) {
                terrestre.setAutocombustion(null);
                terrestre = em.merge(terrestre);
            }
            em.remove(autocombustion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autocombustion> findAutocombustionEntities() {
        return findAutocombustionEntities(true, -1, -1);
    }

    public List<Autocombustion> findAutocombustionEntities(int maxResults, int firstResult) {
        return findAutocombustionEntities(false, maxResults, firstResult);
    }

    private List<Autocombustion> findAutocombustionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autocombustion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Autocombustion findAutocombustion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autocombustion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutocombustionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autocombustion> rt = cq.from(Autocombustion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
