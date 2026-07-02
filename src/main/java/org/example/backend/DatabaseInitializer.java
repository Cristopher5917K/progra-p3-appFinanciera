package org.example.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @Value("${app.db.recreate-on-startup:false}")
    private boolean recreateOnStartup;

    @PostConstruct
    public void initialize() {
        if (!recreateOnStartup) {
            System.out.println("Omitiendo la inicialización de la base de datos.");
            return;
        }

        System.out.println("Iniciando inicialización de base de datos...");
        try (Connection conn = Conexiones.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Ejecutando schema.sql...");
            String schemaSql = readSqlFile("schema.sql");
            for (String sqlStatement : schemaSql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            System.out.println("Estructura de la base de datos creada con éxito.");

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
        System.out.println("Inicialización de base de datos completada.");
    }

    private String readSqlFile(String fileName) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new Exception("No se pudo encontrar el archivo SQL: " + fileName);
        }
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}