-- ============================================================
-- SCRIPT DE DATOS INICIALES - Asistencia Financiera
-- ============================================================
-- Este script carga datos de prueba consistentes para todo el equipo
-- Ejecutar después de crear las tablas
-- ============================================================

USE asistencia_financiera;

-- Limpiar datos previos (opcional - comentar si no quieres borrar)
-- DELETE FROM usuarios;
-- DELETE FROM movimiento;
-- DELETE FROM cliente;

-- ============================================================
-- 1. TABLA: usuarios (Perfil de Usuario)
-- ============================================================
INSERT INTO usuarios (nombre, apellido, correo, contrasena, sueldo, cedula) 
VALUES 
    ('Cristopher', 'Garcia', 'cristopher@correo.com', 'Aots2112', 1500.00, '1726613688'),
    ('Juan', 'Martinez', 'juan@correo.com', 'pass123', 2000.00, '1700000001'),
    ('María', 'López', 'maria@correo.com', 'pass123', 1800.00, '1700000002'),
    ('Carlos', 'Pérez', 'carlos@correo.com', 'pass123', 2200.00, '1700000003');

-- ============================================================
-- 2. TABLA: cliente (Información del Cliente - Dashboard)
-- ============================================================
INSERT INTO cliente (name_cliente, apellido_cliente, password, cedula, initial_salary) 
VALUES 
    ('Cristopher', 'Garcia', 'Aots2112', '1726613688', 1500.00),
    ('Juan', 'Martinez', 'pass123', '1700000001', 2000.00),
    ('María', 'López', 'pass123', '1700000002', 1800.00),
    ('Carlos', 'Pérez', 'pass123', '1700000003', 2200.00);

-- ============================================================
-- 3. TABLA: movimiento (Transacciones)
-- ============================================================
INSERT INTO movimiento (cliente, tipo_movimiento, categoria, frecuencia, monto, fecha) 
VALUES 
    (1, 'INGRESO', 'SUELDO', 'MENSUAL', 1500.00, '2026-06-01'),
    (1, 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 150.00, '2026-06-05'),
    (1, 'GASTO', 'TRANSPORTE', 'SEMANAL', 50.00, '2026-06-06'),
    (1, 'INGRESO', 'FREELANCE', 'QUINCENAL', 300.00, '2026-06-15'),
    (1, 'GASTO', 'ENTRETENIMIENTO', 'MENSUAL', 100.00, '2026-06-10'),
    
    (2, 'INGRESO', 'SUELDO', 'MENSUAL', 2000.00, '2026-06-01'),
    (2, 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 200.00, '2026-06-05'),
    (2, 'GASTO', 'SERVICIOS', 'MENSUAL', 250.00, '2026-06-08'),
    
    (3, 'INGRESO', 'SUELDO', 'MENSUAL', 1800.00, '2026-06-01'),
    (3, 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 180.00, '2026-06-05'),
    (3, 'INGRESO', 'OTROS', 'MENSUAL', 500.00, '2026-06-20');

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SELECT COUNT(*) as total_usuarios FROM usuarios;
SELECT COUNT(*) as total_movimientos FROM movimiento;

-- ============================================================
-- NOTA IMPORTANTE:
-- ============================================================
-- Si los datos ya existen y quieres reemplazarlos, 
-- descomenta las líneas DELETE al inicio del script
-- ============================================================
