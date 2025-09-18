package Modelo;

import Modelo.Terrestre;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T08:55:04", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Camion.class)
public class Camion_ { 

    public static volatile SingularAttribute<Camion, Integer> idVehiculo;
    public static volatile SingularAttribute<Camion, Terrestre> terrestre;
    public static volatile SingularAttribute<Camion, BigDecimal> capacidadCarga;
    public static volatile SingularAttribute<Camion, Integer> ejes;

}