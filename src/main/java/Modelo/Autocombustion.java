/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
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
@Table(name = "autocombustion")
@NamedQueries({
    @NamedQuery(name = "Autocombustion.findAll", query = "SELECT a FROM Autocombustion a"),
    @NamedQuery(name = "Autocombustion.findByIdVehiculo", query = "SELECT a FROM Autocombustion a WHERE a.idVehiculo = :idVehiculo")})
public class Autocombustion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Terrestre terrestre;

    public Autocombustion() {
    }

    public Autocombustion(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
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
        if (!(object instanceof Autocombustion)) {
            return false;
        }
        Autocombustion other = (Autocombustion) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Autocombustion[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
