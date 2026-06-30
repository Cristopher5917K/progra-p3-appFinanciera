# 📊 Configuración de Base de Datos

Este directorio contiene los scripts SQL para configurar la BD con datos consistentes.

## 📁 Archivos

- **`structure.sql`** - Crea las tablas (ejecutar primero)
- **`seed_data.sql`** - Inserta datos de prueba (ejecutar después)

## 🚀 Cómo ejecutar

### Opción 1: MySQL Workbench
1. Abre MySQL Workbench
2. Conéctate a tu servidor MySQL
3. File → Open SQL Script → Selecciona `structure.sql`
4. Ejecuta con Ctrl+Enter (o el botón ⚡)
5. Repite con `seed_data.sql`

### Opción 2: Terminal/CMD
```bash
# Conectar a MySQL
mysql -u root -p

# Ejecutar structure.sql
source C:\ruta\del\proyecto\database\structure.sql

# Ejecutar seed_data.sql
source C:\ruta\del\proyecto\database\seed_data.sql

# Verificar
USE asistencia_financiera;
SELECT * FROM usuarios;
SELECT * FROM movimiento;
```

### Opción 3: Desde CMD directamente
```bash
mysql -u root -p asistencia_financiera < database\structure.sql
mysql -u root -p asistencia_financiera < database\seed_data.sql
```

## 📋 Datos de prueba incluidos

### Usuarios (4 total)
| ID | Nombre | Apellido | Correo | Sueldo | Cédula |
|----|--------|----------|--------|--------|--------|
| 1 | Cristopher | Garcia | cristopher@correo.com | $1,500 | 1726613688 |
| 2 | Juan | Martinez | juan@correo.com | $2,000 | 1700000001 |
| 3 | María | López | maria@correo.com | $1,800 | 1700000002 |
| 4 | Carlos | Pérez | carlos@correo.com | $2,200 | 1700000003 |

### Movimientos (11 total)
- Ingresos por sueldo y trabajos freelance
- Gastos en alimentación, transporte, servicios
- Diferentes frecuencias (semanal, quincenal, mensual)

## ✅ Verificación

Después de ejecutar los scripts, verifica:

```sql
-- Ver todas las tablas
SHOW TABLES;

-- Contar registros
SELECT COUNT(*) as total_usuarios FROM usuarios;
SELECT COUNT(*) as total_movimientos FROM movimiento;

-- Ver usuarios
SELECT * FROM usuarios;

-- Ver movimientos
SELECT * FROM movimiento;
```

## 🔄 Si necesitas reiniciar datos

Descomenta las líneas DELETE al inicio de `seed_data.sql`:

```sql
-- DELETE FROM usuarios;
-- DELETE FROM movimiento;
-- DELETE FROM cliente;
```

Así se borra todo y se carga limpio.

## 📝 Agregar más datos

Para agregar más registros:
1. Abre `seed_data.sql`
2. Agrega más INSERT statements
3. Ejecuta el archivo de nuevo

## ⚠️ Importante

- **Orden de ejecución:**
  1. `structure.sql` (crea tablas)
  2. `seed_data.sql` (inserta datos)

- **Credenciales de prueba:**
  - Usuario: `cristopher@correo.com`
  - Contraseña: `Aots2112`

- Todos los miembros del equipo deben ejecutar esto para tener datos consistentes ✅

