/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Acuatico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Vehiculo;
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
public class AcuaticoJpaController implements Serializable {

    public AcuaticoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public AcuaticoJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Acuatico acuatico) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Vehiculo vehiculoOrphanCheck = acuatico.getVehiculo();
        if (vehiculoOrphanCheck != null) {
            Acuatico oldAcuaticoOfVehiculo = vehiculoOrphanCheck.getAcuatico();
            if (oldAcuaticoOfVehiculo != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Vehiculo " + vehiculoOrphanCheck + " already has an item of type Acuatico whose vehiculo column cannot be null. Please make another selection for the vehiculo field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vehiculo vehiculo = acuatico.getVehiculo();
            if (vehiculo != null) {
                vehiculo = em.getReference(vehiculo.getClass(), vehiculo.getIdVehiculo());
                acuatico.setVehiculo(vehiculo);
            }
            Yate yate = acuatico.getYate();
            if (yate != null) {
                yate = em.getReference(yate.getClass(), yate.getIdVehiculo());
                acuatico.setYate(yate);
            }
            em.persist(acuatico);
            if (vehiculo != null) {
                vehiculo.setAcuatico(acuatico);
                vehiculo = em.merge(vehiculo);
            }
            if (yate != null) {
                Acuatico oldAcuaticoOfYate = yate.getAcuatico();
                if (oldAcuaticoOfYate != null) {
                    oldAcuaticoOfYate.setYate(null);
                    oldAcuaticoOfYate = em.merge(oldAcuaticoOfYate);
                }
                yate.setAcuatico(acuatico);
                yate = em.merge(yate);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAcuatico(acuatico.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Acuatico " + acuatico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Acuatico acuatico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Acuatico persistentAcuatico = em.find(Acuatico.class, acuatico.getIdVehiculo());
            Vehiculo vehiculoOld = persistentAcuatico.getVehiculo();
            Vehiculo vehiculoNew = acuatico.getVehiculo();
            Yate yateOld = persistentAcuatico.getYate();
            Yate yateNew = acuatico.getYate();
            List<String> illegalOrphanMessages = null;
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                Acuatico oldAcuaticoOfVehiculo = vehiculoNew.getAcuatico();
                if (oldAcuaticoOfVehiculo != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Vehiculo " + vehiculoNew + " already has an item of type Acuatico whose vehiculo column cannot be null. Please make another selection for the vehiculo field.");
                }
            }
            if (yateOld != null && !yateOld.equals(yateNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Yate " + yateOld + " since its acuatico field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (vehiculoNew != null) {
                vehiculoNew = em.getReference(vehiculoNew.getClass(), vehiculoNew.getIdVehiculo());
                acuatico.setVehiculo(vehiculoNew);
            }
            if (yateNew != null) {
                yateNew = em.getReference(yateNew.getClass(), yateNew.getIdVehiculo());
                acuatico.setYate(yateNew);
            }
            acuatico = em.merge(acuatico);
            if (vehiculoOld != null && !vehiculoOld.equals(vehiculoNew)) {
                vehiculoOld.setAcuatico(null);
                vehiculoOld = em.merge(vehiculoOld);
            }
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                vehiculoNew.setAcuatico(acuatico);
                vehiculoNew = em.merge(vehiculoNew);
            }
            if (yateNew != null && !yateNew.equals(yateOld)) {
                Acuatico oldAcuaticoOfYate = yateNew.getAcuatico();
                if (oldAcuaticoOfYate != null) {
                    oldAcuaticoOfYate.setYate(null);
                    oldAcuaticoOfYate = em.merge(oldAcuaticoOfYate);
                }
                yateNew.setAcuatico(acuatico);
                yateNew = em.merge(yateNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = acuatico.getIdVehiculo();
                if (findAcuatico(id) == null) {
                    throw new NonexistentEntityException("The acuatico with id " + id + " no longer exists.");
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
            Acuatico acuatico;
            try {
                acuatico = em.getReference(Acuatico.class, id);
                acuatico.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The acuatico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Yate yateOrphanCheck = acuatico.getYate();
            if (yateOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Acuatico (" + acuatico + ") cannot be destroyed since the Yate " + yateOrphanCheck + " in its yate field has a non-nullable acuatico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Vehiculo vehiculo = acuatico.getVehiculo();
            if (vehiculo != null) {
                vehiculo.setAcuatico(null);
                vehiculo = em.merge(vehiculo);
            }
            em.remove(acuatico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Acuatico> findAcuaticoEntities() {
        return findAcuaticoEntities(true, -1, -1);
    }

    public List<Acuatico> findAcuaticoEntities(int maxResults, int firstResult) {
        return findAcuaticoEntities(false, maxResults, firstResult);
    }

    private List<Acuatico> findAcuaticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Acuatico.class));
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

    public Acuatico findAcuatico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Acuatico.class, id);
        } finally {
            em.close();
        }
    }

    public int getAcuaticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Acuatico> rt = cq.from(Acuatico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
