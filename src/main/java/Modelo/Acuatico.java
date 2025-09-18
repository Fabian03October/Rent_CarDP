/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "acuatico")
@NamedQueries({
    @NamedQuery(name = "Acuatico.findAll", query = "SELECT a FROM Acuatico a"),
    @NamedQuery(name = "Acuatico.findByIdVehiculo", query = "SELECT a FROM Acuatico a WHERE a.idVehiculo = :idVehiculo"),
    @NamedQuery(name = "Acuatico.findByCalado", query = "SELECT a FROM Acuatico a WHERE a.calado = :calado"),
    @NamedQuery(name = "Acuatico.findByEslora", query = "SELECT a FROM Acuatico a WHERE a.eslora = :eslora")})
public class Acuatico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVehiculo")
    private Integer idVehiculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calado")
    private BigDecimal calado;
    @Column(name = "eslora")
    private BigDecimal eslora;
    @JoinColumn(name = "idVehiculo", referencedColumnName = "idVehiculo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Vehiculo vehiculo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "acuatico")
    private Yate yate;

    public Acuatico() {
    }

    public Acuatico(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public BigDecimal getCalado() {
        return calado;
    }

    public void setCalado(BigDecimal calado) {
        this.calado = calado;
    }

    public BigDecimal getEslora() {
        return eslora;
    }

    public void setEslora(BigDecimal eslora) {
        this.eslora = eslora;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Yate getYate() {
        return yate;
    }

    public void setYate(Yate yate) {
        this.yate = yate;
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
        if (!(object instanceof Acuatico)) {
            return false;
        }
        Acuatico other = (Acuatico) object;
        if ((this.idVehiculo == null && other.idVehiculo != null) || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Acuatico[ idVehiculo=" + idVehiculo + " ]";
    }
    
}
