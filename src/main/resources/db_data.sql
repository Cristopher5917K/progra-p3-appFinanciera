-- ============================================================
-- SCRIPT DE DATOS INICIALES - Asistencia Financiera
-- ============================================================

-- 1. PRIMERO: TABLA usuarios (No depende de nadie, se usa para el Login)
INSERT INTO usuarios (nombre, apellido, correo, contrasena, sueldo, cedula)
VALUES
    ('Cristopher', 'Garcia', 'cristopher@correo.com', 'Aots2112', 1500.00, '1726613688'),
    ('Juan', 'Martinez', 'juan@correo.com', 'pass123', 2000.00, '1700000001'),
    ('María', 'López', 'maria@correo.com', 'pass123', 1800.00, '1700000002'),
    ('Carlos', 'Pérez', 'carlos@correo.com', 'pass123', 2200.00, '1700000003');

-- 2. SEGUNDO: TABLA cliente (No depende de nadie, se usa para el Dashboard)
INSERT INTO cliente (name_cliente, apellido_cliente, password, cedula, initial_salary)
VALUES
    ('Cristopher', 'Garcia', 'Aots2112', '1726613688', 1500.00),
    ('Juan', 'Martinez', 'pass123', '1700000001', 2000.00),
    ('María', 'López', 'pass123', '1700000002', 1800.00),
    ('Carlos', 'Pérez', 'pass123', '1700000003', 2200.00);

-- 3. TERCERO: TABLA movimiento (Depende de que 'cliente' ya exista)
-- Usamos subconsultas por cédula para que sea 100% a prueba de fallos con los IDs
INSERT INTO movimiento (cliente, tipo_movimiento, categoria, frecuencia, monto, fecha)
VALUES
    -- Movimientos de Cristopher
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'INGRESO', 'SUELDO', 'MENSUAL', 1500.00, '2026-06-01'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 150.00, '2026-06-05'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'GASTO', 'TRANSPORTE', 'SEMANAL', 50.00, '2026-06-06'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'INGRESO', 'FREELANCE', 'QUINCENAL', 300.00, '2026-06-15'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'GASTO', 'ENTRETENIMIENTO', 'MENSUAL', 100.00, '2026-06-10'),

    -- Movimientos de Juan
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000001'), 'INGRESO', 'SUELDO', 'MENSUAL', 2000.00, '2026-06-01'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000001'), 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 200.00, '2026-06-05'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000001'), 'GASTO', 'SERVICIOS', 'MENSUAL', 250.00, '2026-06-08'),

    -- Movimientos de María
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000002'), 'INGRESO', 'SUELDO', 'MENSUAL', 1800.00, '2026-06-01'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000002'), 'GASTO', 'ALIMENTACIÓN', 'SEMANAL', 180.00, '2026-06-05'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1700000002'), 'INGRESO', 'OTROS', 'MENSUAL', 500.00, '2026-06-20');

-- 4. CUARTO: TABLA metas (Depende de que 'cliente' ya exista)
INSERT INTO metas (id_cliente, name, target_amount, saved_amount, deadline, color, category, creation_date)
VALUES
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'Fondo de Emergencia', 50000.00, 21500.00, '2025-12-31', '#27AE60', 'Seguridad', '2024-04-23'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'Viaje a Europa', 35000.00, 12800.00, '2025-08-15', '#3B82F6', 'Viajes', '2024-04-28'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'MacBook Pro', 28000.00, 25200.00, '2025-06-30', '#8B5CF6', 'Tecnología', '2024-05-01'),
    ((SELECT id_cliente FROM cliente WHERE cedula = '1726613688'), 'Entrada para casa', 150000.00, 38000.00, '2027-01-01', '#F59E0B', 'Vivienda', '2024-05-03');