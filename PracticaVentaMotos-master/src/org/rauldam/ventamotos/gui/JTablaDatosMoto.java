package org.rauldam.ventamotos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.sql.*;

public class JTablaDatosMoto extends JTable {

    private DefaultTableModel modeloDatosMoto;
    private Connection conexion;

    public static final String TABLA = "motos";
    public static final String MARCA = "marca";
    public static final String MODELO = "modelo";
    public static final String NUMEROCHASIS = "numerochasis";
    public static final String MATRICULA = "matricula";
    public static final String PRECIOMOTO = "preciomoto";
    public static final String FECHA ="fecha";
    public static final String VENDEDOR = "vendedor";

    public JTablaDatosMoto(Connection conexion){
        super();
        this.conexion=conexion;
        inicializar();
    }

    private void inicializar(){
        modeloDatosMoto = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int fila, int columna){
                return false;
            }
        };

        modeloDatosMoto.addColumn("MARCA");
        modeloDatosMoto.addColumn("MODELO");
        modeloDatosMoto.addColumn("NUMEROCHASIS");
        modeloDatosMoto.addColumn("MATRICULA");
        modeloDatosMoto.addColumn("PRECIOMOTO");
        modeloDatosMoto.addColumn("FECHAVENTA");
        modeloDatosMoto.addColumn("VENDEDOR");

        this.setModel(modeloDatosMoto);
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

    private void cargarFilas(ResultSet resultado) throws SQLException{
        modeloDatosMoto.setNumRows(0);

        while (resultado.next()){
            String[] fila = new String[]{
                    resultado.getString(2),
                    resultado.getString(3),
                    resultado.getString(4),
                    resultado.getString(5),
                    String.valueOf(resultado.getFloat(6)),
                    String.valueOf(resultado.getDate(7)),
                    resultado.getString(8)};
            modeloDatosMoto.addRow(fila);
        }
    }

    public void nuevo(String marca, String modelo, String numchasis, String matricula, float precio, Date fecha, String vendedor) throws SQLException{
        String sentenciaSql = "INSERT INTO " + TABLA +
                " (" + MARCA + ", "
                + MODELO + ", "
                + NUMEROCHASIS + ", "
                + MATRICULA + ", "
                + PRECIOMOTO + ", "
                + FECHA + ", "
                + VENDEDOR +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, marca);
        sentencia.setString(2, modelo);
        sentencia.setString(3, numchasis);
        sentencia.setString(4, matricula);
        sentencia.setFloat(5, precio);
        sentencia.setDate(6, new java.sql.Date(fecha.getTime()));
        sentencia.setString(7, vendedor);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public void modificar(String marca, String modelo, String numchasis, String matricula, float precio, Date fecha, String vendedor, String nombreOriginal) throws SQLException{
        String  sentenciaSql = "UPDATE " + TABLA + " SET " +
                MARCA + " = ?," +
                MODELO + " = ?, " +
                NUMEROCHASIS + " = ?, " +
                MATRICULA + " = ?, " +
                PRECIOMOTO + " = ?, " +
                FECHA + " = ?, " +
                " " + VENDEDOR + " = ? WHERE " +
                MARCA + " = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, marca);
        sentencia.setString(2, modelo);
        sentencia.setString(3, numchasis);
        sentencia.setString(4, matricula);
        sentencia.setFloat(5, precio);
        sentencia.setDate(6, new java.sql.Date(fecha.getTime()));
        sentencia.setString(7, vendedor);
        sentencia.setString(8, nombreOriginal);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public void eliminar(JTable tbMoto) throws SQLException{

        int filaSeleccionada = tbMoto.getSelectedRow();

        if (filaSeleccionada == -1)
            return;

        String nombreSeleccionado = (String) getValueAt(filaSeleccionada, 0);
        String sentenciaSql = "DELETE FROM " + TABLA + " WHERE " + MARCA + " = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombreSeleccionado);
        sentencia.executeUpdate();

        if (sentencia != null)
            sentencia.close();
    }

    public boolean existe(String nombre) throws SQLException {

        String consulta = "SELECT COUNT(*) FROM " + TABLA + " WHERE " + MARCA + " = ?";

        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombre);
        ResultSet resultado = sentencia.executeQuery();

        resultado.next();

        if (resultado.getInt(1) == 1)
            return true;

        return false;
    }

    public DefaultTableModel getModeloDatosMoto() {
        return modeloDatosMoto;
    }
}
