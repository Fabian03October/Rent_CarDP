package Vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SistemaConBD {
    private Scanner scanner;
    private Connection conexion;
    private String usuarioActual;
    private String rolActual;
    
    public SistemaConBD() {
        scanner = new Scanner(System.in);
        conectarBaseDatos();
    }
    
    private void conectarBaseDatos() {
        try {
            String url = "jdbc:mysql://localhost:3306/rent_car";
            String usuario = "root";
            String password = ""; // Cambia esto por tu contraseña de MySQL
            
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("✅ Conexión exitosa a la base de datos rent_car");
        } catch (SQLException e) {
            System.out.println("❌ Error conectando a la base de datos: " + e.getMessage());
            System.out.println("Asegúrate de que MySQL esté ejecutándose y que la base de datos 'rent_car' exista");
        }
    }
    
    public static void main(String[] args) {
        SistemaConBD sistema = new SistemaConBD();
        sistema.mostrarMenuPrincipal();
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("=== SISTEMA DE ALQUILER DE VEHICULOS ===");
        System.out.println("VERSION CON BASE DE DATOS\n");
        
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
        System.out.println("\n--- INICIAR SESION ---");
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
                usuarioActual = rs.getString("nombre") + " " + rs.getString("apellido");
                rolActual = rs.getString("rol");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error en autenticación: " + e.getMessage());
        }
        
        return false;
    }
    
    private void mostrarUsuarios() {
        System.out.println("\n--- USUARIOS EN BASE DE DATOS ---");
        
        if (conexion == null) {
            System.out.println("No hay conexión a la base de datos");
            return;
        }
        
        String sql = "SELECT cedula, nombre, apellido, rol, activo FROM Usuario";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("Cédula\t\tNombre\t\tRol\t\tActivo");
            System.out.println("------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-15s %-15s %-10s %s%n",
                    rs.getString("cedula"),
                    rs.getString("nombre") + " " + rs.getString("apellido"),
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
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Gestionar Vehículos");
            System.out.println("2. Gestionar Usuarios");
            System.out.println("3. Ver Reportes");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        System.out.println("🚗 Gestión de Vehículos - Funcionalidad en desarrollo");
                        break;
                    case 2:
                        System.out.println("👥 Gestión de Usuarios - Funcionalidad en desarrollo");
                        break;
                    case 3:
                        System.out.println("📊 Reportes - Funcionalidad en desarrollo");
                        break;
                    case 4:
                        System.out.println("Cerrando sesión...");
                        usuarioActual = null;
                        rolActual = null;
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void mostrarMenuEmpleado() {
        while (true) {
            System.out.println("\n--- MENU EMPLEADO ---");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Procesar Alquiler");
            System.out.println("3. Ver Vehículos Disponibles");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        System.out.println("👥 Gestión de Clientes - Funcionalidad en desarrollo");
                        break;
                    case 2:
                        System.out.println("📄 Procesar Alquiler - Funcionalidad en desarrollo");
                        break;
                    case 3:
                        System.out.println("🚗 Vehículos Disponibles - Funcionalidad en desarrollo");
                        break;
                    case 4:
                        System.out.println("Cerrando sesión...");
                        usuarioActual = null;
                        rolActual = null;
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
}