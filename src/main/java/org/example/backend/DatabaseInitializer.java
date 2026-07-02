package org.example.backend;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @PostConstruct
    public void initialize() {
        System.out.println("--- INICIANDO INICIALIZACIÓN DE BASE DE DATOS ---");
        try (Connection conn = Conexiones.getConnection();
             Statement stmt = conn.createStatement()) {

            // --- 1. Ejecutar bd_schema.sql para crear la estructura ---
            System.out.println("Ejecutando bd_schema.sql...");
            String schemaSql = readSqlFile("db_schema.sql");
            for (String sqlStatement : schemaSql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            System.out.println("Estructura de la base de datos validada con éxito.");

            // --- TRUCO CONTROL DE DUPLICADOS: Verificar si la base ya tiene datos ---
            var resultSet = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            int cantidadUsuarios = 0;
            if (resultSet.next()) {
                cantidadUsuarios = resultSet.getInt(1);
            }

            if (cantidadUsuarios == 0) {
                // --- 2. Ejecutar bd_data.sql SOLO SI LA TABLA ESTÁ VACÍA ---
                System.out.println("La base de datos está vacía. Poblando datos de prueba...");
                String dataSql = readSqlFile("db_data.sql");
                for (String sqlStatement : dataSql.split(";")) {
                    if (!sqlStatement.trim().isEmpty()) {
                        try {
                            stmt.execute(sqlStatement);
                        } catch (Exception e) {
                            System.out.println("Aviso en inserción: " + e.getMessage());
                        }
                    }
                }
                System.out.println("Datos de prueba insertados con éxito.");
            } else {
                System.out.println("La base de datos ya contiene información. Se omiten los datos de prueba para evitar duplicados.");
            }

        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO: No se pudo inicializer la base de datos.");
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