package Vista;

import Controlador.*;
import Modelo.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Sistema Completo de Rentas
 * Prueba todas las funcionalidades: usuarios, vehículos, reservas, rentas
 */
public class SistemaCompletoBD {
    
    private Scanner scanner;
    private UsuarioJpaController usuarioController;
    private ClienteJpaController clienteController;
    private VehiculoJpaController vehiculoController;
    private TerrestreJpaController terrestreController;
    private AcuaticoJpaController acuaticoController;
    private AutocombustionJpaController autoController;
    private AutoelectricoJpaController electricoController;
    private CamionJpaController camionController;
    private YateJpaController yateController;
    private ReservacionJpaController reservacionController;
    private RentaJpaController rentaController;
    private FacturaJpaController facturaController;
    private ObservacionJpaController observacionController;
    private PrecioVehiculoJpaController precioController;
    
    private Usuario usuarioActual;
    
    public SistemaCompletoBD() {
        this.scanner = new Scanner(System.in);
        this.usuarioController = new UsuarioJpaController();
        this.clienteController = new ClienteJpaController();
        this.vehiculoController = new VehiculoJpaController();
        this.terrestreController = new TerrestreJpaController();
        this.acuaticoController = new AcuaticoJpaController();
        this.autoController = new AutocombustionJpaController();
        this.electricoController = new AutoelectricoJpaController();
        this.camionController = new CamionJpaController();
        this.yateController = new YateJpaController();
        this.reservacionController = new ReservacionJpaController();
        this.rentaController = new RentaJpaController();
        this.facturaController = new FacturaJpaController();
        this.observacionController = new ObservacionJpaController();
        this.precioController = new PrecioVehiculoJpaController();
    }
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA COMPLETO DE ALQUILER DE VEHICULOS ===");
        System.out.println("Conectando a base de datos rent_car...\n");
        
        SistemaCompletoBD sistema = new SistemaCompletoBD();
        
        try {
            sistema.verificarConexion();
            sistema.menuPrincipal();
        } catch (Exception e) {
            System.err.println("❌ ERROR DEL SISTEMA: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void verificarConexion() throws Exception {
        System.out.println("🔍 Verificando conexión a base de datos...");
        
        try {
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            
            System.out.println("✅ Conexión exitosa!");
            System.out.println("📊 Usuarios en BD: " + usuarios.size());
            System.out.println("📊 Vehículos en BD: " + vehiculos.size());
            System.out.println("");
            
        } catch (Exception e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            throw e;
        }
    }
    
    private void menuPrincipal() {
        while (true) {
            System.out.println("--- MENU PRINCIPAL ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Ver Información del Sistema");
            System.out.println("3. Listar Vehículos Disponibles");
            System.out.println("4. Salir");
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
                        listarVehiculosDisponibles();
                        break;
                    case 4:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido");
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
    }
    
    private void iniciarSesion() {
        System.out.println("\n--- INICIAR SESION ---");
        System.out.print("Cédula de usuario: ");
        String cedula = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        try {
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            Usuario usuario = null;
            
            for (Usuario u : usuarios) {
                if (u.getCedula().equals(cedula) && u.getContrasena().equals(password) && u.getActivo()) {
                    usuario = u;
                    break;
                }
            }
            
            if (usuario != null) {
                usuarioActual = usuario;
                System.out.println("✅ Bienvenido " + usuario.getNombre() + " " + usuario.getApellido() + " [" + usuario.getRol() + "]");
                
                if ("ADMIN".equals(usuario.getRol())) {
                    menuAdmin();
                } else {
                    menuEmpleado();
                }
            } else {
                System.out.println("❌ Credenciales incorrectas o usuario inactivo");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en autenticación: " + e.getMessage());
        }
    }
    
    private void menuAdmin() {
        while (true) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Gestionar Vehículos");
            System.out.println("2. Gestionar Clientes");
            System.out.println("3. Gestionar Precios");
            System.out.println("4. Ver Reportes");
            System.out.println("5. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        gestionarVehiculos();
                        break;
                    case 2:
                        gestionarClientes();
                        break;
                    case 3:
                        gestionarPrecios();
                        break;
                    case 4:
                        verReportes();
                        break;
                    case 5:
                        usuarioActual = null;
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido");
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
    }
    
    private void menuEmpleado() {
        while (true) {
            System.out.println("\n--- MENU EMPLEADO ---");
            System.out.println("1. Crear Reservación");
            System.out.println("2. Crear Renta");
            System.out.println("3. Finalizar Renta");
            System.out.println("4. Listar Vehículos");
            System.out.println("5. Buscar Cliente");
            System.out.println("6. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        crearReservacion();
                        break;
                    case 2:
                        crearRenta();
                        break;
                    case 3:
                        finalizarRenta();
                        break;
                    case 4:
                        listarVehiculosDisponibles();
                        break;
                    case 5:
                        buscarCliente();
                        break;
                    case 6:
                        usuarioActual = null;
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido");
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
    }
    
    private void gestionarVehiculos() {
        System.out.println("\n--- GESTIONAR VEHICULOS ---");
        System.out.println("1. Crear Vehículo");
        System.out.println("2. Listar Vehículos");
        System.out.println("3. Cambiar Disponibilidad");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    crearVehiculo();
                    break;
                case 2:
                    listarTodosLosVehiculos();
                    break;
                case 3:
                    cambiarDisponibilidadVehiculo();
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor ingrese un número válido");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    private void crearVehiculo() throws Exception {
        System.out.println("\n--- CREAR VEHICULO ---");
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Año: ");
        int anio = Integer.parseInt(scanner.nextLine());
        System.out.print("Precio: ");
        BigDecimal precio = new BigDecimal(scanner.nextLine());
        
        // Crear vehículo base
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAño(anio);
        vehiculo.setPrecio(precio);
        vehiculo.setDisponible(true);
        
        vehiculoController.create(vehiculo);
        Integer idVehiculo = vehiculo.getIdVehiculo();
        
        System.out.println("\nTipos de vehículo:");
        System.out.println("1. Auto de Combustión");
        System.out.println("2. Auto Eléctrico");
        System.out.println("3. Camión");
        System.out.println("4. Yate");
        System.out.print("Seleccione el tipo: ");
        
        int tipo = Integer.parseInt(scanner.nextLine());
        
        switch (tipo) {
            case 1:
                crearAutoCombustion(idVehiculo);
                break;
            case 2:
                crearAutoElectrico(idVehiculo);
                break;
            case 3:
                crearCamion(idVehiculo);
                break;
            case 4:
                crearYate(idVehiculo);
                break;
            default:
                System.out.println("❌ Tipo inválido");
                vehiculoController.destroy(idVehiculo);
                return;
        }
        
        System.out.println("✅ Vehículo creado exitosamente con ID: " + idVehiculo);
    }
    
    private void crearAutoCombustion(Integer idVehiculo) throws Exception {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        System.out.print("Tipo de combustible: ");
        String combustible = scanner.nextLine();
        
        // Crear registro terrestre
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        terrestre.setNoPuertas(puertas);
        terrestre.setTipoCombustible(combustible);
        terrestreController.create(terrestre);
        
        // Crear registro auto combustión
        Autocombustion auto = new Autocombustion();
        auto.setIdVehiculo(idVehiculo);
        autoController.create(auto);
    }
    
    private void crearAutoElectrico(Integer idVehiculo) throws Exception {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        System.out.print("Capacidad de batería (kWh): ");
        BigDecimal bateria = new BigDecimal(scanner.nextLine());
        System.out.print("Autonomía (km): ");
        BigDecimal autonomia = new BigDecimal(scanner.nextLine());
        
        // Crear registros en cascada
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        terrestre.setNoPuertas(puertas);
        terrestre.setTipoCombustible("ELECTRICO");
        terrestreController.create(terrestre);
        
        Autocombustion auto = new Autocombustion();
        auto.setIdVehiculo(idVehiculo);
        autoController.create(auto);
        
        Autoelectrico electrico = new Autoelectrico();
        electrico.setIdVehiculo(idVehiculo);
        electrico.setCapacidadBateria(bateria);
        electrico.setAutonomia(autonomia);
        electricoController.create(electrico);
    }
    
    private void crearCamion(Integer idVehiculo) throws Exception {
        System.out.print("Número de puertas: ");
        int puertas = Integer.parseInt(scanner.nextLine());
        System.out.print("Capacidad de carga (toneladas): ");
        BigDecimal carga = new BigDecimal(scanner.nextLine());
        
        Terrestre terrestre = new Terrestre();
        terrestre.setIdVehiculo(idVehiculo);
        terrestre.setNoPuertas(puertas);
        terrestre.setTipoCombustible("DIESEL");
        terrestreController.create(terrestre);
        
        Camion camion = new Camion();
        camion.setIdVehiculo(idVehiculo);
        camion.setCapacidadCarga(carga);
        camionController.create(camion);
    }
    
    private void crearYate(Integer idVehiculo) throws Exception {
        System.out.print("Eslora (metros): ");
        BigDecimal eslora = new BigDecimal(scanner.nextLine());
        System.out.print("Calado (metros): ");
        BigDecimal calado = new BigDecimal(scanner.nextLine());
        System.out.print("¿Tiene motor eléctrico? (true/false): ");
        boolean motorElectrico = Boolean.parseBoolean(scanner.nextLine());
        
        Acuatico acuatico = new Acuatico();
        acuatico.setIdVehiculo(idVehiculo);
        acuatico.setEslora(eslora);
        acuatico.setCalado(calado);
        acuaticoController.create(acuatico);
        
        Yate yate = new Yate();
        yate.setIdVehiculo(idVehiculo);
        yate.setMotorElectrico(motorElectrico);
        yateController.create(yate);
    }
    
    private void listarVehiculosDisponibles() {
        System.out.println("\n--- VEHICULOS DISPONIBLES ---");
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            
            for (Vehiculo v : vehiculos) {
                if (v.getDisponible()) {
                    System.out.println("🚗 ID: " + v.getIdVehiculo() + " - " + v.getMarca() + " " + v.getModelo() + 
                                     " (" + v.getAño() + ") - $" + v.getPrecio());
                    mostrarDetallesVehiculo(v);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error listando vehículos: " + e.getMessage());
        }
    }
    
    private void listarTodosLosVehiculos() {
        System.out.println("\n--- TODOS LOS VEHICULOS ---");
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            
            for (Vehiculo v : vehiculos) {
                String estado = v.getDisponible() ? "✅ Disponible" : "❌ No disponible";
                System.out.println("🚗 ID: " + v.getIdVehiculo() + " - " + v.getMarca() + " " + v.getModelo() + 
                                 " (" + v.getAño() + ") - $" + v.getPrecio() + " - " + estado);
                mostrarDetallesVehiculo(v);
            }
        } catch (Exception e) {
            System.err.println("❌ Error listando vehículos: " + e.getMessage());
        }
    }
    
    private void mostrarDetallesVehiculo(Vehiculo v) {
        try {
            // Verificar tipo de vehículo
            try {
                Autoelectrico electrico = electricoController.findAutoelectrico(v.getIdVehiculo());
                if (electrico != null) {
                    System.out.println("   ⚡ Auto Eléctrico - Batería: " + electrico.getCapacidadBateria() + 
                                     " kWh, Autonomía: " + electrico.getAutonomia() + " km");
                    return;
                }
            } catch (Exception e) { }
            
            try {
                Autocombustion auto = autoController.findAutocombustion(v.getIdVehiculo());
                if (auto != null) {
                    Terrestre terrestre = terrestreController.findTerrestre(v.getIdVehiculo());
                    System.out.println("   🚗 Auto Combustión - Puertas: " + terrestre.getNoPuertas() + 
                                     ", Combustible: " + terrestre.getTipoCombustible());
                    return;
                }
            } catch (Exception e) { }
            
            try {
                Camion camion = camionController.findCamion(v.getIdVehiculo());
                if (camion != null) {
                    Terrestre terrestre = terrestreController.findTerrestre(v.getIdVehiculo());
                    System.out.println("   🚚 Camión - Puertas: " + terrestre.getNoPuertas() + 
                                     ", Carga: " + camion.getCapacidadCarga() + " ton");
                    return;
                }
            } catch (Exception e) { }
            
            try {
                Yate yate = yateController.findYate(v.getIdVehiculo());
                if (yate != null) {
                    Acuatico acuatico = acuaticoController.findAcuatico(v.getIdVehiculo());
                    System.out.println("   ⛵ Yate - Eslora: " + acuatico.getEslora() + 
                                     "m, Motor eléctrico: " + yate.getMotorElectrico());
                    return;
                }
            } catch (Exception e) { }
            
        } catch (Exception e) {
            System.out.println("   ❓ Tipo no identificado");
        }
    }
    
    private void crearRenta() {
        System.out.println("\n--- CREAR RENTA ---");
        try {
            // Seleccionar cliente
            System.out.print("Cédula del cliente: ");
            String cedulaCliente = scanner.nextLine();
            
            List<Cliente> clientes = clienteController.findClienteEntities();
            Cliente cliente = null;
            for (Cliente c : clientes) {
                if (c.getCedula().equals(cedulaCliente)) {
                    cliente = c;
                    break;
                }
            }
            
            if (cliente == null) {
                System.out.println("❌ Cliente no encontrado");
                return;
            }
            
            // Seleccionar vehículo
            listarVehiculosDisponibles();
            System.out.print("ID del vehículo a rentar: ");
            int idVehiculo = Integer.parseInt(scanner.nextLine());
            
            Vehiculo vehiculo = vehiculoController.findVehiculo(idVehiculo);
            if (vehiculo == null || !vehiculo.getDisponible()) {
                System.out.println("❌ Vehículo no disponible");
                return;
            }
            
            // Fechas
            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fechaInicioStr = scanner.nextLine();
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fechaFinStr = scanner.nextLine();
            
            LocalDate fechaInicioLocal = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFinLocal = LocalDate.parse(fechaFinStr);
            Date fechaInicio = Date.from(fechaInicioLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fechaFin = Date.from(fechaFinLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Calcular precio
            long dias = fechaFinLocal.toEpochDay() - fechaInicioLocal.toEpochDay();
            System.out.print("Tarifa diaria: ");
            BigDecimal tarifaDiaria = new BigDecimal(scanner.nextLine());
            BigDecimal precioTotal = tarifaDiaria.multiply(BigDecimal.valueOf(dias));
            
            // Crear renta
            Renta renta = new Renta();
            renta.setIdCliente(cliente);
            renta.setIdVehiculo(vehiculo);
            renta.setFechaInicio(fechaInicio);
            renta.setFechaFin(fechaFin);
            renta.setTarifaDiaria(tarifaDiaria);
            renta.setPrecioTotal(precioTotal);
            renta.setEstado("ACTIVA");
            
            rentaController.create(renta);
            
            // Marcar vehículo como no disponible
            vehiculo.setDisponible(false);
            vehiculoController.edit(vehiculo);
            
            System.out.println("✅ Renta creada exitosamente!");
            System.out.println("📋 ID Renta: " + renta.getIdRenta());
            System.out.println("💰 Total: $" + precioTotal + " (" + dias + " días)");
            
        } catch (Exception e) {
            System.err.println("❌ Error creando renta: " + e.getMessage());
        }
    }
    
    private void finalizarRenta() {
        System.out.println("\n--- FINALIZAR RENTA ---");
        try {
            System.out.print("ID de la renta a finalizar: ");
            int idRenta = Integer.parseInt(scanner.nextLine());
            
            Renta renta = rentaController.findRenta(idRenta);
            if (renta == null) {
                System.out.println("❌ Renta no encontrada");
                return;
            }
            
            if (!"ACTIVA".equals(renta.getEstado())) {
                System.out.println("❌ La renta ya está finalizada");
                return;
            }
            
            // Finalizar renta
            renta.setEstado("FINALIZADA");
            rentaController.edit(renta);
            
            // Liberar vehículo
            Vehiculo vehiculo = renta.getIdVehiculo();
            vehiculo.setDisponible(true);
            vehiculoController.edit(vehiculo);
            
            // Crear factura
            Factura factura = new Factura();
            factura.setIdRenta(renta);
            factura.setFecha(new Date());
            factura.setTotal(renta.getPrecioTotal());
            facturaController.create(factura);
            
            System.out.println("✅ Renta finalizada exitosamente!");
            System.out.println("🧾 Factura generada con ID: " + factura.getIdFactura());
            
        } catch (Exception e) {
            System.err.println("❌ Error finalizando renta: " + e.getMessage());
        }
    }
    
    private void crearReservacion() {
        System.out.println("\n--- CREAR RESERVACION ---");
        // Implementar lógica similar a crear renta
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void gestionarClientes() {
        System.out.println("\n--- GESTIONAR CLIENTES ---");
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void gestionarPrecios() {
        System.out.println("\n--- GESTIONAR PRECIOS ---");
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void verReportes() {
        System.out.println("\n--- REPORTES ---");
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void buscarCliente() {
        System.out.println("\n--- BUSCAR CLIENTE ---");
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void cambiarDisponibilidadVehiculo() {
        System.out.println("\n--- CAMBIAR DISPONIBILIDAD ---");
        System.out.println("🔄 Funcionalidad en desarrollo...");
    }
    
    private void mostrarInformacionSistema() {
        System.out.println("\n=== INFORMACION DEL SISTEMA ===");
        try {
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            List<Cliente> clientes = clienteController.findClienteEntities();
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            List<Renta> rentas = rentaController.findRentaEntities();
            
            System.out.println("📊 ESTADISTICAS:");
            System.out.println("   👥 Usuarios: " + usuarios.size());
            System.out.println("   🧑‍💼 Clientes: " + clientes.size());
            System.out.println("   🚗 Vehículos: " + vehiculos.size());
            System.out.println("   📋 Rentas: " + rentas.size());
            
            long vehiculosDisponibles = vehiculos.stream().mapToLong(v -> v.getDisponible() ? 1 : 0).sum();
            System.out.println("   ✅ Vehículos disponibles: " + vehiculosDisponibles);
            
            long rentasActivas = rentas.stream().mapToLong(r -> "ACTIVA".equals(r.getEstado()) ? 1 : 0).sum();
            System.out.println("   🔄 Rentas activas: " + rentasActivas);
            
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo información: " + e.getMessage());
        }
    }
}