/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
/**
 *
 * @author Leonardo
 */
public class TelaVeiculos extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaVeiculos
     */
    public TelaVeiculos() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void adicionar_pessoa(){
        String sql="insert into pessoas (nome, cpf,cnh, cat, tipo, cidade, datacontratacao, listaequipamento, listaepi, nr35, nr10, nr33, nr12, outroscertificados, certificados, idemp, razao, statusInteg) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtPesNome.getText());
            pst.setString(2, txtPesCpf.getText());
            pst.setString(3, txtPesCnh.getText());
            pst.setString(4, cboPesCat.getSelectedItem().toString());
            pst.setString(5, cboPesTipo.getSelectedItem().toString());
            
            // setando valores do restante do objeto pessoa, pois nesse requisito não é necessario, mas para controle sim
            
            String cidade=null;   //seta
            pst.setString(6, cidade);   //atribui a string sql
            
            String dataContratacao=null;
            pst.setString(7, dataContratacao);
            
            String equipamento=null;
            pst.setString(8, equipamento);
            
            String epi=null;
            pst.setString(9, epi);
            
            String nr35="0";
            pst.setString(10, nr35);
            
            String nr10="0";
            pst.setString(11, nr10);
            
            String nr33="0";
            pst.setString(12, nr33);
            
            String nr12="0";
            pst.setString(13, nr12);
            
            String outrosCertificados="0";
            pst.setString(14, outrosCertificados);
            
            String certificados=null;
            pst.setString(15, certificados);
            
            String idemp="1";
            pst.setString(16, idemp);
            
            String razao=null;
            pst.setString(17, razao);
            
            String statusInteg="0";
            pst.setString(18, statusInteg);
            
           // String validade="1000/10/10";
           // pst.setString(19, validade);
            
            
            //validacão do campos
            if ((txtPesNome.getText().isEmpty()) || (txtPesCpf.getText().isEmpty())) {
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
                    //cboPesTipo.setSelectedItem(null);
                    setar_pessoa_apos_cadastro();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void setar_pessoa_apos_cadastro(){
        
        String idcpf=txtPesCpf.getText();
        String sql="select idpes, nome, cpf, cnh,cat, tipo from pessoas where cpf=?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, idcpf);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtPesId.setText(rs.getString(1));
                txtPesNome.setText(rs.getString(2));
                txtPesCpf.setText(rs.getString(3));
                txtPesCnh.setText(rs.getString(4));
                cboPesCat.setSelectedItem(rs.getString(5));
                cboPesTipo.setSelectedItem(rs.getString(6));
          
                //Desativando recursos
                btnAdicionarPessoa.setEnabled(false);
                txtPesId.setEditable(false);
                txtPesNome.setEditable(false);
                txtPesCpf.setEditable(false);
                txtPesCnh.setEditable(false);
                cboPesCat.setEditable(false);
                cboPesTipo.setEditable(false);
      
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void pesquisar_pessoa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idpes as ID, nome as Nome, cpf as CPF, tipo as Tipo from pessoas where nome like ?";
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
    
    private void setar_campos_pessoa(){
        int setar = tblPessoas.getSelectedRow();
        txtPesId.setText(tblPessoas.getModel().getValueAt(setar,0).toString());
        txtPesNome.setText(tblPessoas.getModel().getValueAt(setar,1).toString());
        txtPesCpf.setText(tblPessoas.getModel().getValueAt(setar,2).toString());
        cboPesTipo.setSelectedItem(tblPessoas.getModel().getValueAt(setar,3).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionarPessoa.setEnabled(false); 
        setar_pessoa_apos_cadastro();
        
    }
    //select idvei as ID, placa as Placa, placaCavalo as Placa2, tipoVei as Tipo, descricao as Desc from veiculos where placa like ?
    private void pesquisar_veiculos(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idvei as ID, placa as Placa, placaCarreta as Placa, tipoVei as tipo from veiculos where placa like ?";
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
    
    private void setar_campos_veiculos(){
        int setar = tblVeiculos.getSelectedRow();
        txtVeiId.setText(tblVeiculos.getModel().getValueAt(setar,0).toString());
        txtVeiPlaca.setText(tblVeiculos.getModel().getValueAt(setar,1).toString());
        txtVeiPlacaCarreta.setText(tblVeiculos.getModel().getValueAt(setar,2).toString());
        cboVeiTipo.setSelectedItem(tblVeiculos.getModel().getValueAt(setar,3).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionar.setEnabled(false); 
        setar_campos_restantes_veiculo();
        
    }
    
    private void setar_campos_restantes_veiculo(){
        String idvei=txtVeiId.getText();
        String sql="select descricao, placaCarreta2, placaCarreta3, idpes, idemp, uf from veiculos where idvei=?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, idvei);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtVeiDes.setText(rs.getString(1));
                txtVeiPlacaCarreta1.setText(rs.getString(2));
                txtVeiPlacaCarreta2.setText(rs.getString(3));
                txtPesId.setText(rs.getString(4));
                txtEmpId.setText(rs.getString(5));
                txtVeiUf.setText(rs.getString(6));
                
          
                //Desativando recursos
                
      
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
            
    private void setar_campos_pessoas(){
        //Codigo abaixo seta o restante dos dados
        
        String idpes=txtPesId.getText();
        String sql="select nome, cpf, tipo from pessoas where idpes="+ idpes;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtPesNome.setText(rs.getString(1));
                txtPesCpf.setText(rs.getString(2));
                cboPesTipo.setSelectedItem(rs.getString(3));
          
                //Desativando recursos
                btnAdicionarPessoa.setEnabled(false);
      
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void adicionar(){
        String sql="insert into veiculos (placa, placaCarreta,placaCarreta2, placaCarreta3, tipoVei, descricao, idpes, idemp, uf) values(?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtVeiPlaca.getText());
            pst.setString(2, txtVeiPlacaCarreta.getText());
            pst.setString(3, txtVeiPlacaCarreta1.getText());
            pst.setString(4, txtVeiPlacaCarreta2.getText());
           
            pst.setString(5, cboVeiTipo.getSelectedItem().toString());
            pst.setString(6, txtVeiDes.getText());
            pst.setString(7, txtPesId.getText()); //referencia de pessoa
            //verifica se o campo esta nullo a atribui o padão 1
            if(txtEmpId.getText().isEmpty()){
                txtEmpId.setText("1");
            }
            pst.setString(8, txtEmpId.getText()); //referencia de empresa
            pst.setString(9, txtVeiUf.getText());
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
                    //txtVeiPlacaCarreta.setText(null);
                    //txtVeiDes.setText(null);
                    //txtPesId.setText(null);
                    //txtEmpId.setText(null); //referente ao compo empresa
                    //txtEmpNome.setText(null);
                    //txtEmpCnpj.setText(null);
                    //a linha abixo habilita o botão adicionar
                    //btnAdicionarPessoa.setEnabled(true);
                    
                    //cboPesTipo.setSelectedItem(null);
                    
                    //recupera e seta o id do veiculo criado 
                    txtVeiId.setText(recuperaIdVeiculo());
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private String recuperaIdVeiculo(){
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
     private void setar_campos_empresas(){
        //Codigo abaixo seta o restante dos dados
        
        String idemp=txtEmpId.getText();
        String sql="select nome, cnpj from empresas where idemp="+ idemp;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtEmpNome.setText(rs.getString(1));
                txtEmpCnpj.setText(rs.getString(2));
                  
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    private void alterar(){
        String idvei=txtVeiId.getText();
        String sql="update veiculos set placa=?, placaCarreta=?, placaCarreta2=?, placaCarreta3=?, tipoVei=?, descricao=?, idpes=?, idemp=?, uf=? where idvei="+idvei;
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtVeiPlaca.getText());
            pst.setString(2, txtVeiPlacaCarreta.getText());
            pst.setString(3, txtVeiPlacaCarreta1.getText());
            pst.setString(4, txtVeiPlacaCarreta2.getText());
            pst.setString(5, cboVeiTipo.getSelectedItem().toString());
            pst.setString(6, txtVeiDes.getText());
            pst.setString(7, txtPesId.getText()); //referencia de pessoa
            //verifica se o campo esta nullo a atribui o padão 1
            if(txtEmpId.getText().isEmpty()){
                txtEmpId.setText("1");
            }
            pst.setString(8, txtEmpId.getText()); //referencia de empresa
            pst.setString(9, txtVeiUf.getText());
            //validacão do campos
            if ((txtVeiPlaca.getText().isEmpty()) || (txtPesId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Cadastro alterado com sucesso!");
                    //Limpa as Caixas
                    //limpar(); 
                    //a linha abixo habilita o botão adicionar
                    //btnAdicionarPessoa.setEnabled(true);
                    
                    //cboPesTipo.setSelectedItem(null);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void limpar(){
        txtVeiId.setText(null);
        txtPesNome.setText(null);
        txtPesCpf.setText(null);
        txtPesCnh.setText(null);
        
        txtVeiPlaca.setText(null);
        txtVeiPlacaCarreta.setText(null);
        txtVeiPlacaCarreta1.setText(null);
        txtVeiPlacaCarreta2.setText(null);
        txtVeiDes.setText(null);
        txtPesId.setText(null);
        txtPesNome.setText(null);
        txtEmpId.setText(null);
        txtEmpNome.setText(null);
        txtEmpCnpj.setText(null);
        txtEmpPesquisar.setText(null);
        txtVeiPesquisar.setText(null);
        txtPesPesquisar.setText(null);
        txtVeiId.setText(null);
        
        //a linha abixo habilita o botão adicionar
        btnAdicionarPessoa.setEnabled(true);
        btnAdicionar.setEnabled(true);
                    
        //cboPesTipo.setSelectedItem(null);
        
        ((DefaultTableModel) tblPessoas.getModel()).setRowCount(0);
        ((DefaultTableModel) tblVeiculos.getModel()).setRowCount(0);
        ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
        
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
    
    public void setar_campos_empresa(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
        txtEmpCnpj.setText(tblEmpresas.getModel().getValueAt(setar,2).toString());
        cboEmpTipo.setSelectedItem(tblEmpresas.getModel().getValueAt(setar,3).toString());
       
    }
    
    //metodo remover um veículo do banco
    
    private void remover(){
        
        //a estrutura confima a remoção do usuário
        
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o véiculo?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma==JOptionPane.YES_OPTION){
            String sql="delete from veiculos where idvei=?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, txtVeiId.getText());
               // pst.executeUpdate();  //executa a sql
                int apagado = pst.executeUpdate();
                if (apagado>0){
                    JOptionPane.showMessageDialog(null, "Removido com sucesso");
                    limpar();
                }
                
            } catch (Exception e) {
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtVeiDes = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboVeiTipo = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtVeiId = new javax.swing.JTextField();
        txtVeiPlaca = new javax.swing.JTextField();
        txtVeiPlacaCarreta = new javax.swing.JTextField();
        txtVeiPlacaCarreta1 = new javax.swing.JTextField();
        txtVeiPlacaCarreta2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtVeiUf = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblVeiculos = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtVeiPesquisar = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoas = new javax.swing.JTable();
        txtPesPesquisar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboPesTipo = new javax.swing.JComboBox<>();
        txtPesNome = new javax.swing.JTextField();
        btnAdicionarPessoa = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPesId = new javax.swing.JTextField();
        txtPesCpf = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPesCnh = new javax.swing.JTextField();
        cboPesCat = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        txtEmpPesquisar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtEmpNome = new javax.swing.JTextField();
        txtEmpId = new javax.swing.JTextField();
        txtEmpCnpj = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        cboEmpTipo = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Veículos");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Placa ");

        jLabel8.setText("Placas Carreta");

        jLabel6.setText("Descrição do Veículo");

        jLabel5.setText("Tipo de Véiculo");

        cboVeiTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Particular", "Empresarial", "Moto", "Caminhao", " ", " " }));
        cboVeiTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVeiTipoActionPerformed(evt);
            }
        });
        cboVeiTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboVeiTipoKeyPressed(evt);
            }
        });

        jLabel10.setText("ID");

        txtVeiId.setEditable(false);

        txtVeiPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiPlacaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVeiPlacaKeyTyped(evt);
            }
        });

        txtVeiPlacaCarreta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiPlacaCarretaKeyPressed(evt);
            }
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

        jLabel18.setText("UF");

        txtVeiUf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVeiUfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiDes))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113)
                        .addComponent(jLabel5)
                        .addGap(33, 33, 33)
                        .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiUf, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(182, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)
                        .addComponent(txtVeiUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtVeiDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tblPessoas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblVeiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Placa", "Placa", "Tipo"
            }
        ));
        tblVeiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVeiculosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblVeiculos);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel11.setText("Pesquisar ");

        txtVeiPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiPesquisarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVeiPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );

        jTabbedPane1.addTab("Dados do Veículo", jPanel3);

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
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Tipo"
            }
        ));
        tblPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPessoasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPessoas);

        txtPesPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel7.setText("Pesquisar");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Nome do Motorista");

        jLabel3.setText("CPF");

        jLabel4.setText("Tipo");

        cboPesTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Motorista", "Visitante", "Terceiro", "Funcionario" }));
        cboPesTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesTipoKeyPressed(evt);
            }
        });

        txtPesNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesNomeKeyPressed(evt);
            }
        });

        btnAdicionarPessoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adicionar2.png"))); // NOI18N
        btnAdicionarPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarPessoaActionPerformed(evt);
            }
        });

        jLabel9.setText("ID");

        txtPesId.setEditable(false);

        try {
            txtPesCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPesCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesCpfActionPerformed(evt);
            }
        });
        txtPesCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCpfKeyPressed(evt);
            }
        });

        jLabel14.setText("CNH");

        txtPesCnh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCnhKeyPressed(evt);
            }
        });

        cboPesCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AE", "E", "AD", "D", "AC", "C", "AB", "B", "A" }));

        jLabel15.setText("Cat");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(15, 15, 15)
                        .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionarPessoa)))
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdicionarPessoa, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(17, 17, 17)
                        .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 307, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Dados do Motorista", jPanel4);

        tblEmpresas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblEmpresas);

        txtEmpPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel12.setText("Pesquisar");

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setText("*Razão");

        txtEmpNome.setEditable(false);
        txtEmpNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNomeKeyPressed(evt);
            }
        });

        txtEmpId.setEditable(false);

        txtEmpCnpj.setEditable(false);
        try {
            txtEmpCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel17.setText("*CNPJ");

        cboEmpTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente", "Cliente/Fornecedor", "Tercerizada", "Transportadora" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)
                        .addComponent(cboEmpTipo, 0, 163, Short.MAX_VALUE)
                        .addGap(318, 318, 318))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboEmpTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel16.setText("*Dados não editáveis");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 314, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel16))))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Dados da Empresa", jPanel5);

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/limpar.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(182, 182, 182))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcluir)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnAdicionar))
                    .addComponent(btnLimpar))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setBounds(0, 0, 832, 490);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarPessoaActionPerformed
        // metodo que adiciciona no banco e na tebla pessoas
        adicionar_pessoa();
    }//GEN-LAST:event_btnAdicionarPessoaActionPerformed

    private void txtPesPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesPesquisarKeyReleased
        // metodo para pesquisar pessoas
        pesquisar_pessoa();
    }//GEN-LAST:event_txtPesPesquisarKeyReleased

    private void tblPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoasMouseClicked
        // metodo para setar campos
        setar_campos_pessoa();
    }//GEN-LAST:event_tblPessoasMouseClicked

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // chama o metodo adicionar
        adicionar(); 
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void txtVeiPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar_veiculos(); 
    }//GEN-LAST:event_txtVeiPesquisarKeyReleased

    private void tblVeiculosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVeiculosMouseClicked
        // TODO add your handling code here:
        setar_campos_veiculos();   
        setar_campos_pessoas();
        setar_campos_empresas();
    }//GEN-LAST:event_tblVeiculosMouseClicked

    private void txtPesCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesCpfActionPerformed

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

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        remover();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed

        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
       setar_campos_empresa();
    }//GEN-LAST:event_tblEmpresasMouseClicked

    private void txtEmpPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyReleased
        pesquisar_empresa();
    }//GEN-LAST:event_txtEmpPesquisarKeyReleased

    private void txtVeiPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPesquisarKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtVeiPlaca.requestFocus();
       }
    }//GEN-LAST:event_txtVeiPesquisarKeyPressed

    private void txtVeiPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER){
             cboVeiTipo.requestFocus();
       }
    }//GEN-LAST:event_txtVeiPlacaKeyPressed

    private void cboVeiTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboVeiTipoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtVeiPlacaCarreta.requestFocus();
       }
    }//GEN-LAST:event_cboVeiTipoKeyPressed

    private void txtVeiPlacaCarretaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarretaKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtVeiPlacaCarreta1.requestFocus();
       }
    }//GEN-LAST:event_txtVeiPlacaCarretaKeyPressed

    private void txtPesPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesPesquisarKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesNome.requestFocus();
       }
    }//GEN-LAST:event_txtPesPesquisarKeyPressed

    private void txtPesNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesNomeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             cboPesTipo.requestFocus();
       }
    }//GEN-LAST:event_txtPesNomeKeyPressed

    private void txtPesCpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesCnh.requestFocus();
       }
    }//GEN-LAST:event_txtPesCpfKeyPressed

    private void txtEmpPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpNome.requestFocus();
       }
    }//GEN-LAST:event_txtEmpPesquisarKeyPressed

    private void txtEmpNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNomeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtEmpCnpj.requestFocus();
       }
    }//GEN-LAST:event_txtEmpNomeKeyPressed

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

    private void cboVeiTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVeiTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboVeiTipoActionPerformed

    private void cboPesTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPesTipoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesCpf.requestFocus();
       }
    }//GEN-LAST:event_cboPesTipoKeyPressed

    private void txtPesCnhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCnhKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER){
             cboPesCat.requestFocus();
       }
    }//GEN-LAST:event_txtPesCnhKeyPressed

    private void txtVeiUfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVeiUfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVeiUfActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAdicionarPessoa;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JComboBox<String> cboEmpTipo;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTable tblPessoas;
    private javax.swing.JTable tblVeiculos;
    private javax.swing.JFormattedTextField txtEmpCnpj;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpPesquisar;
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
    private javax.swing.JTextField txtVeiUf;
    // End of variables declaration//GEN-END:variables
}
