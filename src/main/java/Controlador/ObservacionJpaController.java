/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Modelo.Observacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Renta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class ObservacionJpaController implements Serializable {

    public ObservacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public ObservacionJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Observacion observacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Renta idRenta = observacion.getIdRenta();
            if (idRenta != null) {
                idRenta = em.getReference(idRenta.getClass(), idRenta.getIdRenta());
                observacion.setIdRenta(idRenta);
            }
            em.persist(observacion);
            if (idRenta != null) {
                idRenta.getObservacionList().add(observacion);
                idRenta = em.merge(idRenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Observacion observacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Observacion persistentObservacion = em.find(Observacion.class, observacion.getIdObservacion());
            Renta idRentaOld = persistentObservacion.getIdRenta();
            Renta idRentaNew = observacion.getIdRenta();
            if (idRentaNew != null) {
                idRentaNew = em.getReference(idRentaNew.getClass(), idRentaNew.getIdRenta());
                observacion.setIdRenta(idRentaNew);
            }
            observacion = em.merge(observacion);
            if (idRentaOld != null && !idRentaOld.equals(idRentaNew)) {
                idRentaOld.getObservacionList().remove(observacion);
                idRentaOld = em.merge(idRentaOld);
            }
            if (idRentaNew != null && !idRentaNew.equals(idRentaOld)) {
                idRentaNew.getObservacionList().add(observacion);
                idRentaNew = em.merge(idRentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = observacion.getIdObservacion();
                if (findObservacion(id) == null) {
                    throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.");
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
            Observacion observacion;
            try {
                observacion = em.getReference(Observacion.class, id);
                observacion.getIdObservacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The observacion with id " + id + " no longer exists.", enfe);
            }
            Renta idRenta = observacion.getIdRenta();
            if (idRenta != null) {
                idRenta.getObservacionList().remove(observacion);
                idRenta = em.merge(idRenta);
            }
            em.remove(observacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Observacion> findObservacionEntities() {
        return findObservacionEntities(true, -1, -1);
    }

    public List<Observacion> findObservacionEntities(int maxResults, int firstResult) {
        return findObservacionEntities(false, maxResults, firstResult);
    }

    private List<Observacion> findObservacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Observacion.class));
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

    public Observacion findObservacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Observacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getObservacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Observacion> rt = cq.from(Observacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
