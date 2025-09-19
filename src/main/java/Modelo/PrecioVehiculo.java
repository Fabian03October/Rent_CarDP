package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidad para manejar precios por tipo de vehículo
 */
@Entity
@Table(name = "precio_vehiculo")
@NamedQueries({
    @NamedQuery(name = "PrecioVehiculo.findAll", query = "SELECT p FROM PrecioVehiculo p"),
    @NamedQuery(name = "PrecioVehiculo.findByTipo", query = "SELECT p FROM PrecioVehiculo p WHERE p.tipoVehiculo = :tipo AND p.activo = true")
})
public class PrecioVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_precio")
    private Integer idPrecio;
    
    @Basic(optional = false)
    @Column(name = "tipo_vehiculo")
    private String tipoVehiculo;
    
    @Basic(optional = false)
    @Column(name = "precio_por_dia")
    private BigDecimal precioPorDia;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "activo")
    private Boolean activo;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public PrecioVehiculo() {
        this.activo = true;
        this.fechaCreacion = new Date();
    }

    public PrecioVehiculo(String tipoVehiculo, BigDecimal precioPorDia) {
        this();
        this.tipoVehiculo = tipoVehiculo;
        this.precioPorDia = precioPorDia;
    }

    // Getters y Setters
    public Integer getIdPrecio() {
        return idPrecio;
    }

    public void setIdPrecio(Integer idPrecio) {
        this.idPrecio = idPrecio;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrecio != null ? idPrecio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrecioVehiculo)) {
            return false;
        }
        PrecioVehiculo other = (PrecioVehiculo) object;
        if ((this.idPrecio == null && other.idPrecio != null) || (this.idPrecio != null && !this.idPrecio.equals(other.idPrecio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PrecioVehiculo{" + "idPrecio=" + idPrecio + ", tipoVehiculo=" + tipoVehiculo + 
               ", precioPorDia=" + precioPorDia + ", activo=" + activo + '}';
    }
}