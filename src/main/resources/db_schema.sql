-- Elimina las tablas si existen para un inicio limpio
-- DROP TABLE IF EXISTS metas;
-- DROP TABLE IF EXISTS movimiento;
-- DROP TABLE IF EXISTS cliente;
-- DROP TABLE IF EXISTS usuarios;

-- 1. TABLA: usuarios (Información de Login/Perfil)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    sueldo DECIMAL(10, 2) NOT NULL,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. TABLA: cliente (Información del Dashboard)
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    name_cliente VARCHAR(50) NOT NULL,
    apellido_cliente VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    initial_salary DECIMAL(10, 2) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABLA: movimiento (Transacciones/Movimientos)
CREATE TABLE IF NOT EXISTS movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    cliente INT NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    frecuencia VARCHAR(50) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

-- 4. TABLA: metas (Metas de Ahorro)
CREATE TABLE IF NOT EXISTS metas (
    id_meta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_amount DECIMAL(10, 2) NOT NULL,
    saved_amount DECIMAL(10, 2) DEFAULT 0.00,
    deadline DATE,
    color VARCHAR(7),
    category VARCHAR(50),
    creation_date DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

-- ÍNDICES PARA OPTIMIZACIÓN
-- CREATE INDEX idx_cliente_movimiento ON movimiento(cliente);
-- CREATE INDEX idx_cliente_meta ON metas(id_cliente);