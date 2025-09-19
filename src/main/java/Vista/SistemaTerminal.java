package Vista;

import Controlador.*;
import Modelo.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Clase simple para probar el sistema de roles desde terminal
 * Mantiene arquitectura MVC
 */
public class SistemaTerminal {
    
    private UsuarioJpaController usuarioController;
    private VehiculoJpaController vehiculoController;
    private PrecioVehiculoJpaController precioController;
    private Cliente clienteController;
    private Scanner scanner;
    private Usuario usuarioLogueado;
    
    public SistemaTerminal() {
        usuarioController = new UsuarioJpaController();
        vehiculoController = new VehiculoJpaController();
        precioController = new PrecioVehiculoJpaController();
        scanner = new Scanner(System.in);
        usuarioLogueado = null;
    }
    
    public static void main(String[] args) {
        SistemaTerminal sistema = new SistemaTerminal();
        sistema.iniciar();
    }
    
    public void iniciar() {
        System.out.println("=".repeat(50));
        System.out.println("   SISTEMA DE GESTION DE VEHICULOS");
        System.out.println("=".repeat(50));
        
        // Login
        if (login()) {
            mostrarMenuPrincipal();
        } else {
            System.out.println("Acceso denegado. Saliendo del sistema...");
        }
        
        scanner.close();
    }
    
    private boolean login() {
        System.out.println("\n--- INICIAR SESION ---");
        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        try {
            usuarioLogueado = usuarioController.autenticarUsuario(cedula, contrasena);
            if (usuarioLogueado != null) {
                System.out.println("\n¡Bienvenido " + usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido() + "!");
                System.out.println("Rol: " + usuarioLogueado.getRol());
                return true;
            } else {
                System.out.println("Credenciales incorrectas o usuario inactivo.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }
    
    private void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("   MENU PRINCIPAL - " + usuarioLogueado.getRol());
            System.out.println("=".repeat(50));
            
            if ("ADMIN".equals(usuarioLogueado.getRol())) {
                mostrarMenuAdmin();
            } else if ("EMPLEADO".equals(usuarioLogueado.getRol())) {
                mostrarMenuEmpleado();
            }
            
            System.out.print("\nSeleccione una opción: ");
            String opcion = scanner.nextLine();
            
            if ("0".equals(opcion)) {
                System.out.println("Cerrando sesión...");
                break;
            }
            
            procesarOpcion(opcion);
        }
    }
    
    private void mostrarMenuAdmin() {
        System.out.println("1. Gestionar Vehículos");
        System.out.println("2. Gestionar Usuarios");
        System.out.println("3. Configurar Precios");
        System.out.println("4. Ver Historial de Operaciones");
        System.out.println("5. Listar Todos los Vehículos");
        System.out.println("6. Listar Todos los Usuarios");
        System.out.println("0. Cerrar Sesión");
    }
    
    private void mostrarMenuEmpleado() {
        System.out.println("1. Ver Vehículos Disponibles");
        System.out.println("2. Realizar Renta");
        System.out.println("3. Realizar Reservación");
        System.out.println("4. Ver Precios");
        System.out.println("0. Cerrar Sesión");
    }
    
    private void procesarOpcion(String opcion) {
        try {
            if ("ADMIN".equals(usuarioLogueado.getRol())) {
                procesarOpcionAdmin(opcion);
            } else {
                procesarOpcionEmpleado(opcion);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void procesarOpcionAdmin(String opcion) throws Exception {
        switch (opcion) {
            case "1":
                gestionarVehiculos();
                break;
            case "2":
                gestionarUsuarios();
                break;
            case "3":
                configurarPrecios();
                break;
            case "4":
                verHistorialOperaciones();
                break;
            case "5":
                listarTodosLosVehiculos();
                break;
            case "6":
                listarTodosLosUsuarios();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }
    
    private void procesarOpcionEmpleado(String opcion) throws Exception {
        switch (opcion) {
            case "1":
                verVehiculosDisponibles();
                break;
            case "2":
                System.out.println("Funcionalidad de renta en desarrollo...");
                break;
            case "3":
                System.out.println("Funcionalidad de reservación en desarrollo...");
                break;
            case "4":
                verPrecios();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }
    
    // === FUNCIONES PARA ADMIN ===
    private void gestionarVehiculos() {
        System.out.println("\n--- GESTION DE VEHICULOS ---");
        System.out.println("1. Agregar Vehículo");
        System.out.println("2. Listar Vehículos");
        System.out.println("3. Buscar Vehículo");
        System.out.print("Seleccione: ");
        
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1":
                agregarVehiculo();
                break;
            case "2":
                listarTodosLosVehiculos();
                break;
            case "3":
                buscarVehiculo();
                break;
        }
    }
    
    private void agregarVehiculo() {
        System.out.println("\n--- AGREGAR VEHICULO ---");
        
        // Mostrar tipos disponibles
        System.out.println("Tipos de vehículos disponibles:");
        System.out.println("1. Auto de Combustión");
        System.out.println("2. Auto Eléctrico");
        System.out.println("3. Camión");
        System.out.println("4. Yate");
        System.out.print("Seleccione tipo (1-4): ");
        
        int tipoVehiculo = Integer.parseInt(scanner.nextLine());
        
        // Datos básicos del vehículo
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        
        System.out.print("Año: ");
        int año = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine());
        
        try {
            // Crear vehículo base
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setAño(año);
            vehiculo.setPrecio(BigDecimal.valueOf(precio));
            vehiculo.setReservado(false);
            
            vehiculoController.create(vehiculo);
            Integer idVehiculo = vehiculo.getIdVehiculo();
            
            switch (tipoVehiculo) {
                case 1: // Auto de Combustión
                    crearAutoCombustion(idVehiculo);
                    break;
                case 2: // Auto Eléctrico
                    crearAutoElectrico(idVehiculo);
                    break;
                case 3: // Camión
                    crearCamion(idVehiculo);
                    break;
                case 4: // Yate
                    crearYate(idVehiculo);
                    break;
                default:
                    System.out.println("Tipo de vehículo no válido");
                    return;
            }
            
            System.out.println("Vehículo agregado exitosamente con ID: " + idVehiculo);
        } catch (Exception e) {
            System.out.println("Error al agregar vehículo: " + e.getMessage());
        }
    }
    
    private void crearAutoCombustion(Integer idVehiculo) throws Exception {
        // Crear registro terrestre
        TerrestreJpaController terrestreController = new TerrestreJpaController();
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        
        System.out.print("Número de puertas: ");
        terrestre.setNoPuertas(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Tipo de combustible: ");
        terrestre.setTipoCombustible(scanner.nextLine());
        
        terrestreController.create(terrestre);
        
        // Crear registro autocombustión
        AutocombustionJpaController autoController = new AutocombustionJpaController();
        Autocombustion auto = new Autocombustion();
        auto.setIdVehiculo(idVehiculo);
        
        autoController.create(auto);
    }
    
    private void crearAutoElectrico(Integer idVehiculo) throws Exception {
        // Crear registro terrestre
        TerrestreJpaController terrestreController = new TerrestreJpaController();
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        
        System.out.print("Número de puertas: ");
        terrestre.setNoPuertas(Integer.parseInt(scanner.nextLine()));
        terrestre.setTipoCombustible("ELECTRICO");
        
        terrestreController.create(terrestre);
        
        // Crear registro auto (padre de autoelectrico)
        AutocombustionJpaController autoController = new AutocombustionJpaController();
        Autocombustion auto = new Autocombustion();
        auto.setIdVehiculo(idVehiculo);
        autoController.create(auto);
        
        // Crear registro autoeléctrico
        AutoelectricoJpaController electricoController = new AutoelectricoJpaController();
        Autoelectrico electrico = new Autoelectrico();
        electrico.setIdVehiculo(idVehiculo);
        
        System.out.print("Capacidad de batería (kWh): ");
        electrico.setCapacidadBateria(BigDecimal.valueOf(Double.parseDouble(scanner.nextLine())));
        
        System.out.print("Autonomía (km): ");
        electrico.setAutonomia(BigDecimal.valueOf(Integer.parseInt(scanner.nextLine())));
        
        electricoController.create(electrico);
    }
    
    private void crearCamion(Integer idVehiculo) throws Exception {
        // Crear registro terrestre
        TerrestreJpaController terrestreController = new TerrestreJpaController();
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        
        System.out.print("Número de puertas: ");
        terrestre.setNoPuertas(Integer.parseInt(scanner.nextLine()));
        
        terrestreController.create(terrestre);
        
        // Crear registro camión
        CamionJpaController camionController = new CamionJpaController();
        Camion camion = new Camion();
        camion.setIdVehiculo(idVehiculo);
        
        System.out.print("Capacidad de carga (toneladas): ");
        camion.setCapacidadCarga(BigDecimal.valueOf(Double.parseDouble(scanner.nextLine())));
        
        camionController.create(camion);
    }
    
    private void crearYate(Integer idVehiculo) throws Exception {
        // Crear registro acuático
        AcuaticoJpaController acuaticoController = new AcuaticoJpaController();
        Acuatico acuatico = new Acuatico();
        acuatico.setIdVehiculo(idVehiculo);
        
        System.out.print("Eslora (metros): ");
        acuatico.setEslora(BigDecimal.valueOf(Double.parseDouble(scanner.nextLine())));
        
        System.out.print("Calado (metros): ");
        acuatico.setCalado(BigDecimal.valueOf(Double.parseDouble(scanner.nextLine())));
        
        acuaticoController.create(acuatico);
        
        // Crear registro yate
        YateJpaController yateController = new YateJpaController();
        Yate yate = new Yate();
        yate.setIdVehiculo(idVehiculo);
        
        System.out.print("¿Tiene motor eléctrico? (true/false): ");
        yate.setMotorElectrico(Boolean.parseBoolean(scanner.nextLine()));
        
        yateController.create(yate);
    }
    
    private void listarTodosLosVehiculos() {
        System.out.println("\n--- LISTA DE VEHICULOS ---");
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            if (vehiculos.isEmpty()) {
                System.out.println("No hay vehículos registrados.");
                return;
            }
            
            System.out.printf("%-5s %-15s %-15s %-8s %-10s %-15s %-10s%n", 
                            "ID", "Marca", "Modelo", "Año", "Precio", "Tipo", "Estado");
            System.out.println("-".repeat(85));
            
            for (Vehiculo vehiculo : vehiculos) {
                String estado = vehiculo.getReservado() ? "Reservado" : "Disponible";
                String tipoVehiculo = determinarTipoVehiculo(vehiculo.getIdVehiculo());
                
                System.out.printf("%-5d %-15s %-15s %-8d $%-9.2f %-15s %-10s%n",
                                vehiculo.getIdVehiculo(),
                                vehiculo.getMarca(),
                                vehiculo.getModelo(),
                                vehiculo.getAño(),
                                vehiculo.getPrecio(),
                                tipoVehiculo,
                                estado);
            }
        } catch (Exception e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        }
    }
    
    private String determinarTipoVehiculo(Integer idVehiculo) {
        try {
            // Verificar si es Yate
            YateJpaController yateController = new YateJpaController();
            Yate yate = yateController.findYate(idVehiculo);
            if (yate != null) {
                return "Yate";
            }
            
            // Verificar si es Auto Eléctrico
            AutoelectricoJpaController electricoController = new AutoelectricoJpaController();
            Autoelectrico electrico = electricoController.findAutoelectrico(idVehiculo);
            if (electrico != null) {
                return "Auto Eléctrico";
            }
            
            // Verificar si es Camión
            CamionJpaController camionController = new CamionJpaController();
            Camion camion = camionController.findCamion(idVehiculo);
            if (camion != null) {
                return "Camión";
            }
            
            // Verificar si es Auto de Combustión
            AutocombustionJpaController autoController = new AutocombustionJpaController();
            Autocombustion auto = autoController.findAutocombustion(idVehiculo);
            if (auto != null) {
                return "Auto Combustión";
            }
            
            // Verificar si es Acuático genérico
            AcuaticoJpaController acuaticoController = new AcuaticoJpaController();
            Acuatico acuatico = acuaticoController.findAcuatico(idVehiculo);
            if (acuatico != null) {
                return "Acuático";
            }
            
            // Verificar si es Terrestre genérico
            TerrestreJpaController terrestreController = new TerrestreJpaController();
            Terrestre terrestre = terrestreController.findTerrestre(idVehiculo);
            if (terrestre != null) {
                return "Terrestre";
            }
            
            return "Vehículo Base";
        } catch (Exception e) {
            return "Desconocido";
        }
    }
    
    private void buscarVehiculo() {
        System.out.print("Ingrese ID del vehículo: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        try {
            Vehiculo vehiculo = vehiculoController.findVehiculo(id);
            if (vehiculo != null) {
                System.out.println("\n--- DATOS DEL VEHICULO ---");
                System.out.println("ID: " + vehiculo.getIdVehiculo());
                System.out.println("Marca: " + vehiculo.getMarca());
                System.out.println("Modelo: " + vehiculo.getModelo());
                System.out.println("Año: " + vehiculo.getAño());
                System.out.println("Precio: $" + vehiculo.getPrecio());
                System.out.println("Estado: " + (vehiculo.getReservado() ? "Reservado" : "Disponible"));
            } else {
                System.out.println("Vehículo no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar vehículo: " + e.getMessage());
        }
    }
    
    private void gestionarUsuarios() {
        System.out.println("\n--- GESTION DE USUARIOS ---");
        System.out.println("1. Agregar Usuario");
        System.out.println("2. Listar Usuarios");
        System.out.println("3. Crear Administrador");
        System.out.print("Seleccione: ");
        
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1":
                agregarUsuario();
                break;
            case "2":
                listarTodosLosUsuarios();
                break;
            case "3":
                crearAdministrador();
                break;
        }
    }
    
    private void crearAdministrador() {
        System.out.println("\n--- CREAR USUARIO ADMINISTRADOR ---");
        System.out.print("Cédula: ");
        String cedula = scanner.nextLine();
        
        // Verificar si ya existe
        try {
            Usuario existente = usuarioController.findUsuarioByCedula(cedula);
            if (existente != null) {
                System.out.print("El usuario ya existe. ¿Convertir a administrador? (s/n): ");
                String respuesta = scanner.nextLine();
                if ("s".equalsIgnoreCase(respuesta)) {
                    existente.setRol("ADMIN");
                    usuarioController.edit(existente);
                    System.out.println("Usuario convertido a administrador exitosamente.");
                }
                return;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
            return;
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        System.out.print("Teléfono (opcional): ");
        String telefono = scanner.nextLine();
        
        System.out.print("Email (opcional): ");
        String email = scanner.nextLine();
        
        try {
            Usuario admin = new Usuario();
            admin.setCedula(cedula);
            admin.setNombre(nombre);
            admin.setApellido(apellido);
            admin.setContrasena(contrasena);
            admin.setRol("ADMIN");
            admin.setTelefono(telefono.isEmpty() ? null : telefono);
            admin.setEmail(email.isEmpty() ? null : email);
            admin.setActivo(true);
            
            usuarioController.create(admin);
            System.out.println("¡Usuario administrador creado exitosamente!");
            System.out.println("Credenciales:");
            System.out.println("Cédula: " + cedula);
            System.out.println("Contraseña: " + contrasena);
            System.out.println("Rol: ADMIN");
        } catch (Exception e) {
            System.out.println("Error al crear administrador: " + e.getMessage());
        }
    }
    
    private void agregarUsuario() {
        System.out.println("\n--- AGREGAR USUARIO ---");
        System.out.print("Cédula: ");
        String cedula = scanner.nextLine();
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        
        System.out.print("Rol (ADMIN/EMPLEADO): ");
        String rol = scanner.nextLine().toUpperCase();
        
        try {
            Usuario usuario = new Usuario();
            usuario.setCedula(cedula);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setContrasena(contrasena);
            usuario.setRol(rol);
            usuario.setActivo(true);
            
            usuarioController.create(usuario);
            System.out.println("Usuario agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al agregar usuario: " + e.getMessage());
        }
    }
    
    private void listarTodosLosUsuarios() {
        System.out.println("\n--- LISTA DE USUARIOS ---");
        try {
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios registrados.");
                return;
            }
            
            System.out.printf("%-5s %-12s %-15s %-15s %-10s %-8s%n", 
                            "ID", "Cédula", "Nombre", "Apellido", "Rol", "Estado");
            System.out.println("-".repeat(75));
            
            for (Usuario usuario : usuarios) {
                String estado = (usuario.getActivo() != null && usuario.getActivo()) ? "Activo" : "Inactivo";
                System.out.printf("%-5d %-12s %-15s %-15s %-10s %-8s%n",
                                usuario.getIdUsuario(),
                                usuario.getCedula(),
                                usuario.getNombre() != null ? usuario.getNombre() : "",
                                usuario.getApellido() != null ? usuario.getApellido() : "",
                                usuario.getRol(),
                                estado);
            }
        } catch (Exception e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
    }
    
    private void configurarPrecios() {
        System.out.println("\n--- CONFIGURACION DE PRECIOS ---");
        System.out.println("1. Ver Precios Actuales");
        System.out.println("2. Actualizar Precio");
        System.out.print("Seleccione: ");
        
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1":
                verPrecios();
                break;
            case "2":
                actualizarPrecio();
                break;
        }
    }
    
    private void actualizarPrecio() {
        System.out.print("Tipo de vehículo (AUTOCOMBUSTION/AUTOELECTRICO/CAMION/YATE): ");
        String tipo = scanner.nextLine().toUpperCase();
        
        System.out.print("Nuevo precio por día: ");
        double precio = Double.parseDouble(scanner.nextLine());
        
        try {
            precioController.actualizarPrecio(tipo, BigDecimal.valueOf(precio));
            System.out.println("Precio actualizado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar precio: " + e.getMessage());
        }
    }
    
    private void verHistorialOperaciones() {
        System.out.println("\n--- HISTORIAL DE OPERACIONES ---");
        System.out.println("Funcionalidad en desarrollo...");
        // Aquí se implementaría la lógica para mostrar rentas y reservaciones
    }
    
    // === FUNCIONES PARA EMPLEADO ===
    private void verVehiculosDisponibles() {
        System.out.println("\n--- VEHICULOS DISPONIBLES ---");
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            boolean hayDisponibles = false;
            
            System.out.printf("%-5s %-15s %-15s %-8s %-10s%n", 
                            "ID", "Marca", "Modelo", "Año", "Precio");
            System.out.println("-".repeat(60));
            
            for (Vehiculo vehiculo : vehiculos) {
                if (!vehiculo.getReservado()) {
                    hayDisponibles = true;
                    System.out.printf("%-5d %-15s %-15s %-8d $%-9.2f%n",
                                    vehiculo.getIdVehiculo(),
                                    vehiculo.getMarca(),
                                    vehiculo.getModelo(),
                                    vehiculo.getAño(),
                                    vehiculo.getPrecio());
                }
            }
            
            if (!hayDisponibles) {
                System.out.println("No hay vehículos disponibles.");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar vehículos: " + e.getMessage());
        }
    }
    
    private void verPrecios() {
        System.out.println("\n--- PRECIOS POR TIPO DE VEHICULO ---");
        try {
            List<PrecioVehiculo> precios = precioController.getPreciosActivos();
            if (precios.isEmpty()) {
                System.out.println("No hay precios configurados.");
                return;
            }
            
            System.out.printf("%-20s %-15s %-30s%n", "Tipo", "Precio/Día", "Descripción");
            System.out.println("-".repeat(70));
            
            for (PrecioVehiculo precio : precios) {
                System.out.printf("%-20s $%-14.2f %-30s%n",
                                precio.getTipoVehiculo(),
                                precio.getPrecioPorDia(),
                                precio.getDescripcion() != null ? precio.getDescripcion() : "");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar precios: " + e.getMessage());
        }
    }
}