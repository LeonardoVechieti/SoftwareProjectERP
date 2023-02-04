/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

/**
 *
 * @author Leonardo
 */
import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;


public class TelaDestinos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaDestinos() {
        initComponents();
        conexao = ModuloConexao.conector();
        buscaTabela();
    }
    
    private void consultar(){
        
        String sql = "select * from destinos where id = ?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtCadId.getText());
            rs=pst.executeQuery();
            if (rs.next()) {
                txtCadOrigem.setText(rs.getString(2));
                txtCadCnpj.setText(rs.getString(3));
                txtCadDestino.setText(rs.getString(4));
                txtCadCliente.setText(rs.getString(5));
                String status=rs.getString(6);
                if(status.equals("1")){
                    lbnAtivo.setText("Ativo");
                }else{
                    lbnAtivo.setText("Inativo");
                }
                
                
                
            } else {
                JOptionPane.showMessageDialog(null, "Destino não existente!");
                //as linhas abaixo limpam os campos
                                    
                    txtCadOrigem.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadDestino.setText(null);
                    txtCadCliente.setText(null);
                   
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void adicionar(){
        String sql="insert into destinos ( origem, cnpj, destino, cliente, ativo) values(?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            //pst.setString(1, txtCadId.getText());
            pst.setString(1, txtCadOrigem.getText());
            pst.setString(2, txtCadCnpj.getText());
            pst.setString(3, txtCadDestino.getText());
            pst.setString(4, txtCadCliente.getText());
            pst.setString(5, "1");
            
            //validacão do campos
            if ((txtCadCliente.getText().isEmpty()) || (txtCadOrigem.getText().isEmpty()) || (txtCadDestino.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Destino adicionado com sucesso!");
                    //Limpa as Caixas
                    txtCadId.setText(null);
                    txtCadOrigem.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadDestino.setText(null);
                    txtCadCliente.setText(null);
                    
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    //metodo de alterar dados do usuário
    
    private void alterar(){
        
        String sql="update destinos set origem=?, cnpj=?, destino=?, cliente=? where id=?";
            
            
        
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtCadOrigem.getText());
            pst.setString(2, txtCadCnpj.getText());
            pst.setString(3, txtCadDestino.getText());
            pst.setString(4, txtCadCliente.getText());
            pst.setString(5, txtCadId.getText());
            
            //validação dos campos
            
            if ((txtCadId.getText().isEmpty()) || (txtCadOrigem.getText().isEmpty()) || (txtCadDestino.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
                    //Limpa as Caixas
                    txtCadId.setText(null);
                    txtCadOrigem.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadDestino.setText(null);
                    txtCadCliente.setText(null);
                    
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    //metodo remover usuário da tabela
    
    private void inativar(){
        String id=txtCadId.getText();
        String status= null;
        if(lbnAtivo.getText()=="Ativo"){
            status="0";
            
        }else{
            status="1";
        }
        
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja ativar ou inativar?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma==JOptionPane.YES_OPTION){
            String sql="update destinos set ativo=? where id= ?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, id);  
                pst.executeUpdate();  //executa a sql
                int executado = pst.executeUpdate();
                if (executado>0){
                    JOptionPane.showMessageDialog(null, "Alterado com Sucesso");
                   
                    
                }
                
                
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
        
        
    }
    
    private void buscaTabela(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select id as ID, origem as Origem, destino as Destino, cliente as Cliente from destinos";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
           
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblCadDestinos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    private void setaEmpresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        int setar = tblCadDestinos.getSelectedRow();
        txtCadId.setText(tblCadDestinos.getModel().getValueAt(setar,0).toString());
        txtCadOrigem.setText(tblCadDestinos.getModel().getValueAt(setar,1).toString());
        txtCadDestino.setText(tblCadDestinos.getModel().getValueAt(setar,2).toString());
        txtCadCliente.setText(tblCadDestinos.getModel().getValueAt(setar,3).toString());
        txtCadId.setEditable(false);
        consultar();
    }
    
    
    
            

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadId = new javax.swing.JTextField();
        txtCadOrigem = new javax.swing.JTextField();
        txtCadDestino = new javax.swing.JTextField();
        btnCadAdicionar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnCadAlterar = new javax.swing.JButton();
        btnCadInativar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadDestinos = new javax.swing.JTable();
        txtCadCliente = new javax.swing.JTextField();
        txtCadCnpj = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbnAtivo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Destinos");
        setPreferredSize(new java.awt.Dimension(700, 440));
        setRequestFocusEnabled(false);

        jLabel1.setText("ID");

        jLabel3.setText("Destino");

        jLabel4.setText("Cliente");

        txtCadId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadIdActionPerformed(evt);
            }
        });

        txtCadDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadDestinoActionPerformed(evt);
            }
        });

        btnCadAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adicionar2.png"))); // NOI18N
        btnCadAdicionar.setToolTipText("Adicionar");
        btnCadAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadAdicionarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnCadAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/alterar.png"))); // NOI18N
        btnCadAlterar.setToolTipText("Alterar");
        btnCadAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadAlterarActionPerformed(evt);
            }
        });

        btnCadInativar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/inativar.png"))); // NOI18N
        btnCadInativar.setToolTipText("Inativar");
        btnCadInativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadInativarActionPerformed(evt);
            }
        });

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/limpar32.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        tblCadDestinos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Origem", "Destino", "Cliente"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCadDestinos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadDestinosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadDestinos);

        txtCadCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadClienteActionPerformed(evt);
            }
        });

        try {
            txtCadCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel20.setText("CNP-J");

        jLabel5.setText("Origem");

        lbnAtivo.setText("Ativo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addComponent(jLabel1)
                            .addGap(34, 34, 34))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGap(18, 18, 18)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(4, 4, 4)))
                        .addGap(22, 22, 22)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCadCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(lbnAtivo))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(txtCadId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtCadOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCadDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCadCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCadAdicionar)
                .addGap(18, 18, 18)
                .addComponent(btnCadAlterar)
                .addGap(18, 18, 18)
                .addComponent(btnCadInativar)
                .addGap(18, 18, 18)
                .addComponent(btnLimpar)
                .addGap(52, 52, 52))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCadAdicionar, btnCadAlterar, btnCadInativar});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCadId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsultar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCadOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCadDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCadCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCadCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbnAtivo)))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnCadInativar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCadAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpar))
                    .addComponent(btnCadAdicionar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Dados Cadastrais", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Outros", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        getAccessibleContext().setAccessibleName("Parâmetros do Sistema");

        setBounds(0, 0, 700, 440);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCadIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadIdActionPerformed

    private void txtCadDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadDestinoActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // chamando o metodo consultar
        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnCadAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadAdicionarActionPerformed
        // chamando o metodo adicionar
        adicionar();
        buscaTabela();
    }//GEN-LAST:event_btnCadAdicionarActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        //a linha abaixo limpa os dados da caixa
        txtCadId.setText(null);
        txtCadOrigem.setText(null);
        txtCadCnpj.setText(null);
        txtCadDestino.setText(null);
        txtCadCliente.setText(null);
        
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnCadAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadAlterarActionPerformed
        // chamado o metodo alterar 
        alterar();
        buscaTabela();
    }//GEN-LAST:event_btnCadAlterarActionPerformed

    private void btnCadInativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadInativarActionPerformed
        // chama a função deletar
        inativar();
        buscaTabela();
    }//GEN-LAST:event_btnCadInativarActionPerformed

    private void tblCadDestinosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadDestinosMouseClicked
        setaEmpresa();
        
    }//GEN-LAST:event_tblCadDestinosMouseClicked

    private void txtCadClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadClienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadAdicionar;
    private javax.swing.JButton btnCadAlterar;
    private javax.swing.JButton btnCadInativar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbnAtivo;
    private javax.swing.JTable tblCadDestinos;
    private javax.swing.JTextField txtCadCliente;
    private javax.swing.JFormattedTextField txtCadCnpj;
    private javax.swing.JTextField txtCadDestino;
    private javax.swing.JTextField txtCadId;
    private javax.swing.JTextField txtCadOrigem;
    // End of variables declaration//GEN-END:variables
}
