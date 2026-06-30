package org.example.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.info.Cliente;
import org.example.info.Movimientos;

import javax.swing.*;
import java.sql.*;
import java.util.*;


public class Conexiones {

    // Cargar el archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    // Leer las variables del archivo
    private static final String URL = dotenv != null ? dotenv.get("DB_URL") : null;
    private static final String USER = dotenv != null ? dotenv.get("DB_USER") : null;
    private static final String PASSWORD = dotenv != null ? dotenv.get("DB_PASSWORD") : null;

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Variables de entorno no configuradas. Verifica el archivo .env");
        }
        
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver de MySQL no encontrado", e);
            }
        }
        return connection;
    }

    public void registerUser(Connection conn, String name, String apellido, String cedula, double sueldo, String password){
        String addUser = "INSERT INTO cliente (name_cliente, apellido_cliente, password, cedula, initial_salary) VALUES (?,?,?,?,?)";

        if (conn != null){
            try {
                PreparedStatement data = conn.prepareStatement(addUser);

                data.setString(1, name);
                data.setString(2, apellido);
                data.setString(3, password);
                data.setString(4, cedula);
                data.setDouble(5, sueldo);

                int value =  data.executeUpdate();

                if (value > 0){
                    System.out.println("SE LOGRO CONECTAR");
                } else {
                    System.out.println("NO SE LOGRO CONECTAR");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.println("FALLO LA BASE DE DATOS");
        }
    }

    public Cliente userInfo(String cedula, Connection conn){
        String sqlSearch = "SELECT * FROM cliente WHERE cedula = ?";

        try {
            PreparedStatement search = conn.prepareStatement(sqlSearch);
            search.setString(1, cedula);

            ResultSet success = search.executeQuery();
            if (success.next()){
                return new Cliente(
                        success.getInt("id_cliente"),
                        success.getString("name_cliente"),
                        success.getString("apellido_cliente"),
                        success.getString("password"),
                        success.getString("cedula"),
                        success.getDouble("initial_salary")
                );
            } else {
                JOptionPane.showMessageDialog(null, "NO SE GUARDO AL USUARIO");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Cliente userInfoById(int idUsuario, Connection conn){
        Cliente user = getUserByIdHelper(idUsuario, conn);
        if (user == null) {
            user = getUserByIdHelper(1, conn);
        }
        return user;
    }

    private Cliente getUserByIdHelper(int idUsuario, Connection conn) {
        String sqlSearch = "SELECT * FROM cliente WHERE id_cliente = ?";

        try {
            PreparedStatement search = conn.prepareStatement(sqlSearch);
            search.setInt(1, idUsuario);
            ResultSet success = search.executeQuery();
            
            if (success.next()){
                return clienteFromResultSet(success);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Cliente clienteFromResultSet(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("name_cliente"),
                rs.getString("apellido_cliente"),
                rs.getString("password"),
                rs.getString("cedula"),
                rs.getDouble("initial_salary")
        );
    }

    public Map<String, Object> userProfileInfo(int idUsuario, Connection conn){
        Map<String, Object> profile = getUserProfileHelper(idUsuario, conn);
        if (profile == null) {
            profile = getUserProfileHelper(1, conn);
        }
        return profile;
    }

    private Map<String, Object> getUserProfileHelper(int idUsuario, Connection conn) {
        String sqlSearch = "SELECT nombre, apellido,correo, cedula, sueldo FROM usuarios WHERE id = ?";

        try {
            PreparedStatement search = conn.prepareStatement(sqlSearch);
            search.setInt(1, idUsuario);
            ResultSet success = search.executeQuery();
            
            if (success.next()){
                Map<String, Object> profile = new HashMap<>();
                profile.put("nombre", success.getString("nombre"));
                profile.put("apellido", success.getString("apellido"));
                profile.put("correo", success.getString("correo"));
                profile.put("cedula", success.getString("cedula"));
                profile.put("sueldo", success.getDouble("sueldo"));
                return profile;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void insertarMovimiento(Connection conn, int id, String tipo, String categoria, String frecuencia, double monto, java.sql.Date fecha){
        String sql = "INSERT INTO movimiento (cliente, tipo_movimiento, categoria, frecuencia, monto, fecha) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement data = conn.prepareStatement(sql);
            data.setInt(1, id);
            data.setString(2, tipo);
            data.setString(3, categoria);
            data.setString(4, frecuencia);
            data.setDouble(5, monto);
            data.setDate(6, fecha);

            int value = data.executeUpdate();
            if (value > 0){
                System.out.println("SE LOGRO CONECTAR");
            } else {
                System.out.println("NO SE LOGRO CONECTAR");
            }
        } catch (SQLException e){
            System.out.println("FALLO LA BASE DE DATOS");
            e.printStackTrace();
        }
    }

    public List<Movimientos> movements(String tipo, Connection conn){
        String sql = "SELECT * FROM movimiento WHERE tipo_movimiento = ?";

        List<Movimientos> info = new ArrayList<>();

        try{
            PreparedStatement data = conn.prepareStatement(sql);
            data.setString(1, tipo);

            ResultSet search =  data.executeQuery();
            while (search.next()){
                int id = search.getInt("id_movimiento");
                String type = search.getString("tipo_movimiento");
                String category = search.getString("categoria");
                String frecuencia =  search.getString("frecuencia");
                double monto = search.getDouble("monto");
                java.sql.Date fecha = search.getDate("fecha");

                Movimientos movements = new Movimientos(
                        id,
                        type,
                        category,
                        frecuencia,
                        monto,
                        fecha
                );

                info.add(movements);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return info;
    }

    public Cliente userInfoByIdFromUsuarios(int idUsuario, Connection conn){
        Cliente user = getUserFromUsuariosHelper(idUsuario, conn);
        if (user == null) {
            user = getUserFromUsuariosHelper(1, conn);
        }
        return user;
    }

    private Cliente getUserFromUsuariosHelper(int idUsuario, Connection conn) {
        String sqlSearch = "SELECT nombre, apellido, cedula, correo, sueldo FROM usuarios WHERE id = ?";

        try {
            PreparedStatement search = conn.prepareStatement(sqlSearch);
            search.setInt(1, idUsuario);
            ResultSet success = search.executeQuery();
            
            if (success.next()){
                return new Cliente(
                        idUsuario,
                        success.getString("nombre"),
                        success.getString("apellido"),
                        "",
                        success.getString("cedula"),
                        success.getDouble("sueldo")
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}
