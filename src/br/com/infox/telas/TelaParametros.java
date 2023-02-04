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


public class TelaParametros extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaParametros() {
        initComponents();
        conexao = ModuloConexao.conector();
        buscaTabela();
    }
    
    private void consultar(){
        
        String sql = "select * from parametrosEmpresa where id = ?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtCadId.getText());
            rs=pst.executeQuery();
            if (rs.next()) {
                txtCadRazao.setText(rs.getString(2));
                txtCadCnpj.setText(rs.getString(3));
                txtCadRef.setText(rs.getString(4));
                txtCadCidade.setText(rs.getString(5));
                txtCadCep.setText(rs.getString(6));
                txtCadEndereco.setText(rs.getString(7));
                txtCadFantasia.setText(rs.getString(8));
                String status=rs.getString(9);
                if(status.equals("1")){
                    lbnAtivo.setText("Ativo");
                }else{
                    lbnAtivo.setText("Inativo");
                }
                
                
                
            } else {
                JOptionPane.showMessageDialog(null, "Empresa não existente!");
                //as linhas abaixo limpam os campos
                                    
                    txtCadRazao.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadRef.setText(null);
                    txtCadCidade.setText(null);
                    txtCadCep.setText(null);
                    txtCadEndereco.setText(null);
                    txtCadFantasia.setText(null);
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void adicionar(){
        String sql="insert into parametrosEmpresa (id, empresa, cnpj, ref, cidade, cep, endereco, fantasia, ativo) values(?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtCadId.getText());
            pst.setString(2, txtCadRazao.getText());
            pst.setString(3, txtCadCnpj.getText());
            pst.setString(4, txtCadRef.getText());
            pst.setString(5, txtCadCidade.getText());
            pst.setString(6, txtCadCep.getText());
            pst.setString(7, txtCadEndereco.getText());
            pst.setString(8, txtCadFantasia.getText());
            pst.setString(9, "true");
            
            //validacão do campos
            if ((txtCadId.getText().isEmpty()) || (txtCadRazao.getText().isEmpty()) || (txtCadCnpj.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                    //Limpa as Caixas
                    txtCadId.setText(null);
                    txtCadRazao.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadRef.setText(null);
                    txtCadCidade.setText(null);
                    txtCadCep.setText(null);
                    txtCadEndereco.setText(null);
                    txtCadFantasia.setText(null);
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    //metodo de alterar dados do usuário
    
    private void alterar(){
        
        String sql="update parametrosEmpresa set empresa=?, cnpj=?, ref=?, cidade=?, cep=?, endereco=?, fantasia=? where id=?";
            
            
        
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtCadRazao.getText());
            pst.setString(2, txtCadCnpj.getText());
            pst.setString(3, txtCadRef.getText());
            pst.setString(4, txtCadCidade.getText());
            pst.setString(5, txtCadCep.getText());
            pst.setString(6, txtCadEndereco.getText());
            pst.setString(7, txtCadFantasia.getText());
            pst.setString(8, txtCadId.getText());
            
            //validação dos campos
            
            if ((txtCadId.getText().isEmpty()) || (txtCadRazao.getText().isEmpty()) || (txtCadRef.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
                    //Limpa as Caixas
                    txtCadId.setText(null);
                    txtCadRazao.setText(null);
                    txtCadCnpj.setText(null);
                    txtCadRef.setText(null);
                    txtCadCidade.setText(null);
                    txtCadCep.setText(null);
                    txtCadEndereco.setText(null);
                    txtCadFantasia.setText(null);
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
            String sql="update parametrosempresa set ativo=? where id= ?";
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
        String sql="select id as ID, empresa as Empresa, cnpj as CNPJ, cidade as Cidade from parametrosEmpresa";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
           
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblCadEmpresas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    private void setaEmpresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        int setar = tblCadEmpresas.getSelectedRow();
        txtCadId.setText(tblCadEmpresas.getModel().getValueAt(setar,0).toString());
        txtCadRazao.setText(tblCadEmpresas.getModel().getValueAt(setar,1).toString());
        txtCadCnpj.setText(tblCadEmpresas.getModel().getValueAt(setar,2).toString());
        txtCadCidade.setText(tblCadEmpresas.getModel().getValueAt(setar,3).toString());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadId = new javax.swing.JTextField();
        txtCadRazao = new javax.swing.JTextField();
        txtCadRef = new javax.swing.JTextField();
        btnCadAdicionar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnCadAlterar = new javax.swing.JButton();
        btnCadInativar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCadEmpresas = new javax.swing.JTable();
        txtCadCidade = new javax.swing.JTextField();
        txtCadCnpj = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCadFantasia = new javax.swing.JTextField();
        txtCadEndereco = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCadCep = new javax.swing.JTextField();
        lbnAtivo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Gerenciador de Empresas");
        setPreferredSize(new java.awt.Dimension(700, 440));
        setRequestFocusEnabled(false);

        jLabel1.setText("ID");

        jLabel2.setText("Fantasia");

        jLabel3.setText("Referência");

        jLabel4.setText("Cidade");

        txtCadId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadIdActionPerformed(evt);
            }
        });

        txtCadRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadRefActionPerformed(evt);
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

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        tblCadEmpresas.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID", "Empresa", "CNPJ", "Cidade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCadEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadEmpresasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCadEmpresas);

        txtCadCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadCidadeActionPerformed(evt);
            }
        });

        try {
            txtCadCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel20.setText("CNP-J");

        jLabel5.setText("Razão");

        jLabel6.setText("Endereço");

        jLabel7.setText("CEP");

        txtCadCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadCepActionPerformed(evt);
            }
        });

        lbnAtivo.setText("Ativo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel20)
                                            .addComponent(jLabel5))
                                        .addGap(11, 11, 11)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(txtCadId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCadRazao, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCadRef, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCadCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(txtCadCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lbnAtivo))))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtCadFantasia))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(btnCadAdicionar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCadAlterar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCadInativar)
                                .addGap(0, 28, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCadCep, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCadEndereco))))
                        .addGap(35, 35, 35))))
            .addComponent(jScrollPane1)
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCadAdicionar, btnCadAlterar, btnCadInativar});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCadId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConsultar)
                            .addComponent(btnLimpar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCadRazao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtCadRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCadFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCadEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCadCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCadCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(txtCadCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbnAtivo))
                                .addGap(23, 23, 23))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCadAlterar)
                                    .addComponent(btnCadInativar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCadAdicionar)
                        .addGap(0, 0, Short.MAX_VALUE))))
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

    private void txtCadRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadRefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadRefActionPerformed

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
        txtCadRazao.setText(null);
        txtCadCnpj.setText(null);
        txtCadCidade.setText(null);
        
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

    private void tblCadEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadEmpresasMouseClicked
        setaEmpresa();
    }//GEN-LAST:event_tblCadEmpresasMouseClicked

    private void txtCadCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadCidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadCidadeActionPerformed

    private void txtCadCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadCepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadCepActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadAdicionar;
    private javax.swing.JButton btnCadAlterar;
    private javax.swing.JButton btnCadInativar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbnAtivo;
    private javax.swing.JTable tblCadEmpresas;
    private javax.swing.JTextField txtCadCep;
    private javax.swing.JTextField txtCadCidade;
    private javax.swing.JFormattedTextField txtCadCnpj;
    private javax.swing.JTextField txtCadEndereco;
    private javax.swing.JTextField txtCadFantasia;
    private javax.swing.JTextField txtCadId;
    private javax.swing.JTextField txtCadRazao;
    private javax.swing.JTextField txtCadRef;
    // End of variables declaration//GEN-END:variables
}
