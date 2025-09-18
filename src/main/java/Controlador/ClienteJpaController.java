/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Modelo.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Reservacion;
import java.util.ArrayList;
import java.util.List;
import Modelo.Renta;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public ClienteJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getReservacionList() == null) {
            cliente.setReservacionList(new ArrayList<Reservacion>());
        }
        if (cliente.getRentaList() == null) {
            cliente.setRentaList(new ArrayList<Renta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservacion> attachedReservacionList = new ArrayList<Reservacion>();
            for (Reservacion reservacionListReservacionToAttach : cliente.getReservacionList()) {
                reservacionListReservacionToAttach = em.getReference(reservacionListReservacionToAttach.getClass(), reservacionListReservacionToAttach.getIdReservacion());
                attachedReservacionList.add(reservacionListReservacionToAttach);
            }
            cliente.setReservacionList(attachedReservacionList);
            List<Renta> attachedRentaList = new ArrayList<Renta>();
            for (Renta rentaListRentaToAttach : cliente.getRentaList()) {
                rentaListRentaToAttach = em.getReference(rentaListRentaToAttach.getClass(), rentaListRentaToAttach.getIdRenta());
                attachedRentaList.add(rentaListRentaToAttach);
            }
            cliente.setRentaList(attachedRentaList);
            em.persist(cliente);
            for (Reservacion reservacionListReservacion : cliente.getReservacionList()) {
                Cliente oldIdClienteOfReservacionListReservacion = reservacionListReservacion.getIdCliente();
                reservacionListReservacion.setIdCliente(cliente);
                reservacionListReservacion = em.merge(reservacionListReservacion);
                if (oldIdClienteOfReservacionListReservacion != null) {
                    oldIdClienteOfReservacionListReservacion.getReservacionList().remove(reservacionListReservacion);
                    oldIdClienteOfReservacionListReservacion = em.merge(oldIdClienteOfReservacionListReservacion);
                }
            }
            for (Renta rentaListRenta : cliente.getRentaList()) {
                Cliente oldIdClienteOfRentaListRenta = rentaListRenta.getIdCliente();
                rentaListRenta.setIdCliente(cliente);
                rentaListRenta = em.merge(rentaListRenta);
                if (oldIdClienteOfRentaListRenta != null) {
                    oldIdClienteOfRentaListRenta.getRentaList().remove(rentaListRenta);
                    oldIdClienteOfRentaListRenta = em.merge(oldIdClienteOfRentaListRenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Reservacion> reservacionListOld = persistentCliente.getReservacionList();
            List<Reservacion> reservacionListNew = cliente.getReservacionList();
            List<Renta> rentaListOld = persistentCliente.getRentaList();
            List<Renta> rentaListNew = cliente.getRentaList();
            List<String> illegalOrphanMessages = null;
            for (Renta rentaListOldRenta : rentaListOld) {
                if (!rentaListNew.contains(rentaListOldRenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Renta " + rentaListOldRenta + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reservacion> attachedReservacionListNew = new ArrayList<Reservacion>();
            for (Reservacion reservacionListNewReservacionToAttach : reservacionListNew) {
                reservacionListNewReservacionToAttach = em.getReference(reservacionListNewReservacionToAttach.getClass(), reservacionListNewReservacionToAttach.getIdReservacion());
                attachedReservacionListNew.add(reservacionListNewReservacionToAttach);
            }
            reservacionListNew = attachedReservacionListNew;
            cliente.setReservacionList(reservacionListNew);
            List<Renta> attachedRentaListNew = new ArrayList<Renta>();
            for (Renta rentaListNewRentaToAttach : rentaListNew) {
                rentaListNewRentaToAttach = em.getReference(rentaListNewRentaToAttach.getClass(), rentaListNewRentaToAttach.getIdRenta());
                attachedRentaListNew.add(rentaListNewRentaToAttach);
            }
            rentaListNew = attachedRentaListNew;
            cliente.setRentaList(rentaListNew);
            cliente = em.merge(cliente);
            for (Reservacion reservacionListOldReservacion : reservacionListOld) {
                if (!reservacionListNew.contains(reservacionListOldReservacion)) {
                    reservacionListOldReservacion.setIdCliente(null);
                    reservacionListOldReservacion = em.merge(reservacionListOldReservacion);
                }
            }
            for (Reservacion reservacionListNewReservacion : reservacionListNew) {
                if (!reservacionListOld.contains(reservacionListNewReservacion)) {
                    Cliente oldIdClienteOfReservacionListNewReservacion = reservacionListNewReservacion.getIdCliente();
                    reservacionListNewReservacion.setIdCliente(cliente);
                    reservacionListNewReservacion = em.merge(reservacionListNewReservacion);
                    if (oldIdClienteOfReservacionListNewReservacion != null && !oldIdClienteOfReservacionListNewReservacion.equals(cliente)) {
                        oldIdClienteOfReservacionListNewReservacion.getReservacionList().remove(reservacionListNewReservacion);
                        oldIdClienteOfReservacionListNewReservacion = em.merge(oldIdClienteOfReservacionListNewReservacion);
                    }
                }
            }
            for (Renta rentaListNewRenta : rentaListNew) {
                if (!rentaListOld.contains(rentaListNewRenta)) {
                    Cliente oldIdClienteOfRentaListNewRenta = rentaListNewRenta.getIdCliente();
                    rentaListNewRenta.setIdCliente(cliente);
                    rentaListNewRenta = em.merge(rentaListNewRenta);
                    if (oldIdClienteOfRentaListNewRenta != null && !oldIdClienteOfRentaListNewRenta.equals(cliente)) {
                        oldIdClienteOfRentaListNewRenta.getRentaList().remove(rentaListNewRenta);
                        oldIdClienteOfRentaListNewRenta = em.merge(oldIdClienteOfRentaListNewRenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Renta> rentaListOrphanCheck = cliente.getRentaList();
            for (Renta rentaListOrphanCheckRenta : rentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Renta " + rentaListOrphanCheckRenta + " in its rentaList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reservacion> reservacionList = cliente.getReservacionList();
            for (Reservacion reservacionListReservacion : reservacionList) {
                reservacionListReservacion.setIdCliente(null);
                reservacionListReservacion = em.merge(reservacionListReservacion);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
