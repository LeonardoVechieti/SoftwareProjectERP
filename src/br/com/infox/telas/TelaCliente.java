/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import br.com.infox.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Leonardo
 */
public class TelaCliente extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private String contrato;
    private String integracao;
    private String validade;
    
    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
           
                
        
    }
    
    private void adicionar(){
    
        String sql="insert into empresas (nome, cnpj, fone, endereco, cidade, cep, uf, tipo, contrato, ncontrato, statusInteg, validade) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtEmpNome.getText());
            pst.setString(2, txtEmpCnpj.getText());
                if(rbtEmpContrato.isSelected()){
                    contrato="1";      
                 } else{
                    contrato="0";
                 }
            pst.setString(3, txtEmpFone.getText());
            pst.setString(4, txtEmpEndereco.getText());
            pst.setString(5, txtEmpCidade.getText());
            pst.setString(6, txtEmpCep.getText());
            pst.setString(7, txtEmpUf.getText());
            pst.setString(8, cboEmpTipo.getSelectedItem().toString());
            pst.setString(9, contrato);  
            pst.setString(10, txtEmpNcontrato.getText());
                if(rbtEmpIntegracao.isSelected()){
                    integracao="1";      
                 } else{
                    integracao="0";
                 }
            pst.setString(11, integracao); 
            
            if(txtEmpValidade.getText().equals("  /  /    ")){
                txtEmpValidade.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtEmpValidade.getText(); 
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd");
            Date dataFormatada =  formato.parse(dataRecebida);
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato.format(dataFormatada));
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato2.format(dataFormatada)); 
                    
            pst.setString(12, formato2.format(dataFormatada));
             
            
            
            //validacão do campos
            if ((txtEmpNome.getText().isEmpty()) || (txtEmpCnpj.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    
                    JOptionPane.showMessageDialog(null, "Cadastro e integração realizado com sucesso!");

                   
                    //Limpa as Caixas
                    txtEmpNome.setText(null);
                    txtEmpCnpj.setText(null);
                    txtEmpNcontrato.setText(null); 
                    txtEmpValidade.setText(null);
                    rbtEmpContrato.setSelected(false); 
                    rbtEmpIntegracao.setSelected(false); 
                    limpar();
                    
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    
    private void pesquisar_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ, tipo as Tipo from empresas where nome like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtEmpPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblEmpresas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //mtodo para setar os campos do formulario com o conteudo da tabela
    
    public void setar_campos(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
        txtEmpCnpj.setText(tblEmpresas.getModel().getValueAt(setar,2).toString());
        btnAdicionar.setEnabled(false); 
        
    }
    
     private void setar_campos_restantes(){
        //Codigo abaixo seta o restante dos dados
        
        String idEmp=txtEmpId.getText();
        String sql="select fone, endereco, cidade, cep, uf, tipo, contrato as Contrato, ncontrato as Número, statusInteg as Integração, DATE_FORMAT (`validade`,'%d/%m/%Y') AS `Válidade` from empresas where idemp="+idEmp;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                
                txtEmpFone.setText(rs.getString(1));
                txtEmpEndereco.setText(rs.getString(2));
                txtEmpCidade.setText(rs.getString(3));
                txtEmpCep.setText(rs.getString(4));
                txtEmpUf.setText(rs.getString(5));
                cboEmpTipo.setSelectedItem((rs.getString(6)));
                String contrato=rs.getString(7);
                if (contrato.equals("1")) {
                    rbtEmpContrato.setSelected(true);
                    
                } else {
                    rbtEmpContrato.setSelected(false);
                }
                txtEmpNcontrato.setText(rs.getString(8));
                
                String integracao=rs.getString(9);
                if (integracao.equals("1")) {
                    rbtEmpIntegracao.setSelected(true);
                    
                } else {
                    rbtEmpIntegracao.setSelected(false);
                }
                txtEmpValidade.setText(rs.getString(10));
                if(txtEmpValidade.getText().equals("10/10/1000")){
                    txtEmpValidade.setText(null);
                }
                
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void alterar(){
        
        String sql="update empresas set nome=?, cnpj=?, fone=?, endereco=?, cidade=?, cep=?, uf=?, tipo=?, contrato=?, ncontrato=?, statusInteg=?, validade=? where idemp=?";       
        
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtEmpNome.getText());
            pst.setString(2, txtEmpCnpj.getText());
            pst.setString(3, txtEmpFone.getText());
            pst.setString(4, txtEmpEndereco.getText());
            pst.setString(5, txtEmpCidade.getText());
            pst.setString(6, txtEmpCep.getText());
            pst.setString(7, txtEmpUf.getText());
            pst.setString(8, cboEmpTipo.getSelectedItem().toString());
                if(rbtEmpContrato.isSelected()){
                    contrato="1";      
                 } else{
                    contrato="0";
                 }
            pst.setString(9, contrato);  
            pst.setString(10, txtEmpNcontrato.getText());
                if(rbtEmpIntegracao.isSelected()){
                    integracao="1";      
                 } else{
                    integracao="0";
                 }
            pst.setString(11, integracao); 
            
            if(txtEmpValidade.getText().equals("  /  /    ")){
                txtEmpValidade.setText("10/10/1000");
            }
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtEmpValidade.getText(); 
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd");
            Date dataFormatada =  formato.parse(dataRecebida);
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato.format(dataFormatada));
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato2.format(dataFormatada)); 
                    
            pst.setString(12, formato2.format(dataFormatada));
            pst.setString(13, txtEmpId.getText());
            
            //validação dos campos
            
            if ((txtEmpNome.getText().isEmpty()) || (txtEmpCnpj.getText().isEmpty()) ) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Dados da empresa alterados com sucesso!");
                limpar();           
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void limpar(){
        //Limpa as Caixas
        txtEmpId.setText(null);
        txtEmpNome.setText(null);
        txtEmpCnpj.setText(null);
        txtEmpFone.setText(null);
        txtEmpEndereco.setText(null);
        txtEmpCidade.setText(null);
        txtEmpCep.setText(null);
        txtEmpUf.setText(null);
        //txtEmpNome.setText(null);       
        txtEmpNcontrato.setText(null); 
        txtEmpValidade.setText(null);
        rbtEmpContrato.setSelected(false); 
        rbtEmpIntegracao.setSelected(false); 
        btnAdicionar.setEnabled(true);
        ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
    }
    
         private void excluir(){
         
         int confirma=JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir está empresa do sistema, não é possível após realizar movimentação indexada na mesma", "Atenção", JOptionPane.YES_NO_OPTION);
         if (confirma == JOptionPane.YES_OPTION) {
             String sql="delete from empresas where idemp=?";
             try {
                 pst=conexao.prepareStatement(sql);
                // passando o conteudo da caixa de pesquisa para o ?
                pst.setString(1, txtEmpId.getText());
                 int apagado = pst.executeUpdate();
                if (apagado>0){
                    JOptionPane.showMessageDialog(null, "Empresa removida com sucesso");
                    
                    btnAdicionar.setEnabled(true);
                   
                    
                }
             } catch (Exception e) {
                 JOptionPane.showMessageDialog(null, e);
             }
             
         } else {
             
         }
     }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSpinner1 = new javax.swing.JSpinner();
        btnAdicionar = new javax.swing.JButton();
        btnEmpAlterar = new javax.swing.JButton();
        btnEmpInativar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        txtEmpPesquisar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmpNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmpId = new javax.swing.JTextField();
        txtEmpCnpj = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtEmpCidade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtEmpEndereco = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtEmpCep = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtEmpUf = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboEmpTipo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtEmpFone = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        rbtEmpContrato = new javax.swing.JRadioButton();
        rbtEmpIntegracao = new javax.swing.JRadioButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtEmpNcontrato = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEmpValidade = new javax.swing.JFormattedTextField();
        btnLimpar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro de Empresas e Integração");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnEmpAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnEmpAlterar.setToolTipText("Alterar");
        btnEmpAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpAlterarActionPerformed(evt);
            }
        });

        btnEmpInativar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnEmpInativar.setToolTipText("Excluir");
        btnEmpInativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpInativarActionPerformed(evt);
            }
        });

        txtEmpPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyReleased(evt);
            }
        });

        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Razão", "CNPJ", "Tipo"
            }
        ));
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmpresas);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("CNP-J");

        jLabel2.setText("Razão Social");

        txtEmpNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNomeKeyPressed(evt);
            }
        });

        jLabel3.setText("ID");

        txtEmpId.setEditable(false);

        try {
            txtEmpCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtEmpCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpCnpjKeyPressed(evt);
            }
        });

        jLabel7.setText("Cidade");

        txtEmpCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpCidadeKeyPressed(evt);
            }
        });

        jLabel8.setText("Endereço");

        txtEmpEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpEnderecoKeyPressed(evt);
            }
        });

        jLabel9.setText("CEP");

        txtEmpCep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpCepKeyPressed(evt);
            }
        });

        jLabel10.setText("UF");

        txtEmpUf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpUfKeyPressed(evt);
            }
        });

        jLabel11.setText("Tipo");

        cboEmpTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente", "Cliente/Fornecedor", "Tercerizada", "Transportadora" }));

        jLabel12.setText("Fone");

        txtEmpFone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpFoneKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpFone, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpCep, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpUf, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboEmpTipo, 0, 135, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtEmpFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtEmpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(txtEmpCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtEmpUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cboEmpTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel6.setText("Pesquisar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados da Empresa", jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        rbtEmpContrato.setText("Possui contrato de prestação de servico");
        rbtEmpContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtEmpContratoActionPerformed(evt);
            }
        });

        rbtEmpIntegracao.setText("Integração Ativa");
        rbtEmpIntegracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtEmpIntegracaoActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        jButton4.setText("Imprimir Integração");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Contrato:");

        txtEmpNcontrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNcontratoKeyPressed(evt);
            }
        });

        jLabel5.setText("Válidade:");

        try {
            txtEmpValidade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtEmpValidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpValidadeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtEmpContrato)
                    .addComponent(rbtEmpIntegracao))
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmpNcontrato, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbtEmpContrato)
                            .addComponent(jLabel4)
                            .addComponent(txtEmpNcontrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbtEmpIntegracao)
                            .addComponent(jLabel5)
                            .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Integração", jPanel3);

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/limpar.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnEmpAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnEmpInativar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEmpAlterar)
                    .addComponent(btnEmpInativar)
                    .addComponent(btnAdicionar)
                    .addComponent(btnLimpar))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setBounds(0, 0, 832, 490);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void rbtEmpContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtEmpContratoActionPerformed
        
    }//GEN-LAST:event_rbtEmpContratoActionPerformed

    private void rbtEmpIntegracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtEmpIntegracaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtEmpIntegracaoActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void txtEmpPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyReleased
        pesquisar_empresa();
    }//GEN-LAST:event_txtEmpPesquisarKeyReleased

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
        setar_campos();
        setar_campos_restantes();
             
    }//GEN-LAST:event_tblEmpresasMouseClicked

    private void btnEmpAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpAlterarActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_btnEmpAlterarActionPerformed

    private void btnEmpInativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpInativarActionPerformed
        // TODO add your handling code here:
        excluir();
    }//GEN-LAST:event_btnEmpInativarActionPerformed

    private void txtEmpValidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpValidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpValidadeActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtEmpPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpNome.requestFocus();
       }
    }//GEN-LAST:event_txtEmpPesquisarKeyPressed

    private void txtEmpNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNomeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpFone.requestFocus();
       }
    }//GEN-LAST:event_txtEmpNomeKeyPressed

    private void txtEmpFoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpFoneKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpCnpj.requestFocus();
       }
    }//GEN-LAST:event_txtEmpFoneKeyPressed

    private void txtEmpCnpjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpCnpjKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpEndereco.requestFocus();
       }
    }//GEN-LAST:event_txtEmpCnpjKeyPressed

    private void txtEmpEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpEnderecoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpCidade.requestFocus();
       }
    }//GEN-LAST:event_txtEmpEnderecoKeyPressed

    private void txtEmpCidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpCidadeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpCep.requestFocus();
       }
    }//GEN-LAST:event_txtEmpCidadeKeyPressed

    private void txtEmpCepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpCepKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpUf.requestFocus();
       }
    }//GEN-LAST:event_txtEmpCepKeyPressed

    private void txtEmpUfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpUfKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             cboEmpTipo.requestFocus();
       }
    }//GEN-LAST:event_txtEmpUfKeyPressed

    private void txtEmpNcontratoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNcontratoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpValidade.requestFocus();
       }
    }//GEN-LAST:event_txtEmpNcontratoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEmpAlterar;
    private javax.swing.JButton btnEmpInativar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JComboBox<String> cboEmpTipo;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JRadioButton rbtEmpContrato;
    private javax.swing.JRadioButton rbtEmpIntegracao;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTextField txtEmpCep;
    private javax.swing.JTextField txtEmpCidade;
    private javax.swing.JFormattedTextField txtEmpCnpj;
    private javax.swing.JTextField txtEmpEndereco;
    private javax.swing.JTextField txtEmpFone;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpNcontrato;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpPesquisar;
    private javax.swing.JTextField txtEmpUf;
    private javax.swing.JFormattedTextField txtEmpValidade;
    // End of variables declaration//GEN-END:variables
}
