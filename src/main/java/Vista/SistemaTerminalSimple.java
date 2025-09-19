package Vista;

import java.util.Scanner;

/**
 * Sistema Terminal Simple - Sin dependencias JPA para testing
 */
public class SistemaTerminalSimple {
    private Scanner scanner;
    
    public SistemaTerminalSimple() {
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ALQUILER DE VEHICULOS ===");
        System.out.println("VERSION SIMPLE - TESTING");
        
        SistemaTerminalSimple sistema = new SistemaTerminalSimple();
        sistema.mostrarMenuPrincipal();
    }
    
    private void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Información del Sistema");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        iniciarSesion();
                        break;
                    case 2:
                        mostrarInformacionSistema();
                        break;
                    case 3:
                        System.out.println("¡Hasta luego!");
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
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        // Simulación de autenticación
        if ("admin".equals(usuario) && "admin123".equals(password)) {
            System.out.println("¡Bienvenido Administrador!");
            mostrarMenuAdmin();
        } else if ("empleado".equals(usuario) && "emp123".equals(password)) {
            System.out.println("¡Bienvenido Empleado!");
            mostrarMenuEmpleado();
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }
    
    private void mostrarMenuAdmin() {
        while (true) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Gestionar Vehículos");
            System.out.println("2. Gestionar Usuarios");
            System.out.println("3. Gestionar Precios");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        gestionarVehiculos();
                        break;
                    case 2:
                        gestionarUsuarios();
                        break;
                    case 3:
                        gestionarPrecios();
                        break;
                    case 4:
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
            System.out.println("1. Listar Vehículos");
            System.out.println("2. Gestionar Reservaciones");
            System.out.println("3. Gestionar Rentas");
            System.out.println("4. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        listarVehiculos();
                        break;
                    case 2:
                        gestionarReservaciones();
                        break;
                    case 3:
                        gestionarRentas();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void gestionarVehiculos() {
        while (true) {
            System.out.println("\n--- GESTION DE VEHICULOS ---");
            System.out.println("1. Crear Vehículo");
            System.out.println("2. Listar Vehículos");
            System.out.println("3. Editar Vehículo");
            System.out.println("4. Eliminar Vehículo");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        crearVehiculo();
                        break;
                    case 2:
                        listarVehiculos();
                        break;
                    case 3:
                        System.out.println("Función de edición (pendiente implementación con BD)");
                        break;
                    case 4:
                        System.out.println("Función de eliminación (pendiente implementación con BD)");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        }
    }
    
    private void crearVehiculo() {
        System.out.println("\n--- CREAR VEHICULO ---");
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Año: ");
        String anio = scanner.nextLine();
        System.out.print("Precio: ");
        String precio = scanner.nextLine();
        
        System.out.println("\nTipos de vehículo disponibles:");
        System.out.println("1. Auto de Combustión");
        System.out.println("2. Auto Eléctrico");
        System.out.println("3. Camión");
        System.out.println("4. Yate");
        System.out.print("Seleccione el tipo: ");
        
        try {
            int tipo = Integer.parseInt(scanner.nextLine());
            
            switch (tipo) {
                case 1:
                    crearAutoCombustionDemo(marca, modelo, anio, precio);
                    break;
                case 2:
                    crearAutoElectricoDemo(marca, modelo, anio, precio);
                    break;
                case 3:
                    crearCamionDemo(marca, modelo, anio, precio);
                    break;
                case 4:
                    crearYateDemo(marca, modelo, anio, precio);
                    break;
                default:
                    System.out.println("Tipo inválido");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un número válido");
        }
    }
    
    private void crearAutoCombustionDemo(String marca, String modelo, String anio, String precio) {
        System.out.println("\n--- CREANDO AUTO DE COMBUSTION ---");
        System.out.print("Número de puertas: ");
        String puertas = scanner.nextLine();
        System.out.print("Tipo de combustible: ");
        String combustible = scanner.nextLine();
        
        System.out.println("\n✅ VEHICULO CREADO EXITOSAMENTE");
        System.out.println("Tipo: Auto de Combustión");
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Precio: $" + precio);
        System.out.println("Puertas: " + puertas);
        System.out.println("Combustible: " + combustible);
        System.out.println("\n🔄 En producción, esto se guardaría en:");
        System.out.println("- Tabla 'vehiculo' (datos base)");
        System.out.println("- Tabla 'terrestre' (herencia nivel 1)");
        System.out.println("- Tabla 'autocombustion' (herencia nivel 2)");
    }
    
    private void crearAutoElectricoDemo(String marca, String modelo, String anio, String precio) {
        System.out.println("\n--- CREANDO AUTO ELECTRICO ---");
        System.out.print("Número de puertas: ");
        String puertas = scanner.nextLine();
        System.out.print("Capacidad de batería (kWh): ");
        String bateria = scanner.nextLine();
        System.out.print("Autonomía (km): ");
        String autonomia = scanner.nextLine();
        
        System.out.println("\n✅ VEHICULO CREADO EXITOSAMENTE");
        System.out.println("Tipo: Auto Eléctrico");
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Precio: $" + precio);
        System.out.println("Puertas: " + puertas);
        System.out.println("Batería: " + bateria + " kWh");
        System.out.println("Autonomía: " + autonomia + " km");
        System.out.println("\n🔄 En producción, esto se guardaría en:");
        System.out.println("- Tabla 'vehiculo' (datos base)");
        System.out.println("- Tabla 'terrestre' (herencia nivel 1)");
        System.out.println("- Tabla 'autocombustion' (herencia intermedia)");
        System.out.println("- Tabla 'autoelectrico' (herencia nivel 2)");
    }
    
    private void crearCamionDemo(String marca, String modelo, String anio, String precio) {
        System.out.println("\n--- CREANDO CAMION ---");
        System.out.print("Número de puertas: ");
        String puertas = scanner.nextLine();
        System.out.print("Capacidad de carga (toneladas): ");
        String carga = scanner.nextLine();
        
        System.out.println("\n✅ VEHICULO CREADO EXITOSAMENTE");
        System.out.println("Tipo: Camión");
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Precio: $" + precio);
        System.out.println("Puertas: " + puertas);
        System.out.println("Capacidad: " + carga + " toneladas");
        System.out.println("\n🔄 En producción, esto se guardaría en:");
        System.out.println("- Tabla 'vehiculo' (datos base)");
        System.out.println("- Tabla 'terrestre' (herencia nivel 1)");
        System.out.println("- Tabla 'camion' (herencia nivel 2)");
    }
    
    private void crearYateDemo(String marca, String modelo, String anio, String precio) {
        System.out.println("\n--- CREANDO YATE ---");
        System.out.print("Eslora (metros): ");
        String eslora = scanner.nextLine();
        System.out.print("Calado (metros): ");
        String calado = scanner.nextLine();
        System.out.print("¿Tiene motor eléctrico? (true/false): ");
        String motorElectrico = scanner.nextLine();
        
        System.out.println("\n✅ VEHICULO CREADO EXITOSAMENTE");
        System.out.println("Tipo: Yate");
        System.out.println("Marca: " + marca);
        System.out.println("Modelo: " + modelo);
        System.out.println("Año: " + anio);
        System.out.println("Precio: $" + precio);
        System.out.println("Eslora: " + eslora + " metros");
        System.out.println("Calado: " + calado + " metros");
        System.out.println("Motor Eléctrico: " + motorElectrico);
        System.out.println("\n🔄 En producción, esto se guardaría en:");
        System.out.println("- Tabla 'vehiculo' (datos base)");
        System.out.println("- Tabla 'acuatico' (herencia nivel 1)");
        System.out.println("- Tabla 'yate' (herencia nivel 2)");
    }
    
    private void listarVehiculos() {
        System.out.println("\n--- LISTA DE VEHICULOS ---");
        System.out.println("🔄 En producción, aquí aparecerían todos los vehículos de la BD");
        System.out.println("\nEjemplo de vehículos que se mostrarían:");
        System.out.println("1. Toyota Prius 2023 - Auto Eléctrico - $35,000");
        System.out.println("2. Ford F-150 2022 - Camión - $45,000");
        System.out.println("3. BMW 320i 2023 - Auto Combustión - $40,000");
        System.out.println("4. Yacht Prestige 2021 - Yate - $250,000");
    }
    
    private void gestionarUsuarios() {
        System.out.println("\n--- GESTION DE USUARIOS ---");
        System.out.println("🔄 Funcionalidad de gestión de usuarios");
        System.out.println("- Crear usuarios ADMIN/EMPLEADO");
        System.out.println("- Activar/Desactivar usuarios");
        System.out.println("- Gestionar permisos");
    }
    
    private void gestionarPrecios() {
        System.out.println("\n--- GESTION DE PRECIOS ---");
        System.out.println("🔄 Sistema PrecioVehiculo implementado");
        System.out.println("- Configurar precios por tipo de vehículo");
        System.out.println("- Aplicar descuentos por temporada");
        System.out.println("- Gestionar tarifas especiales");
    }
    
    private void gestionarReservaciones() {
        System.out.println("\n--- GESTION DE RESERVACIONES ---");
        System.out.println("🔄 Funcionalidad para empleados");
        System.out.println("- Crear nuevas reservaciones");
        System.out.println("- Consultar reservaciones existentes");
        System.out.println("- Modificar fechas de reservación");
    }
    
    private void gestionarRentas() {
        System.out.println("\n--- GESTION DE RENTAS ---");
        System.out.println("🔄 Funcionalidad para empleados");
        System.out.println("- Procesar alquileres");
        System.out.println("- Generar facturas");
        System.out.println("- Registrar devoluciones");
    }
    
    private void mostrarInformacionSistema() {
        System.out.println("\n=== INFORMACION DEL SISTEMA ===");
        System.out.println("📋 Sistema de Alquiler de Vehículos");
        System.out.println("🔧 Versión: 1.0 (Demo)");
        System.out.println("👥 Roles: ADMIN, EMPLEADO");
        System.out.println("🚗 Vehículos: Auto Combustión, Auto Eléctrico, Camión, Yate");
        System.out.println("💾 Base de Datos: MySQL (JPA/EclipseLink)");
        System.out.println("\n✅ PROBLEMAS RESUELTOS:");
        System.out.println("- ✅ Sistema de roles funcionando");
        System.out.println("- ✅ Herencia de vehículos corregida");
        System.out.println("- ✅ Creación de registros en todas las tablas");
        System.out.println("- ✅ Validación de tipos de datos");
        System.out.println("\n🔐 CREDENCIALES DE DEMO:");
        System.out.println("Admin: usuario='admin', password='admin123'");
        System.out.println("Empleado: usuario='empleado', password='emp123'");
    }
}