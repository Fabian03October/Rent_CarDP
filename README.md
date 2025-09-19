# 🚗 Sistema de Alquiler de Vehículos - Rent Car DP

## 📋 Descripción
Sistema completo de gestión de alquiler de vehículos desarrollado en Java con base de datos MySQL. Incluye herencia de vehículos, autenticación por roles, y funcionalidades completas de CRUD.

## 🚀 Características Principales

### 🏗️ Arquitectura del Sistema
- **Modelo**: Entidades JPA con herencia completa de vehículos
- **Vista**: Interfaces gráficas (JFrame) y sistema de terminal
- **Controlador**: JPA Controllers para gestión de datos
- **Base de Datos**: MySQL con estructura normalizada

### 🚙 Tipos de Vehículos (Herencia)
```
Vehiculo (Clase base)
├── Terrestre
│   ├── AutoCombustion
│   │   └── AutoElectrico
│   └── Camion
└── Acuatico
    └── Yate
```

### 👥 Sistema de Roles
- **👑 Administrador**: Gestión completa del sistema
  - Usuario: `admin` / Contraseña: `admin123`
- **👷 Empleado**: Operaciones de alquiler
  - Usuario: `12345678` / Contraseña: `empleado123`

### 🔧 Funcionalidades

#### Administrador
- ✅ Gestión de vehículos (CRUD completo)
- ✅ Gestión de usuarios del sistema
- ✅ Configuración de tarifas por tipo de vehículo
- ✅ Reportes completos del negocio
- ✅ Control total del sistema

#### Empleado
- ✅ Procesamiento de alquileres
- ✅ Gestión de reservaciones
- ✅ Finalización de alquileres
- ✅ Generación de facturas
- ✅ Consulta de alquileres activos

## 🛠️ Tecnologías Utilizadas
- **Java 21**
- **MySQL 8.0**
- **JPA/Hibernate**
- **Swing (JFrame)**
- **Maven**
- **MySQL Connector J**

## 📊 Base de Datos
- **Esquema**: `rent_car`
- **Tablas**: 13 tablas principales
- **Características**: Herencia, índices optimizados, vistas personalizadas

## 🚀 Formas de Ejecutar

### 1. **Interfaz Gráfica (NetBeans)**
```bash
Clase Principal: Vista.IniciarSesion
```

### 2. **Sistema Terminal (Línea de comandos)**
```bash
java -cp "target\classes;mysql-connector-j-8.0.33.jar" Vista.SistemaSimpleBD
```

### 3. **Sistema Completo**
```bash
java -cp "target\classes;mysql-connector-j-8.0.33.jar" Vista.SistemaCompletoBD
```

## ⚙️ Configuración

### Requisitos Previos
1. **MySQL Server** ejecutándose
2. **Java 21** instalado
3. **Maven** (opcional para compilación)

### Base de Datos
1. Ejecutar el script: `script_corregido.sql`
2. Configurar contraseña MySQL en el código si es necesario

## 📁 Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   ├── Controlador/     # JPA Controllers
│   │   ├── Modelo/          # Entidades JPA
│   │   └── Vista/           # Interfaces de usuario
│   └── resources/
│       ├── META-INF/        # Configuración JPA
│       └── Imagenes/        # Recursos gráficos
└── target/                  # Archivos compilados
```

## 📖 Documentación Adicional
- `CREDENCIALES.md` - Información de acceso al sistema
- `INSTRUCCIONES_BD.md` - Configuración de base de datos
- `SISTEMA_FUNCIONANDO.md` - Guía de funcionamiento
- `script_corregido.sql` - Script de base de datos

## 🎯 Casos de Uso Principales
1. **Registro de clientes**
2. **Gestión de flota de vehículos**
3. **Procesamiento de reservaciones**
4. **Gestión de alquileres activos**
5. **Generación de reportes**
6. **Facturación automática**

## 🔐 Seguridad
- Autenticación por base de datos
- Roles diferenciados (Admin/Empleado)
- Validación de credenciales
- Control de acceso por funcionalidad

## 📞 Soporte
Para dudas o problemas, revisar la documentación incluida en el proyecto.

---
**Desarrollado por**: Fabián de Jesús  
**Fecha**: Septiembre 2025  
**Versión**: 1.0 Final