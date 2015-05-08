package org.rauldam.ventamotos.base;

import java.io.Serializable;

public class Vendedor implements Serializable {
    private String nombre;
    private String apellido;
    private String dni;
    private String idCliente;
    private String provincia;
    private int salario;

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idVendedor) {
        this.idCliente = idVendedor;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return nombre + apellido;
    }
}