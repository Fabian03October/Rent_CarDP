package Modelo;

import Modelo.Terrestre;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T22:28:19", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Autoelectrico.class)
public class Autoelectrico_ { 

    public static volatile SingularAttribute<Autoelectrico, Integer> idVehiculo;
    public static volatile SingularAttribute<Autoelectrico, BigDecimal> autonomia;
    public static volatile SingularAttribute<Autoelectrico, Terrestre> terrestre;
    public static volatile SingularAttribute<Autoelectrico, BigDecimal> capacidadBateria;

}