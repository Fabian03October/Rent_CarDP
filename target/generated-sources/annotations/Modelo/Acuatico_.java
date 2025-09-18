package Modelo;

import Modelo.Vehiculo;
import Modelo.Yate;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T08:55:04", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Acuatico.class)
public class Acuatico_ { 

    public static volatile SingularAttribute<Acuatico, BigDecimal> eslora;
    public static volatile SingularAttribute<Acuatico, Yate> yate;
    public static volatile SingularAttribute<Acuatico, Integer> idVehiculo;
    public static volatile SingularAttribute<Acuatico, BigDecimal> calado;
    public static volatile SingularAttribute<Acuatico, Vehiculo> vehiculo;

}