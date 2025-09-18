package Modelo;

import Modelo.Cliente;
import Modelo.Factura;
import Modelo.Observacion;
import Modelo.Vehiculo;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T22:28:19", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Renta.class)
public class Renta_ { 

    public static volatile ListAttribute<Renta, Observacion> observacionList;
    public static volatile SingularAttribute<Renta, BigDecimal> tarifaDiaria;
    public static volatile SingularAttribute<Renta, String> estado;
    public static volatile SingularAttribute<Renta, Vehiculo> idVehiculo;
    public static volatile SingularAttribute<Renta, Cliente> idCliente;
    public static volatile SingularAttribute<Renta, Date> fechaInicio;
    public static volatile ListAttribute<Renta, Factura> facturaList;
    public static volatile SingularAttribute<Renta, Integer> idRenta;
    public static volatile SingularAttribute<Renta, Date> fechaFin;

}