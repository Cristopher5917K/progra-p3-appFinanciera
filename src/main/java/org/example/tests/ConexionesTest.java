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
        testUpdateUserProfile();
        
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

    /**
     * Test para verificar que updateUserProfile actualiza correctamente los datos
     */
    public static void testUpdateUserProfile() {
        System.out.println("TEST 4: Probando updateUserProfile()...");
        try {
            Connection conn = Conexiones.getConnection();
            Conexiones db = new Conexiones();
            
            // Obtener datos actuales
            Map<String, Object> profileAntes = db.userProfileInfo(1, conn);
            String nombreAntes = (String) profileAntes.get("nombre");
            String apellidoAntes = (String) profileAntes.get("apellido");
            System.out.println("   Datos ANTES: " + nombreAntes + " " + apellidoAntes);
            
            // Actualizar con datos nuevos
            boolean actualizado = db.updateUserProfile(1, "TestNombre", "TestApellido", "999XXXXXX", 999.99, conn);
            
            if (actualizado) {
                // Verificar cambios
                Map<String, Object> profileDespues = db.userProfileInfo(1, conn);
                String nombreDespues = (String) profileDespues.get("nombre");
                String apellidoDespues = (String) profileDespues.get("apellido");
                System.out.println("   Datos DESPUÉS: " + nombreDespues + " " + apellidoDespues);
                
                if ("TestNombre".equals(nombreDespues) && "TestApellido".equals(apellidoDespues)) {
                    System.out.println("✅ PASÓ: Actualización verificada correctamente\n");
                    
                    // Revertir cambios
                    db.updateUserProfile(1, nombreAntes, apellidoAntes, (String) profileAntes.get("cedula"), 
                                       (Double) profileAntes.get("sueldo"), conn);
                    System.out.println("   (Datos revertidos al estado original)\n");
                } else {
                    System.out.println("❌ FALLÓ: Los datos no se actualizaron correctamente\n");
                }
            } else {
                System.out.println("❌ FALLÓ: No se pudo actualizar el usuario\n");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ FALLÓ: " + e.getMessage() + "\n");
        }
    }
}
