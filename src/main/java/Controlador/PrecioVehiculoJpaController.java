package Controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import Modelo.PrecioVehiculo;

/**
 * Controlador JPA para PrecioVehiculo
 */
@SuppressWarnings("unchecked")
public class PrecioVehiculoJpaController implements Serializable {

    public PrecioVehiculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public PrecioVehiculoJpaController() {
        emf = Persistence.createEntityManagerFactory("JPA_Rent_Car");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrecioVehiculo precioVehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(precioVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrecioVehiculo precioVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            precioVehiculo = em.merge(precioVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = precioVehiculo.getIdPrecio();
                if (findPrecioVehiculo(id) == null) {
                    throw new EntityNotFoundException("El precio con id " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PrecioVehiculo precioVehiculo;
            try {
                precioVehiculo = em.getReference(PrecioVehiculo.class, id);
                precioVehiculo.getIdPrecio();
            } catch (EntityNotFoundException enfe) {
                throw new EntityNotFoundException("El precio con id " + id + " no existe.");
            }
            em.remove(precioVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PrecioVehiculo> findPrecioVehiculoEntities() {
        return findPrecioVehiculoEntities(true, -1, -1);
    }

    public List<PrecioVehiculo> findPrecioVehiculoEntities(int maxResults, int firstResult) {
        return findPrecioVehiculoEntities(false, maxResults, firstResult);
    }

    private List<PrecioVehiculo> findPrecioVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrecioVehiculo.class));
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

    public PrecioVehiculo findPrecioVehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrecioVehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrecioVehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrecioVehiculo> rt = cq.from(PrecioVehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca precio por tipo de vehículo
     */
    public PrecioVehiculo findPrecioPorTipo(String tipoVehiculo) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM PrecioVehiculo p WHERE p.tipoVehiculo = :tipo AND p.activo = true");
            query.setParameter("tipo", tipoVehiculo);
            List<PrecioVehiculo> precios = query.getResultList();
            if (!precios.isEmpty()) {
                return precios.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene precios activos
     */
    public List<PrecioVehiculo> getPreciosActivos() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM PrecioVehiculo p WHERE p.activo = true ORDER BY p.tipoVehiculo");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualiza precio por tipo
     */
    public void actualizarPrecio(String tipoVehiculo, BigDecimal nuevoPrecio) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Query query = em.createQuery("UPDATE PrecioVehiculo p SET p.precioPorDia = :precio WHERE p.tipoVehiculo = :tipo");
            query.setParameter("precio", nuevoPrecio);
            query.setParameter("tipo", tipoVehiculo);
            
            int updated = query.executeUpdate();
            if (updated == 0) {
                // Crear nuevo precio si no existe
                PrecioVehiculo nuevoPrecioVehiculo = new PrecioVehiculo(tipoVehiculo, nuevoPrecio);
                em.persist(nuevoPrecioVehiculo);
            }
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}