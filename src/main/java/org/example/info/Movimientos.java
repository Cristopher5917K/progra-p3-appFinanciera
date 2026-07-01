package org.example.info;

import java.time.LocalDate;
import java.util.Date;

public class Movimientos {
    /**Declaración de atributos*/
    private int idMovimiento;
    private String tipoMovimiento;
    private String categoria;
    private String frecuencia;
    private double monto;
    private Date fecha;
    /**Declaración de constructores*/
    public Movimientos() {
    }

    public Movimientos(int idMovimiento, String tipoMovimiento, String categoria, String frecuencia, double monto, Date fecha) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.categoria = categoria;
        this.frecuencia = frecuencia;
        this.monto = monto;
        this.fecha = fecha;
    }

    /**Métodos propios de Java*/
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
