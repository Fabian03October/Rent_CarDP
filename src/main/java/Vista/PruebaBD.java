package Vista;

import Controlador.*;
import Modelo.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Prueba de inserción y consulta en base de datos
 * Verifica que los vehículos se guarden correctamente con herencia completa
 */
public class PruebaBD {
    
    private VehiculoJpaController vehiculoController;
    private TerrestreJpaController terrestreController;
    private AcuaticoJpaController acuaticoController;
    private AutocombustionJpaController autoController;
    private AutoelectricoJpaController electricoController;
    private CamionJpaController camionController;
    private YateJpaController yateController;
    private ClienteJpaController clienteController;
    private UsuarioJpaController usuarioController;
    
    public PruebaBD() {
        this.vehiculoController = new VehiculoJpaController();
        this.terrestreController = new TerrestreJpaController();
        this.acuaticoController = new AcuaticoJpaController();
        this.autoController = new AutocombustionJpaController();
        this.electricoController = new AutoelectricoJpaController();
        this.camionController = new CamionJpaController();
        this.yateController = new YateJpaController();
        this.clienteController = new ClienteJpaController();
        this.usuarioController = new UsuarioJpaController();
    }
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE BASE DE DATOS ===");
        System.out.println("Verificando conexión y persistencia de datos...\n");
        
        PruebaBD prueba = new PruebaBD();
        
        try {
            // 1. Probar conexión básica
            prueba.probarConexion();
            
            // 2. Probar creación de vehículo con herencia
            prueba.probarCreacionAutoElectrico();
            
            // 3. Probar consulta de datos
            prueba.consultarVehiculos();
            
            // 4. Probar usuarios y roles
            prueba.probarUsuarios();
            
            System.out.println("\n✅ TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR EN PRUEBA: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void probarConexion() throws Exception {
        System.out.println("🔍 1. PROBANDO CONEXIÓN A BASE DE DATOS...");
        
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            System.out.println("✅ Conexión exitosa - Vehículos encontrados: " + vehiculos.size());
        } catch (Exception e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            throw e;
        }
    }
    
    private void probarCreacionAutoElectrico() throws Exception {
        System.out.println("\n🚗 2. PROBANDO CREACIÓN DE AUTO ELÉCTRICO CON HERENCIA...");
        
        try {
            // Crear vehículo base
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setMarca("Tesla");
            vehiculo.setModelo("Model Y - Prueba BD");
            vehiculo.setPrecio(BigDecimal.valueOf(55000.00));
            vehiculo.setAño(2024);
            vehiculo.setReservado(false);
            
            vehiculoController.create(vehiculo);
            Integer idVehiculo = vehiculo.getIdVehiculo();
            System.out.println("✅ Vehículo base creado con ID: " + idVehiculo);
            
            // Crear registro terrestre
            Terrestre terrestre = new Terrestre();
            terrestre.setIdVehiculo(idVehiculo);
            terrestre.setNoPuertas(5);
            terrestre.setTipoCombustible("ELECTRICO");
            
            terrestreController.create(terrestre);
            System.out.println("✅ Registro terrestre creado");
            
            // Crear registro auto combustión (intermedio)
            Autocombustion auto = new Autocombustion();
            auto.setIdVehiculo(idVehiculo);
            
            autoController.create(auto);
            System.out.println("✅ Registro auto combustión creado");
            
            // Crear registro auto eléctrico
            Autoelectrico electrico = new Autoelectrico();
            electrico.setIdVehiculo(idVehiculo);
            electrico.setCapacidadBateria(BigDecimal.valueOf(82.0));
            electrico.setAutonomia(BigDecimal.valueOf(515.0));
            
            electricoController.create(electrico);
            System.out.println("✅ Registro auto eléctrico creado");
            
            System.out.println("🎯 HERENCIA COMPLETA GUARDADA:");
            System.out.println("   - Tabla 'vehiculo': " + vehiculo.getMarca() + " " + vehiculo.getModelo());
            System.out.println("   - Tabla 'terrestre': " + terrestre.getNoPuertas() + " puertas, " + terrestre.getTipoCombustible());
            System.out.println("   - Tabla 'autocombustion': Registro intermedio");
            System.out.println("   - Tabla 'autoelectrico': " + electrico.getCapacidadBateria() + " kWh, " + electrico.getAutonomia() + " km");
            
        } catch (Exception e) {
            System.out.println("❌ Error creando auto eléctrico: " + e.getMessage());
            throw e;
        }
    }
    
    private void consultarVehiculos() throws Exception {
        System.out.println("\n📋 3. CONSULTANDO VEHÍCULOS EN BASE DE DATOS...");
        
        try {
            List<Vehiculo> vehiculos = vehiculoController.findVehiculoEntities();
            System.out.println("📊 Total de vehículos encontrados: " + vehiculos.size());
            
            for (Vehiculo v : vehiculos) {
                System.out.println("   🚗 ID: " + v.getIdVehiculo() + " - " + v.getMarca() + " " + v.getModelo() + " (" + v.getAño() + ")");
                
                // Verificar si tiene registro terrestre
                try {
                    Terrestre terrestre = terrestreController.findTerrestre(v.getIdVehiculo());
                    if (terrestre != null) {
                        System.out.println("      🔗 Terrestre: " + terrestre.getNoPuertas() + " puertas, " + terrestre.getTipoCombustible());
                        
                        // Verificar auto eléctrico
                        try {
                            Autoelectrico electrico = electricoController.findAutoelectrico(v.getIdVehiculo());
                            if (electrico != null) {
                                System.out.println("      ⚡ Eléctrico: " + electrico.getCapacidadBateria() + " kWh, " + electrico.getAutonomia() + " km");
                            }
                        } catch (Exception e) {
                            // No es auto eléctrico
                        }
                        
                        // Verificar camión
                        try {
                            Camion camion = camionController.findCamion(v.getIdVehiculo());
                            if (camion != null) {
                                System.out.println("      🚚 Camión: " + camion.getCapacidadCarga() + " toneladas");
                            }
                        } catch (Exception e) {
                            // No es camión
                        }
                    }
                } catch (Exception e) {
                    // No es terrestre, verificar acuático
                    try {
                        Acuatico acuatico = acuaticoController.findAcuatico(v.getIdVehiculo());
                        if (acuatico != null) {
                            System.out.println("      🌊 Acuático: " + acuatico.getEslora() + "m eslora, " + acuatico.getCalado() + "m calado");
                            
                            // Verificar yate
                            try {
                                Yate yate = yateController.findYate(v.getIdVehiculo());
                                if (yate != null) {
                                    System.out.println("      ⛵ Yate: Motor eléctrico = " + yate.getMotorElectrico());
                                }
                            } catch (Exception ex) {
                                // No es yate
                            }
                        }
                    } catch (Exception ex) {
                        // No es acuático
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error consultando vehículos: " + e.getMessage());
            throw e;
        }
    }
    
    private void probarUsuarios() throws Exception {
        System.out.println("\n👥 4. VERIFICANDO USUARIOS Y ROLES...");
        
        try {
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            System.out.println("📊 Total de usuarios encontrados: " + usuarios.size());
            
            for (Usuario u : usuarios) {
                System.out.println("   👤 " + u.getCedula() + " - " + u.getNombre() + " " + u.getApellido() + " [" + u.getRol() + "]");
                System.out.println("      📧 " + u.getEmail() + " | 📞 " + u.getTelefono() + " | Activo: " + u.getActivo());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error consultando usuarios: " + e.getMessage());
            throw e;
        }
    }
}