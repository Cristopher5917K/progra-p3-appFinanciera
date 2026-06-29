package org.example.backend;

import org.apache.commons.configuration2.event.ConfigurationErrorEvent;
import org.example.info.Cliente;

import javax.swing.*;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Conexiones {

    public Connection getConnection(){
        String url = "jdbc:mysql://localhost:3306/smart_saving";
        String user = "root";
        String password = "isaacmasache65*"; //Deben registrar sus contraseñas

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "NO SE LOGRO CONECTAR A LA BASE DE DATOS" );
            e.printStackTrace();
        }

        return null;
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
                    JOptionPane.showMessageDialog(null, "EL USUARIO SE REGISTRO EXISTOSAMENTE");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE LOGRO REGISTRAR AL USUARIO");
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
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


}
