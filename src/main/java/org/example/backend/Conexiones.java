package org.example.backend;

import org.example.info.Cliente;
import org.example.info.Movimientos;

import javax.swing.*;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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

    public void insertarMovimiento(Connection conn, int id, String tipo, String categoria, String frecuencia, double monto, Date fecha){
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
                Date fecha = search.getDate("fecha");

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



}
