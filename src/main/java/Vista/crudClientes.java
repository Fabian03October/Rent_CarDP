/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Controlador.ClienteJpaController;
import Modelo.Cliente;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sergi
 */
public class crudClientes extends javax.swing.JFrame {

    private EntityManagerFactory emf;
    private ClienteJpaController clienteController;
    private DefaultTableModel modelo;
    private Cliente clienteSeleccionado;
    /**
     * Creates new form renta
     */
    public crudClientes() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializar();
    }
    
    
    private void inicializar() {
        try {
            configurarTabla();
            
            emf = Persistence.createEntityManagerFactory("JPA_Rent_Car");
            clienteController = new ClienteJpaController(emf);
            
            int count = clienteController.getClienteCount();
            
            listarClientes();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void configurarTabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Cédula (CURP)");
        jtDatos.setModel(modelo);
    }

    private void listarClientes() {
        try {
            modelo.setRowCount(0); 
            List<Cliente> clientes = clienteController.findClienteEntities();
            
            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay clientes registrados en la base de datos");
                return;
            }
            
            for (Cliente cliente : clientes) {
                Object[] fila = {
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getTelefono() != null ? cliente.getTelefono() : "",
                    cliente.getCedula()
                };
                modelo.addRow(fila);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar clientes: " + ex.getMessage());
            ex.printStackTrace(); 
        }
    }

    private void buscarClientes() {
        try {
            String busqueda = txtBusqueda.getText().trim();
            modelo.setRowCount(0);
            
            List<Cliente> clientes = clienteController.findClienteEntities();
            
            for (Cliente cliente : clientes) {
                if (cliente.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                    cliente.getCedula().toLowerCase().contains(busqueda.toLowerCase()) ||
                    (cliente.getTelefono() != null && cliente.getTelefono().toLowerCase().contains(busqueda.toLowerCase()))) {
                    
                    Object[] fila = {
                        cliente.getIdCliente(),
                        cliente.getNombre(),
                        cliente.getTelefono() != null ? cliente.getTelefono() : "",
                        cliente.getCedula()
                    };
                    modelo.addRow(fila);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar clientes: " + ex.getMessage());
        }
    }

    private void registrarCliente() {
        try {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String cedula = txtCURP.getText().trim();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los son campos obligatorios");
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setTelefono(telefono);
            cliente.setCedula(cedula);

            clienteController.create(cliente);
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente");
            limpiarCampos();
            listarClientes();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

private void seleccionarCliente() {
    int filaSeleccionada = jtDatos.getSelectedRow();
    if (filaSeleccionada >= 0) {
        try {
            Object idObj = jtDatos.getValueAt(filaSeleccionada, 0);
            int idCliente;
            
            if (idObj instanceof Integer) {
                idCliente = (Integer) idObj;
            } else {
                idCliente = Integer.parseInt(idObj.toString());
            }
           
            clienteSeleccionado = clienteController.findCliente(idCliente);

            if (clienteSeleccionado != null) {
                txtNombre.setText(clienteSeleccionado.getNombre());
                txtTelefono.setText(clienteSeleccionado.getTelefono() != null ? clienteSeleccionado.getTelefono() : "");
                txtCURP.setText(clienteSeleccionado.getCedula());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

private void editarCliente() {
    if (clienteSeleccionado == null) {
        int filaSeleccionada = jtDatos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente");
            return;
        } else {
            seleccionarCliente();
            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar el cliente seleccionado");
                return;
            }
        }
    }

    try {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String cedula = txtCURP.getText().trim();

        if (nombre.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los son campos obligatorios");
            return;
        }

        clienteSeleccionado.setNombre(nombre);
        clienteSeleccionado.setTelefono(telefono);
        clienteSeleccionado.setCedula(cedula);

        clienteController.edit(clienteSeleccionado);
        JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente");
        listarClientes();
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al editar cliente: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    private void eliminarCliente() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar este cliente?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                clienteController.destroy(clienteSeleccionado.getIdCliente());
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
                limpiarCampos();
                clienteSeleccionado = null;
                listarClientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCURP.setText("");
        txtBusqueda.setText("");
        clienteSeleccionado = null;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCURP = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtDatos = new javax.swing.JTable();
        txtBusqueda = new javax.swing.JTextField();
        btnGEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNombre.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre:"));
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 260, 60));

        txtTelefono.setBorder(javax.swing.BorderFactory.createTitledBorder("Telefono:"));
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 260, 70));

        txtCURP.setBorder(javax.swing.BorderFactory.createTitledBorder("CURP:"));
        jPanel1.add(txtCURP, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 260, 70));

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 200, -1));

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 90, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 200, -1));

        btnListar.setText("Listar");
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });
        jPanel1.add(btnListar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, 200, -1));

        jtDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", ""
            }
        ));
        jtDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtDatosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jtDatos);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 580, 147));

        txtBusqueda.setBorder(javax.swing.BorderFactory.createTitledBorder("Busqueda"));
        txtBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaActionPerformed(evt);
            }
        });
        jPanel1.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 200, -1));

        btnGEdit.setText("Limpiar");
        btnGEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGEditActionPerformed(evt);
            }
        });
        jPanel1.add(btnGEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 70, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
        buscarClientes();
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarCliente();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        registrarCliente();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        editarCliente();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
        // TODO add your handling code here:
        listarClientes();
    }//GEN-LAST:event_btnListarActionPerformed

    private void btnGEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGEditActionPerformed
        // TODO add your handling code here:
        limpiarCampos();   
    }//GEN-LAST:event_btnGEditActionPerformed

    private void jtDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtDatosMouseClicked
        // TODO add your handling code here:
        seleccionarCliente();
    }//GEN-LAST:event_jtDatosMouseClicked
  
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        jtDatosMouseClicked(evt);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(crudClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(crudClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(crudClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(crudClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new crudClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnEditar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGEdit;
    public javax.swing.JButton btnListar;
    public javax.swing.JButton btnRegistrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTable jtDatos;
    public javax.swing.JTextField txtBusqueda;
    public javax.swing.JTextField txtCURP;
    public javax.swing.JTextField txtNombre;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
