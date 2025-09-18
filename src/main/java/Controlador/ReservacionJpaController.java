/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Cliente;
import Modelo.Reservacion;
import Modelo.Vehiculo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class ReservacionJpaController implements Serializable {

    public ReservacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public ReservacionJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservacion reservacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = reservacion.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                reservacion.setIdCliente(idCliente);
            }
            Vehiculo idVehiculo = reservacion.getIdVehiculo();
            if (idVehiculo != null) {
                idVehiculo = em.getReference(idVehiculo.getClass(), idVehiculo.getIdVehiculo());
                reservacion.setIdVehiculo(idVehiculo);
            }
            em.persist(reservacion);
            if (idCliente != null) {
                idCliente.getReservacionList().add(reservacion);
                idCliente = em.merge(idCliente);
            }
            if (idVehiculo != null) {
                idVehiculo.getReservacionList().add(reservacion);
                idVehiculo = em.merge(idVehiculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservacion reservacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservacion persistentReservacion = em.find(Reservacion.class, reservacion.getIdReservacion());
            Cliente idClienteOld = persistentReservacion.getIdCliente();
            Cliente idClienteNew = reservacion.getIdCliente();
            Vehiculo idVehiculoOld = persistentReservacion.getIdVehiculo();
            Vehiculo idVehiculoNew = reservacion.getIdVehiculo();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                reservacion.setIdCliente(idClienteNew);
            }
            if (idVehiculoNew != null) {
                idVehiculoNew = em.getReference(idVehiculoNew.getClass(), idVehiculoNew.getIdVehiculo());
                reservacion.setIdVehiculo(idVehiculoNew);
            }
            reservacion = em.merge(reservacion);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getReservacionList().remove(reservacion);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getReservacionList().add(reservacion);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idVehiculoOld != null && !idVehiculoOld.equals(idVehiculoNew)) {
                idVehiculoOld.getReservacionList().remove(reservacion);
                idVehiculoOld = em.merge(idVehiculoOld);
            }
            if (idVehiculoNew != null && !idVehiculoNew.equals(idVehiculoOld)) {
                idVehiculoNew.getReservacionList().add(reservacion);
                idVehiculoNew = em.merge(idVehiculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reservacion.getIdReservacion();
                if (findReservacion(id) == null) {
                    throw new NonexistentEntityException("The reservacion with id " + id + " no longer exists.");
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
            Reservacion reservacion;
            try {
                reservacion = em.getReference(Reservacion.class, id);
                reservacion.getIdReservacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservacion with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = reservacion.getIdCliente();
            if (idCliente != null) {
                idCliente.getReservacionList().remove(reservacion);
                idCliente = em.merge(idCliente);
            }
            Vehiculo idVehiculo = reservacion.getIdVehiculo();
            if (idVehiculo != null) {
                idVehiculo.getReservacionList().remove(reservacion);
                idVehiculo = em.merge(idVehiculo);
            }
            em.remove(reservacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservacion> findReservacionEntities() {
        return findReservacionEntities(true, -1, -1);
    }

    public List<Reservacion> findReservacionEntities(int maxResults, int firstResult) {
        return findReservacionEntities(false, maxResults, firstResult);
    }

    private List<Reservacion> findReservacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservacion.class));
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

    public Reservacion findReservacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservacion> rt = cq.from(Reservacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
