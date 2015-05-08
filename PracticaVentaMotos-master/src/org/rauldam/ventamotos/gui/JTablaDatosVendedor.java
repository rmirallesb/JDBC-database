package org.rauldam.ventamotos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class JTablaDatosVendedor extends JTable {

    private DefaultTableModel modeloDatosVendedor;
    private Connection conexion;

    public static final String TABLA = "vendedores";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String DNI = "dni";
    public static final String IDCLIENTE = "idcliente";
    public static final String PROVINCIA = "provincia";
    public static final String SALARIO = "salario";

    public JTablaDatosVendedor(Connection conexion){
        super();
        this.conexion=conexion;
        inicializar();
    }

    private void inicializar(){
        modeloDatosVendedor = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna){
                return false;
            }
        };

        modeloDatosVendedor.addColumn("NOMBRE");
        modeloDatosVendedor.addColumn("APELLIDO");
        modeloDatosVendedor.addColumn("DNI");
        modeloDatosVendedor.addColumn("IDCLIENTE");
        modeloDatosVendedor.addColumn("PROVINCIA");
        modeloDatosVendedor.addColumn("SALARIO");

        this.setModel(modeloDatosVendedor);
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
        modeloDatosVendedor.setNumRows(0);

        while (resultado.next()){
            String[] fila = new String[]{
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getString(5),
                    resultado.getString(6),
                    String.valueOf(resultado.getInt(7))};
            modeloDatosVendedor.addRow(fila);
        }
    }

    public void nuevo(String nombre, String apellido, String dni, String idcliente, String provincia, int salario) throws SQLException{
        String  sentenciaSql = "INSERT INTO " + TABLA +
                " (" + NOMBRE + ", "
                + APELLIDO + ", "
                + DNI + ", "
                + IDCLIENTE + ", "
                + PROVINCIA + ", "
                + SALARIO +
                ") VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombre);
        sentencia.setString(2, apellido);
        sentencia.setString(3, dni);
        sentencia.setString(4, idcliente);
        sentencia.setString(5, provincia);
        sentencia.setInt(6, salario);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public void modificar(String nombre, String apellido, String dni, String idcliente, String provincia, int salario, String nombreOriginal) throws SQLException{

        String  sentenciaSql = "UPDATE " + TABLA + " SET " +
                NOMBRE + " = ?," +
                APELLIDO + " = ?, " +
                DNI + " = ?, " +
                IDCLIENTE + " = ?, " +
                PROVINCIA + " = ?, " +
                " " + SALARIO + " = ? WHERE " +
                NOMBRE + " = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombre);
        sentencia.setString(2, apellido);
        sentencia.setString(3, dni);
        sentencia.setString(4, idcliente);
        sentencia.setString(5, provincia);
        sentencia.setInt(6, salario);
        sentencia.setString(7, nombreOriginal);
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

    public DefaultTableModel getModeloDatosVendedor() {
        return modeloDatosVendedor;
    }
}
