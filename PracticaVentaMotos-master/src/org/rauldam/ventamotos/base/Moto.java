package org.rauldam.ventamotos.base;

import javax.swing.*;
import java.util.Date;

public class Moto extends JDialog {

    private String marca;
    private String modelo;
    private String numeroChasis;
    private String matricula;
    private float precioMoto;
    private Date fechaVenta;

    private String vendedor;

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNumeroChasis() {
        return numeroChasis;
    }

    public void setNumeroChasis(String numeroChasis) {
        this.numeroChasis = numeroChasis;
    }

    public float getPrecioMoto() {
        return precioMoto;
    }

    public void setPrecioMoto(float precioFinal) {
        this.precioMoto = precioFinal;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public String toString() {
        return marca + modelo;
    }

}
