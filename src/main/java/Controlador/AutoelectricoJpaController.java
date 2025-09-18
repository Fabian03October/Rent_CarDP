/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Autoelectrico;
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
public class AutoelectricoJpaController implements Serializable {

    public AutoelectricoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public AutoelectricoJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autoelectrico autoelectrico) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Terrestre terrestreOrphanCheck = autoelectrico.getTerrestre();
        if (terrestreOrphanCheck != null) {
            Autoelectrico oldAutoelectricoOfTerrestre = terrestreOrphanCheck.getAutoelectrico();
            if (oldAutoelectricoOfTerrestre != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Terrestre " + terrestreOrphanCheck + " already has an item of type Autoelectrico whose terrestre column cannot be null. Please make another selection for the terrestre field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terrestre terrestre = autoelectrico.getTerrestre();
            if (terrestre != null) {
                terrestre = em.getReference(terrestre.getClass(), terrestre.getIdVehiculo());
                autoelectrico.setTerrestre(terrestre);
            }
            em.persist(autoelectrico);
            if (terrestre != null) {
                terrestre.setAutoelectrico(autoelectrico);
                terrestre = em.merge(terrestre);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAutoelectrico(autoelectrico.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Autoelectrico " + autoelectrico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autoelectrico autoelectrico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autoelectrico persistentAutoelectrico = em.find(Autoelectrico.class, autoelectrico.getIdVehiculo());
            Terrestre terrestreOld = persistentAutoelectrico.getTerrestre();
            Terrestre terrestreNew = autoelectrico.getTerrestre();
            List<String> illegalOrphanMessages = null;
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                Autoelectrico oldAutoelectricoOfTerrestre = terrestreNew.getAutoelectrico();
                if (oldAutoelectricoOfTerrestre != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Terrestre " + terrestreNew + " already has an item of type Autoelectrico whose terrestre column cannot be null. Please make another selection for the terrestre field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (terrestreNew != null) {
                terrestreNew = em.getReference(terrestreNew.getClass(), terrestreNew.getIdVehiculo());
                autoelectrico.setTerrestre(terrestreNew);
            }
            autoelectrico = em.merge(autoelectrico);
            if (terrestreOld != null && !terrestreOld.equals(terrestreNew)) {
                terrestreOld.setAutoelectrico(null);
                terrestreOld = em.merge(terrestreOld);
            }
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                terrestreNew.setAutoelectrico(autoelectrico);
                terrestreNew = em.merge(terrestreNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autoelectrico.getIdVehiculo();
                if (findAutoelectrico(id) == null) {
                    throw new NonexistentEntityException("The autoelectrico with id " + id + " no longer exists.");
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
            Autoelectrico autoelectrico;
            try {
                autoelectrico = em.getReference(Autoelectrico.class, id);
                autoelectrico.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autoelectrico with id " + id + " no longer exists.", enfe);
            }
            Terrestre terrestre = autoelectrico.getTerrestre();
            if (terrestre != null) {
                terrestre.setAutoelectrico(null);
                terrestre = em.merge(terrestre);
            }
            em.remove(autoelectrico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autoelectrico> findAutoelectricoEntities() {
        return findAutoelectricoEntities(true, -1, -1);
    }

    public List<Autoelectrico> findAutoelectricoEntities(int maxResults, int firstResult) {
        return findAutoelectricoEntities(false, maxResults, firstResult);
    }

    private List<Autoelectrico> findAutoelectricoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autoelectrico.class));
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

    public Autoelectrico findAutoelectrico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autoelectrico.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutoelectricoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autoelectrico> rt = cq.from(Autoelectrico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
