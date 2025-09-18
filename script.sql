CREATE DATABASE sistema_renta_vehiculos;
USE sistema_renta_vehiculos;

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    cedula VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE Vehiculo (
    idVehiculo INT PRIMARY KEY AUTO_INCREMENT,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    año INT,
    reservado BOOLEAN DEFAULT FALSE );

CREATE TABLE Terrestre (
    idVehiculo INT PRIMARY KEY,
    no_puertas INT,
    tipo_combustible VARCHAR(20),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE Acuatico (
    idVehiculo INT PRIMARY KEY,
    calado DECIMAL(5,2),
    eslora DECIMAL(5,2),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE Camion (
    idVehiculo INT PRIMARY KEY,
    capacidad_carga DECIMAL(8,2),
    ejes INT,
    FOREIGN KEY (idVehiculo) REFERENCES Terrestre(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE AutoElectrico (
    idVehiculo INT PRIMARY KEY,
    capacidad_bateria DECIMAL(6,2),
    autonomia DECIMAL(6,2),
    FOREIGN KEY (idVehiculo) REFERENCES Terrestre(idVehiculo) ON DELETE CASCADE
);


CREATE TABLE AutoCombustion (
    idVehiculo INT PRIMARY KEY,
    FOREIGN KEY (idVehiculo) REFERENCES Terrestre(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE Yate (
    idVehiculo INT PRIMARY KEY,
    motorElectrico BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idVehiculo) REFERENCES Acuatico(idVehiculo) ON DELETE CASCADE
);

CREATE TABLE Reservacion (
    idReservacion INT PRIMARY KEY AUTO_INCREMENT,
    idVehiculo INT NOT NULL,
    idCliente INT,
    activa BOOLEAN DEFAULT TRUE,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo)
);

CREATE TABLE Renta (
    idRenta INT PRIMARY KEY AUTO_INCREMENT,
    idCliente INT NOT NULL,
    idVehiculo INT NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    tarifaDiaria DECIMAL(8,2) NOT NULL,
    Estado VARCHAR(20) DEFAULT 'Activa',
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo)
);

CREATE TABLE Factura (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    idRenta INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idRenta) REFERENCES Renta(idRenta)
);

CREATE TABLE Observacion (
    id_observacion INT PRIMARY KEY AUTO_INCREMENT,
    idRenta INT NOT NULL,
    descripcion TEXT,
    FOREIGN KEY (idRenta) REFERENCES Renta(idRenta)
);

-- Inserts de ejemplo

-- Usuarios
INSERT INTO Usuario (cedula, contrasena) VALUES ('123456789', 'password123');
INSERT INTO Usuario (cedula, contrasena) VALUES ('987654321', 'admin123');

-- Clientes
INSERT INTO Cliente (nombre, telefono, cedula) VALUES ('Juan Pérez', '555-1234', '123456789');
INSERT INTO Cliente (nombre, telefono, cedula) VALUES ('María García', '555-5678', '987654321');
INSERT INTO Cliente (nombre, telefono, cedula) VALUES ('Carlos López', '555-9012', '456789123');

-- Vehículos
INSERT INTO Vehiculo (marca, modelo, precio, año, reservado) VALUES ('Toyota', 'Corolla', 15000.00, 2020, FALSE);
INSERT INTO Vehiculo (marca, modelo, precio, año, reservado) VALUES ('Honda', 'Civic', 16000.00, 2019, FALSE);
INSERT INTO Vehiculo (marca, modelo, precio, año, reservado) VALUES ('Ford', 'F-150', 25000.00, 2021, FALSE);
INSERT INTO Vehiculo (marca, modelo, precio, año, reservado) VALUES ('BMW', 'X5', 35000.00, 2022, FALSE);

-- Terrestres
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (1, 4, 'Gasolina');
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (2, 4, 'Gasolina');
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (3, 2, 'Diesel');
INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (4, 4, 'Gasolina');

-- AutoCombustion
INSERT INTO AutoCombustion (idVehiculo) VALUES (1);
INSERT INTO AutoCombustion (idVehiculo) VALUES (2);
INSERT INTO AutoCombustion (idVehiculo) VALUES (3);
INSERT INTO AutoCombustion (idVehiculo) VALUES (4);

-- Camion
INSERT INTO Camion (idVehiculo, capacidad_carga, ejes) VALUES (3, 1000.00, 2);

-- Reservaciones
INSERT INTO Reservacion (idVehiculo, idCliente, activa, fechaInicio, fechaFin) VALUES (1, 1, TRUE, '2025-09-20', '2025-09-22');
INSERT INTO Reservacion (idVehiculo, idCliente, activa, fechaInicio, fechaFin) VALUES (2, 2, TRUE, '2025-09-25', '2025-09-27');

-- Rentas
INSERT INTO Renta (idCliente, idVehiculo, fechaInicio, fechaFin, tarifaDiaria, Estado) VALUES (1, 1, '2025-09-18', '2025-09-20', 50.00, 'Activa');
INSERT INTO Renta (idCliente, idVehiculo, fechaInicio, fechaFin, tarifaDiaria, Estado) VALUES (2, 2, '2025-09-15', '2025-09-17', 60.00, 'Completada');

-- Observaciones
INSERT INTO Observacion (idRenta, descripcion) VALUES (1, 'Vehículo en buen estado');
INSERT INTO Observacion (idRenta, descripcion) VALUES (2, 'Cliente devolvió tarde');