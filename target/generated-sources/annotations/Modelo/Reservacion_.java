package Modelo;

import Modelo.Cliente;
import Modelo.Vehiculo;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-18T21:30:30", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Reservacion.class)
public class Reservacion_ { 

    public static volatile SingularAttribute<Reservacion, Vehiculo> idVehiculo;
    public static volatile SingularAttribute<Reservacion, Cliente> idCliente;
    public static volatile SingularAttribute<Reservacion, Date> fechaInicio;
    public static volatile SingularAttribute<Reservacion, Integer> idReservacion;
    public static volatile SingularAttribute<Reservacion, Boolean> activa;
    public static volatile SingularAttribute<Reservacion, Date> fechaFin;

}