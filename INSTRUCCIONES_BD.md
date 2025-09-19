# Instrucciones de Instalación - Sistema de Alquiler de Vehículos

## 📋 **CONFIGURACIÓN DE BASE DE DATOS**

### **1. Ejecutar Script Principal**
```bash
# Conectar a MySQL y ejecutar el script
mysql -u root -p < script_corregido.sql
```

### **2. Verificar Instalación**
```sql
USE rent_car;
SHOW TABLES;
SELECT * FROM VehiculosCompletos;
```

## 🚗 **ESTRUCTURA DE LA BASE DE DATOS**

### **Jerarquía de Vehículos (según Diagrama E-R):**
```
Vehiculo (base)
├── Terrestre
│   ├── AutoCombustion
│   │   └── AutoElectrico
│   └── Camion
└── Acuatico
    └── Yate
```

### **Tablas Creadas:**
- ✅ `Cliente` - Información de clientes
- ✅ `Usuario` - Sistema de roles (ADMIN/EMPLEADO)  
- ✅ `Vehiculo` - Datos base de todos los vehículos
- ✅ `Terrestre` - Herencia nivel 1 (terrestres)
- ✅ `Acuatico` - Herencia nivel 1 (acuáticos)
- ✅ `AutoCombustion` - Herencia nivel 2 (autos combustión)
- ✅ `AutoElectrico` - Herencia nivel 3 (autos eléctricos)
- ✅ `Camion` - Herencia nivel 2 (camiones)
- ✅ `Yate` - Herencia nivel 2 (yates)
- ✅ `Reservacion` - Gestión de reservas
- ✅ `Renta` - Gestión de alquileres
- ✅ `Factura` - Facturación
- ✅ `Observacion` - Notas de rentas
- ✅ `PrecioVehiculo` - Precios por tipo

## 👥 **USUARIOS PREDEFINIDOS**

```sql
-- Administrador
Usuario: admin
Contraseña: admin123
Rol: ADMIN

-- Empleado
Usuario: 12345678  
Contraseña: empleado123
Rol: EMPLEADO
```

## 🎯 **DATOS DE PRUEBA INCLUIDOS**

### **Vehículos con Herencia Completa:**
1. **Toyota Corolla 2020** - Auto Combustión
2. **Tesla Model 3 2023** - Auto Eléctrico  
3. **Ford F-150 2021** - Camión
4. **Sea Ray Sundancer 265 2022** - Yate

### **Clientes de Ejemplo:**
- Juan Pérez (111111111)
- María García (222222222)  
- Carlos López (333333333)

## 🔧 **CONFIGURACIÓN DE APLICACIÓN**

### **persistence.xml actualizado:**
```xml
<property name="javax.persistence.jdbc.url" 
          value="jdbc:mysql://localhost:3306/rent_car"/>
```

### **Ejecutar Aplicación:**
```bash
# Terminal
java -cp target\classes Vista.SistemaTerminalSimple

# Con JPA (requiere dependencias)
java -cp target\classes Vista.SistemaTerminal
```

## ✅ **VERIFICACIÓN DE FUNCIONAMIENTO**

### **Comando SQL para verificar herencia:**
```sql
SELECT 
    v.marca, v.modelo,
    CASE 
        WHEN ae.idVehiculo IS NOT NULL THEN 'Auto Eléctrico'
        WHEN ac.idVehiculo IS NOT NULL THEN 'Auto Combustión'
        WHEN c.idVehiculo IS NOT NULL THEN 'Camión'
        WHEN y.idVehiculo IS NOT NULL THEN 'Yate'
    END AS tipo_vehiculo
FROM Vehiculo v
LEFT JOIN AutoElectrico ae ON v.idVehiculo = ae.idVehiculo
LEFT JOIN AutoCombustion ac ON v.idVehiculo = ac.idVehiculo  
LEFT JOIN Camion c ON v.idVehiculo = c.idVehiculo
LEFT JOIN Yate y ON v.idVehiculo = y.idVehiculo;
```

## 🚀 **FUNCIONALIDADES IMPLEMENTADAS**

- ✅ **Sistema de Roles**: ADMIN y EMPLEADO con permisos diferenciados
- ✅ **Herencia de Vehículos**: Creación correcta en todas las tablas
- ✅ **Gestión Completa**: Reservas, rentas, facturación
- ✅ **Precios Dinámicos**: Por tipo de vehículo
- ✅ **Integridad Referencial**: FK con CASCADE apropiado
- ✅ **Optimización**: Índices y vistas para consultas eficientes

---
*Script validado según Diagrama E-R proporcionado*