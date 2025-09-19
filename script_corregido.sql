-- Script de Base de Datos Corregido según Diagrama E-R
-- Ejecutar en MySQL para crear estructura completa

CREATE DATABASE IF NOT EXISTS rent_car;
USE rent_car;

-- ==============================
-- TABLAS PRINCIPALES
-- ==============================

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    cedula VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) DEFAULT 'EMPLEADO',
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT true
);

CREATE TABLE Vehiculo (
    idVehiculo INT PRIMARY KEY AUTO_INCREMENT,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    año INT,
    disponible BOOLEAN DEFAULT TRUE
);

-- ==============================
-- JERARQUÍA DE HERENCIA NIVEL 1
-- ==============================

-- Vehículos Terrestres
CREATE TABLE Terrestre (
    idVehiculo INT PRIMARY KEY,
    no_puertas INT,
    tipo_combustible VARCHAR(30),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE
);

-- Vehículos Acuáticos
CREATE TABLE Acuatico (
    idVehiculo INT PRIMARY KEY,
    eslora DECIMAL(8,2), -- Longitud del barco
    calado DECIMAL(8,2), -- Profundidad bajo el agua
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE
);

-- ==============================
-- JERARQUÍA DE HERENCIA NIVEL 2
-- ==============================

-- Auto a Combustión (hereda de Terrestre)
CREATE TABLE AutoCombustion (
    idVehiculo INT PRIMARY KEY,
    tipo_combustible VARCHAR(30), -- Gasolina, Diesel, Gas
    FOREIGN KEY (idVehiculo) REFERENCES Terrestre(idVehiculo) ON DELETE CASCADE
);

-- Auto Eléctrico (hereda de Auto a Combustión según diagrama)
CREATE TABLE AutoElectrico (
    idVehiculo INT PRIMARY KEY,
    capacidad_bateria DECIMAL(8,2), -- kWh
    autonomia DECIMAL(8,2), -- Kilómetros
    FOREIGN KEY (idVehiculo) REFERENCES AutoCombustion(idVehiculo) ON DELETE CASCADE
);

-- Camión (hereda de Terrestre)
CREATE TABLE Camion (
    idVehiculo INT PRIMARY KEY,
    carga DECIMAL(10,2), -- Capacidad de carga en toneladas
    FOREIGN KEY (idVehiculo) REFERENCES Terrestre(idVehiculo) ON DELETE CASCADE
);

-- Yate (hereda de Acuático)
CREATE TABLE Yate (
    idVehiculo INT PRIMARY KEY,
    motorElectrico BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idVehiculo) REFERENCES Acuatico(idVehiculo) ON DELETE CASCADE
);

-- ==============================
-- TABLAS DE OPERACIONES
-- ==============================

CREATE TABLE Reservacion (
    idReservacion INT PRIMARY KEY AUTO_INCREMENT,
    idVehiculo INT NOT NULL,
    idCliente INT NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE CASCADE,
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE Renta (
    idRenta INT PRIMARY KEY AUTO_INCREMENT,
    idCliente INT NOT NULL,
    idVehiculo INT NOT NULL,
    idReservacion INT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    tarifaDiaria DECIMAL(10,2) NOT NULL,
    precio_total DECIMAL(10,2),
    Estado VARCHAR(20) DEFAULT 'ACTIVA',
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE CASCADE,
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE,
    FOREIGN KEY (idReservacion) REFERENCES Reservacion(idReservacion) ON DELETE SET NULL
);

CREATE TABLE Factura (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    idRenta INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idRenta) REFERENCES Renta(idRenta) ON DELETE CASCADE
);

CREATE TABLE Observacion (
    id_observacion INT PRIMARY KEY AUTO_INCREMENT,
    idRenta INT NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idRenta) REFERENCES Renta(idRenta) ON DELETE CASCADE
);

-- ==============================
-- TABLA DE PRECIOS
-- ==============================

CREATE TABLE PrecioVehiculo (
    id_precio INT AUTO_INCREMENT PRIMARY KEY,
    tipo_vehiculo VARCHAR(50) NOT NULL,
    precio_por_dia DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_tipo (tipo_vehiculo)
);

-- ==============================
-- DATOS INICIALES
-- ==============================

-- Usuarios del sistema
INSERT INTO Usuario (cedula, contrasena, rol, nombre, apellido, activo) VALUES 
('admin', 'admin123', 'ADMIN', 'Administrador', 'Sistema', true),
('12345678', 'empleado123', 'EMPLEADO', 'Juan', 'Pérez', true);

-- Precios por tipo de vehículo
INSERT INTO PrecioVehiculo (tipo_vehiculo, precio_por_dia, descripcion) VALUES
('AUTOCOMBUSTION', 50.00, 'Precio por día para autos de combustión'),
('AUTOELECTRICO', 75.00, 'Precio por día para autos eléctricos'),
('CAMION', 100.00, 'Precio por día para camiones'),
('YATE', 200.00, 'Precio por día para yates');

-- Clientes de ejemplo
INSERT INTO Cliente (nombre, telefono, cedula) VALUES 
('Juan Pérez', '555-1234', '111111111'),
('María García', '555-5678', '222222222'),
('Carlos López', '555-9012', '333333333');

-- Vehículos de ejemplo con herencia completa
-- Auto de Combustión Toyota Corolla
INSERT INTO Vehiculo (idVehiculo, marca, modelo, precio, año, disponible) VALUES (1, 'Toyota', 'Corolla', 15000.00, 2020, TRUE);
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (1, 4, 'Gasolina');
INSERT INTO AutoCombustion (idVehiculo, tipo_combustible) VALUES (1, 'Gasolina');

-- Auto Eléctrico Tesla Model 3
INSERT INTO Vehiculo (idVehiculo, marca, modelo, precio, año, disponible) VALUES (2, 'Tesla', 'Model 3', 45000.00, 2023, TRUE);
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (2, 4, 'Eléctrico');
INSERT INTO AutoCombustion (idVehiculo, tipo_combustible) VALUES (2, 'Eléctrico');
INSERT INTO AutoElectrico (idVehiculo, capacidad_bateria, autonomia) VALUES (2, 75.0, 500.0);

-- Camión Ford F-150
INSERT INTO Vehiculo (idVehiculo, marca, modelo, precio, año, disponible) VALUES (3, 'Ford', 'F-150', 25000.00, 2021, TRUE);
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (3, 2, 'Diesel');
INSERT INTO Camion (idVehiculo, carga) VALUES (3, 1.5);

-- Yate Sea Ray
INSERT INTO Vehiculo (idVehiculo, marca, modelo, precio, año, disponible) VALUES (4, 'Sea Ray', 'Sundancer 265', 85000.00, 2022, TRUE);
INSERT INTO Acuatico (idVehiculo, eslora, calado) VALUES (4, 8.23, 0.91);
INSERT INTO Yate (idVehiculo, motorElectrico) VALUES (4, FALSE);

-- ==============================
-- ÍNDICES PARA OPTIMIZACIÓN
-- ==============================

CREATE INDEX idx_vehiculo_marca ON Vehiculo(marca);
CREATE INDEX idx_vehiculo_disponible ON Vehiculo(disponible);
CREATE INDEX idx_renta_fechas ON Renta(fechaInicio, fechaFin);
CREATE INDEX idx_reservacion_fechas ON Reservacion(fechaInicio, fechaFin);
CREATE INDEX idx_usuario_cedula ON Usuario(cedula);
CREATE INDEX idx_cliente_cedula ON Cliente(cedula);

-- ==============================
-- VISTAS ÚTILES
-- ==============================

-- Vista de vehículos con información completa
CREATE VIEW VehiculosCompletos AS
SELECT 
    v.idVehiculo,
    v.marca,
    v.modelo,
    v.precio,
    v.año,
    v.disponible,
    CASE 
        WHEN ae.idVehiculo IS NOT NULL THEN 'Auto Eléctrico'
        WHEN ac.idVehiculo IS NOT NULL THEN 'Auto Combustión'
        WHEN c.idVehiculo IS NOT NULL THEN 'Camión'
        WHEN y.idVehiculo IS NOT NULL THEN 'Yate'
        ELSE 'Sin clasificar'
    END AS tipo_vehiculo,
    t.no_puertas,
    t.tipo_combustible,
    ac2.eslora,
    ac2.calado,
    ae.capacidad_bateria,
    ae.autonomia,
    c.carga,
    y.motorElectrico
FROM Vehiculo v
LEFT JOIN Terrestre t ON v.idVehiculo = t.idVehiculo
LEFT JOIN Acuatico ac2 ON v.idVehiculo = ac2.idVehiculo
LEFT JOIN AutoCombustion ac ON v.idVehiculo = ac.idVehiculo
LEFT JOIN AutoElectrico ae ON v.idVehiculo = ae.idVehiculo
LEFT JOIN Camion c ON v.idVehiculo = c.idVehiculo
LEFT JOIN Yate y ON v.idVehiculo = y.idVehiculo;

-- Mostrar estructura creada
SHOW TABLES;
SELECT 'Script ejecutado correctamente - Base de datos estructurada según Diagrama E-R' AS Status;