package org.example.backend;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @PostConstruct
    public void initialize() {
        System.out.println("--- INICIANDO INICIALIZACIÓN DE BASE DE DATOS ---");
        try (Connection conn = Conexiones.getConnection();
             Statement stmt = conn.createStatement()) {

            // --- 1. Ejecutar schema.sql para crear la estructura ---
            System.out.println("Ejecutando schema.sql...");
            String schemaSql = readSqlFile("schema.sql");
            // Dividir el script en sentencias individuales por el punto y coma
            for (String sqlStatement : schemaSql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            System.out.println("Estructura de la base de datos creada con éxito.");

            // --- 2. Ejecutar data.sql para poblar los datos ---
            System.out.println("Ejecutando data.sql...");
            String dataSql = readSqlFile("data.sql");
            for (String sqlStatement : dataSql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            System.out.println("Datos de prueba insertados con éxito.");

        } catch (Exception e) {
            System.err.println("ERROR: No se pudo inicializar la base de datos.");
            e.printStackTrace();
        }
        System.out.println("--- INICIALIZACIÓN DE BASE DE DATOS COMPLETADA ---");
    }

    private String readSqlFile(String fileName) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new Exception("No se pudo encontrar el archivo SQL: " + fileName);
        }
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}