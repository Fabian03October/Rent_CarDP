package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SistemaSimpleBD {
    private Scanner scanner;
    private Connection conexion;
    private String usuarioActual;
    private String rolActual;
    
    public SistemaSimpleBD() {
        scanner = new Scanner(System.in);
        conectarBaseDatos();
    }
    
    private void conectarBaseDatos() {
        try {
            String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
            String usuario = "root";
            String password = "Castillejos16"; // Tu contraseña configurada
            
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("✅ Conexión exitosa a MySQL");
            crearBaseDatos();
            
        } catch (SQLException e) {
            System.out.println("❌ Error conectando a MySQL: " + e.getMessage());
            System.out.println("Verifica que MySQL esté ejecutándose y la contraseña sea correcta");
        }
    }
    
    private void crearBaseDatos() {
        try {
            // Crear base de datos si no existe
            PreparedStatement stmt = conexion.prepareStatement("CREATE DATABASE IF NOT EXISTS rent_car");
            stmt.executeUpdate();
            
            // Usar la base de datos
            stmt = conexion.prepareStatement("USE rent_car");
            stmt.executeUpdate();
            
            // Crear tabla Usuario si no existe
            String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS Usuario (
                    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
                    cedula VARCHAR(20) UNIQUE NOT NULL,
                    contrasena VARCHAR(255) NOT NULL,
                    rol VARCHAR(20) DEFAULT 'EMPLEADO',
                    nombre VARCHAR(100),
                    apellido VARCHAR(100),
                    activo BOOLEAN DEFAULT true
                )
                """;
            stmt = conexion.prepareStatement(sqlUsuario);
            stmt.executeUpdate();
            
            // Insertar usuarios por defecto si no existen
            String checkUsuario = "SELECT COUNT(*) FROM Usuario WHERE cedula = ?";
            
            // Admin
            PreparedStatement checkStmt = conexion.prepareStatement(checkUsuario);
            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO Usuario (cedula, contrasena, rol, nombre, apellido) VALUES ('admin', 'admin123', 'ADMIN', 'Administrador', 'Sistema')";
                stmt = conexion.prepareStatement(insertAdmin);
                stmt.executeUpdate();
            }
            
            // Empleado
            checkStmt.setString(1, "12345678");
            rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertEmpleado = "INSERT INTO Usuario (cedula, contrasena, rol, nombre, apellido) VALUES ('12345678', 'empleado123', 'EMPLEADO', 'Juan', 'Pérez')";
                stmt = conexion.prepareStatement(insertEmpleado);
                stmt.executeUpdate();
            }
            
            System.out.println("✅ Base de datos rent_car configurada correctamente");
            
        } catch (SQLException e) {
            System.out.println("❌ Error configurando base de datos: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SistemaSimpleBD sistema = new SistemaSimpleBD();
        if (sistema.conexion != null) {
            sistema.mostrarMenuPrincipal();
        }
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("\\n=== SISTEMA DE ALQUILER DE VEHICULOS ===");
        System.out.println("VERSION CON BASE DE DATOS\\n");
        
        while (true) {
            System.out.println("--- MENU PRINCIPAL ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Ver Usuarios en BD");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        iniciarSesion();
                        break;
                    case 2:
                        mostrarUsuarios();
                        break;
                    case 3:
                        System.out.println("¡Hasta luego!");
                        if (conexion != null) {
                            try {
                                conexion.close();
                            } catch (SQLException e) {
                                System.out.println("Error cerrando conexión: " + e.getMessage());
                            }
                        }
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void iniciarSesion() {
        System.out.println("\\n--- INICIAR SESION ---");
        System.out.print("Usuario (cédula): ");
        String cedula = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (autenticar(cedula, password)) {
            System.out.println("✅ ¡Bienvenido " + usuarioActual + "!");
            System.out.println("Rol: " + rolActual);
            
            if ("ADMIN".equals(rolActual)) {
                mostrarMenuAdmin();
            } else if ("EMPLEADO".equals(rolActual)) {
                mostrarMenuEmpleado();
            }
        } else {
            System.out.println("❌ Credenciales incorrectas");
            System.out.println("💡 Credenciales válidas:");
            System.out.println("   Admin: admin / admin123");
            System.out.println("   Empleado: 12345678 / empleado123");
        }
    }
    
    private boolean autenticar(String cedula, String password) {
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return false;
        }
        
        String sql = "SELECT nombre, apellido, rol FROM Usuario WHERE cedula = ? AND contrasena = ? AND activo = true";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                usuarioActual = nombre + (apellido != null ? " " + apellido : "");
                rolActual = rs.getString("rol");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error en autenticación: " + e.getMessage());
        }
        
        return false;
    }
    
    private void mostrarUsuarios() {
        System.out.println("\\n--- USUARIOS EN BASE DE DATOS ---");
        
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return;
        }
        
        String sql = "SELECT cedula, nombre, apellido, rol, activo FROM Usuario";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("Cédula\\t\\tNombre\\t\\tRol\\t\\tActivo");
            System.out.println("------------------------------------------------");
            
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String nombreCompleto = nombre + (apellido != null ? " " + apellido : "");
                
                System.out.printf("%-15s %-15s %-10s %s%n",
                    rs.getString("cedula"),
                    nombreCompleto,
                    rs.getString("rol"),
                    rs.getBoolean("activo") ? "Sí" : "No"
                );
            }
        } catch (SQLException e) {
            System.out.println("Error consultando usuarios: " + e.getMessage());
        }
    }
    
    private void mostrarMenuAdmin() {
        while (true) {
            System.out.println("\\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Gestionar Vehículos");
            System.out.println("2. Gestionar Usuarios");
            System.out.println("3. Gestionar Tarifas");
            System.out.println("4. Ver Reportes");
            System.out.println("5. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1 -> gestionarVehiculos();
                    case 2 -> gestionarUsuarios();
                    case 3 -> gestionarTarifas();
                    case 4 -> mostrarReportes();
                    case 5 -> {
                        System.out.println("Cerrando sesión...");
                        usuarioActual = null;
                        rolActual = null;
                        return;
                    }
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void gestionarTarifas() {
        System.out.println("\\n--- GESTIONAR TARIFAS ---");
        System.out.println("1. Ver Tarifas Actuales");
        System.out.println("2. Actualizar Tarifa");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1 -> mostrarTarifas();
                case 2 -> actualizarTarifa();
                case 3 -> { return; }
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void mostrarTarifas() {
        System.out.println("\\n--- TARIFAS ACTUALES ---");
        
        String sql = "SELECT tipo_vehiculo, precio_por_dia, descripcion, activo FROM PrecioVehiculo ORDER BY tipo_vehiculo";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("Tipo Vehículo\\t\\tPrecio/Día\\tEstado\\t\\tDescripción");
            System.out.println("----------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-20s\\t$%-10.2f\\t%-8s\\t%s%n",
                    rs.getString("tipo_vehiculo"),
                    rs.getDouble("precio_por_dia"),
                    rs.getBoolean("activo") ? "Activo" : "Inactivo",
                    rs.getString("descripcion")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando tarifas: " + e.getMessage());
        }
    }
    
    private void actualizarTarifa() {
        System.out.println("\\n--- ACTUALIZAR TARIFA ---");
        mostrarTarifas();
        
        System.out.println("\\nTipos disponibles:");
        System.out.println("1. AUTOCOMBUSTION");
        System.out.println("2. AUTOELECTRICO");
        System.out.println("3. CAMION");
        System.out.println("4. YATE");
        
        try {
            System.out.print("\\nSeleccione tipo de vehículo (1-4): ");
            int tipoOpcion = Integer.parseInt(scanner.nextLine());
            
            String tipoVehiculo = switch (tipoOpcion) {
                case 1 -> "AUTOCOMBUSTION";
                case 2 -> "AUTOELECTRICO";
                case 3 -> "CAMION";
                case 4 -> "YATE";
                default -> {
                    System.out.println("Opción inválida");
                    yield null;
                }
            };
            
            if (tipoVehiculo == null) return;
            
            System.out.print("Nuevo precio por día: $");
            double nuevoPrecio = Double.parseDouble(scanner.nextLine());
            
            if (nuevoPrecio <= 0) {
                System.out.println("El precio debe ser mayor a 0");
                return;
            }
            
            String sql = "UPDATE PrecioVehiculo SET precio_por_dia = ? WHERE tipo_vehiculo = ?";
            
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setDouble(1, nuevoPrecio);
            stmt.setString(2, tipoVehiculo);
            
            if (stmt.executeUpdate() > 0) {
                System.out.printf("✅ Tarifa actualizada: %s = $%.2f por día%n", tipoVehiculo, nuevoPrecio);
            } else {
                System.out.println("❌ Error actualizando la tarifa");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        } catch (SQLException e) {
            System.out.println("Error actualizando tarifa: " + e.getMessage());
        }
    }
    
    // ========================================
    // GESTIÓN DE VEHÍCULOS (ADMIN)
    // ========================================
    
    private void gestionarVehiculos() {
        System.out.println("\\n--- GESTIONAR VEHICULOS ---");
        System.out.println("1. Ver Todos los Vehículos");
        System.out.println("2. Agregar Vehículo");
        System.out.println("3. Modificar Vehículo");
        System.out.println("4. Eliminar Vehículo");
        System.out.println("5. Cambiar Disponibilidad");
        System.out.println("6. Volver");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1 -> mostrarTodosVehiculos();
                case 2 -> agregarVehiculo();
                case 3 -> modificarVehiculo();
                case 4 -> eliminarVehiculo();
                case 5 -> cambiarDisponibilidad();
                case 6 -> { return; }
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void mostrarTodosVehiculos() {
        System.out.println("\\n--- TODOS LOS VEHICULOS ---");
        
        String sql = """
            SELECT v.idVehiculo, v.marca, v.modelo, v.año, v.precio, v.disponible,
                   CASE 
                       WHEN ae.idVehiculo IS NOT NULL THEN 'Auto Eléctrico'
                       WHEN ac.idVehiculo IS NOT NULL THEN 'Auto Combustión'
                       WHEN c.idVehiculo IS NOT NULL THEN 'Camión'
                       WHEN y.idVehiculo IS NOT NULL THEN 'Yate'
                       ELSE 'Vehículo'
                   END AS tipo_vehiculo,
                   COALESCE(ae.capacidad_bateria, 0) as bateria,
                   COALESCE(ae.autonomia, 0) as autonomia,
                   COALESCE(c.carga, 0) as carga,
                   COALESCE(t.no_puertas, 0) as puertas,
                   COALESCE(y.motorElectrico, false) as motor_electrico
            FROM Vehiculo v
            LEFT JOIN Terrestre t ON v.idVehiculo = t.idVehiculo
            LEFT JOIN Acuatico ac2 ON v.idVehiculo = ac2.idVehiculo
            LEFT JOIN AutoCombustion ac ON v.idVehiculo = ac.idVehiculo
            LEFT JOIN AutoElectrico ae ON v.idVehiculo = ae.idVehiculo
            LEFT JOIN Camion c ON v.idVehiculo = c.idVehiculo
            LEFT JOIN Yate y ON v.idVehiculo = y.idVehiculo
            ORDER BY v.marca, v.modelo
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tTipo\\t\\tMarca\\t\\tModelo\\t\\tAño\\tPrecio\\t\\tDisponible\\tDetalles");
            System.out.println("----------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                String detalles = "";
                String tipo = rs.getString("tipo_vehiculo");
                
                if ("Auto Eléctrico".equals(tipo)) {
                    detalles = String.format("Batería: %.1fkWh, Autonomía: %.0fkm", 
                        rs.getDouble("bateria"), rs.getDouble("autonomia"));
                } else if ("Camión".equals(tipo)) {
                    detalles = String.format("Carga: %.1f ton", rs.getDouble("carga"));
                } else if ("Yate".equals(tipo)) {
                    detalles = String.format("Motor eléctrico: %s", 
                        rs.getBoolean("motor_electrico") ? "Sí" : "No");
                } else if (rs.getInt("puertas") > 0) {
                    detalles = String.format("Puertas: %d", rs.getInt("puertas"));
                }
                
                System.out.printf("%-3d\\t%-15s\\t%-10s\\t%-10s\\t%d\\t$%-10.2f\\t%-10s\\t%s%n",
                    rs.getInt("idVehiculo"),
                    tipo,
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("año"),
                    rs.getDouble("precio"),
                    rs.getBoolean("disponible") ? "Sí" : "No",
                    detalles
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando vehículos: " + e.getMessage());
        }
    }
    
    private void agregarVehiculo() {
        System.out.println("\\n--- AGREGAR VEHICULO ---");
        System.out.println("Tipos disponibles:");
        System.out.println("1. Auto de Combustión");
        System.out.println("2. Auto Eléctrico");
        System.out.println("3. Camión");
        System.out.println("4. Yate");
        
        try {
            System.out.print("Seleccione tipo (1-4): ");
            int tipo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Marca: ");
            String marca = scanner.nextLine();
            
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();
            
            System.out.print("Año: ");
            int año = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            
            // Insertar vehículo base
            String sqlVehiculo = "INSERT INTO Vehiculo (marca, modelo, precio, año) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtVehiculo = conexion.prepareStatement(sqlVehiculo, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtVehiculo.setString(1, marca);
            stmtVehiculo.setString(2, modelo);
            stmtVehiculo.setDouble(3, precio);
            stmtVehiculo.setInt(4, año);
            
            if (stmtVehiculo.executeUpdate() > 0) {
                ResultSet keys = stmtVehiculo.getGeneratedKeys();
                if (keys.next()) {
                    int idVehiculo = keys.getInt(1);
                    
                    switch (tipo) {
                        case 1 -> crearAutoCombustion(idVehiculo);
                        case 2 -> crearAutoElectrico(idVehiculo);
                        case 3 -> crearCamion(idVehiculo);
                        case 4 -> crearYate(idVehiculo);
                    }
                    
                    System.out.println("✅ Vehículo agregado exitosamente con ID: " + idVehiculo);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error agregando vehículo: " + e.getMessage());
        }
    }
    
    private void crearAutoCombustion(int idVehiculo) throws SQLException {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Tipo de combustible (Gasolina/Diesel/Gas): ");
        String combustible = scanner.nextLine();
        
        // Insertar en Terrestre
        String sqlTerrestre = "INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (?, ?, ?)";
        PreparedStatement stmtTerrestre = conexion.prepareStatement(sqlTerrestre);
        stmtTerrestre.setInt(1, idVehiculo);
        stmtTerrestre.setInt(2, puertas);
        stmtTerrestre.setString(3, combustible);
        stmtTerrestre.executeUpdate();
        
        // Insertar en AutoCombustion
        String sqlAuto = "INSERT INTO AutoCombustion (idVehiculo, tipo_combustible) VALUES (?, ?)";
        PreparedStatement stmtAuto = conexion.prepareStatement(sqlAuto);
        stmtAuto.setInt(1, idVehiculo);
        stmtAuto.setString(2, combustible);
        stmtAuto.executeUpdate();
    }
    
    private void crearAutoElectrico(int idVehiculo) throws SQLException {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Capacidad de batería (kWh): ");
        double bateria = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Autonomía (km): ");
        double autonomia = Double.parseDouble(scanner.nextLine());
        
        // Insertar en Terrestre
        String sqlTerrestre = "INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (?, ?, 'Eléctrico')";
        PreparedStatement stmtTerrestre = conexion.prepareStatement(sqlTerrestre);
        stmtTerrestre.setInt(1, idVehiculo);
        stmtTerrestre.setInt(2, puertas);
        stmtTerrestre.executeUpdate();
        
        // Insertar en AutoCombustion
        String sqlAuto = "INSERT INTO AutoCombustion (idVehiculo, tipo_combustible) VALUES (?, 'Eléctrico')";
        PreparedStatement stmtAuto = conexion.prepareStatement(sqlAuto);
        stmtAuto.setInt(1, idVehiculo);
        stmtAuto.executeUpdate();
        
        // Insertar en AutoElectrico
        String sqlElectrico = "INSERT INTO AutoElectrico (idVehiculo, capacidad_bateria, autonomia) VALUES (?, ?, ?)";
        PreparedStatement stmtElectrico = conexion.prepareStatement(sqlElectrico);
        stmtElectrico.setInt(1, idVehiculo);
        stmtElectrico.setDouble(2, bateria);
        stmtElectrico.setDouble(3, autonomia);
        stmtElectrico.executeUpdate();
    }
    
    private void crearCamion(int idVehiculo) throws SQLException {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Tipo de combustible (Diesel/Gasolina): ");
        String combustible = scanner.nextLine();
        
        System.out.print("Capacidad de carga (toneladas): ");
        double carga = Double.parseDouble(scanner.nextLine());
        
        // Insertar en Terrestre
        String sqlTerrestre = "INSERT INTO Terrestre (idVehiculo, no_puertas, tipo_combustible) VALUES (?, ?, ?)";
        PreparedStatement stmtTerrestre = conexion.prepareStatement(sqlTerrestre);
        stmtTerrestre.setInt(1, idVehiculo);
        stmtTerrestre.setInt(2, puertas);
        stmtTerrestre.setString(3, combustible);
        stmtTerrestre.executeUpdate();
        
        // Insertar en Camion
        String sqlCamion = "INSERT INTO Camion (idVehiculo, carga) VALUES (?, ?)";
        PreparedStatement stmtCamion = conexion.prepareStatement(sqlCamion);
        stmtCamion.setInt(1, idVehiculo);
        stmtCamion.setDouble(2, carga);
        stmtCamion.executeUpdate();
    }
    
    private void crearYate(int idVehiculo) throws SQLException {
        System.out.print("Eslora (longitud en metros): ");
        double eslora = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Calado (profundidad en metros): ");
        double calado = Double.parseDouble(scanner.nextLine());
        
        System.out.print("¿Tiene motor eléctrico? (s/n): ");
        boolean motorElectrico = "s".equalsIgnoreCase(scanner.nextLine());
        
        // Insertar en Acuatico
        String sqlAcuatico = "INSERT INTO Acuatico (idVehiculo, eslora, calado) VALUES (?, ?, ?)";
        PreparedStatement stmtAcuatico = conexion.prepareStatement(sqlAcuatico);
        stmtAcuatico.setInt(1, idVehiculo);
        stmtAcuatico.setDouble(2, eslora);
        stmtAcuatico.setDouble(3, calado);
        stmtAcuatico.executeUpdate();
        
        // Insertar en Yate
        String sqlYate = "INSERT INTO Yate (idVehiculo, motorElectrico) VALUES (?, ?)";
        PreparedStatement stmtYate = conexion.prepareStatement(sqlYate);
        stmtYate.setInt(1, idVehiculo);
        stmtYate.setBoolean(2, motorElectrico);
        stmtYate.executeUpdate();
    }
    
    private void modificarVehiculo() {
        System.out.println("\\n--- MODIFICAR VEHICULO ---");
        mostrarTodosVehiculos();
        
        try {
            System.out.print("\\nID del vehículo a modificar: ");
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nueva marca (Enter para mantener): ");
            String marca = scanner.nextLine();
            
            System.out.print("Nuevo modelo (Enter para mantener): ");
            String modelo = scanner.nextLine();
            
            System.out.print("Nuevo año (0 para mantener): ");
            String añoStr = scanner.nextLine();
            
            System.out.print("Nuevo precio (0 para mantener): ");
            String precioStr = scanner.nextLine();
            
            StringBuilder sql = new StringBuilder("UPDATE Vehiculo SET ");
            boolean hayActualizacion = false;
            
            if (!marca.isEmpty()) {
                sql.append("marca = '").append(marca).append("'");
                hayActualizacion = true;
            }
            
            if (!modelo.isEmpty()) {
                if (hayActualizacion) sql.append(", ");
                sql.append("modelo = '").append(modelo).append("'");
                hayActualizacion = true;
            }
            
            if (!añoStr.isEmpty() && !añoStr.equals("0")) {
                if (hayActualizacion) sql.append(", ");
                sql.append("año = ").append(Integer.parseInt(añoStr));
                hayActualizacion = true;
            }
            
            if (!precioStr.isEmpty() && !precioStr.equals("0")) {
                if (hayActualizacion) sql.append(", ");
                sql.append("precio = ").append(Double.parseDouble(precioStr));
                hayActualizacion = true;
            }
            
            if (hayActualizacion) {
                sql.append(" WHERE idVehiculo = ").append(idVehiculo);
                
                PreparedStatement stmt = conexion.prepareStatement(sql.toString());
                if (stmt.executeUpdate() > 0) {
                    System.out.println("✅ Vehículo modificado exitosamente");
                } else {
                    System.out.println("❌ Vehículo no encontrado");
                }
            } else {
                System.out.println("No se realizaron cambios");
            }
            
        } catch (Exception e) {
            System.out.println("Error modificando vehículo: " + e.getMessage());
        }
    }
    
    private void eliminarVehiculo() {
        System.out.println("\\n--- ELIMINAR VEHICULO ---");
        mostrarTodosVehiculos();
        
        try {
            System.out.print("\\nID del vehículo a eliminar: ");
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("¿Está seguro? Esta acción no se puede deshacer (s/n): ");
            String confirmar = scanner.nextLine();
            
            if ("s".equalsIgnoreCase(confirmar)) {
                String sql = "DELETE FROM Vehiculo WHERE idVehiculo = ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, idVehiculo);
                
                if (stmt.executeUpdate() > 0) {
                    System.out.println("✅ Vehículo eliminado exitosamente");
                } else {
                    System.out.println("❌ Vehículo no encontrado");
                }
            } else {
                System.out.println("Eliminación cancelada");
            }
            
        } catch (Exception e) {
            System.out.println("Error eliminando vehículo: " + e.getMessage());
        }
    }
    
    private void cambiarDisponibilidad() {
        System.out.println("\\n--- CAMBIAR DISPONIBILIDAD ---");
        mostrarTodosVehiculos();
        
        try {
            System.out.print("\\nID del vehículo: ");
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("¿Disponible? (s/n): ");
            boolean disponible = "s".equalsIgnoreCase(scanner.nextLine());
            
            actualizarDisponibilidadVehiculo(idVehiculo, disponible);
            System.out.println("✅ Disponibilidad actualizada");
            
        } catch (Exception e) {
            System.out.println("Error cambiando disponibilidad: " + e.getMessage());
        }
    }
    
    // ========================================
    // GESTIÓN DE USUARIOS (ADMIN)
    // ========================================
    
    private void gestionarUsuarios() {
        System.out.println("\\n--- GESTIONAR USUARIOS ---");
        System.out.println("1. Ver Todos los Usuarios");
        System.out.println("2. Crear Usuario");
        System.out.println("3. Modificar Usuario");
        System.out.println("4. Cambiar Estado (Activar/Desactivar)");
        System.out.println("5. Cambiar Rol");
        System.out.println("6. Volver");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1 -> mostrarTodosUsuarios();
                case 2 -> crearUsuario();
                case 3 -> modificarUsuario();
                case 4 -> cambiarEstadoUsuario();
                case 5 -> cambiarRolUsuario();
                case 6 -> { return; }
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void mostrarTodosUsuarios() {
        System.out.println("\\n--- TODOS LOS USUARIOS ---");
        
        String sql = "SELECT idUsuario, cedula, nombre, apellido, rol, telefono, email, activo FROM Usuario ORDER BY rol, nombre";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tCédula\\t\\tNombre Completo\\t\\tRol\\t\\tTeléfono\\t\\tEmail\\t\\t\\tActivo");
            System.out.println("----------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + 
                    (rs.getString("apellido") != null ? rs.getString("apellido") : "");
                
                System.out.printf("%-3d\\t%-15s\\t%-20s\\t%-10s\\t%-15s\\t%-20s\\t%s%n",
                    rs.getInt("idUsuario"),
                    rs.getString("cedula"),
                    nombreCompleto,
                    rs.getString("rol"),
                    rs.getString("telefono") != null ? rs.getString("telefono") : "N/A",
                    rs.getString("email") != null ? rs.getString("email") : "N/A",
                    rs.getBoolean("activo") ? "Sí" : "No"
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando usuarios: " + e.getMessage());
        }
    }
    
    private void crearUsuario() {
        System.out.println("\\n--- CREAR USUARIO ---");
        
        try {
            System.out.print("Cédula: ");
            String cedula = scanner.nextLine();
            
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            
            System.out.print("Teléfono (opcional): ");
            String telefono = scanner.nextLine();
            if (telefono.isEmpty()) telefono = null;
            
            System.out.print("Email (opcional): ");
            String email = scanner.nextLine();
            if (email.isEmpty()) email = null;
            
            System.out.print("Rol (ADMIN/EMPLEADO): ");
            String rol = scanner.nextLine().toUpperCase();
            
            if (!rol.equals("ADMIN") && !rol.equals("EMPLEADO")) {
                System.out.println("Rol inválido. Use ADMIN o EMPLEADO");
                return;
            }
            
            String sql = "INSERT INTO Usuario (cedula, contrasena, nombre, apellido, telefono, email, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, cedula);
            stmt.setString(2, contrasena);
            stmt.setString(3, nombre);
            stmt.setString(4, apellido);
            stmt.setString(5, telefono);
            stmt.setString(6, email);
            stmt.setString(7, rol);
            
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Usuario creado exitosamente");
            } else {
                System.out.println("❌ Error creando usuario");
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("❌ Ya existe un usuario con esa cédula");
            } else {
                System.out.println("Error creando usuario: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void modificarUsuario() {
        System.out.println("\\n--- MODIFICAR USUARIO ---");
        mostrarTodosUsuarios();
        
        try {
            System.out.print("\\nID del usuario a modificar: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine();
            
            System.out.print("Nuevo apellido (Enter para mantener): ");
            String apellido = scanner.nextLine();
            
            System.out.print("Nuevo teléfono (Enter para mantener): ");
            String telefono = scanner.nextLine();
            
            System.out.print("Nuevo email (Enter para mantener): ");
            String email = scanner.nextLine();
            
            System.out.print("Nueva contraseña (Enter para mantener): ");
            String contrasena = scanner.nextLine();
            
            StringBuilder sql = new StringBuilder("UPDATE Usuario SET ");
            boolean hayActualizacion = false;
            
            if (!nombre.isEmpty()) {
                sql.append("nombre = '").append(nombre).append("'");
                hayActualizacion = true;
            }
            
            if (!apellido.isEmpty()) {
                if (hayActualizacion) sql.append(", ");
                sql.append("apellido = '").append(apellido).append("'");
                hayActualizacion = true;
            }
            
            if (!telefono.isEmpty()) {
                if (hayActualizacion) sql.append(", ");
                sql.append("telefono = '").append(telefono).append("'");
                hayActualizacion = true;
            }
            
            if (!email.isEmpty()) {
                if (hayActualizacion) sql.append(", ");
                sql.append("email = '").append(email).append("'");
                hayActualizacion = true;
            }
            
            if (!contrasena.isEmpty()) {
                if (hayActualizacion) sql.append(", ");
                sql.append("contrasena = '").append(contrasena).append("'");
                hayActualizacion = true;
            }
            
            if (hayActualizacion) {
                sql.append(" WHERE idUsuario = ").append(idUsuario);
                
                PreparedStatement stmt = conexion.prepareStatement(sql.toString());
                if (stmt.executeUpdate() > 0) {
                    System.out.println("✅ Usuario modificado exitosamente");
                } else {
                    System.out.println("❌ Usuario no encontrado");
                }
            } else {
                System.out.println("No se realizaron cambios");
            }
            
        } catch (Exception e) {
            System.out.println("Error modificando usuario: " + e.getMessage());
        }
    }
    
    private void cambiarEstadoUsuario() {
        System.out.println("\\n--- CAMBIAR ESTADO USUARIO ---");
        mostrarTodosUsuarios();
        
        try {
            System.out.print("\\nID del usuario: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());
            
            System.out.print("¿Activar usuario? (s/n): ");
            boolean activo = "s".equalsIgnoreCase(scanner.nextLine());
            
            String sql = "UPDATE Usuario SET activo = ? WHERE idUsuario = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setBoolean(1, activo);
            stmt.setInt(2, idUsuario);
            
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Estado del usuario actualizado");
            } else {
                System.out.println("❌ Usuario no encontrado");
            }
            
        } catch (Exception e) {
            System.out.println("Error cambiando estado: " + e.getMessage());
        }
    }
    
    private void cambiarRolUsuario() {
        System.out.println("\\n--- CAMBIAR ROL USUARIO ---");
        mostrarTodosUsuarios();
        
        try {
            System.out.print("\\nID del usuario: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nuevo rol (ADMIN/EMPLEADO): ");
            String rol = scanner.nextLine().toUpperCase();
            
            if (!rol.equals("ADMIN") && !rol.equals("EMPLEADO")) {
                System.out.println("Rol inválido. Use ADMIN o EMPLEADO");
                return;
            }
            
            String sql = "UPDATE Usuario SET rol = ? WHERE idUsuario = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, rol);
            stmt.setInt(2, idUsuario);
            
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Rol actualizado exitosamente");
            } else {
                System.out.println("❌ Usuario no encontrado");
            }
            
        } catch (Exception e) {
            System.out.println("Error cambiando rol: " + e.getMessage());
        }
    }
    
    // ========================================
    // SISTEMA DE REPORTES (ADMIN)
    // ========================================
    
    private void mostrarReportes() {
        System.out.println("\\n--- REPORTES DEL SISTEMA ---");
        System.out.println("1. Alquileres Activos");
        System.out.println("2. Historial de Alquileres");
        System.out.println("3. Ingresos por Período");
        System.out.println("4. Vehículos Más Rentados");
        System.out.println("5. Estadísticas Generales");
        System.out.println("6. Volver");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1 -> reporteAlquileresActivos();
                case 2 -> reporteHistorialAlquileres();
                case 3 -> reporteIngresosPeriodo();
                case 4 -> reporteVehiculosMasRentados();
                case 5 -> reporteEstadisticasGenerales();
                case 6 -> { return; }
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void reporteAlquileresActivos() {
        System.out.println("\\n--- ALQUILERES ACTIVOS ---");
        
        String sql = """
            SELECT r.idRenta, c.nombre, c.cedula, v.marca, v.modelo, 
                   r.fechaInicio, r.fechaFin, r.tarifaDiaria, r.precio_total
            FROM Renta r
            JOIN Cliente c ON r.idCliente = c.idCliente
            JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
            WHERE r.Estado = 'ACTIVA'
            ORDER BY r.fechaInicio
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tCliente\\t\\t\\tVehículo\\t\\tInicio\\t\\tFin\\t\\tTarifa\\t\\tTotal");
            System.out.println("----------------------------------------------------------------------------------------");
            
            double totalIngresos = 0;
            int totalAlquileres = 0;
            
            while (rs.next()) {
                totalAlquileres++;
                double total = rs.getDouble("precio_total");
                totalIngresos += total;
                
                System.out.printf("%-3d\\t%-20s\\t%-15s\\t%s\\t%s\\t$%-10.2f\\t$%.2f%n",
                    rs.getInt("idRenta"),
                    rs.getString("nombre"),
                    rs.getString("marca") + " " + rs.getString("modelo"),
                    rs.getString("fechaInicio"),
                    rs.getString("fechaFin"),
                    rs.getDouble("tarifaDiaria"),
                    total
                );
            }
            
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("Total de alquileres activos: %d\\n", totalAlquileres);
            System.out.printf("Ingresos proyectados: $%.2f\\n", totalIngresos);
            
        } catch (SQLException e) {
            System.out.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    private void reporteHistorialAlquileres() {
        System.out.println("\\n--- HISTORIAL DE ALQUILERES ---");
        
        String sql = """
            SELECT r.idRenta, c.nombre, v.marca, v.modelo, 
                   r.fechaInicio, r.fechaFin, r.precio_total, r.Estado
            FROM Renta r
            JOIN Cliente c ON r.idCliente = c.idCliente
            JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
            ORDER BY r.fechaInicio DESC
            LIMIT 50
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tCliente\\t\\t\\tVehículo\\t\\tInicio\\t\\tFin\\t\\tTotal\\t\\tEstado");
            System.out.println("----------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-3d\\t%-20s\\t%-15s\\t%s\\t%s\\t$%-10.2f\\t%s%n",
                    rs.getInt("idRenta"),
                    rs.getString("nombre"),
                    rs.getString("marca") + " " + rs.getString("modelo"),
                    rs.getString("fechaInicio"),
                    rs.getString("fechaFin"),
                    rs.getDouble("precio_total"),
                    rs.getString("Estado")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    private void reporteIngresosPeriodo() {
        System.out.println("\\n--- INGRESOS POR PERÍODO ---");
        
        try {
            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fechaInicio = scanner.nextLine();
            
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fechaFin = scanner.nextLine();
            
            String sql = """
                SELECT 
                    COUNT(*) as total_alquileres,
                    SUM(precio_total) as ingresos_totales,
                    AVG(precio_total) as promedio_alquiler,
                    MIN(precio_total) as minimo,
                    MAX(precio_total) as maximo
                FROM Renta 
                WHERE fechaInicio >= ? AND fechaFin <= ?
                """;
            
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\\n📊 RESUMEN DEL PERÍODO:");
                System.out.printf("Total de alquileres: %d\\n", rs.getInt("total_alquileres"));
                System.out.printf("Ingresos totales: $%.2f\\n", rs.getDouble("ingresos_totales"));
                System.out.printf("Promedio por alquiler: $%.2f\\n", rs.getDouble("promedio_alquiler"));
                System.out.printf("Alquiler mínimo: $%.2f\\n", rs.getDouble("minimo"));
                System.out.printf("Alquiler máximo: $%.2f\\n", rs.getDouble("maximo"));
            }
            
        } catch (Exception e) {
            System.out.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    private void reporteVehiculosMasRentados() {
        System.out.println("\\n--- VEHÍCULOS MÁS RENTADOS ---");
        
        String sql = """
            SELECT v.marca, v.modelo, 
                   COUNT(*) as veces_rentado,
                   SUM(r.precio_total) as ingresos_generados,
                   AVG(r.precio_total) as promedio_alquiler
            FROM Renta r
            JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
            GROUP BY v.idVehiculo, v.marca, v.modelo
            ORDER BY veces_rentado DESC, ingresos_generados DESC
            LIMIT 10
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("Vehículo\\t\\t\\tVeces Rentado\\tIngresos\\t\\tPromedio");
            System.out.println("--------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-20s\\t%-12d\\t$%-12.2f\\t$%.2f%n",
                    rs.getString("marca") + " " + rs.getString("modelo"),
                    rs.getInt("veces_rentado"),
                    rs.getDouble("ingresos_generados"),
                    rs.getDouble("promedio_alquiler")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    private void reporteEstadisticasGenerales() {
        System.out.println("\\n--- ESTADÍSTICAS GENERALES ---");
        
        try {
            // Estadísticas de vehículos
            String sqlVehiculos = """
                SELECT 
                    COUNT(*) as total_vehiculos,
                    SUM(CASE WHEN disponible = true THEN 1 ELSE 0 END) as disponibles,
                    SUM(CASE WHEN disponible = false THEN 1 ELSE 0 END) as rentados
                FROM Vehiculo
                """;
            
            PreparedStatement stmt1 = conexion.prepareStatement(sqlVehiculos);
            ResultSet rs1 = stmt1.executeQuery();
            
            if (rs1.next()) {
                System.out.println("🚗 VEHÍCULOS:");
                System.out.printf("  Total: %d\\n", rs1.getInt("total_vehiculos"));
                System.out.printf("  Disponibles: %d\\n", rs1.getInt("disponibles"));
                System.out.printf("  Rentados: %d\\n", rs1.getInt("rentados"));
            }
            
            // Estadísticas de usuarios
            String sqlUsuarios = """
                SELECT 
                    COUNT(*) as total_usuarios,
                    SUM(CASE WHEN activo = true THEN 1 ELSE 0 END) as activos,
                    SUM(CASE WHEN rol = 'ADMIN' THEN 1 ELSE 0 END) as admins,
                    SUM(CASE WHEN rol = 'EMPLEADO' THEN 1 ELSE 0 END) as empleados
                FROM Usuario
                """;
            
            PreparedStatement stmt2 = conexion.prepareStatement(sqlUsuarios);
            ResultSet rs2 = stmt2.executeQuery();
            
            if (rs2.next()) {
                System.out.println("\\n👥 USUARIOS:");
                System.out.printf("  Total: %d\\n", rs2.getInt("total_usuarios"));
                System.out.printf("  Activos: %d\\n", rs2.getInt("activos"));
                System.out.printf("  Administradores: %d\\n", rs2.getInt("admins"));
                System.out.printf("  Empleados: %d\\n", rs2.getInt("empleados"));
            }
            
            // Estadísticas de alquileres
            String sqlAlquileres = """
                SELECT 
                    COUNT(*) as total_alquileres,
                    SUM(CASE WHEN Estado = 'ACTIVA' THEN 1 ELSE 0 END) as activos,
                    SUM(precio_total) as ingresos_totales
                FROM Renta
                """;
            
            PreparedStatement stmt3 = conexion.prepareStatement(sqlAlquileres);
            ResultSet rs3 = stmt3.executeQuery();
            
            if (rs3.next()) {
                System.out.println("\\n📄 ALQUILERES:");
                System.out.printf("  Total histórico: %d\\n", rs3.getInt("total_alquileres"));
                System.out.printf("  Activos: %d\\n", rs3.getInt("activos"));
                System.out.printf("  Ingresos totales: $%.2f\\n", rs3.getDouble("ingresos_totales"));
            }
            
            // Estadísticas de clientes
            String sqlClientes = "SELECT COUNT(*) as total_clientes FROM Cliente";
            PreparedStatement stmt4 = conexion.prepareStatement(sqlClientes);
            ResultSet rs4 = stmt4.executeQuery();
            
            if (rs4.next()) {
                System.out.println("\\n👤 CLIENTES:");
                System.out.printf("  Total registrados: %d\\n", rs4.getInt("total_clientes"));
            }
            
        } catch (SQLException e) {
            System.out.println("Error generando estadísticas: " + e.getMessage());
        }
    }
    
    // ========================================
    // FUNCIONES ADICIONALES EMPLEADO
    // ========================================
    
    private void finalizarAlquiler() {
        System.out.println("\\n--- FINALIZAR ALQUILER ---");
        verAlquileresActivos();
        
        try {
            System.out.print("\\nID del alquiler a finalizar: ");
            int idRenta = Integer.parseInt(scanner.nextLine());
            
            // Obtener información del alquiler
            String sqlInfo = """
                SELECT r.idVehiculo, r.precio_total, c.nombre, v.marca, v.modelo
                FROM Renta r
                JOIN Cliente c ON r.idCliente = c.idCliente
                JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
                WHERE r.idRenta = ? AND r.Estado = 'ACTIVA'
                """;
            
            PreparedStatement stmtInfo = conexion.prepareStatement(sqlInfo);
            stmtInfo.setInt(1, idRenta);
            ResultSet rsInfo = stmtInfo.executeQuery();
            
            if (!rsInfo.next()) {
                System.out.println("❌ Alquiler no encontrado o ya finalizado");
                return;
            }
            
            int idVehiculo = rsInfo.getInt("idVehiculo");
            double total = rsInfo.getDouble("precio_total");
            String cliente = rsInfo.getString("nombre");
            String vehiculo = rsInfo.getString("marca") + " " + rsInfo.getString("modelo");
            
            System.out.printf("\\n📋 FINALIZANDO ALQUILER:\\n");
            System.out.printf("Cliente: %s\\n", cliente);
            System.out.printf("Vehículo: %s\\n", vehiculo);
            System.out.printf("Total: $%.2f\\n", total);
            
            System.out.print("\\n¿Confirmar finalización? (s/n): ");
            String confirmar = scanner.nextLine();
            
            if ("s".equalsIgnoreCase(confirmar)) {
                // Finalizar alquiler
                String sqlFinalizar = "UPDATE Renta SET Estado = 'FINALIZADA' WHERE idRenta = ?";
                PreparedStatement stmtFinalizar = conexion.prepareStatement(sqlFinalizar);
                stmtFinalizar.setInt(1, idRenta);
                stmtFinalizar.executeUpdate();
                
                // Liberar vehículo
                actualizarDisponibilidadVehiculo(idVehiculo, true);
                
                // Generar factura
                String sqlFactura = "INSERT INTO Factura (idRenta, fecha, total) VALUES (?, CURDATE(), ?)";
                PreparedStatement stmtFactura = conexion.prepareStatement(sqlFactura);
                stmtFactura.setInt(1, idRenta);
                stmtFactura.setDouble(2, total);
                stmtFactura.executeUpdate();
                
                System.out.println("✅ Alquiler finalizado exitosamente");
                System.out.println("✅ Vehículo liberado y disponible");
                System.out.println("✅ Factura generada");
            } else {
                System.out.println("Finalización cancelada");
            }
            
        } catch (Exception e) {
            System.out.println("Error finalizando alquiler: " + e.getMessage());
        }
    }
    
    private void verAlquileresActivos() {
        System.out.println("\\n--- ALQUILERES ACTIVOS ---");
        
        String sql = """
            SELECT r.idRenta, c.nombre, c.cedula, v.marca, v.modelo, 
                   r.fechaInicio, r.fechaFin, r.tarifaDiaria, r.precio_total,
                   DATEDIFF(CURDATE(), r.fechaInicio) as dias_transcurridos
            FROM Renta r
            JOIN Cliente c ON r.idCliente = c.idCliente
            JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
            WHERE r.Estado = 'ACTIVA'
            ORDER BY r.fechaInicio
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tCliente\\t\\t\\tVehículo\\t\\tInicio\\t\\tFin\\t\\tDías\\tTotal");
            System.out.println("----------------------------------------------------------------------------------------");
            
            boolean hayAlquileres = false;
            while (rs.next()) {
                hayAlquileres = true;
                
                int diasTranscurridos = rs.getInt("dias_transcurridos");
                String estado = diasTranscurridos > 0 ? "(" + diasTranscurridos + " días)" : "(Hoy)";
                
                System.out.printf("%-3d\\t%-20s\\t%-15s\\t%s\\t%s\\t%s\\t$%.2f%n",
                    rs.getInt("idRenta"),
                    rs.getString("nombre"),
                    rs.getString("marca") + " " + rs.getString("modelo"),
                    rs.getString("fechaInicio"),
                    rs.getString("fechaFin"),
                    estado,
                    rs.getDouble("precio_total")
                );
            }
            
            if (!hayAlquileres) {
                System.out.println("No hay alquileres activos en este momento.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando alquileres: " + e.getMessage());
        }
    }
    
    private void mostrarMenuEmpleado() {
        while (true) {
            System.out.println("\\n--- MENU EMPLEADO ---");
            System.out.println("1. Ver Vehículos Disponibles");
            System.out.println("2. Procesar Alquiler");
            System.out.println("3. Gestionar Reservas");
            System.out.println("4. Ver Clientes");
            System.out.println("5. Finalizar Alquiler");
            System.out.println("6. Ver Alquileres Activos");
            System.out.println("7. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1 -> mostrarVehiculosDisponibles();
                    case 2 -> procesarAlquiler();
                    case 3 -> gestionarReservas();
                    case 4 -> mostrarClientes();
                    case 5 -> finalizarAlquiler();
                    case 6 -> verAlquileresActivos();
                    case 7 -> {
                        System.out.println("Cerrando sesión...");
                        usuarioActual = null;
                        rolActual = null;
                        return;
                    }
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void mostrarVehiculosDisponibles() {
        System.out.println("\\n--- VEHICULOS DISPONIBLES ---");
        
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return;
        }
        
        String sql = """
            SELECT v.idVehiculo, v.marca, v.modelo, v.año, v.precio, v.disponible,
                   CASE 
                       WHEN ae.idVehiculo IS NOT NULL THEN 'Auto Eléctrico'
                       WHEN ac.idVehiculo IS NOT NULL THEN 'Auto Combustión'
                       WHEN c.idVehiculo IS NOT NULL THEN 'Camión'
                       WHEN y.idVehiculo IS NOT NULL THEN 'Yate'
                       ELSE 'Vehículo'
                   END AS tipo_vehiculo
            FROM Vehiculo v
            LEFT JOIN Terrestre t ON v.idVehiculo = t.idVehiculo
            LEFT JOIN Acuatico ac2 ON v.idVehiculo = ac2.idVehiculo
            LEFT JOIN AutoCombustion ac ON v.idVehiculo = ac.idVehiculo
            LEFT JOIN AutoElectrico ae ON v.idVehiculo = ae.idVehiculo
            LEFT JOIN Camion c ON v.idVehiculo = c.idVehiculo
            LEFT JOIN Yate y ON v.idVehiculo = y.idVehiculo
            WHERE v.disponible = TRUE
            ORDER BY v.marca, v.modelo
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tTipo\\t\\tMarca\\t\\tModelo\\t\\tAño\\tPrecio");
            System.out.println("------------------------------------------------------------");
            
            boolean hayVehiculos = false;
            while (rs.next()) {
                hayVehiculos = true;
                System.out.printf("%-3d\\t%-15s\\t%-10s\\t%-10s\\t%d\\t$%.2f%n",
                    rs.getInt("idVehiculo"),
                    rs.getString("tipo_vehiculo"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("año"),
                    rs.getDouble("precio")
                );
            }
            
            if (!hayVehiculos) {
                System.out.println("No hay vehículos disponibles en este momento.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando vehículos: " + e.getMessage());
        }
    }
    
    private void procesarAlquiler() {
        System.out.println("\\n--- PROCESAR ALQUILER ---");
        
        // Mostrar vehículos disponibles
        mostrarVehiculosDisponibles();
        
        System.out.print("\\nIngrese ID del vehículo a alquilar (0 para cancelar): ");
        try {
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            if (idVehiculo == 0) return;
            
            // Verificar que el vehículo esté disponible
            if (!verificarVehiculoDisponible(idVehiculo)) {
                System.out.println("❌ El vehículo no está disponible o no existe");
                return;
            }
            
            System.out.print("Cédula del cliente: ");
            String cedulaCliente = scanner.nextLine();
            
            // Verificar si el cliente existe, si no, crearlo
            int idCliente = obtenerOCrearCliente(cedulaCliente);
            if (idCliente == -1) return;
            
            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fechaInicio = scanner.nextLine();
            
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fechaFin = scanner.nextLine();
            
            // Obtener tarifa automática según tipo de vehículo
            double tarifaDiaria = obtenerTarifaVehiculo(idVehiculo);
            if (tarifaDiaria <= 0) {
                System.out.println("❌ No se pudo obtener la tarifa para este vehículo");
                return;
            }
            
            // Calcular días y precio total
            int dias = calcularDias(fechaInicio, fechaFin);
            double precioTotal = dias * tarifaDiaria;
            
            System.out.printf("\\n📋 RESUMEN DEL ALQUILER:\\n");
            System.out.printf("Tarifa por día: $%.2f\\n", tarifaDiaria);
            System.out.printf("Días de alquiler: %d\\n", dias);
            System.out.printf("Precio total: $%.2f\\n", precioTotal);
            
            System.out.print("\\n¿Confirmar alquiler? (s/n): ");
            String confirmar = scanner.nextLine();
            
            if ("s".equalsIgnoreCase(confirmar) || "si".equalsIgnoreCase(confirmar)) {
                if (crearAlquiler(idCliente, idVehiculo, fechaInicio, fechaFin, tarifaDiaria, precioTotal)) {
                    System.out.println("✅ Alquiler creado exitosamente");
                    // Marcar vehículo como no disponible
                    actualizarDisponibilidadVehiculo(idVehiculo, false);
                } else {
                    System.out.println("❌ Error creando el alquiler");
                }
            } else {
                System.out.println("Alquiler cancelado");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void gestionarReservas() {
        System.out.println("\\n--- GESTIONAR RESERVAS ---");
        System.out.println("1. Ver Reservas Activas");
        System.out.println("2. Crear Nueva Reserva");
        System.out.println("3. Cancelar Reserva");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1 -> mostrarReservasActivas();
                case 2 -> crearReserva();
                case 3 -> cancelarReserva();
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void mostrarClientes() {
        System.out.println("\\n--- CLIENTES REGISTRADOS ---");
        
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return;
        }
        
        String sql = "SELECT idCliente, nombre, telefono, cedula FROM Cliente ORDER BY nombre";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tNombre\\t\\t\\tTeléfono\\t\\tCédula");
            System.out.println("----------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-3d\\t%-20s\\t%-15s\\t%s%n",
                    rs.getInt("idCliente"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("cedula")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando clientes: " + e.getMessage());
        }
    }
    
    private boolean verificarVehiculoDisponible(int idVehiculo) {
        String sql = "SELECT disponible FROM Vehiculo WHERE idVehiculo = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVehiculo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("disponible");
            }
        } catch (SQLException e) {
            System.out.println("Error verificando vehículo: " + e.getMessage());
        }
        
        return false;
    }
    
    private int obtenerOCrearCliente(String cedula) {
        // Buscar cliente existente
        String sqlBuscar = "SELECT idCliente FROM Cliente WHERE cedula = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sqlBuscar)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("idCliente");
            }
            
            // Cliente no existe, crear uno nuevo
            System.out.print("Cliente no encontrado. Nombre del cliente: ");
            String nombre = scanner.nextLine();
            System.out.print("Teléfono del cliente: ");
            String telefono = scanner.nextLine();
            
            String sqlInsertar = "INSERT INTO Cliente (nombre, telefono, cedula) VALUES (?, ?, ?)";
            PreparedStatement stmtInsertar = conexion.prepareStatement(sqlInsertar, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtInsertar.setString(1, nombre);
            stmtInsertar.setString(2, telefono);
            stmtInsertar.setString(3, cedula);
            
            int rowsAffected = stmtInsertar.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmtInsertar.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("✅ Cliente creado exitosamente");
                    return generatedKeys.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error gestionando cliente: " + e.getMessage());
        }
        
        return -1;
    }
    
    private boolean crearAlquiler(int idCliente, int idVehiculo, String fechaInicio, String fechaFin, double tarifaDiaria, double precioTotal) {
        String sql = "INSERT INTO Renta (idCliente, idVehiculo, fechaInicio, fechaFin, tarifaDiaria, precio_total, Estado) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVA')";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idVehiculo);
            stmt.setString(3, fechaInicio);
            stmt.setString(4, fechaFin);
            stmt.setDouble(5, tarifaDiaria);
            stmt.setDouble(6, precioTotal);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error creando alquiler: " + e.getMessage());
            return false;
        }
    }
    
    private double obtenerTarifaVehiculo(int idVehiculo) {
        String sql = """
            SELECT pv.precio_por_dia
            FROM Vehiculo v
            LEFT JOIN AutoElectrico ae ON v.idVehiculo = ae.idVehiculo
            LEFT JOIN AutoCombustion ac ON v.idVehiculo = ac.idVehiculo
            LEFT JOIN Camion c ON v.idVehiculo = c.idVehiculo
            LEFT JOIN Yate y ON v.idVehiculo = y.idVehiculo
            LEFT JOIN PrecioVehiculo pv ON (
                (ae.idVehiculo IS NOT NULL AND pv.tipo_vehiculo = 'AUTOELECTRICO') OR
                (ac.idVehiculo IS NOT NULL AND ae.idVehiculo IS NULL AND pv.tipo_vehiculo = 'AUTOCOMBUSTION') OR
                (c.idVehiculo IS NOT NULL AND pv.tipo_vehiculo = 'CAMION') OR
                (y.idVehiculo IS NOT NULL AND pv.tipo_vehiculo = 'YATE')
            )
            WHERE v.idVehiculo = ? AND pv.activo = true
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idVehiculo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("precio_por_dia");
            }
        } catch (SQLException e) {
            System.out.println("Error obteniendo tarifa: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    private int calcularDias(String fechaInicio, String fechaFin) {
        try {
            java.time.LocalDate inicio = java.time.LocalDate.parse(fechaInicio);
            java.time.LocalDate fin = java.time.LocalDate.parse(fechaFin);
            
            long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin);
            
            // Mínimo 1 día
            return (int) Math.max(1, dias);
            
        } catch (Exception e) {
            System.out.println("Error calculando días: " + e.getMessage());
            return 1; // Por defecto 1 día
        }
    }
    
    private void actualizarDisponibilidadVehiculo(int idVehiculo, boolean disponible) {
        String sql = "UPDATE Vehiculo SET disponible = ? WHERE idVehiculo = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setBoolean(1, disponible);
            stmt.setInt(2, idVehiculo);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error actualizando disponibilidad: " + e.getMessage());
        }
    }
    
    private void mostrarReservasActivas() {
        System.out.println("\\n--- RESERVAS ACTIVAS ---");
        
        String sql = """
            SELECT r.idReservacion, c.nombre, v.marca, v.modelo, 
                   r.fechaInicio, r.fechaFin, r.estado
            FROM Reservacion r
            JOIN Cliente c ON r.idCliente = c.idCliente
            JOIN Vehiculo v ON r.idVehiculo = v.idVehiculo
            WHERE r.activa = TRUE
            ORDER BY r.fechaInicio
            """;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("ID\\tCliente\\t\\t\\tVehículo\\t\\tFecha Inicio\\tFecha Fin\\tEstado");
            System.out.println("------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-3d\\t%-15s\\t%-15s\\t%s\\t%s\\t%s%n",
                    rs.getInt("idReservacion"),
                    rs.getString("nombre"),
                    rs.getString("marca") + " " + rs.getString("modelo"),
                    rs.getString("fechaInicio"),
                    rs.getString("fechaFin"),
                    rs.getString("estado")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error consultando reservas: " + e.getMessage());
        }
    }
    
    private void crearReserva() {
        System.out.println("\\n--- CREAR RESERVA ---");
        mostrarVehiculosDisponibles();
        
        try {
            System.out.print("\\nID del vehículo: ");
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Cédula del cliente: ");
            String cedula = scanner.nextLine();
            
            int idCliente = obtenerOCrearCliente(cedula);
            if (idCliente == -1) return;
            
            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fechaInicio = scanner.nextLine();
            
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fechaFin = scanner.nextLine();
            
            String sql = "INSERT INTO Reservacion (idVehiculo, idCliente, fechaInicio, fechaFin, estado) VALUES (?, ?, ?, ?, 'CONFIRMADA')";
            
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idVehiculo);
            stmt.setInt(2, idCliente);
            stmt.setString(3, fechaInicio);
            stmt.setString(4, fechaFin);
            
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Reserva creada exitosamente");
            } else {
                System.out.println("❌ Error creando la reserva");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void cancelarReserva() {
        System.out.println("\\n--- CANCELAR RESERVA ---");
        mostrarReservasActivas();
        
        try {
            System.out.print("\\nID de la reserva a cancelar: ");
            int idReservacion = Integer.parseInt(scanner.nextLine());
            
            String sql = "UPDATE Reservacion SET activa = FALSE, estado = 'CANCELADA' WHERE idReservacion = ?";
            
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idReservacion);
            
            if (stmt.executeUpdate() > 0) {
                System.out.println("✅ Reserva cancelada exitosamente");
            } else {
                System.out.println("❌ Reserva no encontrada");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}