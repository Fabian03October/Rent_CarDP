# 🔐 CREDENCIALES DEL SISTEMA

## 📋 **SISTEMAS DISPONIBLES**

### **1. SistemaTerminalSimple.java** (SIN base de datos)
```bash
java -cp target\classes Vista.SistemaTerminalSimple
```

**Credenciales hardcodeadas:**
- **ADMIN**: 
  - Usuario: `admin`
  - Contraseña: `admin123`

- **EMPLEADO**: 
  - Usuario: `empleado`
  - Contraseña: `emp123`

---

### **2. SistemaCompletoBD.java** (CON base de datos)
```bash
java -cp target\classes Vista.SistemaCompletoBD
```

**Credenciales de base de datos (según script_corregido.sql):**
- **ADMIN**: 
  - Usuario: `admin`
  - Contraseña: `admin123`

- **EMPLEADO**: 
  - Usuario: `1`
  - Contraseña: `empleado123`

---

## ⚠️ **PROBLEMA IDENTIFICADO**

Estás intentando usar las credenciales:
- Usuario: `12345678`
- Contraseña: `empleado123`

**Pero estas NO existen en ninguno de los sistemas.**

## ✅ **SOLUCIÓN**

### **Opción 1: Usar SistemaTerminalSimple**
```
Usuario: empleado
Contraseña: emp123
```

### **Opción 2: Usar SistemaCompletoBD**
```
Usuario: 1
Contraseña: empleado123
```

### **Opción 3: Actualizar script SQL**
Si quieres usar `12345678` / `empleado123`, cambia en `script_corregido.sql`:

```sql
INSERT INTO Usuario (cedula, contrasena, rol, nombre, apellido, activo) VALUES 
('admin', 'admin123', 'ADMIN', 'Administrador', 'Sistema', true),
('12345678', 'empleado123', 'EMPLEADO', 'Juan', 'Pérez', true);
```

---

## 🚀 **PARA PROBAR AHORA MISMO**

Ejecuta:
```bash
java -cp target\classes Vista.SistemaTerminalSimple
```

Y usa:
- **Usuario**: `empleado`
- **Contraseña**: `emp123`