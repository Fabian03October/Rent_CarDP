/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Cliente;
import Modelo.Vehiculo;
import Modelo.Factura;
import java.util.ArrayList;
import java.util.List;
import Modelo.Observacion;
import Modelo.Renta;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class RentaJpaController implements Serializable {

    public RentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public RentaJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Renta renta) {
        if (renta.getFacturaList() == null) {
            renta.setFacturaList(new ArrayList<Factura>());
        }
        if (renta.getObservacionList() == null) {
            renta.setObservacionList(new ArrayList<Observacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = renta.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                renta.setIdCliente(idCliente);
            }
            Vehiculo idVehiculo = renta.getIdVehiculo();
            if (idVehiculo != null) {
                idVehiculo = em.getReference(idVehiculo.getClass(), idVehiculo.getIdVehiculo());
                renta.setIdVehiculo(idVehiculo);
            }
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : renta.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdFactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            renta.setFacturaList(attachedFacturaList);
            List<Observacion> attachedObservacionList = new ArrayList<Observacion>();
            for (Observacion observacionListObservacionToAttach : renta.getObservacionList()) {
                observacionListObservacionToAttach = em.getReference(observacionListObservacionToAttach.getClass(), observacionListObservacionToAttach.getIdObservacion());
                attachedObservacionList.add(observacionListObservacionToAttach);
            }
            renta.setObservacionList(attachedObservacionList);
            em.persist(renta);
            if (idCliente != null) {
                idCliente.getRentaList().add(renta);
                idCliente = em.merge(idCliente);
            }
            if (idVehiculo != null) {
                idVehiculo.getRentaList().add(renta);
                idVehiculo = em.merge(idVehiculo);
            }
            for (Factura facturaListFactura : renta.getFacturaList()) {
                Renta oldIdRentaOfFacturaListFactura = facturaListFactura.getIdRenta();
                facturaListFactura.setIdRenta(renta);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdRentaOfFacturaListFactura != null) {
                    oldIdRentaOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdRentaOfFacturaListFactura = em.merge(oldIdRentaOfFacturaListFactura);
                }
            }
            for (Observacion observacionListObservacion : renta.getObservacionList()) {
                Renta oldIdRentaOfObservacionListObservacion = observacionListObservacion.getIdRenta();
                observacionListObservacion.setIdRenta(renta);
                observacionListObservacion = em.merge(observacionListObservacion);
                if (oldIdRentaOfObservacionListObservacion != null) {
                    oldIdRentaOfObservacionListObservacion.getObservacionList().remove(observacionListObservacion);
                    oldIdRentaOfObservacionListObservacion = em.merge(oldIdRentaOfObservacionListObservacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Renta renta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Renta persistentRenta = em.find(Renta.class, renta.getIdRenta());
            Cliente idClienteOld = persistentRenta.getIdCliente();
            Cliente idClienteNew = renta.getIdCliente();
            Vehiculo idVehiculoOld = persistentRenta.getIdVehiculo();
            Vehiculo idVehiculoNew = renta.getIdVehiculo();
            List<Factura> facturaListOld = persistentRenta.getFacturaList();
            List<Factura> facturaListNew = renta.getFacturaList();
            List<Observacion> observacionListOld = persistentRenta.getObservacionList();
            List<Observacion> observacionListNew = renta.getObservacionList();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idRenta field is not nullable.");
                }
            }
            for (Observacion observacionListOldObservacion : observacionListOld) {
                if (!observacionListNew.contains(observacionListOldObservacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Observacion " + observacionListOldObservacion + " since its idRenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                renta.setIdCliente(idClienteNew);
            }
            if (idVehiculoNew != null) {
                idVehiculoNew = em.getReference(idVehiculoNew.getClass(), idVehiculoNew.getIdVehiculo());
                renta.setIdVehiculo(idVehiculoNew);
            }
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdFactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            renta.setFacturaList(facturaListNew);
            List<Observacion> attachedObservacionListNew = new ArrayList<Observacion>();
            for (Observacion observacionListNewObservacionToAttach : observacionListNew) {
                observacionListNewObservacionToAttach = em.getReference(observacionListNewObservacionToAttach.getClass(), observacionListNewObservacionToAttach.getIdObservacion());
                attachedObservacionListNew.add(observacionListNewObservacionToAttach);
            }
            observacionListNew = attachedObservacionListNew;
            renta.setObservacionList(observacionListNew);
            renta = em.merge(renta);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getRentaList().remove(renta);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getRentaList().add(renta);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idVehiculoOld != null && !idVehiculoOld.equals(idVehiculoNew)) {
                idVehiculoOld.getRentaList().remove(renta);
                idVehiculoOld = em.merge(idVehiculoOld);
            }
            if (idVehiculoNew != null && !idVehiculoNew.equals(idVehiculoOld)) {
                idVehiculoNew.getRentaList().add(renta);
                idVehiculoNew = em.merge(idVehiculoNew);
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Renta oldIdRentaOfFacturaListNewFactura = facturaListNewFactura.getIdRenta();
                    facturaListNewFactura.setIdRenta(renta);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdRentaOfFacturaListNewFactura != null && !oldIdRentaOfFacturaListNewFactura.equals(renta)) {
                        oldIdRentaOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdRentaOfFacturaListNewFactura = em.merge(oldIdRentaOfFacturaListNewFactura);
                    }
                }
            }
            for (Observacion observacionListNewObservacion : observacionListNew) {
                if (!observacionListOld.contains(observacionListNewObservacion)) {
                    Renta oldIdRentaOfObservacionListNewObservacion = observacionListNewObservacion.getIdRenta();
                    observacionListNewObservacion.setIdRenta(renta);
                    observacionListNewObservacion = em.merge(observacionListNewObservacion);
                    if (oldIdRentaOfObservacionListNewObservacion != null && !oldIdRentaOfObservacionListNewObservacion.equals(renta)) {
                        oldIdRentaOfObservacionListNewObservacion.getObservacionList().remove(observacionListNewObservacion);
                        oldIdRentaOfObservacionListNewObservacion = em.merge(oldIdRentaOfObservacionListNewObservacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = renta.getIdRenta();
                if (findRenta(id) == null) {
                    throw new NonexistentEntityException("The renta with id " + id + " no longer exists.");
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
            Renta renta;
            try {
                renta = em.getReference(Renta.class, id);
                renta.getIdRenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The renta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Factura> facturaListOrphanCheck = renta.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Renta (" + renta + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idRenta field.");
            }
            List<Observacion> observacionListOrphanCheck = renta.getObservacionList();
            for (Observacion observacionListOrphanCheckObservacion : observacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Renta (" + renta + ") cannot be destroyed since the Observacion " + observacionListOrphanCheckObservacion + " in its observacionList field has a non-nullable idRenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = renta.getIdCliente();
            if (idCliente != null) {
                idCliente.getRentaList().remove(renta);
                idCliente = em.merge(idCliente);
            }
            Vehiculo idVehiculo = renta.getIdVehiculo();
            if (idVehiculo != null) {
                idVehiculo.getRentaList().remove(renta);
                idVehiculo = em.merge(idVehiculo);
            }
            em.remove(renta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Renta> findRentaEntities() {
        return findRentaEntities(true, -1, -1);
    }

    public List<Renta> findRentaEntities(int maxResults, int firstResult) {
        return findRentaEntities(false, maxResults, firstResult);
    }

    private List<Renta> findRentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Renta.class));
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

    public Renta findRenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Renta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Renta> rt = cq.from(Renta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
