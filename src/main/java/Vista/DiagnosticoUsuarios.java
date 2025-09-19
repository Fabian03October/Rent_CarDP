package Vista;

import Controlador.UsuarioJpaController;
import Modelo.Usuario;
import java.util.List;

/**
 * Diagnóstico de usuarios en base de datos
 */
public class DiagnosticoUsuarios {
    
    public static void main(String[] args) {
        System.out.println("=== DIAGNÓSTICO DE USUARIOS ===\n");
        
        try {
            UsuarioJpaController usuarioController = new UsuarioJpaController();
            List<Usuario> usuarios = usuarioController.findUsuarioEntities();
            
            System.out.println("📊 Total de usuarios encontrados: " + usuarios.size());
            System.out.println("\n--- LISTA DE USUARIOS ---");
            
            for (Usuario u : usuarios) {
                System.out.println("👤 ID: " + u.getIdUsuario());
                System.out.println("   Cédula: '" + u.getCedula() + "'");
                System.out.println("   Contraseña: '" + u.getContrasena() + "'");
                System.out.println("   Rol: " + u.getRol());
                System.out.println("   Nombre: " + u.getNombre() + " " + u.getApellido());
                System.out.println("   Activo: " + u.getActivo());
                System.out.println("   Email: " + u.getEmail());
                System.out.println("   Teléfono: " + u.getTelefono());
                System.out.println();
            }
            
            // Probar autenticación
            System.out.println("--- PRUEBAS DE AUTENTICACIÓN ---");
            
            // Probar admin
            if (autenticar(usuarios, "admin", "admin123")) {
                System.out.println("✅ admin / admin123 - FUNCIONA");
            } else {
                System.out.println("❌ admin / admin123 - NO FUNCIONA");
            }
            
            // Probar empleado con cédula 1
            if (autenticar(usuarios, "1", "empleado123")) {
                System.out.println("✅ 1 / empleado123 - FUNCIONA");
            } else {
                System.out.println("❌ 1 / empleado123 - NO FUNCIONA");
            }
            
            // Probar empleado con cédula antigua
            if (autenticar(usuarios, "12345678", "empleado123")) {
                System.out.println("✅ 12345678 / empleado123 - FUNCIONA");
            } else {
                System.out.println("❌ 12345678 / empleado123 - NO FUNCIONA");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error conectando a BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean autenticar(List<Usuario> usuarios, String cedula, String password) {
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(cedula) && u.getContrasena().equals(password) && u.getActivo()) {
                return true;
            }
        }
        return false;
    }
}