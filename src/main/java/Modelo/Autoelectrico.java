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
@Table(name = "autoelectrico")
@NamedQueries({
    @NamedQuery(name = "Autoelectrico.findAll", query = "SELECT a FROM Autoelectrico a"),
    @NamedQuery(name = "Autoelectrico.findByIdVehiculo", query = "SELECT a FROM Autoelectrico a WHERE a.idVehiculo = :idVehiculo"),
    @NamedQuery(name = "Autoelectrico.findByCapacidadBateria", query = "SELECT a FROM Autoelectrico a WHERE a.capacidadBateria = :capacidadBateria"),
    @NamedQuery(name = "Autoelectrico.findByAutonomia", query = "SELECT a FROM Autoelectrico a WHERE a.autonomia = :autonomia")})
public class Autoelectrico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capacidad_bateria")
    private BigDecimal capacidadBateria;
    @Column(name = "autonomia")
    private BigDecimal autonomia;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Terrestre terrestre;

    public Autoelectrico() {
    }

    public Autoelectrico(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public BigDecimal getCapacidadBateria() {
        return capacidadBateria;
    }

    public void setCapacidadBateria(BigDecimal capacidadBateria) {
        this.capacidadBateria = capacidadBateria;
    }

    public BigDecimal getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(BigDecimal autonomia) {
        this.autonomia = autonomia;
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
        if (!(object instanceof Autoelectrico)) {
            return false;
        }
        Autoelectrico other = (Autoelectrico) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Autoelectrico[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
