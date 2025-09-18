package Modelo;

import Modelo.Renta;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-09-17T22:28:19", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Observacion.class)
public class Observacion_ { 

    public static volatile SingularAttribute<Observacion, String> descripcion;
    public static volatile SingularAttribute<Observacion, Renta> idRenta;
    public static volatile SingularAttribute<Observacion, Integer> idObservacion;

}