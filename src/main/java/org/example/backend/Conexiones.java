package org.example.backend;

import com.nimbusds.jose.shaded.gson.internal.NonNullElementWrapperList;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.info.Cliente;
import org.example.info.Meta;
import org.example.info.Movimientos;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Conexiones {

    private static final Dotenv dotenv = Dotenv.load();

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

    public void registerClient(Connection conn, String name, String apellido, String cedula, double sueldo, String password){
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

    public void registerUser(Connection conn, String name, String apellido, String correo, String password, double sueldo, String cedula){
        String sql = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, sueldo, cedula) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement data = conn.prepareStatement(sql);
            data.setString(1, name);
            data.setString(2, apellido);
            data.setString(3, correo);
            data.setString(4, password);
            data.setDouble(5, sueldo);
            data.setString(6, cedula);

            int value = data.executeUpdate();
            if (value > 0){
                System.out.println("SE INSERTO EL USUARIO");
            } else {
                System.out.println("NO SE INSERTO EL USUARIO");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("ERROR EN LA BASE DE DATOS");
        }
    }

    public Cliente userLogin(String correo, String password, Connection conn){
        String sqlSearch = "SELECT * FROM cliente WHERE password = ?";

        try {
            PreparedStatement search = conn.prepareStatement(sqlSearch);
            search.setString(1, password);

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

    public double sumarGastosDelMes(int clienteId, Connection conn) {
        String sql = "SELECT SUM(monto) AS total_gastos FROM movimiento WHERE cliente = ? AND tipo_movimiento = 'GASTO'";

        try (PreparedStatement data = conn.prepareStatement(sql)) {
            data.setInt(1, clienteId);
            ResultSet rs = data.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_gastos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double sumarIngresosDelMes(int clienteId, Connection conn) {
        String sql = "SELECT SUM(monto) AS total_ingresos FROM movimiento WHERE cliente = ? AND tipo_movimiento = 'INGRESO'";

        try (PreparedStatement data = conn.prepareStatement(sql)) {
            data.setInt(1, clienteId);
            ResultSet rs = data.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_ingresos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<Movimientos> movements(String tipo, int idUsuario, Connection conn){
        String sql = "SELECT * FROM movimiento WHERE tipo_movimiento = ? AND cliente = ?";

        List<Movimientos> info = new ArrayList<>();

        try{
            PreparedStatement data = conn.prepareStatement(sql);
            data.setString(1, tipo);
            data.setInt(2, idUsuario);

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

    public boolean updateUserProfile(int idUsuario, String nombre, String apellido, String cedula, double sueldo, Connection conn) {
        String sqlUpdate = "UPDATE usuarios SET nombre = ?, apellido = ?, cedula = ?, sueldo = ? WHERE id = ?";

        try {
            PreparedStatement update = conn.prepareStatement(sqlUpdate);
            update.setString(1, nombre);
            update.setString(2, apellido);
            update.setString(3, cedula);
            update.setDouble(4, sueldo);
            update.setInt(5, idUsuario);

            int result = update.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void updateSalary(int idCliente, double salary, Connection conn){
        String update = "UPDATE cliente SET initial_salary = ? WHERE id_cliente = ?";

        try {
            PreparedStatement newData = conn.prepareStatement(update);
            newData.setInt(1, idCliente);
            newData.setDouble(2, salary);

            int value = newData.executeUpdate();
            if (value > 0){
                System.out.println("SE ACTUALIZO EL SALARIO");
            } else {
                System.out.println("NO SE ACTUALIZO EL SALARIO");
            }
        } catch (Exception e){

        }

    }

    public Map<String, Object> guardarCambiosUsuario(int idUsuario, String nombre, String apellido, String cedula, String sueldo, Connection conn) {
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty()) {
                resultado.put("success", false);
                resultado.put("message", "Completa todos los campos");
                return resultado;
            }

            double sueldoNumerico = Double.parseDouble(sueldo);
            boolean actualizado = updateUserProfile(idUsuario, nombre, apellido, cedula, sueldoNumerico, conn);

            if (actualizado) {
                resultado.put("success", true);
                resultado.put("message", "Perfil actualizado exitosamente");
                resultado.put("nombre", nombre);
                resultado.put("apellido", apellido);
                resultado.put("cedula", cedula);
                resultado.put("sueldo", sueldoNumerico);
            } else {
                resultado.put("success", false);
                resultado.put("message", "Error al guardar cambios");
            }
        } catch (NumberFormatException e) {
            resultado.put("success", false);
            resultado.put("message", "El sueldo debe ser un número válido");
        } catch (Exception e) {
            resultado.put("success", false);
            resultado.put("message", "Error: " + e.getMessage());
        }
        
        return resultado;
    }

    public List<Meta> getMetasByUsuario(int idUsuario, Connection conn) {
        List<Meta> metas = new ArrayList<>();
        String sql = "SELECT * FROM metas WHERE id_cliente = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                metas.add(new Meta(
                        rs.getInt("id_meta"),
                        rs.getString("name"),
                        rs.getDouble("target_amount"),
                        rs.getDouble("saved_amount"),
                        rs.getDate("deadline").toLocalDate(),
                        rs.getString("color"),
                        rs.getString("category"),
                        rs.getDate("creation_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metas;
    }

    public boolean addMeta(Meta meta, int idUsuario, Connection conn) {
        String sql = "INSERT INTO metas (id_cliente, name, target_amount, saved_amount, deadline, color, category, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, meta.getName());
            ps.setDouble(3, meta.getTargetAmount());
            ps.setDouble(4, meta.getSavedAmount());
            ps.setDate(5, java.sql.Date.valueOf(meta.getDeadline()));
            ps.setString(6, meta.getColor());
            ps.setString(7, meta.getCategory());
            ps.setDate(8, java.sql.Date.valueOf(meta.getCreationDate()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMeta(Meta meta, Connection conn) {
        String sql = "UPDATE metas SET name = ?, target_amount = ?, saved_amount = ?, deadline = ?, color = ?, category = ? WHERE id_meta = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, meta.getName());
            ps.setDouble(2, meta.getTargetAmount());
            ps.setDouble(3, meta.getSavedAmount());
            ps.setDate(4, java.sql.Date.valueOf(meta.getDeadline()));
            ps.setString(5, meta.getColor());
            ps.setString(6, meta.getCategory());
            ps.setInt(7, meta.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMeta(int idMeta, Connection conn) {
        String sql = "DELETE FROM metas WHERE id_meta = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMeta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}