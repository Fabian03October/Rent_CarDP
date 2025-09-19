# Sistema de Gestión de Vehículos - Modo Terminal

## Descripción
Sistema de gestión de vehículos con roles de usuario (ADMIN/EMPLEADO) que funciona desde terminal, manteniendo la arquitectura MVC.

## Configuración de Base de Datos

### 1. Ejecutar script SQL
Ejecutar el archivo `actualizar_bd.sql` en MySQL para crear las tablas y datos necesarios:

```sql
-- Ejecutar en MySQL
source actualizar_bd.sql;
-- O copiar y pegar el contenido del archivo
```

### 2. Verificar conexión
Asegurarse de que la base de datos `sistema_renta_vehiculos` existe y el usuario tiene permisos.

## Usuarios Pre-configurados

| Cédula | Contraseña | Rol | Nombre |
|--------|------------|-----|---------|
| admin | admin123 | ADMIN | Administrador Sistema |
| 12345678 | empleado123 | EMPLEADO | Juan Pérez |

## Funcionalidades por Rol

### ADMINISTRADOR
- ✅ Gestionar Vehículos (agregar, listar, buscar)
- ✅ Gestionar Usuarios (agregar, listar)
- ✅ Configurar Precios por tipo de vehículo
- ✅ Ver todos los vehículos y usuarios
- 🚧 Historial de operaciones (en desarrollo)

### EMPLEADO
- ✅ Ver vehículos disponibles
- ✅ Consultar precios
- 🚧 Realizar rentas (en desarrollo)
- 🚧 Realizar reservaciones (en desarrollo)

## Cómo Ejecutar

### Opción 1: Desde Terminal
```bash
cd "c:\Users\Fabian de jesus\Downloads\vehiculos\proyecto\Rent_CarDP"
java -cp "target/classes;[TODAS_LAS_DEPENDENCIAS]" Vista.SistemaTerminal
```

### Opción 2: Desde VS Code
1. Abrir `src/main/java/Vista/SistemaTerminal.java`
2. Hacer clic en "Run" o presionar F5

## Arquitectura

### Modelo (Entidades JPA)
- `Usuario.java` - Gestión de usuarios con roles
- `Vehiculo.java` - Vehículos base
- `PrecioVehiculo.java` - Precios por tipo de vehículo
- `Cliente.java`, `Renta.java`, `Reservacion.java` - Operaciones

### Vista
- `SistemaTerminal.java` - Interfaz de terminal simple
- `IniciarSesion.java` - Login con validación de roles

### Controlador (JPA Controllers)
- `UsuarioJpaController.java` - CRUD usuarios
- `VehiculoJpaController.java` - CRUD vehículos
- `PrecioVehiculoJpaController.java` - CRUD precios

## Tipos de Vehículos Soportados
- AUTOCOMBUSTION - $50.00/día
- AUTOELECTRICO - $75.00/día
- CAMION - $100.00/día
- YATE - $200.00/día

## Próximas Funcionalidades
- [ ] Sistema completo de rentas
- [ ] Sistema completo de reservaciones
- [ ] Historial detallado de operaciones
- [ ] Reportes y estadísticas
- [ ] Gestión de clientes desde formulario de venta

## Estructura de Desarrollo
```
src/main/java/
├── Modelo/           # Entidades JPA
├── Vista/            # Interfaces (terminal)
├── Controlador/      # Lógica de negocio
└── resources/
    └── META-INF/
        └── persistence.xml
```

## Notas Técnicas
- Java 21
- JPA/EclipseLink 2.7.12
- MySQL 8.0
- Patrón MVC estricto
- Consultas optimizadas con JPA
- Sistema de roles basado en base de datos