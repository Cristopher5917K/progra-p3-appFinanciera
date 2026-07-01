package org.example.info;

public class Cliente {
    /**Declaración de atributos*/
    private int idCliente;
    private String nameCliente;
    private String apellidoCliente;
    private String password;
    private String cedula;
    private double initialSalary;
    /**Declaración de constructores*/
    public Cliente() {
    }

    public Cliente(int idCliente, String nameCliente, String apellidoCliente, String password, String cedula, double initialSalary) {
        this.idCliente = idCliente;
        this.nameCliente = nameCliente;
        this.apellidoCliente = apellidoCliente;
        this.password = password;
        this.cedula = cedula;
        this.initialSalary = initialSalary;
    }
    /**Métodos propios de Java*/
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNameCliente() {
        return nameCliente;
    }

    public void setNameCliente(String nameCliente) {
        this.nameCliente = nameCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getInitialSalary() {
        return initialSalary;
    }

    public void setInitialSalary(double initialSalary) {
        this.initialSalary = initialSalary;
    }
}
