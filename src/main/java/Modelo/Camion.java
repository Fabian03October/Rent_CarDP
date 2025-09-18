/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author es982
 */
@Entity
@Table(name = "camion")
@NamedQueries({
    @NamedQuery(name = "Camion.findAll", query = "SELECT c FROM Camion c"),
    @NamedQuery(name = "Camion.findByIdVehiculo", query = "SELECT c FROM Camion c WHERE c.idVehiculo = :idVehiculo"),
    @NamedQuery(name = "Camion.findByCapacidadCarga", query = "SELECT c FROM Camion c WHERE c.capacidadCarga = :capacidadCarga"),
    @NamedQuery(name = "Camion.findByEjes", query = "SELECT c FROM Camion c WHERE c.ejes = :ejes")})
public class Camion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capacidad_carga")
    private BigDecimal capacidadCarga;
    @Column(name = "ejes")
    private Integer ejes;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Terrestre terrestre;

    public Camion() {
    }

    public Camion(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public BigDecimal getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(BigDecimal capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public Integer getEjes() {
        return ejes;
    }

    public void setEjes(Integer ejes) {
        this.ejes = ejes;
    }

    public Terrestre getTerrestre() {
        return terrestre;
    }

    public void setTerrestre(Terrestre terrestre) {
        this.terrestre = terrestre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVehiculo != null ? idVehiculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Camion)) {
            return false;
        }
        Camion other = (Camion) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Camion[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
