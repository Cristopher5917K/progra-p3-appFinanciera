package org.example.info;

public class Reporte {
    /**Declaración de atributos*/
    private double sueldoInicial;
    private double totalIngresos;
    private double totalGastos;
    private double ahorroDisponible;
    public String[] categorias;
    public double[] montos;
    /**Declaración de constructores*/
    public Reporte() {
    }

    public Reporte(double sueldoInicial, double totalIngresos, double totalGastos, double ahorroDisponible, String[] categorias, double[] montos) {
        this.sueldoInicial = sueldoInicial;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.ahorroDisponible = ahorroDisponible;
        this.categorias = categorias;
        this.montos = montos;
    }
    /**Métodos proios de Java*/
    public double getSueldoInicial() {
        return sueldoInicial;
    }

    public void setSueldoInicial(double sueldoInicial) {
        this.sueldoInicial = sueldoInicial;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getAhorroDisponible() {
        return ahorroDisponible;
    }

    public void setAhorroDisponible(double ahorroDisponible) {
        this.ahorroDisponible = ahorroDisponible;
    }

    public String[] getCategorias() {
        return categorias;
    }

    public void setCategorias(String[] categorias) {
        this.categorias = categorias;
    }

    public double[] getMontos() {
        return montos;
    }

    public void setMontos(double[] montos) {
        this.montos = montos;
    }
}
