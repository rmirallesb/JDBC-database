package org.rauldam.ventamotos.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JConecta extends JDialog{
    private JPanel panel1;
    private JButton btAceptar;
    private JButton btCancelar;
    private JTextField tfUsuario;
    private JPasswordField tfContrasena;

    private String usuario;
    private String contrasena;

    public enum Accion {
        ACEPTAR, CANCELAR
    }
    private Accion accion;

    public JConecta() {
        setTitle("Conexion");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        btAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });
        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void aceptar() {
        usuario = tfUsuario.getText();
        contrasena = String.valueOf(tfContrasena.getPassword());

        accion = Accion.ACEPTAR;
        setVisible(false);
    }

    private void cancelar() {

        accion = Accion.CANCELAR;
        setVisible(false);
        System.exit(0);
    }

    public Accion mostrarDialogo() {
        setVisible(true);
        return accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}
