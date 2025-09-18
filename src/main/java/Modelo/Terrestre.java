/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "terrestre")
@NamedQueries({
    @NamedQuery(name = "Terrestre.findAll", query = "SELECT t FROM Terrestre t"),
    @NamedQuery(name = "Terrestre.findByIdVehiculo", query = "SELECT t FROM Terrestre t WHERE t.idVehiculo = :idVehiculo"),
    @NamedQuery(name = "Terrestre.findByNoPuertas", query = "SELECT t FROM Terrestre t WHERE t.noPuertas = :noPuertas"),
    @NamedQuery(name = "Terrestre.findByTipoCombustible", query = "SELECT t FROM Terrestre t WHERE t.tipoCombustible = :tipoCombustible")})
public class Terrestre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    @Column(name = "no_puertas")
    private Integer noPuertas;
    @Column(name = "tipo_combustible")
    private String tipoCombustible;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "terrestre")
    private Camion camion;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Vehiculo vehiculo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "terrestre")
    private Autoelectrico autoelectrico;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "terrestre")
    private Autocombustion autocombustion;

    public Terrestre() {
    }

    public Terrestre(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getNoPuertas() {
        return noPuertas;
    }

    public void setNoPuertas(Integer noPuertas) {
        this.noPuertas = noPuertas;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Autoelectrico getAutoelectrico() {
        return autoelectrico;
    }

    public void setAutoelectrico(Autoelectrico autoelectrico) {
        this.autoelectrico = autoelectrico;
    }

    public Autocombustion getAutocombustion() {
        return autocombustion;
    }

    public void setAutocombustion(Autocombustion autocombustion) {
        this.autocombustion = autocombustion;
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
        if (!(object instanceof Terrestre)) {
            return false;
        }
        Terrestre other = (Terrestre) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Terrestre[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
