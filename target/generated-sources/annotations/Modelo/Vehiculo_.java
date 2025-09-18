package Modelo;

import Modelo.Acuatico;
import Modelo.Renta;
import Modelo.Reservacion;
import Modelo.Terrestre;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T08:55:04", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Vehiculo.class)
public class Vehiculo_ { 

    public static volatile SingularAttribute<Vehiculo, Boolean> reservado;
    public static volatile SingularAttribute<Vehiculo, Acuatico> acuatico;
    public static volatile SingularAttribute<Vehiculo, String> marca;
    public static volatile SingularAttribute<Vehiculo, BigDecimal> precio;
    public static volatile SingularAttribute<Vehiculo, Integer> idVehiculo;
    public static volatile SingularAttribute<Vehiculo, Terrestre> terrestre;
    public static volatile ListAttribute<Vehiculo, Renta> rentaList;
    public static volatile SingularAttribute<Vehiculo, String> modelo;
    public static volatile ListAttribute<Vehiculo, Reservacion> reservacionList;
    public static volatile SingularAttribute<Vehiculo, Integer> año;

}