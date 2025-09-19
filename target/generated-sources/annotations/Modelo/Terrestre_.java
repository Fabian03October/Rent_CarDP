package Modelo;

import Modelo.Autocombustion;
import Modelo.Autoelectrico;
import Modelo.Camion;
import Modelo.Vehiculo;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-18T21:30:30", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Terrestre.class)
public class Terrestre_ { 

    public static volatile SingularAttribute<Terrestre, Camion> camion;
    public static volatile SingularAttribute<Terrestre, Integer> idVehiculo;
    public static volatile SingularAttribute<Terrestre, String> tipoCombustible;
    public static volatile SingularAttribute<Terrestre, Autoelectrico> autoelectrico;
    public static volatile SingularAttribute<Terrestre, Autocombustion> autocombustion;
    public static volatile SingularAttribute<Terrestre, Integer> noPuertas;
    public static volatile SingularAttribute<Terrestre, Vehiculo> vehiculo;

}