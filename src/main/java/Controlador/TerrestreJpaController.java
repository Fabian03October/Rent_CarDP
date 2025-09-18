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
import Modelo.Camion;
import Modelo.Vehiculo;
import Modelo.Autoelectrico;
import Modelo.Autocombustion;
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
public class TerrestreJpaController implements Serializable {

    public TerrestreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public TerrestreJpaController(){
        emf=Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Terrestre terrestre) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Vehiculo vehiculoOrphanCheck = terrestre.getVehiculo();
        if (vehiculoOrphanCheck != null) {
            Terrestre oldTerrestreOfVehiculo = vehiculoOrphanCheck.getTerrestre();
            if (oldTerrestreOfVehiculo != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Vehiculo " + vehiculoOrphanCheck + " already has an item of type Terrestre whose vehiculo column cannot be null. Please make another selection for the vehiculo field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion camion = terrestre.getCamion();
            if (camion != null) {
                camion = em.getReference(camion.getClass(), camion.getIdVehiculo());
                terrestre.setCamion(camion);
            }
            Vehiculo vehiculo = terrestre.getVehiculo();
            if (vehiculo != null) {
                vehiculo = em.getReference(vehiculo.getClass(), vehiculo.getIdVehiculo());
                terrestre.setVehiculo(vehiculo);
            }
            Autoelectrico autoelectrico = terrestre.getAutoelectrico();
            if (autoelectrico != null) {
                autoelectrico = em.getReference(autoelectrico.getClass(), autoelectrico.getIdVehiculo());
                terrestre.setAutoelectrico(autoelectrico);
            }
            Autocombustion autocombustion = terrestre.getAutocombustion();
            if (autocombustion != null) {
                autocombustion = em.getReference(autocombustion.getClass(), autocombustion.getIdVehiculo());
                terrestre.setAutocombustion(autocombustion);
            }
            em.persist(terrestre);
            if (camion != null) {
                Terrestre oldTerrestreOfCamion = camion.getTerrestre();
                if (oldTerrestreOfCamion != null) {
                    oldTerrestreOfCamion.setCamion(null);
                    oldTerrestreOfCamion = em.merge(oldTerrestreOfCamion);
                }
                camion.setTerrestre(terrestre);
                camion = em.merge(camion);
            }
            if (vehiculo != null) {
                vehiculo.setTerrestre(terrestre);
                vehiculo = em.merge(vehiculo);
            }
            if (autoelectrico != null) {
                Terrestre oldTerrestreOfAutoelectrico = autoelectrico.getTerrestre();
                if (oldTerrestreOfAutoelectrico != null) {
                    oldTerrestreOfAutoelectrico.setAutoelectrico(null);
                    oldTerrestreOfAutoelectrico = em.merge(oldTerrestreOfAutoelectrico);
                }
                autoelectrico.setTerrestre(terrestre);
                autoelectrico = em.merge(autoelectrico);
            }
            if (autocombustion != null) {
                Terrestre oldTerrestreOfAutocombustion = autocombustion.getTerrestre();
                if (oldTerrestreOfAutocombustion != null) {
                    oldTerrestreOfAutocombustion.setAutocombustion(null);
                    oldTerrestreOfAutocombustion = em.merge(oldTerrestreOfAutocombustion);
                }
                autocombustion.setTerrestre(terrestre);
                autocombustion = em.merge(autocombustion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTerrestre(terrestre.getIdVehiculo()) != null) {
                throw new PreexistingEntityException("Terrestre " + terrestre + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Terrestre terrestre) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terrestre persistentTerrestre = em.find(Terrestre.class, terrestre.getIdVehiculo());
            Camion camionOld = persistentTerrestre.getCamion();
            Camion camionNew = terrestre.getCamion();
            Vehiculo vehiculoOld = persistentTerrestre.getVehiculo();
            Vehiculo vehiculoNew = terrestre.getVehiculo();
            Autoelectrico autoelectricoOld = persistentTerrestre.getAutoelectrico();
            Autoelectrico autoelectricoNew = terrestre.getAutoelectrico();
            Autocombustion autocombustionOld = persistentTerrestre.getAutocombustion();
            Autocombustion autocombustionNew = terrestre.getAutocombustion();
            List<String> illegalOrphanMessages = null;
            if (camionOld != null && !camionOld.equals(camionNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Camion " + camionOld + " since its terrestre field is not nullable.");
            }
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                Terrestre oldTerrestreOfVehiculo = vehiculoNew.getTerrestre();
                if (oldTerrestreOfVehiculo != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Vehiculo " + vehiculoNew + " already has an item of type Terrestre whose vehiculo column cannot be null. Please make another selection for the vehiculo field.");
                }
            }
            if (autoelectricoOld != null && !autoelectricoOld.equals(autoelectricoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Autoelectrico " + autoelectricoOld + " since its terrestre field is not nullable.");
            }
            if (autocombustionOld != null && !autocombustionOld.equals(autocombustionNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Autocombustion " + autocombustionOld + " since its terrestre field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (camionNew != null) {
                camionNew = em.getReference(camionNew.getClass(), camionNew.getIdVehiculo());
                terrestre.setCamion(camionNew);
            }
            if (vehiculoNew != null) {
                vehiculoNew = em.getReference(vehiculoNew.getClass(), vehiculoNew.getIdVehiculo());
                terrestre.setVehiculo(vehiculoNew);
            }
            if (autoelectricoNew != null) {
                autoelectricoNew = em.getReference(autoelectricoNew.getClass(), autoelectricoNew.getIdVehiculo());
                terrestre.setAutoelectrico(autoelectricoNew);
            }
            if (autocombustionNew != null) {
                autocombustionNew = em.getReference(autocombustionNew.getClass(), autocombustionNew.getIdVehiculo());
                terrestre.setAutocombustion(autocombustionNew);
            }
            terrestre = em.merge(terrestre);
            if (camionNew != null && !camionNew.equals(camionOld)) {
                Terrestre oldTerrestreOfCamion = camionNew.getTerrestre();
                if (oldTerrestreOfCamion != null) {
                    oldTerrestreOfCamion.setCamion(null);
                    oldTerrestreOfCamion = em.merge(oldTerrestreOfCamion);
                }
                camionNew.setTerrestre(terrestre);
                camionNew = em.merge(camionNew);
            }
            if (vehiculoOld != null && !vehiculoOld.equals(vehiculoNew)) {
                vehiculoOld.setTerrestre(null);
                vehiculoOld = em.merge(vehiculoOld);
            }
            if (vehiculoNew != null && !vehiculoNew.equals(vehiculoOld)) {
                vehiculoNew.setTerrestre(terrestre);
                vehiculoNew = em.merge(vehiculoNew);
            }
            if (autoelectricoNew != null && !autoelectricoNew.equals(autoelectricoOld)) {
                Terrestre oldTerrestreOfAutoelectrico = autoelectricoNew.getTerrestre();
                if (oldTerrestreOfAutoelectrico != null) {
                    oldTerrestreOfAutoelectrico.setAutoelectrico(null);
                    oldTerrestreOfAutoelectrico = em.merge(oldTerrestreOfAutoelectrico);
                }
                autoelectricoNew.setTerrestre(terrestre);
                autoelectricoNew = em.merge(autoelectricoNew);
            }
            if (autocombustionNew != null && !autocombustionNew.equals(autocombustionOld)) {
                Terrestre oldTerrestreOfAutocombustion = autocombustionNew.getTerrestre();
                if (oldTerrestreOfAutocombustion != null) {
                    oldTerrestreOfAutocombustion.setAutocombustion(null);
                    oldTerrestreOfAutocombustion = em.merge(oldTerrestreOfAutocombustion);
                }
                autocombustionNew.setTerrestre(terrestre);
                autocombustionNew = em.merge(autocombustionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = terrestre.getIdVehiculo();
                if (findTerrestre(id) == null) {
                    throw new NonexistentEntityException("The terrestre with id " + id + " no longer exists.");
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
            Terrestre terrestre;
            try {
                terrestre = em.getReference(Terrestre.class, id);
                terrestre.getIdVehiculo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The terrestre with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Camion camionOrphanCheck = terrestre.getCamion();
            if (camionOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Terrestre (" + terrestre + ") cannot be destroyed since the Camion " + camionOrphanCheck + " in its camion field has a non-nullable terrestre field.");
            }
            Autoelectrico autoelectricoOrphanCheck = terrestre.getAutoelectrico();
            if (autoelectricoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Terrestre (" + terrestre + ") cannot be destroyed since the Autoelectrico " + autoelectricoOrphanCheck + " in its autoelectrico field has a non-nullable terrestre field.");
            }
            Autocombustion autocombustionOrphanCheck = terrestre.getAutocombustion();
            if (autocombustionOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Terrestre (" + terrestre + ") cannot be destroyed since the Autocombustion " + autocombustionOrphanCheck + " in its autocombustion field has a non-nullable terrestre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Vehiculo vehiculo = terrestre.getVehiculo();
            if (vehiculo != null) {
                vehiculo.setTerrestre(null);
                vehiculo = em.merge(vehiculo);
            }
            em.remove(terrestre);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Terrestre> findTerrestreEntities() {
        return findTerrestreEntities(true, -1, -1);
    }

    public List<Terrestre> findTerrestreEntities(int maxResults, int firstResult) {
        return findTerrestreEntities(false, maxResults, firstResult);
    }

    private List<Terrestre> findTerrestreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Terrestre.class));
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

    public Terrestre findTerrestre(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Terrestre.class, id);
        } finally {
            em.close();
        }
    }

    public int getTerrestreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Terrestre> rt = cq.from(Terrestre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
