package org.rauldam.ventamotos.gui;

import com.toedter.calendar.JDateChooser;
import org.rauldam.ventamotos.base.Cliente;
import org.rauldam.ventamotos.base.Moto;
import org.rauldam.ventamotos.base.Vendedor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class Ventana extends JFrame {
    private JPanel panel1;
    private JTabbedPane tpPestanas;
    private JTextField tfNombreCliente;
    private JTextField tfApellidoCliente;
    private JTextField tfDniCliente;
    private JTextField tfDireccionCliente;
    private JTextField tfProvinciaCliente;
    private JTextField tfTelefonoCliente;
    private JButton btModificarCliente;
    private JButton btGuardarCliente;
    private JButton btEliminarCliente;
    private JComboBox cbMotoCliente;
    private JTextField tfMarcaMoto;
    private JTextField tfModeloMoto;
    private JTextField tfNumChasisMoto;
    private JTextField tfMatriculaMoto;
    private JTextField tfPrecioMoto;
    private JDateChooser dcFechaMoto;
    private JButton btModificarMoto;
    private JButton btGuardarMoto;
    private JButton btEliminarMoto;
    private JTextField tfNombreVendedor;
    private JTextField tfApellidoVendedor;
    private JTextField tfDniVendedor;
    private JTextField tfSalarioVendedor;
    private JButton btModificarVendedor;
    private JButton btGuardarVendedor;
    private JButton btEliminarVendedor;
    private JComboBox cbVendedorMoto;
    private JTextField tfIdClienteVendedor;
    private JTextField tfProvinciaVendedor;
    private JTable tbVendedor;
    private JTable tbCliente;
    private JTable tbMoto;
    private JButton btNuevoCliente;
    private JButton btNuevaMoto;
    private JButton btNuevoVendedor;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Connection conexion;

    private boolean modificar;

    public JTablaDatosCliente tablaDatosCliente;
    public JTablaDatosMoto tablaDatosMoto;
    public JTablaDatosVendedor tablaDatosVendedor;

    public Cliente cliente;
    public Moto moto;
    public Vendedor vendedor;

    private boolean nuevoCliente;
    private boolean nuevaMoto;
    private boolean nuevoVendedor;

    DefaultTableModel modeloTablaDatosCliente;
    DefaultTableModel modeloTablaDatosMoto;
    DefaultTableModel modeloTablaDatosVendedor;

    private enum Pestana {
        CLIENTE, MOTO, VENDEDOR
    }

    public Ventana() {
        conexion();
        setVisible(true);
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menuOpciones());
        pack();
        setLocationRelativeTo(null);
        setTitle("Tienda de Motos");

        modeloTablaDatosCliente = tablaDatosCliente.getModeloDatosCliente();
        tbCliente.setModel(modeloTablaDatosCliente);

        modeloTablaDatosMoto = tablaDatosMoto.getModeloDatosMoto();
        tbMoto.setModel(modeloTablaDatosMoto);

        modeloTablaDatosVendedor = tablaDatosVendedor.getModeloDatosVendedor();
        tbVendedor.setModel(modeloTablaDatosVendedor);

        btGuardarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        btModificarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });
        btEliminarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btGuardarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        btModificarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });
        btEliminarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btGuardarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        btModificarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });
        btEliminarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btNuevoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });
        btNuevaMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });
        btNuevoVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });

        tpPestanas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cargarPestanaActual();
            }
        });
    }

    public JMenuBar menuOpciones() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Servidor");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Preferencias");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                conecta();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Desconectar");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                desconectar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Salir");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                salir();
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    private void conecta() {
        JConecta conecta = new JConecta();
        if (conecta.mostrarDialogo() == JConecta.Accion.CANCELAR)
            return;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/ventamotos", conecta.getUsuario(), conecta.getContrasena());

            tablaDatosCliente = new JTablaDatosCliente(conexion);
            tablaDatosMoto = new JTablaDatosMoto(conexion);
            tablaDatosVendedor = new JTablaDatosVendedor(conexion);

            cargarPestanaActual();

        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
            System.exit(0);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
            System.exit(0);
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void conexion(){
        Properties configuracion = new Properties();

        try {
            configuracion.load(new FileInputStream("configuracion.props"));

            String driver = configuracion.getProperty("driver");
            String protocolo = configuracion.getProperty("protocolo");
            String servidor = configuracion.getProperty("servidor");
            String puerto = configuracion.getProperty("puerto");
            String baseDatos = configuracion.getProperty("basedatos");
            String usuario = configuracion.getProperty("usuario");
            String contrasena = configuracion.getProperty("contrasena");

            Class.forName(driver).newInstance();
            conexion = DriverManager.getConnection(
                    protocolo +
                            servidor + ":" + puerto +
                            "/" + baseDatos,
                    usuario, contrasena);
            tablaDatosCliente = new JTablaDatosCliente(conexion);
            tablaDatosMoto = new JTablaDatosMoto(conexion);
            tablaDatosVendedor = new JTablaDatosVendedor(conexion);
            cargarPestanaActual();

        } catch (FileNotFoundException fnfe) {
            JConecta jCon = new JConecta();
            jCon.mostrarDialogo();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error al leer el fichero de configuracion");
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void desconectar(){
        try {
            conexion.close();
            conexion = null;
            JOptionPane.showMessageDialog(null, "Se ha desconectado");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void salir(){
        desconectar();
        System.exit(0);
    }

    private Pestana getPestanaActual() {
        int indice = tpPestanas.getSelectedIndex();
        return Pestana.values()[indice];
    }

    private void cargarPestanaActual() {
        switch (getPestanaActual()){
            case CLIENTE:
                try {
                    tablaDatosCliente.listar();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                ArrayList<String> listaMotosCliente = new ArrayList<String>();

                try {
                    Statement sentencia = conexion.createStatement();
                    ResultSet result = sentencia.executeQuery("SELECT marca FROM motos");

                    while (result.next()) {
                        String fila = result.getString(1);
                        listaMotosCliente.add(fila);
                    }
                    cbMotoCliente.removeAllItems();
                    for (int i = 0; i < listaMotosCliente.size(); i++){
                        cbMotoCliente.addItem(listaMotosCliente.get(i));
                    }
                    sentencia.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MOTO:
                try {
                    tablaDatosMoto.listar();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                ArrayList<String> listaVendedoresMoto = new ArrayList<String>();

                try {
                    Statement sentencia = conexion.createStatement();
                    ResultSet result = sentencia.executeQuery("SELECT nombre FROM vendedores");

                    while (result.next()) {
                        String fila = result.getString(1);
                        listaVendedoresMoto.add(fila);
                    }
                    cbVendedorMoto.removeAllItems();
                    for (int i = 0; i < listaVendedoresMoto.size(); i++){
                        cbVendedorMoto.addItem(listaVendedoresMoto.get(i));
                    }
                    sentencia.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case VENDEDOR:
                try {
                    tablaDatosVendedor.listar();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                break;
        }
    }

    private void nuevo(){
        switch (getPestanaActual()){
            case CLIENTE:
                nuevoCliente = true;
                break;
            case MOTO:
                nuevaMoto = true;
                break;
            case VENDEDOR:
                nuevoVendedor = true;
                break;
        }
    }

    private void guardar(){
        switch (getPestanaActual()){
            case CLIENTE:
                if(modificar==true){
                    modificar=false;
                    try {
                        String nombreOriginal = cliente.getNombre();

                        cliente.setNombre(tfNombreCliente.getText());
                        cliente.setApellido(tfApellidoCliente.getText());
                        cliente.setDni(tfDniCliente.getText());
                        cliente.setDireccion(tfDireccionCliente.getText());
                        cliente.setProvincia(tfProvinciaCliente.getText());
                        cliente.setTelefono(Integer.parseInt(tfTelefonoCliente.getText()));
                        cliente.setMoto(String.valueOf(cbMotoCliente.getSelectedItem()));

                        tablaDatosCliente.modificar(cliente.getNombre(), cliente.getApellido(), cliente.getDni(),
                                cliente.getDireccion(), cliente.getProvincia(), cliente.getTelefono(),
                                cliente.getMoto(), nombreOriginal);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    cliente=null;
                    if (nuevoCliente)
                        cliente = new Cliente();
                        cliente.setNombre(tfNombreCliente.getText());
                        cliente.setApellido(tfApellidoCliente.getText());
                        cliente.setDni(tfDniCliente.getText());
                        cliente.setDireccion(tfDireccionCliente.getText());
                        cliente.setProvincia(tfProvinciaCliente.getText());
                        cliente.setTelefono(Integer.parseInt(tfTelefonoCliente.getText()));
                        cliente.setMoto(String.valueOf(cbMotoCliente.getSelectedItem()));
                    try {
                        if (tablaDatosCliente.existe(cliente.getNombre())){
                            JOptionPane.showMessageDialog(null, "Ya existe ese nombre", "Alta", JOptionPane.ERROR_MESSAGE);
                            return;
                        }else{
                            tablaDatosCliente.nuevo(cliente.getNombre(), cliente.getApellido(), cliente.getDni(),
                                    cliente.getDireccion(), cliente.getProvincia(), cliente.getTelefono(), cliente.getMoto());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MOTO:
                if(modificar==true){
                    modificar=false;
                    try {
                        String nombreOriginal = moto.getMarca();

                        moto.setMarca(tfMarcaMoto.getText());
                        moto.setModelo(tfModeloMoto.getText());
                        moto.setNumeroChasis(tfNumChasisMoto.getText());
                        moto.setMatricula(tfMatriculaMoto.getText());
                        moto.setPrecioMoto(Float.parseFloat(tfPrecioMoto.getText()));
                        moto.setFechaVenta(dcFechaMoto.getDate());
                        moto.setVendedor(String.valueOf(cbVendedorMoto.getSelectedItem()));

                        tablaDatosMoto.modificar(moto.getMarca(), moto.getModelo(), moto.getNumeroChasis(),
                                moto.getMatricula(), moto.getPrecioMoto(), moto.getFechaVenta(), moto.getVendedor(),
                                nombreOriginal);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }else {
                    moto=null;
                    if (nuevaMoto)
                        moto = new Moto();
                        moto.setMarca(tfMarcaMoto.getText());
                        moto.setModelo(tfModeloMoto.getText());
                        moto.setNumeroChasis(tfNumChasisMoto.getText());
                        moto.setMatricula(tfMatriculaMoto.getText());
                        moto.setPrecioMoto(Float.parseFloat(tfPrecioMoto.getText()));
                        moto.setFechaVenta(dcFechaMoto.getDate());
                        moto.setVendedor(String.valueOf(cbVendedorMoto.getSelectedItem()));
                    try {
                        if (tablaDatosMoto.existe(moto.getMarca())){
                            JOptionPane.showMessageDialog(null, "Ya existe esa marca", "Alta", JOptionPane.ERROR_MESSAGE);
                            return;
                        }else {
                            tablaDatosMoto.nuevo(moto.getMarca(), moto.getModelo(), moto.getNumeroChasis(),
                                    moto.getMatricula(), moto.getPrecioMoto(), moto.getFechaVenta(), moto.getVendedor());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case VENDEDOR:
                if(modificar==true){
                    modificar=false;
                    try {
                        String nombreOriginal = vendedor.getNombre();

                        vendedor.setNombre(tfNombreVendedor.getText());
                        vendedor.setApellido(tfApellidoVendedor.getText());
                        vendedor.setDni(tfDniVendedor.getText());
                        vendedor.setIdCliente(tfIdClienteVendedor.getText());
                        vendedor.setProvincia(tfProvinciaVendedor.getText());
                        vendedor.setSalario(Integer.parseInt(tfSalarioVendedor.getText()));

                        tablaDatosVendedor.modificar(vendedor.getNombre(), vendedor.getApellido(), vendedor.getDni(),
                                vendedor.getIdCliente(), vendedor.getProvincia(), vendedor.getSalario(), nombreOriginal);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    vendedor=null;
                    if (nuevoVendedor)
                        vendedor = new Vendedor();
                        vendedor.setNombre(tfNombreVendedor.getText());
                        vendedor.setApellido(tfApellidoVendedor.getText());
                        vendedor.setDni(tfDniVendedor.getText());
                        vendedor.setIdCliente(tfIdClienteVendedor.getText());
                        vendedor.setProvincia(tfProvinciaVendedor.getText());
                        vendedor.setSalario(Integer.parseInt(tfSalarioVendedor.getText()));
                    try {
                        if (tablaDatosVendedor.existe(vendedor.getNombre())){
                            JOptionPane.showMessageDialog(null, "Ya existe ese nombre", "Alta", JOptionPane.ERROR_MESSAGE);
                            return;
                        }else {
                            tablaDatosVendedor.nuevo(vendedor.getNombre(), vendedor.getApellido(), vendedor.getDni(),
                                    vendedor.getIdCliente(), vendedor.getProvincia(), vendedor.getSalario());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
        cargarPestanaActual();
    }

    private void modificar(){
        int filaSeleccionada=0;
        switch (getPestanaActual()){
            case CLIENTE:
                modificar=true;
                nuevoCliente=false;
                filaSeleccionada=0;

                filaSeleccionada = tbCliente.getSelectedRow();
                if (filaSeleccionada == -1)
                    return;

                cliente = new Cliente();

                cliente.setNombre((String) tbCliente.getValueAt(filaSeleccionada, 0));
                cliente.setApellido((String) tbCliente.getValueAt(filaSeleccionada, 1));
                cliente.setDni((String) tbCliente.getValueAt(filaSeleccionada, 2));
                cliente.setDireccion((String) tbCliente.getValueAt(filaSeleccionada, 3));
                cliente.setProvincia((String) tbCliente.getValueAt(filaSeleccionada, 4));
                cliente.setTelefono(Integer.parseInt((String) tbCliente.getValueAt(filaSeleccionada, 5)));
                cliente.setMoto((String) tbCliente.getValueAt(filaSeleccionada, 6));

                tfNombreCliente.setText(cliente.getNombre());
                tfApellidoCliente.setText(cliente.getApellido());
                tfDniCliente.setText(cliente.getDni());
                tfDireccionCliente.setText(cliente.getDireccion());
                tfProvinciaCliente.setText(cliente.getProvincia());
                tfTelefonoCliente.setText(String.valueOf(cliente.getTelefono()));
                cbMotoCliente.setSelectedItem(cliente.getMoto());
                break;
            case MOTO:
                modificar=true;
                nuevaMoto=false;
                filaSeleccionada=0;

                filaSeleccionada = tbMoto.getSelectedRow();
                if (filaSeleccionada == -1)
                    return;

                moto = new Moto();

                moto.setMarca((String) tbMoto.getValueAt(filaSeleccionada, 0));
                moto.setModelo((String) tbMoto.getValueAt(filaSeleccionada,1));
                moto.setNumeroChasis((String) tbMoto.getValueAt(filaSeleccionada,2));
                moto.setMatricula((String) tbMoto.getValueAt(filaSeleccionada,3));
                moto.setPrecioMoto((Float.parseFloat((String) tbMoto.getValueAt(filaSeleccionada,4))));
                try {
                    moto.setFechaVenta(dateFormat.parse((String) tbMoto.getValueAt(filaSeleccionada,5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                moto.setVendedor((String) tbMoto.getValueAt(filaSeleccionada,6));

                tfMarcaMoto.setText(moto.getMarca());
                tfModeloMoto.setText(moto.getModelo());
                tfNumChasisMoto.setText(moto.getNumeroChasis());
                tfMatriculaMoto.setText(moto.getMatricula());
                tfPrecioMoto.setText(String.valueOf(moto.getPrecioMoto()));
                dcFechaMoto.setDate(moto.getFechaVenta());
                cbVendedorMoto.setSelectedItem(moto.getVendedor());
                break;
            case VENDEDOR:
                modificar=true;
                nuevoVendedor=false;
                filaSeleccionada=0;

                filaSeleccionada = tbVendedor.getSelectedRow();
                if (filaSeleccionada == -1)
                    return;

                vendedor = new Vendedor();

                vendedor.setNombre((String) tbVendedor.getValueAt(filaSeleccionada,0));
                vendedor.setApellido((String) tbVendedor.getValueAt(filaSeleccionada,1));
                vendedor.setDni((String) tbVendedor.getValueAt(filaSeleccionada,2));
                vendedor.setIdCliente((String) tbVendedor.getValueAt(filaSeleccionada,3));
                vendedor.setProvincia((String) tbVendedor.getValueAt(filaSeleccionada,4));
                vendedor.setSalario((Integer.parseInt((String) tbVendedor.getValueAt(filaSeleccionada, 5))));

                tfNombreVendedor.setText(vendedor.getNombre());
                tfApellidoVendedor.setText(vendedor.getApellido());
                tfDniVendedor.setText(vendedor.getDni());
                tfIdClienteVendedor.setText(vendedor.getIdCliente());
                tfProvinciaVendedor.setText(vendedor.getProvincia());
                tfSalarioVendedor.setText(String.valueOf(vendedor.getSalario()));
                break;
        }

    }

    private void eliminar(){
        switch (getPestanaActual()) {
            case CLIENTE:
                try {
                    tablaDatosCliente.eliminar(tbCliente);

                    JOptionPane.showMessageDialog(null, "El cliente se ha eliminado");

                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                break;
            case MOTO:
                try {
                    tablaDatosMoto.eliminar(tbMoto);

                    JOptionPane.showMessageDialog(null, "La moto se ha eliminado");

                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                break;
            case VENDEDOR:
                try {
                    tablaDatosVendedor.eliminar(tbVendedor);

                    JOptionPane.showMessageDialog(null, "El vendedor se ha eliminado");

                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                break;
        }
        cargarPestanaActual();
    }

    public static void main(String[] args) {
        new Ventana();
    }
}
