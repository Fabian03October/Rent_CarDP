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
@Table(name = "yate")
@NamedQueries({
    @NamedQuery(name = "Yate.findAll", query = "SELECT y FROM Yate y"),
    @NamedQuery(name = "Yate.findByIdVehiculo", query = "SELECT y FROM Yate y WHERE y.idVehiculo = :idVehiculo"),
    @NamedQuery(name = "Yate.findByMotorElectrico", query = "SELECT y FROM Yate y WHERE y.motorElectrico = :motorElectrico")})
public class Yate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    @Column(name = "motorElectrico")
    private Boolean motorElectrico;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Acuatico acuatico;

    public Yate() {
    }

    public Yate(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Boolean getMotorElectrico() {
        return motorElectrico;
    }

    public void setMotorElectrico(Boolean motorElectrico) {
        this.motorElectrico = motorElectrico;
    }

    public Acuatico getAcuatico() {
        return acuatico;
    }

    public void setAcuatico(Acuatico acuatico) {
        this.acuatico = acuatico;
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
        if (!(object instanceof Yate)) {
            return false;
        }
        Yate other = (Yate) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Yate[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
