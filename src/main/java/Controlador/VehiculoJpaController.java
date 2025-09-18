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
import Modelo.Acuatico;
import Modelo.Terrestre;
import Modelo.Reservacion;
import java.util.ArrayList;
import java.util.List;
import Modelo.Renta;
import Modelo.Vehiculo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author es982
 */
public class VehiculoJpaController implements Serializable {

    public VehiculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public VehiculoJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vehiculo vehiculo) {
        if (vehiculo.getReservacionList() == null) {
            vehiculo.setReservacionList(new ArrayList<Reservacion>());
        }
        if (vehiculo.getRentaList() == null) {
            vehiculo.setRentaList(new ArrayList<Renta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Acuatico acuatico = vehiculo.getAcuatico();
            if (acuatico != null) {
                acuatico = em.getReference(acuatico.getClass(), acuatico.getIdVehiculo());
                vehiculo.setAcuatico(acuatico);
            }
            Terrestre terrestre = vehiculo.getTerrestre();
            if (terrestre != null) {
                terrestre = em.getReference(terrestre.getClass(), terrestre.getIdVehiculo());
                vehiculo.setTerrestre(terrestre);
            }
            List<Reservacion> attachedReservacionList = new ArrayList<Reservacion>();
            for (Reservacion reservacionListReservacionToAttach : vehiculo.getReservacionList()) {
                reservacionListReservacionToAttach = em.getReference(reservacionListReservacionToAttach.getClass(), reservacionListReservacionToAttach.getIdReservacion());
                attachedReservacionList.add(reservacionListReservacionToAttach);
            }
            vehiculo.setReservacionList(attachedReservacionList);
            List<Renta> attachedRentaList = new ArrayList<Renta>();
            for (Renta rentaListRentaToAttach : vehiculo.getRentaList()) {
                rentaListRentaToAttach = em.getReference(rentaListRentaToAttach.getClass(), rentaListRentaToAttach.getIdRenta());
                attachedRentaList.add(rentaListRentaToAttach);
            }
            vehiculo.setRentaList(attachedRentaList);
            em.persist(vehiculo);
            if (acuatico != null) {
                Vehiculo oldVehiculoOfAcuatico = acuatico.getVehiculo();
                if (oldVehiculoOfAcuatico != null) {
                    oldVehiculoOfAcuatico.setAcuatico(null);
                    oldVehiculoOfAcuatico = em.merge(oldVehiculoOfAcuatico);
                }
                acuatico.setVehiculo(vehiculo);
                acuatico = em.merge(acuatico);
            }
            if (terrestre != null) {
                Vehiculo oldVehiculoOfTerrestre = terrestre.getVehiculo();
                if (oldVehiculoOfTerrestre != null) {
                    oldVehiculoOfTerrestre.setTerrestre(null);
                    oldVehiculoOfTerrestre = em.merge(oldVehiculoOfTerrestre);
                }
                terrestre.setVehiculo(vehiculo);
                terrestre = em.merge(terrestre);
            }
            for (Reservacion reservacionListReservacion : vehiculo.getReservacionList()) {
                Vehiculo oldIdVehiculoOfReservacionListReservacion = reservacionListReservacion.getIdVehiculo();
                reservacionListReservacion.setIdVehiculo(vehiculo);
                reservacionListReservacion = em.merge(reservacionListReservacion);
                if (oldIdVehiculoOfReservacionListReservacion != null) {
                    oldIdVehiculoOfReservacionListReservacion.getReservacionList().remove(reservacionListReservacion);
                    oldIdVehiculoOfReservacionListReservacion = em.merge(oldIdVehiculoOfReservacionListReservacion);
                }
            }
            for (Renta rentaListRenta : vehiculo.getRentaList()) {
                Vehiculo oldIdVehiculoOfRentaListRenta = rentaListRenta.getIdVehiculo();
                rentaListRenta.setIdVehiculo(vehiculo);
                rentaListRenta = em.merge(rentaListRenta);
                if (oldIdVehiculoOfRentaListRenta != null) {
                    oldIdVehiculoOfRentaListRenta.getRentaList().remove(rentaListRenta);
                    oldIdVehiculoOfRentaListRenta = em.merge(oldIdVehiculoOfRentaListRenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vehiculo vehiculo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo persistentVehiculo = em.find(Vehiculo.class, vehiculo.getIdVehiculo());
            Acuatico acuaticoOld = persistentVehiculo.getAcuatico();
            Acuatico acuaticoNew = vehiculo.getAcuatico();
            Terrestre terrestreOld = persistentVehiculo.getTerrestre();
            Terrestre terrestreNew = vehiculo.getTerrestre();
            List<Reservacion> reservacionListOld = persistentVehiculo.getReservacionList();
            List<Reservacion> reservacionListNew = vehiculo.getReservacionList();
            List<Renta> rentaListOld = persistentVehiculo.getRentaList();
            List<Renta> rentaListNew = vehiculo.getRentaList();
            List<String> illegalOrphanMessages = null;
            if (acuaticoOld != null && !acuaticoOld.equals(acuaticoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Acuatico " + acuaticoOld + " since its vehiculo field is not nullable.");
            }
            if (terrestreOld != null && !terrestreOld.equals(terrestreNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Terrestre " + terrestreOld + " since its vehiculo field is not nullable.");
            }
            for (Reservacion reservacionListOldReservacion : reservacionListOld) {
                if (!reservacionListNew.contains(reservacionListOldReservacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservacion " + reservacionListOldReservacion + " since its idVehiculo field is not nullable.");
                }
            }
            for (Renta rentaListOldRenta : rentaListOld) {
                if (!rentaListNew.contains(rentaListOldRenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Renta " + rentaListOldRenta + " since its idVehiculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (acuaticoNew != null) {
                acuaticoNew = em.getReference(acuaticoNew.getClass(), acuaticoNew.getIdVehiculo());
                vehiculo.setAcuatico(acuaticoNew);
            }
            if (terrestreNew != null) {
                terrestreNew = em.getReference(terrestreNew.getClass(), terrestreNew.getIdVehiculo());
                vehiculo.setTerrestre(terrestreNew);
            }
            List<Reservacion> attachedReservacionListNew = new ArrayList<Reservacion>();
            for (Reservacion reservacionListNewReservacionToAttach : reservacionListNew) {
                reservacionListNewReservacionToAttach = em.getReference(reservacionListNewReservacionToAttach.getClass(), reservacionListNewReservacionToAttach.getIdReservacion());
                attachedReservacionListNew.add(reservacionListNewReservacionToAttach);
            }
            reservacionListNew = attachedReservacionListNew;
            vehiculo.setReservacionList(reservacionListNew);
            List<Renta> attachedRentaListNew = new ArrayList<Renta>();
            for (Renta rentaListNewRentaToAttach : rentaListNew) {
                rentaListNewRentaToAttach = em.getReference(rentaListNewRentaToAttach.getClass(), rentaListNewRentaToAttach.getIdRenta());
                attachedRentaListNew.add(rentaListNewRentaToAttach);
            }
            rentaListNew = attachedRentaListNew;
            vehiculo.setRentaList(rentaListNew);
            vehiculo = em.merge(vehiculo);
            if (acuaticoNew != null && !acuaticoNew.equals(acuaticoOld)) {
                Vehiculo oldVehiculoOfAcuatico = acuaticoNew.getVehiculo();
                if (oldVehiculoOfAcuatico != null) {
                    oldVehiculoOfAcuatico.setAcuatico(null);
                    oldVehiculoOfAcuatico = em.merge(oldVehiculoOfAcuatico);
                }
                acuaticoNew.setVehiculo(vehiculo);
                acuaticoNew = em.merge(acuaticoNew);
            }
            if (terrestreNew != null && !terrestreNew.equals(terrestreOld)) {
                Vehiculo oldVehiculoOfTerrestre = terrestreNew.getVehiculo();
                if (oldVehiculoOfTerrestre != null) {
                    oldVehiculoOfTerrestre.setTerrestre(null);
                    oldVehiculoOfTerrestre = em.merge(oldVehiculoOfTerrestre);
                }
                terrestreNew.setVehiculo(vehiculo);
                terrestreNew = em.merge(terrestreNew);
            }
            for (Reservacion reservacionListNewReservacion : reservacionListNew) {
                if (!reservacionListOld.contains(reservacionListNewReservacion)) {
                    Vehiculo oldIdVehiculoOfReservacionListNewReservacion = reservacionListNewReservacion.getIdVehiculo();
                    reservacionListNewReservacion.setIdVehiculo(vehiculo);
                    reservacionListNewReservacion = em.merge(reservacionListNewReservacion);
                    if (oldIdVehiculoOfReservacionListNewReservacion != null && !oldIdVehiculoOfReservacionListNewReservacion.equals(vehiculo)) {
                        oldIdVehiculoOfReservacionListNewReservacion.getReservacionList().remove(reservacionListNewReservacion);
                        oldIdVehiculoOfReservacionListNewReservacion = em.merge(oldIdVehiculoOfReservacionListNewReservacion);
                    }
                }
            }
            for (Renta rentaListNewRenta : rentaListNew) {
                if (!rentaListOld.contains(rentaListNewRenta)) {
                    Vehiculo oldIdVehiculoOfRentaListNewRenta = rentaListNewRenta.getIdVehiculo();
                    rentaListNewRenta.setIdVehiculo(vehiculo);
                    rentaListNewRenta = em.merge(rentaListNewRenta);
                    if (oldIdVehiculoOfRentaListNewRenta != null && !oldIdVehiculoOfRentaListNewRenta.equals(vehiculo)) {
                        oldIdVehiculoOfRentaListNewRenta.getRentaList().remove(rentaListNewRenta);
                        oldIdVehiculoOfRentaListNewRenta = em.merge(oldIdVehiculoOfRentaListNewRenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vehiculo.getIdVehiculo();
                if (findVehiculo(id) == null) {
                    throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.");
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
            Vehiculo vehiculo;
            try {
                vehiculo = em.getReference(Vehiculo.class, id);
                vehiculo.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vehiculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Acuatico acuaticoOrphanCheck = vehiculo.getAcuatico();
            if (acuaticoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Acuatico " + acuaticoOrphanCheck + " in its acuatico field has a non-nullable vehiculo field.");
            }
            Terrestre terrestreOrphanCheck = vehiculo.getTerrestre();
            if (terrestreOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Terrestre " + terrestreOrphanCheck + " in its terrestre field has a non-nullable vehiculo field.");
            }
            List<Reservacion> reservacionListOrphanCheck = vehiculo.getReservacionList();
            for (Reservacion reservacionListOrphanCheckReservacion : reservacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Reservacion " + reservacionListOrphanCheckReservacion + " in its reservacionList field has a non-nullable idVehiculo field.");
            }
            List<Renta> rentaListOrphanCheck = vehiculo.getRentaList();
            for (Renta rentaListOrphanCheckRenta : rentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vehiculo (" + vehiculo + ") cannot be destroyed since the Renta " + rentaListOrphanCheckRenta + " in its rentaList field has a non-nullable idVehiculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vehiculo> findVehiculoEntities() {
        return findVehiculoEntities(true, -1, -1);
    }

    public List<Vehiculo> findVehiculoEntities(int maxResults, int firstResult) {
        return findVehiculoEntities(false, maxResults, firstResult);
    }

    private List<Vehiculo> findVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vehiculo.class));
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

    public Vehiculo findVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vehiculo> rt = cq.from(Vehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
