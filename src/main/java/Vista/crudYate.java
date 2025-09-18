/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Controlador.YateJpaController;
import Controlador.AcuaticoJpaController;
import Controlador.VehiculoJpaController;
import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Yate;
import Modelo.Acuatico;
import Modelo.Vehiculo;
import java.math.BigDecimal;
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
public class crudYate extends javax.swing.JFrame {

    private EntityManagerFactory emf;
    private YateJpaController yateController;
    private AcuaticoJpaController acuaticoController;
    private VehiculoJpaController vehiculoController;
    private DefaultTableModel modelo;
    private Yate yateSeleccionado;

    /**
     * Creates new form Yate
     */
    public crudYate() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializar();
    }

    private void inicializar() {
        try {
            emf = Persistence.createEntityManagerFactory("JPA_Rent_Car");
            yateController = new YateJpaController(emf);
            acuaticoController = new AcuaticoJpaController(emf);
            vehiculoController = new VehiculoJpaController(emf);
            configurarTabla();
            listarYates();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al inicializar: " + ex.getMessage());
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
        modelo.addColumn("Marca");
        modelo.addColumn("Modelo");
        modelo.addColumn("Precio");
        modelo.addColumn("Año");
        modelo.addColumn("Eslora");
        modelo.addColumn("Calado");
        modelo.addColumn("Motor Eléctrico");
        jtDatos.setModel(modelo);
    }

    private void listarYates() {
        try {
            modelo.setRowCount(0);
            List<Yate> yates = yateController.findYateEntities();
            
            for (Yate yate : yates) {
                Acuatico acuatico = yate.getAcuatico();
                Vehiculo vehiculo = acuatico != null ? acuatico.getVehiculo() : null;
                
                Object[] fila = {
                    yate.getIdVehiculo(),
                    vehiculo != null ? vehiculo.getMarca() : "",
                    vehiculo != null ? vehiculo.getModelo() : "",
                    vehiculo != null ? vehiculo.getPrecio() : "",
                    vehiculo != null ? vehiculo.getAño() : "",
                    acuatico != null ? acuatico.getEslora() : "",
                    acuatico != null ? acuatico.getCalado() : "",
                    yate.getMotorElectrico() != null ? (yate.getMotorElectrico() ? "Sí" : "No") : "No"
                };
                modelo.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar yates: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buscarYates() {
        try {
            String busqueda = txtBusqueda.getText().trim().toLowerCase();
            modelo.setRowCount(0);
            List<Yate> yates = yateController.findYateEntities();
            
            for (Yate yate : yates) {
                Acuatico acuatico = yate.getAcuatico();
                Vehiculo vehiculo = acuatico != null ? acuatico.getVehiculo() : null;
                
                if (vehiculo != null && 
                    (vehiculo.getMarca().toLowerCase().contains(busqueda) ||
                     vehiculo.getModelo().toLowerCase().contains(busqueda) ||
                     String.valueOf(vehiculo.getAño()).contains(busqueda))) {
                    
                    Object[] fila = {
                        yate.getIdVehiculo(),
                        vehiculo.getMarca(),
                        vehiculo.getModelo(),
                        vehiculo.getPrecio(),
                        vehiculo.getAño(),
                        acuatico.getEslora(),
                        acuatico.getCalado(),
                        yate.getMotorElectrico() != null ? (yate.getMotorElectrico() ? "Sí" : "No") : "No"
                    };
                    modelo.addRow(fila);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar yates: " + ex.getMessage());
        }
    }

    private void registrarYate() {
        try {
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String añoStr = txtAño.getText().trim();
            String esloraStr = txtEslora.getText().trim();
            String caladoStr = txtCalado.getText().trim();
            
            if (marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los son campos obligatorios");
                return;
            }
            
            BigDecimal precio = new BigDecimal(precioStr);
            Integer año = añoStr.isEmpty() ? null : Integer.parseInt(añoStr);
            BigDecimal eslora = esloraStr.isEmpty() ? null : new BigDecimal(esloraStr);
            BigDecimal calado = caladoStr.isEmpty() ? null : new BigDecimal(caladoStr);
            Boolean motorElectrico = CheckBoxMotorElectrico.isSelected();
            
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setPrecio(precio);
            vehiculo.setAño(año);
            vehiculo.setReservado(false);
            vehiculoController.create(vehiculo);
            Acuatico acuatico = new Acuatico();
            acuatico.setEslora(eslora);
            acuatico.setCalado(calado);
            acuatico.setVehiculo(vehiculo);
            acuatico.setIdVehiculo(vehiculo.getIdVehiculo());
            acuaticoController.create(acuatico);
            Yate yate = new Yate();
            yate.setMotorElectrico(motorElectrico);
            //yate.setAcuatico(acuatico);
            yate.setIdVehiculo(acuatico.getIdVehiculo());
            yateController.create(yate);
            
            
            
            
            JOptionPane.showMessageDialog(this, "Yate registrado exitosamente");
            limpiarCampos();
            listarYates();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en formato numérico: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar yate: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void seleccionarYate() {
        int filaSeleccionada = jtDatos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object valorId = modelo.getValueAt(filaSeleccionada, 0);
                int idYate;
                
                if (valorId instanceof Integer) {
                    idYate = (Integer) valorId;
                } else {
                    idYate = Integer.parseInt(valorId.toString());
                }
                
                yateSeleccionado = yateController.findYate(idYate);
                
                if (yateSeleccionado != null && yateSeleccionado.getAcuatico() != null && 
                    yateSeleccionado.getAcuatico().getVehiculo() != null) {
                    
                    Acuatico acuatico = yateSeleccionado.getAcuatico();
                    Vehiculo vehiculo = acuatico.getVehiculo();
                    
                    txtMarca.setText(vehiculo.getMarca());
                    txtModelo.setText(vehiculo.getModelo());
                    txtPrecio.setText(vehiculo.getPrecio().toString());
                    txtAño.setText(vehiculo.getAño() != null ? vehiculo.getAño().toString() : "");
                    txtEslora.setText(acuatico.getEslora() != null ? acuatico.getEslora().toString() : "");
                    txtCalado.setText(acuatico.getCalado() != null ? acuatico.getCalado().toString() : "");
                    CheckBoxMotorElectrico.setSelected(Boolean.TRUE.equals(yateSeleccionado.getMotorElectrico()));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar yate: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void editarYate() {
        if (yateSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un yate primero");
            return;
        }

        try {
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            
            if (marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "todos los campos son obligatorios");
                return;
            }
            
            Acuatico acuatico = yateSeleccionado.getAcuatico();
            Vehiculo vehiculo = acuatico != null ? acuatico.getVehiculo() : null;
            
            if (vehiculo == null) {
                JOptionPane.showMessageDialog(this, "Error: Vehículo no encontrado");
                return;
            }
            
            vehiculo.setMarca(marca);
            vehiculo.setModelo(txtModelo.getText().trim());
            vehiculo.setPrecio(new BigDecimal(precioStr));
            vehiculo.setAño(txtAño.getText().isEmpty() ? null : Integer.parseInt(txtAño.getText().trim()));
            
            acuatico.setEslora(txtEslora.getText().isEmpty() ? null : new BigDecimal(txtEslora.getText().trim()));
            acuatico.setCalado(txtCalado.getText().isEmpty() ? null : new BigDecimal(txtCalado.getText().trim()));
            
            yateSeleccionado.setMotorElectrico(CheckBoxMotorElectrico.isSelected());
            
            vehiculoController.edit(vehiculo);
            acuaticoController.edit(acuatico);
            yateController.edit(yateSeleccionado);
            
            JOptionPane.showMessageDialog(this, "Yate actualizado exitosamente");
            limpiarCampos();
            yateSeleccionado = null;
            listarYates();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar yate: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarYate() {
        if (yateSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un yate primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar este yate?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int idYate = yateSeleccionado.getIdVehiculo();
                yateController.destroy(idYate);
                acuaticoController.destroy(idYate);
                vehiculoController.destroy(idYate);
                
                JOptionPane.showMessageDialog(this, "Yate eliminado exitosamente");
                limpiarCampos();
                yateSeleccionado = null;
                listarYates();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar yate: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        txtMarca.setText("");
        txtModelo.setText("");
        txtPrecio.setText("");
        txtAño.setText("");
        txtEslora.setText("");
        txtCalado.setText("");
        CheckBoxMotorElectrico.setSelected(false);
        txtBusqueda.setText("");
        yateSeleccionado = null;
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
        txtModelo = new javax.swing.JTextField();
        txtAño = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtDatos = new javax.swing.JTable();
        txtBusqueda = new javax.swing.JTextField();
        btnGEdit = new javax.swing.JButton();
        txtMarca = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtEslora = new javax.swing.JTextField();
        txtCalado = new javax.swing.JTextField();
        CheckBoxMotorElectrico = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtModelo.setBorder(javax.swing.BorderFactory.createTitledBorder("Modelo:"));
        txtModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeloActionPerformed(evt);
            }
        });
        jPanel1.add(txtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 290, 60));

        txtAño.setBorder(javax.swing.BorderFactory.createTitledBorder("Año:"));
        jPanel1.add(txtAño, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 290, 60));

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

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 580, 147));

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

        txtMarca.setBorder(javax.swing.BorderFactory.createTitledBorder("Marca:"));
        jPanel1.add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 290, 60));

        txtPrecio.setBorder(javax.swing.BorderFactory.createTitledBorder("Precio:"));
        jPanel1.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 290, 60));

        txtEslora.setBorder(javax.swing.BorderFactory.createTitledBorder("Eslora:"));
        jPanel1.add(txtEslora, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 290, 60));

        txtCalado.setBorder(javax.swing.BorderFactory.createTitledBorder("Calado:"));
        jPanel1.add(txtCalado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 290, 60));

        CheckBoxMotorElectrico.setForeground(new java.awt.Color(0, 0, 0));
        CheckBoxMotorElectrico.setText("Si ");
        jPanel1.add(CheckBoxMotorElectrico, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 340, 100, 30));

        jLabel1.setBackground(new java.awt.Color(102, 102, 102));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Motor electrico:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, -1, -1));

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
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModeloActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        registrarYate();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        editarYate();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarYate();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
        // TODO add your handling code here:
        listarYates();
    }//GEN-LAST:event_btnListarActionPerformed

    private void jtDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtDatosMouseClicked
        // TODO add your handling code here:
        seleccionarYate();
    }//GEN-LAST:event_jtDatosMouseClicked

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
        buscarYates();
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void btnGEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGEditActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
    }//GEN-LAST:event_btnGEditActionPerformed

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

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new crudYate().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckBoxMotorElectrico;
    public javax.swing.JButton btnEditar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGEdit;
    public javax.swing.JButton btnListar;
    public javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTable jtDatos;
    public javax.swing.JTextField txtAño;
    public javax.swing.JTextField txtBusqueda;
    public javax.swing.JTextField txtCalado;
    public javax.swing.JTextField txtEslora;
    public javax.swing.JTextField txtMarca;
    public javax.swing.JTextField txtModelo;
    public javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
