package Modelo;

/**
 * Clase singleton para manejar la sesión del usuario autenticado
 */
public class SesionUsuario {
    private static SesionUsuario instance;
    private Usuario usuarioActual;
    
    private SesionUsuario() {}
    
    public static SesionUsuario getInstance() {
        if (instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }
    
    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }
    
    public boolean esAdmin() {
        return usuarioActual != null && "ADMIN".equals(usuarioActual.getRol());
    }
    
    public boolean esEmpleado() {
        return usuarioActual != null && "EMPLEADO".equals(usuarioActual.getRol());
    }
    
    public String getNombreCompleto() {
        if (usuarioActual != null) {
            return usuarioActual.getNombre() + " " + usuarioActual.getApellido();
        }
        return "";
    }
}