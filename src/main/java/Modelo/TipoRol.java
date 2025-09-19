package Modelo;

/**
 * Enumeración para los tipos de roles de usuario en el sistema
 */
public enum TipoRol {
    ADMIN("ADMIN", "Administrador"),
    EMPLEADO("EMPLEADO", "Empleado");
    
    private final String codigo;
    private final String descripcion;
    
    TipoRol(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
    public static TipoRol fromCodigo(String codigo) {
        for (TipoRol rol : values()) {
            if (rol.codigo.equals(codigo)) {
                return rol;
            }
        }
        return EMPLEADO; // Por defecto
    }
}