package org.example;

import database.DatabaseConnection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DE CONFIGURACIÓN ===");

        // 1. Diagnóstico: Verificar si Dotenv lee el archivo
        try {
            Dotenv dotenv = Dotenv.load();
            System.out.println("-> Archivo .env cargado correctamente.");
            System.out.println("-> URL detectada: " + dotenv.get("DB_URL"));
            System.out.println("-> Usuario detectado: " + dotenv.get("DB_USER"));
        } catch (Exception e) {
            System.err.println("❌ Error: No se pudo encontrar o leer el archivo .env en la raíz del proyecto.");
            return;
        }

        System.out.println("\n=== INTENTANDO CONEXIÓN A MYSQL ===");

        // 2. Diagnóstico: Intentar conectar usando las credenciales del .env
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("=================================================");
                System.out.println("¡ÉXITO: Conexión establecida usando tu archivo .env!");
                System.out.println("=================================================");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error crítico al conectar a la base de datos:");
            System.err.println("Mensaje de MySQL: " + e.getMessage());
            System.err.println("\nVerifica que:");
            System.err.println("1. El servidor de MySQL esté encendido.");
            System.err.println("2. Las credenciales en tu .env no tengan espacios o comillas de más.");
            e.printStackTrace();
        }
    }
}