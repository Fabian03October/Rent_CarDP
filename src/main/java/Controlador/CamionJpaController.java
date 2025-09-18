/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Camion;
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
public class CamionJpaController implements Serializable {

    public CamionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public CamionJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Camion camion) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Terrestre terrestreOrphanCheck = camion.getTerrestre();
        if (terrestreOrphanCheck != null) {
            Camion oldCamionOfTerrestre = terrestreOrphanCheck.getCamion();
            if (oldCamionOfTerrestre != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Terrestre " + terrestreOrphanCheck + " already has an item of type Camion whose terrestre column cannot be null. Please make another selection for the terrestre field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terrestre terrestre = camion.getTerrestre();
            if (terrestre != null) {
                terrestre = em.getReference(terrestre.getClass(), terrestre.getIdVehiculo());
                camion.setTerrestre(terrestre);
            }
            em.persist(camion);
            if (terrestre != null) {
                terrestre.setCamion(camion);
                terrestre = em.merge(terrestre);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCamion(camion.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Camion " + camion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Camion camion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion persistentCamion = em.find(Camion.class, camion.getIdVehiculo());
            Terrestre terrestreOld = persistentCamion.getTerrestre();
            Terrestre terrestreNew = camion.getTerrestre();
            List<String> illegalOrphanMessages = null;
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                Camion oldCamionOfTerrestre = terrestreNew.getCamion();
                if (oldCamionOfTerrestre != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Terrestre " + terrestreNew + " already has an item of type Camion whose terrestre column cannot be null. Please make another selection for the terrestre field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (terrestreNew != null) {
                terrestreNew = em.getReference(terrestreNew.getClass(), terrestreNew.getIdVehiculo());
                camion.setTerrestre(terrestreNew);
            }
            camion = em.merge(camion);
            if (terrestreOld != null && !terrestreOld.equals(terrestreNew)) {
                terrestreOld.setCamion(null);
                terrestreOld = em.merge(terrestreOld);
            }
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                terrestreNew.setCamion(camion);
                terrestreNew = em.merge(terrestreNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = camion.getIdVehiculo();
                if (findCamion(id) == null) {
                    throw new NonexistentEntityException("The camion with id " + id + " no longer exists.");
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
            Camion camion;
            try {
                camion = em.getReference(Camion.class, id);
                camion.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The camion with id " + id + " no longer exists.", enfe);
            }
            Terrestre terrestre = camion.getTerrestre();
            if (terrestre != null) {
                terrestre.setCamion(null);
                terrestre = em.merge(terrestre);
            }
            em.remove(camion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Camion> findCamionEntities() {
        return findCamionEntities(true, -1, -1);
    }

    public List<Camion> findCamionEntities(int maxResults, int firstResult) {
        return findCamionEntities(false, maxResults, firstResult);
    }

    private List<Camion> findCamionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Camion.class));
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

    public Camion findCamion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Camion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCamionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Camion> rt = cq.from(Camion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
