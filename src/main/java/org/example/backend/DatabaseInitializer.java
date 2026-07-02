package org.example.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@Component
public class DatabaseInitializer {

    @Value("${app.db.recreate-on-startup:false}")
    private boolean recreateOnStartup;

    @PostConstruct
    public void initialize() {
        System.out.println("--- INICIANDO INICIALIZACIÓN DE BASE DE DATOS ---");

        // Ensure the database exists on the server (create DB when absent)
        try {
            Conexiones.ensureDatabaseExists();
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo crear/verificar la base de datos: " + e.getMessage());
            // Proceed: getConnection() will fail later if DB is not present and credentials are invalid
        }

        try (Connection conn = Conexiones.getConnection();
             Statement stmt = conn.createStatement()) {

            // If app.db.recreate-on-startup==true, force re-run. Otherwise run only when schema is missing.
            boolean shouldRunScripts = recreateOnStartup;

            if (!shouldRunScripts) {
                DatabaseMetaData meta = conn.getMetaData();
                try (ResultSet rs = meta.getTables(null, null, "usuarios", new String[]{"TABLE"})) {
                    if (!rs.next()) {
                        shouldRunScripts = true;
                    }
                }
            }

            if (shouldRunScripts) {
                System.out.println("Ejecutando schema.sql...");
                String schemaSql = readSqlFile("schema.sql");
                for (String sqlStatement : schemaSql.split(";")) {
                    if (!sqlStatement.trim().isEmpty()) {
                        stmt.execute(sqlStatement.trim());
                    }
                }
                System.out.println("Estructura de la base de datos creada con éxito.");

                System.out.println("La base de datos está vacía o se solicitó recrear. Poblando datos de prueba...");
                String dataSql = readSqlFile("data.sql");
                for (String sqlStatement : dataSql.split(";")) {
                    if (!sqlStatement.trim().isEmpty()) {
                        try {
                            stmt.execute(sqlStatement.trim());
                        } catch (Exception e) {
                            System.out.println("Aviso en inserción: " + e.getMessage());
                        }
                    }
                }
                System.out.println("Datos de prueba insertados con éxito.");
            } else {
                System.out.println("La base de datos ya contiene tablas. Se omite la inicialización de schema/data.");
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