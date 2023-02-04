/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import br.com.infox.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Leonardo
 */
public class TelaControleAcesso extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private String idpes=null;
    private String idemp=null;
    
    private String dataInsert="1000/10/10"; 
    /**
     * Creates new form TelaControleAcesso
     */
    public TelaControleAcesso() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
  private void adicionar_pessoa(){
         String sql="insert into pessoas (nome, cpf, cnh, cat, tipo, cidade, datacontratacao, listaequipamento, listaepi, nr35, nr10, nr33, nr12, outroscertificados, certificados, idemp, razao, iud) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtPesNome.getText());
            pst.setString(2, txtPesCpf.getText());
            pst.setString(3, txtPesCnh.getText());
            pst.setString(4, cboPesCat.getSelectedItem().toString());
            pst.setString(5, cboPesTipo.getSelectedItem().toString());
            pst.setString(6, "");     
            pst.setString(7, dataInsert); //Passa a data do formato no formato2 americano
            pst.setString(8, "");
            pst.setString(9, "");
            pst.setString(10, "0");     
            pst.setString(11, "0");            
            pst.setString(12, "0");            
            pst.setString(13, "0");
            pst.setString(14, "0");              
            pst.setString(15, "");
            pst.setString(16, "1");
            pst.setString(17, "");  
            pst.setString(18, "");
            
            //validacão do campos
            if ((txtPesNome.getText().isEmpty()) || (txtPesCpf.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Cadastro adicionado com sucesso!");
                    txtPesId.setText(recuperaIdCadastro());
                    btnAdicionarPessoa.setEnabled(false);
                    //Limpa as Caixas
                    //limpar();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
  private String recuperaIdCadastro(){
        String sql ="select max(idpes) from pessoas"; //busca no banco o ultimo id criado
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            if (rs.next()){
                return rs.getString(1); //retorna a funcao o id solicitado em String
        }
        } catch (Exception e) {
        }
        return "0"; //caso erro retorna nulo
    }
  
  
    private void adicionarVeiculo(){
        
        if(txtPesId.getText().isEmpty()){
            adicionar_pessoa();
        }
        if(txtEmpId.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Empresa e/ou pessoa não selecionada, favor selecione!");
        }
        
        String sql="insert into veiculos (placa, placaCarreta, placaCarreta2, placaCarreta3, tipoVei, descricao, idpes, idemp) values(?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtVeiPlaca.getText());
            pst.setString(2, txtVeiPlacaCarreta.getText());
            pst.setString(3, txtVeiPlacaCarreta1.getText());
            pst.setString(4, txtVeiPlacaCarreta2.getText());
            pst.setString(5, cboVeiTipo.getSelectedItem().toString());
            pst.setString(6, txtVeiDes.getText());
            pst.setString(7, txtPesId.getText()); //referencia de pessoa
            pst.setString(8, txtEmpId.getText()); //referencia de empresa/transportadora
            //validacão do campos
            if ((txtVeiPlaca.getText().isEmpty()) || (txtPesId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Cadastro adicionado com sucesso!");
                    //Limpa as Caixas
                    //txtPesNome.setText(null);
                    //txtPesCpf.setText(null);
                    //txtVeiPlaca.setText(null);
                    //txtVeiDes.setText(null);
                    //txtPesId.setText(null);
                    //a linha abixo habilita o botão adicionar
                    btnAdicionarVeiculo.setEnabled(false);
                    
                    //cboPesTipo.setSelectedItem(null);
                    
                    txtVeiId.setText(recuperaIdCadastroVeiculo());
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    private String recuperaIdCadastroVeiculo(){
        String sql ="select max(idvei) from veiculos"; //busca no banco o ultimo id criado
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            if (rs.next()){
                return rs.getString(1); //retorna a funcao o id solicitado em String
        }
        } catch (Exception e) {
        }
        return "0"; //caso erro retorna nulo
    }
    
        private void pesquisar_pessoa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idpes as ID, nome as Nome, cpf as CPF, tipo as Tipo, idemp as Empresa from pessoas where nome like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPessoas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //mtodo para setar os campos do formulario com o conteudo da tabela
    
    public void setar_campos_pessoa(){
        int setar = tblPessoas.getSelectedRow();
        txtPesId.setText(tblPessoas.getModel().getValueAt(setar,0).toString());
        txtPesNome.setText(tblPessoas.getModel().getValueAt(setar,1).toString());
        txtPesCpf.setText(tblPessoas.getModel().getValueAt(setar,2).toString());
        cboPesTipo.setSelectedItem(tblPessoas.getModel().getValueAt(setar,3).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionarPessoa.setEnabled(false); 
        txtPesNome.setEditable(false);
        txtPesCpf.setEditable(false);
        txtPesCnh.setEditable(false);
        cboPesCat.setEditable(false);
        txtPesCidade.setEditable(false);
        
        //cboPesTipo.setEnabled(false);
        txtPesId.setEditable(false);
        //codigo abaixo seta o valor da variavel idemp, usada para atribuir uma identidade de empresa nos campos empresa.
        idemp= tblPessoas.getModel().getValueAt(setar,4).toString();
        
        
    }
    //select idvei as ID, placa as Placa, placaCavalo as Placa2, tipoVei as Tipo, descricao as Desc from veiculos where placa like ?
    private void pesquisar_veiculos(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idvei as ID, placa as Placa, placaCarreta as Placa, tipoVei as tipo,descricao as Descrição, idpes as Motorista, idemp as Empresa from veiculos where placa like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtVeiPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblVeiculos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //mtodo para setar os campos do formulario com o conteudo da tabela
    
    public void setar_campos_veiculos(){
        int setar = tblVeiculos.getSelectedRow();
        txtVeiId.setText(tblVeiculos.getModel().getValueAt(setar,0).toString());
        txtVeiPlaca.setText(tblVeiculos.getModel().getValueAt(setar,1).toString());
        txtVeiPlacaCarreta.setText(tblVeiculos.getModel().getValueAt(setar,2).toString());
        cboVeiTipo.setSelectedItem(tblVeiculos.getModel().getValueAt(setar,3).toString());
        txtVeiDes.setText(tblVeiculos.getModel().getValueAt(setar,4).toString());
        //setando valor as vairiaveis globais da tela de acesso
        idpes=tblVeiculos.getModel().getValueAt(setar,5).toString();
        idemp=tblVeiculos.getModel().getValueAt(setar,6).toString();
        //txtPesId.setText(tblVeiculos.getModel().getValueAt(setar,5).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionarVeiculo.setEnabled(false); 
        txtVeiId.setEditable(false);
        txtVeiPlaca.setEditable(false);
        txtVeiPlacaCarreta.setEditable(false);
        cboVeiTipo.setEditable(false);
        txtVeiDes.setEditable(false); 
      
        
        
    }
    
    private void criarAcesso(){
        String sql="insert into acesso (nomeAcesso, tipoAcesso, placaAcesso, empresa, acompanhantes, obs, matEntrada, matSaida, idpes, idvei, statusAcesso, idemp) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtPesNome.getText());
            pst.setString(2, cboPesTipo.getSelectedItem().toString());
            pst.setString(3, txtVeiPlaca.getText());
            pst.setString(4, txtEmpNome.getText());
            pst.setString(5, txtConAcompanhante.getText());
            pst.setString(6, txtConObs.getText());
            pst.setString(7, txtConMatE.getText());
            pst.setString(8, txtConMatS.getText());
            pst.setString(9, txtPesId.getText());
            if(txtVeiId.getText().equals("")){
                txtVeiId.setText("1");
            }
            
            pst.setString(10, txtVeiId.getText());
            pst.setString(11, "1"); //seta como aberto apos criação
            pst.setString(12, txtEmpId.getText());
            
            
            
            //validacão do campos
            if ((txtPesId.getText().isEmpty()) || (txtEmpId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Acesso criado com sucesso!");
                    //Limpa as Caixas
                    limpar(); 
                    //cboPesTipo.setSelectedItem(null);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void pesquisar_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ from empresas where nome like ?";
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
    
    public void setar_campos_tblEmpresa(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
        txtEmpCnpj.setText(tblEmpresas.getModel().getValueAt(setar,2).toString());
       
    }
    
    private void limpar(){
        
        txtPesNome.setText(null);
        txtPesCpf.setText(null);
        txtVeiPlaca.setText(null);
        txtVeiPlacaCarreta.setText(null);
        txtVeiDes.setText(null);
        
        btnAdicionarPessoa.setEnabled(true); 
        txtPesNome.setEnabled(true);
        txtPesCpf.setEnabled(true);
        txtPesCnh.setEnabled(true);
        txtPesCidade.setEnabled(true);
        cboPesTipo.setEnabled(true);
        txtPesId.setEnabled(true);
        txtPesNome.setText(null);
        txtPesCpf.setText(null);
        txtEmpNome.setEnabled(true);
        txtEmpCnpj.setEnabled(true);
        cboPesTipo.setEnabled(true);
        txtEmpId.setEnabled(true);
        txtEmpId.setText(null);
        txtEmpNome.setText(null);
        txtEmpCnpj.setText(null);
        txtVeiPlaca.setText(null);
        txtVeiPlacaCarreta.setText(null);
        txtVeiDes.setText(null);
        txtVeiId.setText(null);
        txtVeiPlaca.setEnabled(true);
        txtVeiPlacaCarreta.setEnabled(true);
        txtVeiDes.setEnabled(true);
        cboVeiTipo.setEnabled(true);
        cboPesTipo.setEnabled(true);
        txtConAcompanhante.setText(null);
        txtConMatE.setText(null);
        txtConMatS.setText(null);
        txtConObs.setText(null);
        
        
        
        //a linha abixo habilita o botão adicionar
        btnAdicionarPessoa.setEnabled(true);
                    
        //cboPesTipo.setSelectedItem(null);
        
        ((DefaultTableModel) tblPessoas.getModel()).setRowCount(0);
        ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
        ((DefaultTableModel) tblVeiculos.getModel()).setRowCount(0);
        
    }
    
   
    
    private void pesquisar_setar_pessoa(){
        
                int confirma=JOptionPane.showConfirmDialog(null, "Deseja buscar todo o cadastro associado a essa placa? ", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            
                 
            String sql="select idpes, nome, cpf, cnh, cat , cidade, tipo from pessoas where idpes="+idpes;
            try {
                pst=conexao.prepareStatement(sql);
                rs=pst.executeQuery();
            
                if (rs.next()){
                    txtPesId.setText(rs.getString(1));
                    txtPesNome.setText(rs.getString(2));
                    txtPesCpf.setText(rs.getString(3));
                    txtPesCnh.setText(rs.getString(4));
                    cboPesCat.setSelectedItem(rs.getString(5));
                    txtPesCidade.setText(rs.getString(6));
                    cboPesTipo.setSelectedItem(rs.getString(7));
                     //a linha abixo desabilita o botão adicionar
                    btnAdicionarPessoa.setEnabled(false); 
                    txtPesNome.setEditable(false);
                    txtPesCpf.setEditable(false);
                    cboPesTipo.setEditable(false);
                    txtPesId.setEditable(false);
                    

                }
            } catch (Exception e){
                  JOptionPane.showMessageDialog(null, e);
            }
            pesquisar_setar_empresa(); //chama o resto da função
        }   
    }
    
    private void pesquisar_setar_empresa(){
        
 
                 
            String sql="select idemp, nome, cnpj from empresas where idemp="+idemp;
            try {
                pst=conexao.prepareStatement(sql);
                rs=pst.executeQuery();
            
                if (rs.next()){
                    txtEmpId.setText(rs.getString(1));
                    txtEmpNome.setText(rs.getString(2));
                    txtEmpCnpj.setText(rs.getString(3));
                    
               
                    txtEmpNome.setEditable(false);
                    txtEmpCnpj.setEditable(false);
                    cboPesTipo.setEditable(false);
                    txtEmpId.setEditable(false);
                    

                }
            } catch (Exception e){
                  JOptionPane.showMessageDialog(null, e);
            }
        
    }
    
    private void setar_empresa_pela_pessoa(){
        
         int confirma=JOptionPane.showConfirmDialog(null, "Deseja buscar todo o cadastro da empresa associada a essa pessoa? ", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            
                 String sql="select idemp, nome, cnpj from empresas where idemp="+idemp;
            try {
                pst=conexao.prepareStatement(sql);
                rs=pst.executeQuery();
            
                if (rs.next()){
                    txtEmpId.setText(rs.getString(1));
                    txtEmpNome.setText(rs.getString(2));
                    txtEmpCnpj.setText(rs.getString(3));
                    
               
                    txtEmpNome.setEditable(false);
                    txtEmpCnpj.setEditable(false);
                    cboPesTipo.setEditable(false);
                    txtEmpId.setEditable(false);
                    

                }
            } catch (Exception e){
                  JOptionPane.showMessageDialog(null, e);
            }
            
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtPesPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoas = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtPesNome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboPesTipo = new javax.swing.JComboBox<>();
        txtPesId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnAdicionarPessoa = new javax.swing.JButton();
        txtPesCpf = new javax.swing.JFormattedTextField();
        txtPesCnh = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cboPesCat = new javax.swing.JComboBox<>();
        txtPesCidade = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtVeiPesquisar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblVeiculos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtVeiPlaca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtVeiDes = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cboVeiTipo = new javax.swing.JComboBox<>();
        btnAdicionarVeiculo = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtVeiId = new javax.swing.JTextField();
        txtVeiPlacaCarreta = new javax.swing.JTextField();
        txtVeiPlacaCarreta1 = new javax.swing.JTextField();
        txtVeiPlacaCarreta2 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtEmpPesquisar = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtEmpNome = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtEmpId = new javax.swing.JTextField();
        txtEmpCnpj = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtConMatE = new javax.swing.JTextField();
        txtConMatS = new javax.swing.JTextField();
        txtConAcompanhante = new javax.swing.JTextField();
        btnConEntrada = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtConObs = new javax.swing.JTextField();

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
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTable2);

        setClosable(true);
        setIconifiable(true);
        setTitle("Controle de Acesso de Terceiros");
        setPreferredSize(new java.awt.Dimension(800, 860));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel7.setText("Pesquisar");

        txtPesPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyReleased(evt);
            }
        });

        tblPessoas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPessoas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Tipo"
            }
        ));
        tblPessoas.setFocusable(false);
        tblPessoas.getTableHeader().setReorderingAllowed(false);
        tblPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPessoasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPessoas);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Nome:");

        jLabel5.setText("CPF:");

        jLabel6.setText("Tipo:");

        cboPesTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Motorista", "Visitante", "Terceiro", "Funcionario" }));

        txtPesId.setEditable(false);

        jLabel9.setText("Identificação:");

        btnAdicionarPessoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adicionar2.png"))); // NOI18N
        btnAdicionarPessoa.setToolTipText("Cadastrar");
        btnAdicionarPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarPessoaActionPerformed(evt);
            }
        });

        try {
            txtPesCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtPesCnh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCnhKeyPressed(evt);
            }
        });

        jLabel17.setText("CNH");

        jLabel18.setText("Cat");

        cboPesCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AE", "E", "AD", "D", "AC", "C", "AB", "B", "A" }));
        cboPesCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesCatKeyPressed(evt);
            }
        });

        txtPesCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCidadeKeyPressed(evt);
            }
        });

        jLabel19.setText("Cidade");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)))))
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(btnAdicionarPessoa)
                        .addGap(51, 51, 51))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17)
                                .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnAdicionarPessoa)))
                .addGap(96, 96, 96))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pessoa", jPanel4);

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel16.setText("Pesquisar");

        txtVeiPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVeiPesquisarKeyReleased(evt);
            }
        });

        tblVeiculos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblVeiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Placa", "Placa", "Tipo", "Descrição", "Motorista", "Empresa"
            }
        ));
        tblVeiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVeiculosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblVeiculos);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("Veículo");

        txtVeiPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVeiPlacaKeyTyped(evt);
            }
        });

        jLabel1.setText("Placa ");

        jLabel8.setText("Placa Carreta");

        jLabel13.setText("Descrição do Veículo");

        jLabel14.setText("Tipo de Véiculo");

        cboVeiTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carro Particular", "Carro Empresa", "Moto", "Caminhao", "Carreta", " " }));

        btnAdicionarVeiculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adicionar2.png"))); // NOI18N
        btnAdicionarVeiculo.setText("Cadastrar Veículo");
        btnAdicionarVeiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarVeiculoActionPerformed(evt);
            }
        });

        jLabel15.setText("Idetificação:");

        txtVeiPlacaCarreta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarretaKeyTyped(evt);
            }
        });

        txtVeiPlacaCarreta1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarreta1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarreta1KeyTyped(evt);
            }
        });

        txtVeiPlacaCarreta2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarreta2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarreta2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiDes, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdicionarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addComponent(btnAdicionarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(14, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 2, Short.MAX_VALUE)
                                .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtVeiDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Veículo", jPanel5);

        txtEmpPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyReleased(evt);
            }
        });

        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Razão", "CNPJ", "Contrato", "Número", "Integração", "Válidade"
            }
        ));
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblEmpresas);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel20.setText("CNP-J");

        jLabel21.setText("Razão Social");

        jLabel22.setText("Identificação:");

        txtEmpId.setEditable(false);

        try {
            txtEmpCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 511, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel22)))
                .addGap(18, 18, 18)
                .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel23.setText("Pesquisar");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23)
                    .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 807, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Empresa", jPanel8);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setToolTipText("");

        jLabel11.setText("Acompanhantes");

        jLabel2.setText("Material de Entrada");

        jLabel3.setText("Material de Saída");

        txtConMatE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConMatEActionPerformed(evt);
            }
        });

        txtConMatS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConMatSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3))
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtConAcompanhante, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                            .addComponent(txtConMatE)
                            .addComponent(txtConMatS))))
                .addContainerGap(199, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtConAcompanhante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtConMatE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtConMatS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(119, 119, 119))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dados Adicionais", jPanel6);

        btnConEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/mais.png"))); // NOI18N
        btnConEntrada.setText("Entrada");
        btnConEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConEntradaActionPerformed(evt);
            }
        });

        jLabel12.setText("Observação");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(505, 505, 505)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(txtConObs, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(661, 661, 661)
                        .addComponent(btnConEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1111, 1111, 1111)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(txtConObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        setBounds(0, 0, 829, 466);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConEntradaActionPerformed
        // TODO add your handling code here:
        criarAcesso();
    }//GEN-LAST:event_btnConEntradaActionPerformed

    private void btnAdicionarVeiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarVeiculoActionPerformed
        // chama o metodo adicionar
        adicionarVeiculo();
    }//GEN-LAST:event_btnAdicionarVeiculoActionPerformed

    private void tblVeiculosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVeiculosMouseClicked
        // TODO add your handling code here:
        setar_campos_veiculos();
        pesquisar_setar_pessoa();
    }//GEN-LAST:event_tblVeiculosMouseClicked

    private void txtVeiPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar_veiculos();
    }//GEN-LAST:event_txtVeiPesquisarKeyReleased

    private void btnAdicionarPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarPessoaActionPerformed
        // metodo que adiciciona no banco e na tebla pessoas
        adicionar_pessoa();
    }//GEN-LAST:event_btnAdicionarPessoaActionPerformed

    private void tblPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoasMouseClicked
        // chamando o metodo setar campos
        setar_campos_pessoa();
        setar_empresa_pela_pessoa();
    }//GEN-LAST:event_tblPessoasMouseClicked

    private void txtPesPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar_pessoa();
    }//GEN-LAST:event_txtPesPesquisarKeyReleased

    private void txtEmpPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyReleased
        pesquisar_empresa();
    }//GEN-LAST:event_txtEmpPesquisarKeyReleased

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
        setar_campos_tblEmpresa();

    }//GEN-LAST:event_tblEmpresasMouseClicked

    private void txtConMatSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConMatSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConMatSActionPerformed

    private void txtConMatEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConMatEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConMatEActionPerformed

    private void txtVeiPlacaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaKeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlaca.getText();
        txtVeiPlaca.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaKeyTyped

    private void txtVeiPlacaCarretaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarretaKeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta.getText();
        txtVeiPlacaCarreta.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarretaKeyTyped

    private void txtPesCnhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCnhKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            cboPesCat.requestFocus();
        }
    }//GEN-LAST:event_txtPesCnhKeyPressed

    private void cboPesCatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPesCatKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtPesCidade.requestFocus();
        }
    }//GEN-LAST:event_cboPesCatKeyPressed

    private void txtPesCidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCidadeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
           // txtPesDataContratacao.requestFocus();
        }
    }//GEN-LAST:event_txtPesCidadeKeyPressed

    private void txtVeiPlacaCarreta1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta1KeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiPlacaCarreta2.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaCarreta1KeyPressed

    private void txtVeiPlacaCarreta1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta1KeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta1.getText();
        txtVeiPlacaCarreta1.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarreta1KeyTyped

    private void txtVeiPlacaCarreta2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta2KeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiDes.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaCarreta2KeyPressed

    private void txtVeiPlacaCarreta2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta2KeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta2.getText();
        txtVeiPlacaCarreta2.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarreta2KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPessoa;
    private javax.swing.JButton btnAdicionarVeiculo;
    private javax.swing.JButton btnConEntrada;
    private javax.swing.JComboBox<String> cboPesCat;
    private javax.swing.JComboBox<String> cboPesTipo;
    private javax.swing.JComboBox<String> cboVeiTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTable tblPessoas;
    private javax.swing.JTable tblVeiculos;
    private javax.swing.JTextField txtConAcompanhante;
    private javax.swing.JTextField txtConMatE;
    private javax.swing.JTextField txtConMatS;
    private javax.swing.JTextField txtConObs;
    private javax.swing.JFormattedTextField txtEmpCnpj;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpPesquisar;
    private javax.swing.JTextField txtPesCidade;
    private javax.swing.JTextField txtPesCnh;
    private javax.swing.JFormattedTextField txtPesCpf;
    private javax.swing.JTextField txtPesId;
    private javax.swing.JTextField txtPesNome;
    private javax.swing.JTextField txtPesPesquisar;
    private javax.swing.JTextField txtVeiDes;
    private javax.swing.JTextField txtVeiId;
    private javax.swing.JTextField txtVeiPesquisar;
    private javax.swing.JTextField txtVeiPlaca;
    private javax.swing.JTextField txtVeiPlacaCarreta;
    private javax.swing.JTextField txtVeiPlacaCarreta1;
    private javax.swing.JTextField txtVeiPlacaCarreta2;
    // End of variables declaration//GEN-END:variables
}
