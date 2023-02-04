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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Leonardo
 */
public class TelaAcessoAtivo extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Creates new form TelaAcessoAtivo
     */
    public TelaAcessoAtivo() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_acesso();
        
        btnFinalizar.setEnabled(false); 
        
        Date data = new Date();
        //DateFormat formatador;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        //formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        txtAceDataPesquisa.setText(formato.format(data));
        
        pesquisar_acesso_finalizados(); 
        controle_arduino();
    }
    
    //select idvei as ID, placa as Placa, placaCavalo as Placa2, tipoVei as Tipo, descricao as Desc from veiculos where placa like ?
    private void pesquisar_acesso(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo from acesso where statusAcesso='1'";
        try {
            pst=conexao.prepareStatement(sql);
          
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblAcessos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void setar_campos(){
        int setar = tblAcessos.getSelectedRow();
        txtAceId.setText(tblAcessos.getModel().getValueAt(setar,0).toString());
        txtAcePlaca.setText(tblAcessos.getModel().getValueAt(setar,2).toString());
        txtAceEmpresa.setText(tblAcessos.getModel().getValueAt(setar,3).toString());
        txtAcePessoa.setText(tblAcessos.getModel().getValueAt(setar,1).toString());
       
        btnFinalizar.setEnabled(true); 
        
        //Codigo abaixo seta o restante dos dados
        
        String acesso=txtAceId.getText();
        String sql="select acompanhantes, obs, matEntrada, matSaida from acesso where idacesso="+ acesso;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtAceAcomp.setText(rs.getString(1));
                txtAceObs.setText(rs.getString(2));
                txtAceMatE.setText(rs.getString(3));
                txtAceMatS.setText(rs.getString(4));
                
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void pesquisar_acesso_finalizados(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo from acesso where statusAcesso='0' and date(`horaEntrada`) =? ";
        try {
            pst=conexao.prepareStatement(sql);
            
            String dataRecebida = txtAceDataPesquisa.getText(); //passa a String para a variavel string  
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padão brasileiro
            String dataInsert=formato2.format(dataFormatada);
            pst.setString(1, dataInsert);
          
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblAcessosFinalizados.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void setar_campos_finalizados(){
        int setar = tblAcessosFinalizados.getSelectedRow();
        txtAceIdFinalizado.setText(tblAcessosFinalizados.getModel().getValueAt(setar,0).toString());
        txtAcePlacaFinalizado.setText(tblAcessosFinalizados.getModel().getValueAt(setar,2).toString());
        txtAceEmpresaFinalizado.setText(tblAcessosFinalizados.getModel().getValueAt(setar,3).toString());
        txtAcePessoaFinalizado.setText(tblAcessosFinalizados.getModel().getValueAt(setar,1).toString());
        
        //Codigo abaixo seta o restante dos dados
        
        String acesso=txtAceIdFinalizado.getText();
        String sql="select acompanhantes, obs, matEntrada, matSaida, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i'), DATE_FORMAT (`horaSaida`,'%d/%m/%Y  %Hh%i') from acesso where idacesso="+ acesso;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtAceAcompFinalizado.setText(rs.getString(1));
                txtAceObsFinalizado.setText(rs.getString(2));
                txtAceMatEFinalizado.setText(rs.getString(3));
                txtAceMatSFinalizado.setText(rs.getString(4));
                txtAceFinalizadoEntrada.setText(rs.getString(5));
                txtAceFinalizadoSaida.setText(rs.getString(6));
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
     public void setar_campos_rap(){
        int setar = tblPesquisaRapida.getSelectedRow();
        txtAceIdFinalizadoRap.setText(tblPesquisaRapida.getModel().getValueAt(setar,0).toString());
        txtAcePlacaFinalizadoRap.setText(tblPesquisaRapida.getModel().getValueAt(setar,2).toString());
        txtAceEmpresaFinalizadoRap.setText(tblPesquisaRapida.getModel().getValueAt(setar,3).toString());
        txtAcePessoaFinalizadoRap.setText(tblPesquisaRapida.getModel().getValueAt(setar,1).toString());
        
        //Codigo abaixo seta o restante dos dados
        
        String acesso=txtAceIdFinalizadoRap.getText();
        String sql="select acompanhantes, obs, matEntrada, matSaida, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i'), DATE_FORMAT (`horaSaida`,'%d/%m/%Y  %Hh%i') from acesso where idacesso="+ acesso;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtAceAcompFinalizadoRap.setText(rs.getString(1));
                txtAceObsFinalizadoRap.setText(rs.getString(2));
                txtAceMatEFinalizadoRap.setText(rs.getString(3));
                txtAceMatSFinalizadoRap.setText(rs.getString(4));
                txtAceRapEntrada.setText(rs.getString(5));
                txtAceRapSaida.setText(rs.getString(6));
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void finalizarAcesso(){
        
        //cria a conteudo da data para inserir no banco ao finalizar
        Date data = new Date();
        //Cria um formatador do tipo SimpleDataFormat no formato para banco mysql 
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataFormatada=formato.format(data); //armazena a data já formatada em string
        
        
        
        String idAcesso=txtAceId.getText(); //busaca o id do campo da janela
        String sql="update acesso set statusAcesso= '0', acompanhantes=?, obs=?, matEntrada=?, matSaida=?, horaSaida=? where idacesso="+idAcesso;
        try {
            
            
            //validacão do campos
            if ((txtAceId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Para finalizar selecione o acesso!");
            } else {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, txtAceAcomp.getText());
                pst.setString(2, txtAceObs.getText());
                pst.setString(3, txtAceMatE.getText());
                pst.setString(4, txtAceMatS.getText());
                pst.setString(5, dataFormatada); 
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Acesso finalizado com sucesso!");
                    
                    btnFinalizar.setEnabled(false);
                    txtAceId.setText(null);
                    txtAceEmpresa.setText(null);
                    txtAcePessoa.setText(null);
                    txtAceAcomp.setText(null);
                    txtAceObs.setText(null);
                    txtAceMatE.setText(null);
                    txtAceMatS.setText(null);
                  
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    
        pesquisar_acesso();
    }
    
    
    private void pesquisar_acesso_placa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i') as Data from acesso where placaAcesso like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesquisaRapPlaca.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPesquisaRapida.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
     private void pesquisar_acesso_nome(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i') as Data from acesso where nomeAcesso like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesquisaRapNome.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPesquisaRapida.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
      private void pesquisar_acesso_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i') as Data from acesso where empresa like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesquisaRapEmpresa.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPesquisaRapida.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
     private void pesquisar_acesso_data(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idAcesso as Acesso, nomeAcesso as Nome, placaAcesso as Placa, empresa as Empresa, tipoAcesso as Tipo, DATE_FORMAT (`horaEntrada`,'%d/%m/%Y  %Hh%i') as Data from acesso where horaEntrada like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesquisaRapData.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPesquisaRapida.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
     
     private void controle_arduino(){
         
         String id = null, iud = null, dataIud = null;

               String sql="select * from arduino where processado=false";
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                id=rs.getString(1);
                iud= rs.getString(2);
                dataIud=rs.getString(3);
               
                
               pesquisa_rfid(id, iud, dataIud);
                
            } 
        } catch (Exception e){
            
            JOptionPane.showMessageDialog(null, e);
        
            System.out.println("estou aqui erro controle arduino");
           
        }
         
        
    }
     
     private void pesquisa_rfid(String id, String iud, String dataIud){
        System.out.println(iud);
        String idpes = null;
        String nome = null;
        String tipo = null;
        String idemp = null;
        
          //String sql="select idpes, nome, tipo, idemp from pessoas where iud= 59 "+iud  
                  ;  
         //String sql="select idpes, nome, tipo, idemp from pessoas where iud=' 59 AE 75 C2'";
         String sql="select idpes, nome, tipo, idemp from pessoas where iud= ?";
         System.out.println("estou aqui (pesquisa rfid antes da sql)");
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, iud);
            rs=pst.executeQuery();
             
            if (rs.next()) {
                idpes= rs.getString(1);
                nome= rs.getString(2);
                tipo= rs.getString(3);
                idemp= rs.getString(4);
               System.out.println("estou aqui (pesquisa rfid durante da sql)");
               System.out.println(nome);
               criaAcesso_rfid(id, nome, tipo, idpes, idemp);
               
               
            } 
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        
            System.out.println("estou aqui 4");
            
        }
         
     }     
     
     
     private void criaAcesso_rfid(String id, String nome, String tipo, String idpes, String idemp ){
         
         System.out.println(nome);
         System.out.println(tipo);
         System.out.println(idpes);
         System.out.println(nome);
         String sql="insert into acesso (nomeAcesso, tipoAcesso, placaAcesso, empresa, acompanhantes, obs, matEntrada, matSaida, idpes, idvei, statusAcesso, idemp) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setString(2, tipo);
            pst.setString(3, "");
            pst.setString(4, "");
            pst.setString(5, "");
            pst.setString(6, "");
            pst.setString(7, "");
            pst.setString(8, "");
            pst.setString(9, idpes);
            pst.setString(10, "1");
            pst.setString(11, "1"); //seta como aberto apos criação
            pst.setString(12, idemp);
          
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    //JOptionPane.showMessageDialog(null, "Acesso criado com sucesso!");
                    finaliza_iud(id);
                }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
     }
     
     private void finaliza_iud(String id){
         
        String sql="update arduino set processado= '1' where id="+id;
        try {
           
                pst=conexao.prepareStatement(sql);
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                   // JOptionPane.showMessageDialog(null, "Acesso finalizado com sucesso!");
                  
                }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcessos = new javax.swing.JTable();
        btnFinalizar = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAceId = new javax.swing.JTextField();
        txtAcePessoa = new javax.swing.JTextField();
        txtAceEmpresa = new javax.swing.JTextField();
        btnAtualizar = new javax.swing.JButton();
        txtAcePlaca = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtAceObs = new javax.swing.JTextField();
        txtAceAcomp = new javax.swing.JTextField();
        txtAceMatE = new javax.swing.JTextField();
        txtAceMatS = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAcessosFinalizados = new javax.swing.JTable();
        txtAceIdFinalizado = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAceEmpresaFinalizado = new javax.swing.JTextField();
        txtAcePessoaFinalizado = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnFinalizar1 = new javax.swing.JToggleButton();
        txtAceDataPesquisa = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAcePlacaFinalizado = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtAceAcompFinalizado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtAceObsFinalizado = new javax.swing.JTextField();
        txtAceMatEFinalizado = new javax.swing.JTextField();
        txtAceMatSFinalizado = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtAceFinalizadoEntrada = new javax.swing.JLabel();
        txtAceFinalizadoSaida = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPesquisaRapida = new javax.swing.JTable();
        txtAceIdFinalizadoRap = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtAceEmpresaFinalizadoRap = new javax.swing.JTextField();
        txtAcePessoaFinalizadoRap = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtAcePlacaFinalizadoRap = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtAceAcompFinalizadoRap = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtAceObsFinalizadoRap = new javax.swing.JTextField();
        txtAceMatEFinalizadoRap = new javax.swing.JTextField();
        txtAceMatSFinalizadoRap = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtAceRapEntrada = new javax.swing.JLabel();
        txtAceRapSaida = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtPesquisaRapPlaca = new javax.swing.JTextField();
        txtPesquisaRapNome = new javax.swing.JTextField();
        txtPesquisaRapEmpresa = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtPesquisaRapData = new javax.swing.JFormattedTextField();
        btnPesquisarData = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Gerenciador de Acessos");

        tblAcessos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblAcessos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Acesso", "Nome", "Placa", "Empresa", "Tipo"
            }
        ));
        tblAcessos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAcessosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAcessos);

        btnFinalizar.setText("Finalizar");
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFinalizarMouseClicked(evt);
            }
        });

        jLabel1.setText("N° Acesso:");

        jLabel2.setText("Nome da Pessoa:");

        jLabel3.setText("Empresa:");

        txtAceId.setEditable(false);

        txtAcePessoa.setEditable(false);

        txtAceEmpresa.setEditable(false);

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/refresh_icon48.png"))); // NOI18N
        btnAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAtualizarMouseClicked(evt);
            }
        });
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        txtAcePlaca.setEditable(false);

        jLabel6.setText("Placa:");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setText("OBS:");

        jLabel16.setText("Acompanhantes:");

        jLabel17.setText("Material Entrada:");

        jLabel18.setText("Material Saída:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAceObs))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(12, 12, 12)
                        .addComponent(txtAceAcomp))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAceMatE)
                            .addComponent(txtAceMatS))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtAceObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtAceAcomp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtAceMatE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtAceMatS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtAceId, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAceEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAcePlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAcePessoa)))
                        .addGap(132, 132, 132))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtAceId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtAceEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAtualizar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtAcePlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtAcePessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Acessos Ativos", jPanel1);

        tblAcessosFinalizados.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblAcessosFinalizados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Acesso", "Nome", "Placa", "Empresa", "Tipo"
            }
        ));
        tblAcessosFinalizados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAcessosFinalizadosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblAcessosFinalizados);

        txtAceIdFinalizado.setEditable(false);

        jLabel4.setText("N° Acesso:");

        txtAceEmpresaFinalizado.setEditable(false);

        txtAcePessoaFinalizado.setEditable(false);

        jLabel5.setText("Nome da Pessoa:");

        btnFinalizar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar.png"))); // NOI18N
        btnFinalizar1.setText("Pesquisar");
        btnFinalizar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFinalizar1MouseClicked(evt);
            }
        });
        btnFinalizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizar1ActionPerformed(evt);
            }
        });

        txtAceDataPesquisa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        try {
            txtAceDataPesquisa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtAceDataPesquisa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtAceDataPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAceDataPesquisaActionPerformed(evt);
            }
        });

        jLabel7.setText("Empresa:");

        txtAcePlacaFinalizado.setEditable(false);

        jLabel8.setText("Placa:");

        jLabel10.setText("Acompanhantes:");

        txtAceAcompFinalizado.setEditable(false);

        jLabel11.setText("Obs:");

        jLabel12.setText("Material Entrada:");

        jLabel13.setText("Material Saída:");

        txtAceObsFinalizado.setEditable(false);

        txtAceMatEFinalizado.setEditable(false);

        txtAceMatSFinalizado.setEditable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Entrada:");

        jLabel14.setText("Saída:");

        txtAceFinalizadoEntrada.setText("  /  /       :  ");

        txtAceFinalizadoSaida.setText("  /  /       :  ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAceFinalizadoEntrada))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtAceFinalizadoSaida)))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtAceFinalizadoEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtAceFinalizadoSaida))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAceIdFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAceEmpresaFinalizado, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAcePessoaFinalizado))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAcePlacaFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(txtAceObsFinalizado))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAceAcompFinalizado)
                            .addComponent(txtAceMatEFinalizado)
                            .addComponent(txtAceMatSFinalizado))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(txtAceDataPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFinalizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtAceIdFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAceEmpresaFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtAcePessoaFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtAcePlacaFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txtAceObsFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtAceAcompFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAceDataPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFinalizar1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAceMatEFinalizado)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAceMatSFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Acessos Finalizados", jPanel2);

        tblPesquisaRapida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblPesquisaRapida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Acesso", "Nome", "Placa", "Empresa", "Data"
            }
        ));
        tblPesquisaRapida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaRapidaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblPesquisaRapida);

        txtAceIdFinalizadoRap.setEditable(false);

        jLabel23.setText("N° Acesso:");

        txtAceEmpresaFinalizadoRap.setEditable(false);

        txtAcePessoaFinalizadoRap.setEditable(false);

        jLabel24.setText("Nome da Pessoa:");

        jLabel25.setText("Empresa:");

        txtAcePlacaFinalizadoRap.setEditable(false);

        jLabel26.setText("Placa:");

        jLabel27.setText("Acompanhantes:");

        txtAceAcompFinalizadoRap.setEditable(false);

        jLabel28.setText("Obs:");

        jLabel29.setText("Material Entrada:");

        jLabel30.setText("Material Saída:");

        txtAceObsFinalizadoRap.setEditable(false);

        txtAceMatEFinalizadoRap.setEditable(false);

        txtAceMatSFinalizadoRap.setEditable(false);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel31.setText("Entrada:");

        jLabel32.setText("Saída:");

        txtAceRapEntrada.setText("  /  /       :  ");

        txtAceRapSaida.setText("  /  /       :  ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAceRapEntrada))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtAceRapSaida)))
                .addContainerGap(138, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtAceRapEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtAceRapSaida))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel33.setText("Placa:");

        txtPesquisaRapPlaca.setToolTipText("");
        txtPesquisaRapPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaRapPlacaKeyReleased(evt);
            }
        });

        txtPesquisaRapNome.setToolTipText("");
        txtPesquisaRapNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaRapNomeKeyReleased(evt);
            }
        });

        txtPesquisaRapEmpresa.setToolTipText("");
        txtPesquisaRapEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaRapEmpresaKeyReleased(evt);
            }
        });

        jLabel34.setText("Nome:");

        jLabel35.setText("Empresa:");

        jLabel36.setText("Data:");

        try {
            txtPesquisaRapData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPesquisaRapData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaRapDataActionPerformed(evt);
            }
        });

        btnPesquisarData.setText("Pesquisar");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesquisaRapPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesquisaRapNome, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesquisaRapEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesquisaRapData, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisarData))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAceIdFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAceEmpresaFinalizadoRap, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAcePessoaFinalizadoRap))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAcePlacaFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(txtAceObsFinalizadoRap))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(12, 12, 12)
                                .addComponent(txtAceAcompFinalizadoRap))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel29))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAceMatEFinalizadoRap)
                                    .addComponent(txtAceMatSFinalizadoRap))))
                        .addGap(53, 53, 53)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesquisaRapNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(txtPesquisaRapPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(txtPesquisaRapEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisaRapData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36)
                            .addComponent(btnPesquisarData))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtAceIdFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAceEmpresaFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(txtAcePessoaFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtAcePlacaFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)
                            .addComponent(txtAceObsFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtAceAcompFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtAceMatEFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtAceMatSFinalizadoRap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Pesquisa Rápida", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        setBounds(0, 0, 820, 490);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisaRapDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaRapDataActionPerformed
        pesquisar_acesso_data();
    }//GEN-LAST:event_txtPesquisaRapDataActionPerformed

    private void txtPesquisaRapEmpresaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaRapEmpresaKeyReleased
        pesquisar_acesso_empresa();
    }//GEN-LAST:event_txtPesquisaRapEmpresaKeyReleased

    private void txtPesquisaRapNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaRapNomeKeyReleased
       pesquisar_acesso_nome();
    }//GEN-LAST:event_txtPesquisaRapNomeKeyReleased

    private void txtPesquisaRapPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaRapPlacaKeyReleased
        
        pesquisar_acesso_placa();
    }//GEN-LAST:event_txtPesquisaRapPlacaKeyReleased

    private void tblPesquisaRapidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaRapidaMouseClicked
        setar_campos_rap();
    }//GEN-LAST:event_tblPesquisaRapidaMouseClicked

    private void txtAceDataPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAceDataPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAceDataPesquisaActionPerformed

    private void btnFinalizar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFinalizar1MouseClicked
        // TODO add your handling code here:
        pesquisar_acesso_finalizados();
    }//GEN-LAST:event_btnFinalizar1MouseClicked

    private void tblAcessosFinalizadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAcessosFinalizadosMouseClicked
        // TODO add your handling code here:
        setar_campos_finalizados();

    }//GEN-LAST:event_tblAcessosFinalizadosMouseClicked

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnAtualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtualizarMouseClicked
        pesquisar_acesso();
        controle_arduino();
    }//GEN-LAST:event_btnAtualizarMouseClicked

    private void btnFinalizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFinalizarMouseClicked
        finalizarAcesso();
    }//GEN-LAST:event_btnFinalizarMouseClicked

    private void tblAcessosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAcessosMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblAcessosMouseClicked

    private void btnFinalizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFinalizar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JToggleButton btnFinalizar;
    private javax.swing.JToggleButton btnFinalizar1;
    private javax.swing.JButton btnPesquisarData;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblAcessos;
    private javax.swing.JTable tblAcessosFinalizados;
    private javax.swing.JTable tblPesquisaRapida;
    private javax.swing.JTextField txtAceAcomp;
    private javax.swing.JTextField txtAceAcompFinalizado;
    private javax.swing.JTextField txtAceAcompFinalizadoRap;
    private javax.swing.JFormattedTextField txtAceDataPesquisa;
    private javax.swing.JTextField txtAceEmpresa;
    private javax.swing.JTextField txtAceEmpresaFinalizado;
    private javax.swing.JTextField txtAceEmpresaFinalizadoRap;
    private javax.swing.JLabel txtAceFinalizadoEntrada;
    private javax.swing.JLabel txtAceFinalizadoSaida;
    private javax.swing.JTextField txtAceId;
    private javax.swing.JTextField txtAceIdFinalizado;
    private javax.swing.JTextField txtAceIdFinalizadoRap;
    private javax.swing.JTextField txtAceMatE;
    private javax.swing.JTextField txtAceMatEFinalizado;
    private javax.swing.JTextField txtAceMatEFinalizadoRap;
    private javax.swing.JTextField txtAceMatS;
    private javax.swing.JTextField txtAceMatSFinalizado;
    private javax.swing.JTextField txtAceMatSFinalizadoRap;
    private javax.swing.JTextField txtAceObs;
    private javax.swing.JTextField txtAceObsFinalizado;
    private javax.swing.JTextField txtAceObsFinalizadoRap;
    private javax.swing.JTextField txtAcePessoa;
    private javax.swing.JTextField txtAcePessoaFinalizado;
    private javax.swing.JTextField txtAcePessoaFinalizadoRap;
    private javax.swing.JTextField txtAcePlaca;
    private javax.swing.JTextField txtAcePlacaFinalizado;
    private javax.swing.JTextField txtAcePlacaFinalizadoRap;
    private javax.swing.JLabel txtAceRapEntrada;
    private javax.swing.JLabel txtAceRapSaida;
    private javax.swing.JFormattedTextField txtPesquisaRapData;
    private javax.swing.JTextField txtPesquisaRapEmpresa;
    private javax.swing.JTextField txtPesquisaRapNome;
    private javax.swing.JTextField txtPesquisaRapPlaca;
    // End of variables declaration//GEN-END:variables
}
