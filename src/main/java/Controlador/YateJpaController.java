/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Acuatico;
import Modelo.Yate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class YateJpaController implements Serializable {

    public YateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public YateJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Yate yate) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Acuatico acuaticoOrphanCheck = yate.getAcuatico();
        if (acuaticoOrphanCheck != null) {
            Yate oldYateOfAcuatico = acuaticoOrphanCheck.getYate();
            if (oldYateOfAcuatico != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Acuatico " + acuaticoOrphanCheck + " already has an item of type Yate whose acuatico column cannot be null. Please make another selection for the acuatico field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Acuatico acuatico = yate.getAcuatico();
            if (acuatico != null) {
                acuatico = em.getReference(acuatico.getClass(), acuatico.getIdVehiculo());
                yate.setAcuatico(acuatico);
            }
            em.persist(yate);
            if (acuatico != null) {
                acuatico.setYate(yate);
                acuatico = em.merge(acuatico);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findYate(yate.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Yate " + yate + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Yate yate) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Yate persistentYate = em.find(Yate.class, yate.getIdVehiculo());
            Acuatico acuaticoOld = persistentYate.getAcuatico();
            Acuatico acuaticoNew = yate.getAcuatico();
            List<String> illegalOrphanMessages = null;
            if (acuaticoNew != null && !acuaticoNew.equals(acuaticoOld)) {
                Yate oldYateOfAcuatico = acuaticoNew.getYate();
                if (oldYateOfAcuatico != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Acuatico " + acuaticoNew + " already has an item of type Yate whose acuatico column cannot be null. Please make another selection for the acuatico field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (acuaticoNew != null) {
                acuaticoNew = em.getReference(acuaticoNew.getClass(), acuaticoNew.getIdVehiculo());
                yate.setAcuatico(acuaticoNew);
            }
            yate = em.merge(yate);
            if (acuaticoOld != null && !acuaticoOld.equals(acuaticoNew)) {
                acuaticoOld.setYate(null);
                acuaticoOld = em.merge(acuaticoOld);
            }
            if (acuaticoNew != null && !acuaticoNew.equals(acuaticoOld)) {
                acuaticoNew.setYate(yate);
                acuaticoNew = em.merge(acuaticoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = yate.getIdVehiculo();
                if (findYate(id) == null) {
                    throw new NonexistentEntityException("The yate with id " + id + " no longer exists.");
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
            Yate yate;
            try {
                yate = em.getReference(Yate.class, id);
                yate.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The yate with id " + id + " no longer exists.", enfe);
            }
            Acuatico acuatico = yate.getAcuatico();
            if (acuatico != null) {
                acuatico.setYate(null);
                acuatico = em.merge(acuatico);
            }
            em.remove(yate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Yate> findYateEntities() {
        return findYateEntities(true, -1, -1);
    }

    public List<Yate> findYateEntities(int maxResults, int firstResult) {
        return findYateEntities(false, maxResults, firstResult);
    }

    private List<Yate> findYateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Yate.class));
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

    public Yate findYate(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Yate.class, id);
        } finally {
            em.close();
        }
    }

    public int getYateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Yate> rt = cq.from(Yate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
