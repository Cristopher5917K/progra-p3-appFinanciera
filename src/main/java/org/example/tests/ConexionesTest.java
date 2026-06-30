package org.example.tests;

import org.example.backend.Conexiones;
import org.example.info.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ConexionesTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Tests de Conexiones ===\n");
        
        testGetConnection();
        testUserInfoById();
        testUserProfileInfo();
        
        System.out.println("\n=== Tests completados ===");
    }

    /**
     * Test para verificar que la conexión a la BD funciona correctamente
     */
    public static void testGetConnection() {
        System.out.println("TEST 1: Probando getConnection()...");
        try {
            Connection conn = Conexiones.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ PASÓ: Conexión establecida correctamente\n");
                conn.close();
            } else {
                System.out.println("❌ FALLÓ: La conexión es nula o está cerrada\n");
            }
        } catch (SQLException e) {
            System.out.println("❌ FALLÓ: " + e.getMessage() + "\n");
        }
    }

    /**
     * Test para verificar que userInfoById retorna un Cliente válido
     */
    public static void testUserInfoById() {
        System.out.println("TEST 2: Probando userInfoById()...");
        try {
            Connection conn = Conexiones.getConnection();
            Conexiones db = new Conexiones();
            Cliente usuario = db.userInfoById(1, conn);
            
            if (usuario != null) {
                System.out.println("✅ PASÓ: Usuario encontrado");
                System.out.println("   - Nombre: " + usuario.getNameCliente());
                System.out.println("   - Apellido: " + usuario.getApellidoCliente());
                System.out.println("   - Cédula: " + usuario.getCedula());
                System.out.println("   - Sueldo: $" + usuario.getInitialSalary() + "\n");
            } else {
                System.out.println("❌ FALLÓ: No se encontró el usuario\n");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ FALLÓ: " + e.getMessage() + "\n");
        }
    }

    /**
     * Test para verificar que userProfileInfo retorna un Map válido
     */
    public static void testUserProfileInfo() {
        System.out.println("TEST 3: Probando userProfileInfo()...");
        try {
            Connection conn = Conexiones.getConnection();
            Conexiones db = new Conexiones();
            Map<String, Object> profile = db.userProfileInfo(1, conn);
            
            if (profile != null && !profile.isEmpty()) {
                System.out.println("✅ PASÓ: Perfil de usuario obtenido");
                System.out.println("   - Nombre: " + profile.get("nombre"));
                System.out.println("   - Apellido: " + profile.get("apellido"));
                System.out.println("   - Cédula: " + profile.get("cedula"));
                System.out.println("   - Sueldo: $" + profile.get("sueldo") + "\n");
            } else {
                System.out.println("❌ FALLÓ: El perfil está vacío o es nulo\n");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ FALLÓ: " + e.getMessage() + "\n");
        }
    }
}
