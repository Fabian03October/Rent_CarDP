# Solución al Problema de Herencia de Vehículos

## Script de Base de Datos
**Archivo único:** `script_corregido.sql`
- ✅ Estructurado según Diagrama E-R
- ✅ Herencia completa de vehículos
- ✅ Sistema de roles implementado
- ✅ Datos de prueba incluidos

## Problema Identificado
El sistema solo guardaba el vehículo base en la tabla `vehiculo`, pero no creaba los registros correspondientes en las tablas especializadas (`terrestre`, `acuatico`, `autocombustion`, `autoelectrico`, `camion`, `yate`).

## Solución Implementada

### 1. Análisis de la Jerarquía de Herencia
La jerarquía de vehículos es:
```
Vehiculo (tabla: vehiculo)
├── Terrestre (tabla: terrestre)
│   ├── Autocombustion (tabla: autocombustion)
│   ├── Autoelectrico (tabla: autoelectrico)
│   └── Camion (tabla: camion)
└── Acuatico (tabla: acuatico)
    └── Yate (tabla: yate)
```

### 2. Métodos Creados en SistemaTerminal.java

#### crearAutoCombustion(Integer idVehiculo)
- Crea registro en tabla `terrestre`
- Crea registro en tabla `autocombustion`
- Incluye: número de puertas, tipo de combustible

#### crearAutoElectrico(Integer idVehiculo)
- Crea registro en tabla `terrestre`
- Crea registro en tabla `autocombustion` (herencia intermedia)
- Crea registro en tabla `autoelectrico`
- Incluye: número de puertas, capacidad de batería, autonomía

#### crearCamion(Integer idVehiculo)
- Crea registro en tabla `terrestre`
- Crea registro en tabla `camion`
- Incluye: número de puertas, capacidad de carga

#### crearYate(Integer idVehiculo)
- Crea registro en tabla `acuatico`
- Crea registro en tabla `yate`
- Incluye: eslora, calado, motor eléctrico

### 3. Correcciones Realizadas

#### Problema de Métodos de Entidad
- **Error**: Uso de `setNumPuertas()` en lugar de `setNoPuertas()`
- **Solución**: Corregido a `setNoPuertas()` según la definición en la entidad `Terrestre`

#### Problema de Tipos de Datos
- **Error**: Uso de tipos primitivos (`double`, `int`) para campos `BigDecimal`
- **Solución**: Uso de `BigDecimal.valueOf()` para conversiones correctas

### 4. Flujo de Creación Corregido

1. **Crear vehículo base** (tabla `vehiculo`)
2. **Crear registro de herencia nivel 1** (`terrestre` o `acuatico`)
3. **Crear registro de herencia nivel 2** (tipo específico)

### 5. Ejemplo de Uso en SistemaTerminal

```
--- CREAR VEHICULO ---
Marca: Toyota
Modelo: Prius
Año: 2023
Precio: 35000.00

Tipos disponibles:
1. Auto de Combustión
2. Auto Eléctrico
3. Camión
4. Yate
```

### 6. Validación
Al crear un vehículo ahora se guardan:
- ✅ Registro base en tabla `vehiculo`
- ✅ Registro especializado en tabla correspondiente
- ✅ Todos los atributos específicos del tipo de vehículo

### 7. Sistema de Roles Integrado
- **ADMIN**: Puede crear, editar y eliminar vehículos
- **EMPLEADO**: Puede listar y gestionar rentas

## Estado del Proyecto
- ✅ Sistema de roles funcionando
- ✅ Herencia de vehículos corregida
- ✅ Compilación sin errores
- ✅ Métodos de creación especializados
- ✅ Validación de tipos de datos