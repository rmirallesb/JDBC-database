package org.rauldam.ventamotos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class JTablaDatosCliente extends JTable {

    private DefaultTableModel modeloDatosCliente;
    private Connection conexion;

    public static final String TABLA = "clientes";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String DNI = "dni";
    public static final String DIRECCION = "direccion";
    public static final String PROVINCIA = "provincia";
    public static final String TELEFONO = "telefono";
    public static final String MOTO = "moto";

    public JTablaDatosCliente(Connection conexion){
        super();
        this.conexion=conexion;
        inicializar();
    }

    private void inicializar(){
        modeloDatosCliente = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna){
                return false;
            }
        };

        modeloDatosCliente.addColumn("NOMBRE");
        modeloDatosCliente.addColumn("APELLIDO");
        modeloDatosCliente.addColumn("DNI");
        modeloDatosCliente.addColumn("DIRECCION");
        modeloDatosCliente.addColumn("PROVINCIA");
        modeloDatosCliente.addColumn("TELEFONO");
        modeloDatosCliente.addColumn("MOTO");

        this.setModel(modeloDatosCliente);
    }

    public void listar() throws SQLException {

        if (conexion == null)
            return;

        if (conexion.isClosed())
            return;

        String consulta = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        consulta = "SELECT * FROM " + TABLA;
        sentencia = conexion.prepareStatement(consulta);
        resultado = sentencia.executeQuery();

        cargarFilas(resultado);
    }

    private void cargarFilas(ResultSet resultado) throws SQLException {
        modeloDatosCliente.setNumRows(0);

        while (resultado.next()) {
            String[] fila = new String[]{
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getString(5),
                    resultado.getString(6),
                    String.valueOf(resultado.getInt(7)),
                    resultado.getString(8)};
            modeloDatosCliente.addRow(fila);
        }
    }

    public void nuevo(String nombre, String apellido, String dni, String direccion, String provincia, int telefono, String moto) throws SQLException{
        String  sentenciaSql = "INSERT INTO " + TABLA +
                " (" + NOMBRE + ", "
                + APELLIDO + ", "
                + DNI + ", "
                + DIRECCION + ", "
                + PROVINCIA + ", "
                + TELEFONO + ", "
                + MOTO +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombre);
        sentencia.setString(2, apellido);
        sentencia.setString(3, dni);
        sentencia.setString(4, direccion);
        sentencia.setString(5, provincia);
        sentencia.setInt(6, telefono);
        sentencia.setString(7, moto);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public void modificar(String nombre, String apellido, String dni, String direccion, String provincia, int telefono, String moto, String nombreOriginal) throws SQLException{
        String  sentenciaSql = "UPDATE " + TABLA + " SET " +
                NOMBRE + " = ?," +
                APELLIDO + " = ?, " +
                DNI + " = ?, " +
                DIRECCION + " = ?, " +
                PROVINCIA + " = ?, " +
                TELEFONO + " = ?, " +
                " " + MOTO + " = ? WHERE " +
                NOMBRE + " = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombre);
        sentencia.setString(2, apellido);
        sentencia.setString(3, dni);
        sentencia.setString(4, direccion);
        sentencia.setString(5, provincia);
        sentencia.setInt(6, telefono);
        sentencia.setString(7, moto);
        sentencia.setString(8, nombreOriginal);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public void eliminar(JTable tbCliente) throws SQLException {

        int filaSeleccionada = tbCliente.getSelectedRow();

        if (filaSeleccionada == -1)
            return;

        String nombreSeleccionado = (String) getValueAt(filaSeleccionada, 0);
        String sentenciaSql = "DELETE FROM " + TABLA + " WHERE " + NOMBRE + " = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombreSeleccionado);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public boolean existe(String nombre) throws SQLException {

        String consulta = "SELECT COUNT(*) FROM " + TABLA + " WHERE " + NOMBRE + " = ?";

        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombre);
        ResultSet resultado = sentencia.executeQuery();

        resultado.next();

        if (resultado.getInt(1) == 1)
            return true;

        return false;
    }

    public DefaultTableModel getModeloDatosCliente() {
        return modeloDatosCliente;
    }
}

