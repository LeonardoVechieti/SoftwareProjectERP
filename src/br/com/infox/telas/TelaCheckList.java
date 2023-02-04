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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Leonardo
 */
public class TelaCheckList extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaIntegracao
     */
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    String idemp;
    private String dataInsert="1000/10/10"; 
    
    public TelaCheckList() {
        initComponents();
        conexao = ModuloConexao.conector();
        
        ((DefaultTableModel) tblPessoas.getModel()).setRowCount(0);
        ((DefaultTableModel) tblVeiculos.getModel()).setRowCount(0);
        ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
        
        //codigo dexa selecionado item1 e item2
        cbo1.setSelected(true);
        cbo2.setSelected(true);
        //limpa dados de geracao e data
        lblIdCheckList.setText(null);
        lblData.setText(null);
        
        //inicia a consulta do destino setado (padrao1)
        
        consultar_destino();
       
    }
    
    private void adicionar_pessoa(){
        
        if(txtPesId.getText().isEmpty()){
            
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
                    btnAdicionarPessoas.setEnabled(false);
                    //Limpa as Caixas
                    //limpar();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
            
        }else{
            verifica_cpf_ja_cadastrado(idemp);
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
    
     private void verifica_cpf_ja_cadastrado(String cfp){
        
        String sql ="select idpes, nome from pessoas where cpf=? "; //busca no banco o ultimo id criado
        try {
            pst=conexao.prepareStatement(sql);
            
            pst.setString(1, cfp);
            rs=pst.executeQuery();
            if (rs.next()){
                
                JOptionPane.showMessageDialog(null, "CPF já cadastrado no sistema para a pessoa ("+ rs.getString(2) + "). " + "Id do cadastro: " + rs.getString(1) + "!");
                txtPesCpf.setText(null);
        }
        } catch (Exception e) {
            
        }
        
        
    }
    
    private void adicionarVeiculo(){
        
        if(txtPesId.getText().isEmpty()){
            adicionar_pessoa();
        }
        if(txtEmpId.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Transportadora não selecionada, favor selecione!");
        }
        
        String sql="insert into veiculos (placa, placaCarreta, placaCarreta2, placaCarreta3, tipoVei, descricao, idpes, idemp, uf) values(?,?,?,?,?,?,?,?,?)";
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
    private void pesquisar_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ from empresas where tipo='Transportadora' and nome like ?";
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
    
    //metodo para setar os campos do formulario com o conteudo da tabela
    
    public void setar_campos_empresa(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
        txtEmpCnpj.setText(tblEmpresas.getModel().getValueAt(setar,2).toString());
        
       
    }
    
        private void pesquisar_pessoa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idpes as ID, nome as Nome, cpf as CPF from pessoas where nome like ?";
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
    
    //metodo para setar os campos do formulario com o conteudo da tabela
    
    public void setar_campos_pessoa(){
        int setar = tblPessoas.getSelectedRow();
        txtPesId.setText(tblPessoas.getModel().getValueAt(setar,0).toString());
        txtPesNome.setText(tblPessoas.getModel().getValueAt(setar,1).toString());
        txtPesCpf.setText(tblPessoas.getModel().getValueAt(setar,2).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionarPessoas.setEnabled(false); 
        txtPesNome.setEditable(false);
        txtPesCpf.setEditable(false);
        cboPesTipo.setEditable(false);
        txtPesId.setEditable(false);
        //seta o restante dos campos
        setar_campos_restantes_pessoas();
        
    }
     private void setar_campos_restantes_pessoas(){
        //Codigo abaixo seta o restante dos dados
        String idpes=txtPesId.getText();
        String sql="select cnh, cat, tipo from pessoas where idpes="+ idpes;
        
       
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                
                txtPesCnh.setText(rs.getString(1));
                cboPesCat.setSelectedItem(rs.getString(2).toString());
                cboPesTipo.setSelectedItem(rs.getString(3).toString());
                txtPesCnh.setEditable(false);
                cboPesCat.setEditable(false);
                cboPesTipo.setEditable(false);
  
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    //select idvei as ID, placa as Placa, placaCavalo as Placa2, tipoVei as Tipo, descricao as Desc from veiculos where placa like ?
    private void pesquisar_veiculos(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select v.idvei as ID, v.placa as Placa, p.nome as Motorista from veiculos v\n" +
        "join pessoas p\n" +
        "on v.idpes = p.idpes\n" +
        "where v.placa like ?";
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
        //a linha abixo desabilita o botão adicionar
        btnAdicionarVeiculo.setEnabled(false); 
        txtVeiId.setEditable(false);
        txtVeiPlaca.setEditable(false);
        //busca e seta os campos associados ao cadastro do veiculo
        setar_campos_restantes_veiculos();

    }
    private void setar_campos_restantes_veiculos(){
        //Codigo abaixo seta o restante dos dados
        String idvei=txtVeiId.getText();
        String sql="select v.placa, v.placaCarreta, v.placaCarreta2, v.placaCarreta3, v.descricao, v.tipoVei, p.idpes, p.nome, p.cpf, p.cnh, p.cat, p.tipo, e.nome, e.cnpj, e.idemp from veiculos v\n" +
        "join pessoas p\n" +
        "on v.idpes = p.idpes\n" +
        "join empresas e\n" +
        "on v.idemp= e.idemp\n" +
        "where v.idvei="+ idvei;
        
       
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                
                
                txtVeiPlaca.setText(rs.getString(1));
                txtVeiPlacaCarreta.setText(rs.getString(2));
                txtVeiPlacaCarreta1.setText(rs.getString(3));
                txtVeiPlacaCarreta2.setText(rs.getString(4));
                txtVeiDes.setText(rs.getString(5));
                cboVeiTipo.setSelectedItem(rs.getString(6));
                txtPesId.setText(rs.getString(7));
                txtPesNome.setText(rs.getString(8));
                txtPesCpf.setText(rs.getString(9));
                txtPesCnh.setText(rs.getString(10));
                cboPesCat.setSelectedItem(rs.getString(11));
                cboPesTipo.setSelectedItem(rs.getString(12));
                txtEmpNome.setText(rs.getString(13));
                txtEmpCnpj.setText(rs.getString(14));
                txtEmpId.setText(rs.getString(15));
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void pesquisa_rapida_completa_por_placa(){
        //Codigo abaixo seta o restante dos dados
        String placa=txtVeiPlaca.getText();
        String sql="select v.placa, v.placaCarreta, v.placaCarreta2, v.placaCarreta3, v.descricao, v.tipoVei, p.idpes, p.nome, p.cpf, p.cnh, p.cat, p.tipo, e.nome, e.cnpj, e.idemp, v.idvei from veiculos v\n" +
        "join pessoas p\n" +
        "on v.idpes = p.idpes\n" +
        "join empresas e\n" +
        "on v.idemp= e.idemp\n" +
        "where v.placa=?";
        
       
        try {
            pst=conexao.prepareStatement(sql);
            
            pst.setString(1, placa);
            
            rs=pst.executeQuery();
            
            if (rs.next()) {
                
                
                txtVeiPlaca.setText(rs.getString(1));
                txtVeiPlacaCarreta.setText(rs.getString(2));
                txtVeiPlacaCarreta1.setText(rs.getString(3));
                txtVeiPlacaCarreta2.setText(rs.getString(4));
                txtVeiDes.setText(rs.getString(5));
                cboVeiTipo.setSelectedItem(rs.getString(6));
                txtPesId.setText(rs.getString(7));
                txtPesNome.setText(rs.getString(8));
                txtPesCpf.setText(rs.getString(9));
                txtPesCnh.setText(rs.getString(10));
                cboPesCat.setSelectedItem(rs.getString(11));
                cboPesTipo.setSelectedItem(rs.getString(12));
                txtEmpNome.setText(rs.getString(13));
                txtEmpCnpj.setText(rs.getString(14));
                txtEmpId.setText(rs.getString(15));
                txtVeiId.setText(rs.getString(16));
                
                btnAdicionarVeiculo.setEnabled(false);
                btnAdicionarPessoas.setEnabled(false);
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void criar_checklist(){
         String sql="insert into checklist (statusCheck, iddestino, idempresa, idusuario, idemp, idvei, lacres, item1, obs1, item2, obs2, item3, obs3, item4, obs4, item5, obs5, item6, obs6, item7, obs7, item8, obs8, item9, obs9, item10, obs10, carga1, nota1, regimelimpeza1, carga2, nota2, regimelimpeza2, carga3, nota3, regimelimpeza3, notacarga, idpes) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            
            pst=conexao.prepareStatement(sql);
            String status="1";      
            pst.setString(1, status);
            pst.setString(2, txtIdDestino.getText());
            pst.setString(3, TelaPrincipal.lblEmpId.getText());   //pega o id da tela pricipal
            pst.setString(4, TelaPrincipal.lblUsuarioId.getText());   //pega o id da tela pricipal
            
            pst.setString(5, txtEmpId.getText());
            pst.setString(6, txtVeiId.getText());
            pst.setString(7, txtLacres.getText());
            
            String item1;
            if(cbo1.isSelected()){
                    item1="1";      
                 } else{
                    item1="0";
                 } 
            pst.setString(8, item1);
            pst.setString(9, txtObs1.getText());
            
            String item2;
            if(cbo2.isSelected()){
                    item2="1";      
                 } else{
                    item2="0";
                 } 
            pst.setString(10, item2);
            pst.setString(11, txtObs2.getText());
            
            String item3;
            if(cbo3.isSelected()){
                    item3="1";      
                 } else{
                    item3="0";
                 } 
            pst.setString(12, item3);
            pst.setString(13, txtObs3.getText());
            
            String item4;
            if(cbo4.isSelected()){
                    item4="1";      
                 } else{
                    item4="0";
                 } 
            pst.setString(14, item4);
            pst.setString(15, txtObs4.getText());
            
            String item5;
            if(cbo5.isSelected()){
                    item5="1";      
                 } else{
                    item5="0";
                 } 
            pst.setString(16, item5);
            pst.setString(17, txtObs5.getText());
            
            String item6;
            if(cbo6.isSelected()){
                    item6="1";      
                 } else{
                    item6="0";
                 } 
            pst.setString(18, item6);
            pst.setString(19, txtObs6.getText());
            
            String item7;
            if(cbo7.isSelected()){
                    item7="1";      
                 } else{
                    item7="0";
                 } 
            pst.setString(20, item7);
            pst.setString(21, txtObs7.getText());
            
            String item8;
            if(cbo8.isSelected()){
                    item8="1";      
                 } else{
                    item8="0";
                 } 
            pst.setString(22, item8);
            pst.setString(23, txtObs8.getText());
            
            String item9;
            if(cbo9.isSelected()){
                    item9="1";      
                 } else{
                    item9="0";
                 } 
            pst.setString(24, item9);
            pst.setString(25, txtObs9.getText());
            
            String item10;
            if(cbo10.isSelected()){
                    item10="1";      
                 } else{
                    item10="0";
                 } 
            pst.setString(26, item10);
            pst.setString(27, txtObs10.getText());
            
            pst.setString(28, txtCarga1.getText()); 
            pst.setString(29, txtNota1.getText());  
            pst.setString(30, txtLimpeza1.getText());  
            
            pst.setString(31, txtCarga2.getText()); 
            pst.setString(32, txtNota2.getText());  
            pst.setString(33, txtLimpeza2.getText());  
            
            pst.setString(34, txtCarga3.getText()); 
            pst.setString(35, txtNota3.getText());  
            pst.setString(36, txtLimpeza3.getText());  
            pst.setString(37, txtNotaCarga.getText());  
            
            pst.setString(38, txtPesId.getText());
           
            
            //validacão do campos
            if ((txtVeiId.getText().isEmpty()) || (txtPesId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "CheckList gerado com sucesso!");
                    lblIdCheckList.setText(recuperaIdCheckList());
                    lblStatus.setText("Aberto");
                    btnGerarCheckList.setText("Finalizar");
                    imprime_checklist();
                    //btnAdicionarPessoas.setEnabled(false);

                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private String recuperaIdCheckList(){
        String sql ="select max(id) from checklist"; //busca no banco o ultimo id criado
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
    
    
    
    private void alterar_checklist(){
         String sql="update checklist set statusCheck=?, iddestino=?, idempresa=?, idusuario=?, idemp=?, idvei=?, lacres=?, item1=?, obs1=?, item2=?, obs2=?, item3=?, obs3=?, item4=?, obs4=?, item5=?, obs5=?, item6=?, obs6=?, item7=?, obs7=?, item8=?, obs8=?, item9=?, obs9=?, item10=?, obs10=?, carga1=?, nota1=?, regimelimpeza1=?, carga2=?, nota2=?, regimelimpeza2=?, carga3=?, nota3=?, regimelimpeza3=?, notacarga=?, idpes=? where id=?";
        try {
            
            pst=conexao.prepareStatement(sql);
            String status="1";
            if(lblStatus.equals("Aberto")){
                status="1";
            }else{
                status="0";
            }
                  
            pst.setString(1, status);
            pst.setString(2, txtIdDestino.getText());
            pst.setString(3, TelaPrincipal.lblEmpId.getText());   //pega o id da tela pricipal
            pst.setString(4, TelaPrincipal.lblUsuarioId.getText());   //pega o id da tela pricipal
            
            pst.setString(5, txtEmpId.getText());
            pst.setString(6, txtVeiId.getText());
            pst.setString(7, txtLacres.getText());
            
            String item1;
            if(cbo1.isSelected()){
                    item1="1";      
                 } else{
                    item1="0";
                 } 
            pst.setString(8, item1);
            pst.setString(9, txtObs1.getText());
            
            String item2;
            if(cbo2.isSelected()){
                    item2="1";      
                 } else{
                    item2="0";
                 } 
            pst.setString(10, item2);
            pst.setString(11, txtObs2.getText());
            
            String item3;
            if(cbo3.isSelected()){
                    item3="1";      
                 } else{
                    item3="0";
                 } 
            pst.setString(12, item3);
            pst.setString(13, txtObs3.getText());
            
            String item4;
            if(cbo4.isSelected()){
                    item4="1";      
                 } else{
                    item4="0";
                 } 
            pst.setString(14, item4);
            pst.setString(15, txtObs4.getText());
            
            String item5;
            if(cbo5.isSelected()){
                    item5="1";      
                 } else{
                    item5="0";
                 } 
            pst.setString(16, item5);
            pst.setString(17, txtObs5.getText());
            
            String item6;
            if(cbo6.isSelected()){
                    item6="1";      
                 } else{
                    item6="0";
                 } 
            pst.setString(18, item6);
            pst.setString(19, txtObs6.getText());
            
            String item7;
            if(cbo7.isSelected()){
                    item7="1";      
                 } else{
                    item7="0";
                 } 
            pst.setString(20, item7);
            pst.setString(21, txtObs7.getText());
            
            String item8;
            if(cbo8.isSelected()){
                    item8="1";      
                 } else{
                    item8="0";
                 } 
            pst.setString(22, item8);
            pst.setString(23, txtObs8.getText());
            
            String item9;
            if(cbo9.isSelected()){
                    item9="1";      
                 } else{
                    item9="0";
                 } 
            pst.setString(24, item9);
            pst.setString(25, txtObs9.getText());
            
            String item10;
            if(cbo10.isSelected()){
                    item10="1";      
                 } else{
                    item10="0";
                 } 
            pst.setString(26, item10);
            pst.setString(27, txtObs10.getText());
            
            pst.setString(28, txtCarga1.getText()); 
            pst.setString(29, txtNota1.getText());  
            pst.setString(30, txtLimpeza1.getText());  
            
            pst.setString(31, txtCarga2.getText()); 
            pst.setString(32, txtNota2.getText());  
            pst.setString(33, txtLimpeza2.getText());  
            
            pst.setString(34, txtCarga3.getText()); 
            pst.setString(35, txtNota3.getText());  
            pst.setString(36, txtLimpeza3.getText());  
            pst.setString(37, txtNotaCarga.getText());  
            
            pst.setString(38, txtPesId.getText());
            pst.setString(39, lblIdCheckList.getText());
           
            
            //validacão do campos
            if ((txtVeiId.getText().isEmpty()) || (txtPesId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
                    
                    btnGerarCheckList.setText("Finalizar");
                    //imprime_checklist();
                    //btnAdicionarPessoas.setEnabled(false);

                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void finalizar_checklist(){
         String sql="update checklist set statusCheck=?, iddestino=?, idempresa=?, idusuario=?, idemp=?, idvei=?, lacres=?, item1=?, obs1=?, item2=?, obs2=?, item3=?, obs3=?, item4=?, obs4=?, item5=?, obs5=?, item6=?, obs6=?, item7=?, obs7=?, item8=?, obs8=?, item9=?, obs9=?, item10=?, obs10=?, carga1=?, nota1=?, regimelimpeza1=?, carga2=?, nota2=?, regimelimpeza2=?, carga3=?, nota3=?, regimelimpeza3=?, notacarga=?, idpes=? where id=?";
        try {
            
            pst=conexao.prepareStatement(sql);
            String status="0";      
            pst.setString(1, status);
            pst.setString(2, txtIdDestino.getText());
            pst.setString(3, TelaPrincipal.lblEmpId.getText());   //pega o id da tela pricipal
            pst.setString(4, TelaPrincipal.lblUsuarioId.getText());   //pega o id da tela pricipal
            
            pst.setString(5, txtEmpId.getText());
            pst.setString(6, txtVeiId.getText());
            pst.setString(7, txtLacres.getText());
            
            String item1;
            if(cbo1.isSelected()){
                    item1="1";      
                 } else{
                    item1="0";
                 } 
            pst.setString(8, item1);
            pst.setString(9, txtObs1.getText());
            
            String item2;
            if(cbo2.isSelected()){
                    item2="1";      
                 } else{
                    item2="0";
                 } 
            pst.setString(10, item2);
            pst.setString(11, txtObs2.getText());
            
            String item3;
            if(cbo3.isSelected()){
                    item3="1";      
                 } else{
                    item3="0";
                 } 
            pst.setString(12, item3);
            pst.setString(13, txtObs3.getText());
            
            String item4;
            if(cbo4.isSelected()){
                    item4="1";      
                 } else{
                    item4="0";
                 } 
            pst.setString(14, item4);
            pst.setString(15, txtObs4.getText());
            
            String item5;
            if(cbo5.isSelected()){
                    item5="1";      
                 } else{
                    item5="0";
                 } 
            pst.setString(16, item5);
            pst.setString(17, txtObs5.getText());
            
            String item6;
            if(cbo6.isSelected()){
                    item6="1";      
                 } else{
                    item6="0";
                 } 
            pst.setString(18, item6);
            pst.setString(19, txtObs6.getText());
            
            String item7;
            if(cbo7.isSelected()){
                    item7="1";      
                 } else{
                    item7="0";
                 } 
            pst.setString(20, item7);
            pst.setString(21, txtObs7.getText());
            
            String item8;
            if(cbo8.isSelected()){
                    item8="1";      
                 } else{
                    item8="0";
                 } 
            pst.setString(22, item8);
            pst.setString(23, txtObs8.getText());
            
            String item9;
            if(cbo9.isSelected()){
                    item9="1";      
                 } else{
                    item9="0";
                 } 
            pst.setString(24, item9);
            pst.setString(25, txtObs9.getText());
            
            String item10;
            if(cbo10.isSelected()){
                    item10="1";      
                 } else{
                    item10="0";
                 } 
            pst.setString(26, item10);
            pst.setString(27, txtObs10.getText());
            
            pst.setString(28, txtCarga1.getText()); 
            pst.setString(29, txtNota1.getText());  
            pst.setString(30, txtLimpeza1.getText());  
            
            pst.setString(31, txtCarga2.getText()); 
            pst.setString(32, txtNota2.getText());  
            pst.setString(33, txtLimpeza2.getText());  
            
            pst.setString(34, txtCarga3.getText()); 
            pst.setString(35, txtNota3.getText());  
            pst.setString(36, txtLimpeza3.getText());  
            pst.setString(37, txtNotaCarga.getText());  
            
            pst.setString(38, txtPesId.getText());
            pst.setString(39, lblIdCheckList.getText());
           
            
            //validacão do campos
            if ((lblIdCheckList.getText().isEmpty()) || (txtPesId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    //JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
                    
                    btnGerarCheckList.setText("Finalizar");
                    lblStatus.setText("Finalizado");
                    imprime_checklist_finalizado();
                    //btnAdicionarPessoas.setEnabled(false);

                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void pesquisarCheckList(){
        
        //limpa os campos para os novos dados localizados
        limpar();
        
        //a linha abaixo cria uma caixa de entrada do tipo JOption Pane
        
        String id=JOptionPane.showInputDialog("Número do CheckList");
        
        String sql="select c.id,DATE_FORMAT (c.datageracao,'%d/%m/%Y') as datag, v.placa, v.placaCarreta, v.placaCarreta2, v.placaCarreta3, v.uf, v.descricao, v.idvei, v.tipoVei,  p.nome as motorista, p.cpf, p.tipo, p.idpes, p.cnh, p.cat, e.idemp, e.cnpj, e.nome as transportadora, c.item1, c.item2, c.item3, c.item4, c.item5, c.item6, c.item7, c.item8, c.item9, c.item10, c.obs1, c.obs2, c.obs3, c.obs4, c.obs5, c.obs6, c.obs7, c.obs8, c.obs9, c.obs10, c.carga1, c.carga2, c.carga3, c.nota1, c.nota2, c.nota3, c.regimelimpeza1, c.regimelimpeza2, c.regimelimpeza3, c.lacres, c.notacarga, c.iddestino, c.idusuario, c.statuscheck from checklist c\n" +
"join destinos d\n" +
"on c.iddestino = d.id\n" +
"join pessoas p\n" +
"on c.idpes = p.idpes\n" +
"join empresas e\n" +
"on c.idemp= e.idemp\n" +
"join veiculos v \n" +
"on c.idvei= v.idvei\n" +
"where c.id=?" ;
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs=pst.executeQuery();
            
            if (rs.next()) {

                //dados do veiculo
                lblIdCheckList.setText(rs.getString(1));
                lblData.setText(rs.getString(2));
                txtVeiPlaca.setText(rs.getString(3));
                txtVeiPlacaCarreta.setText(rs.getString(4));
                txtVeiPlacaCarreta1.setText(rs.getString(5));
                txtVeiPlacaCarreta2.setText(rs.getString(6));
                txtVeiUf.setText(rs.getString(7));
                txtVeiDes.setText(rs.getString(8));
                txtVeiId.setText(rs.getString(9));
                cboVeiTipo.setSelectedItem(rs.getString(10));
                
                //dados da pessoa
                
                txtPesNome.setText(rs.getString(11));
                txtPesCpf.setText(rs.getString(12));
                cboPesTipo.setSelectedItem(rs.getString(13));
                txtPesId.setText(rs.getString(14));
                txtPesCnh.setText(rs.getString(15));
                cboPesCat.setSelectedItem(rs.getString(16));
                //txtPesCpf.setText(rs.getString(17));
                
                //dados da empresa
                
                txtEmpId.setText(rs.getString(17));
                txtEmpCnpj.setText(rs.getString(18));
                txtEmpNome.setText(rs.getString(19));
                
                
                //dados gerais do checklist
                
                String item1=rs.getString(20);
                if (item1.equals("1")) {
                    cbo1.setSelected(true);
                    
                } else {
                    cbo1.setSelected(false);
                }
                
                String item2=rs.getString(21);
                if (item1.equals("1")) {
                    cbo2.setSelected(true);
                    
                } else {
                    cbo2.setSelected(false);
                }
                
                String item3=rs.getString(22);
                if (item3.equals("1")) {
                    cbo3.setSelected(true);
                    
                } else {
                    cbo3.setSelected(false);
                }
                
                String item4=rs.getString(23);
                if (item4.equals("1")) {
                    cbo4.setSelected(true);
                    
                } else {
                    cbo4.setSelected(false);
                }
                
                String item5=rs.getString(24);
                if (item5.equals("1")) {
                    cbo5.setSelected(true);
                    
                } else {
                    cbo5.setSelected(false);
                }
                
                String item6=rs.getString(25);
                if (item6.equals("1")) {
                    cbo6.setSelected(true);
                    
                } else {
                    cbo6.setSelected(false);
                }
                
                String item7=rs.getString(26);
                if (item7.equals("1")) {
                    cbo7.setSelected(true);
                    
                } else {
                    cbo7.setSelected(false);
                }
                
                String item8=rs.getString(27);
                if (item8.equals("1")) {
                    cbo8.setSelected(true);
                    
                } else {
                    cbo8.setSelected(false);
                }
                
                String item9=rs.getString(28);
                if (item9.equals("1")) {
                    cbo9.setSelected(true);
                    
                } else {
                    cbo9.setSelected(false);
                }
                
                String item10=rs.getString(29);
                if (item10.equals("1")) {
                    cbo10.setSelected(true);
                    
                } else {
                    cbo10.setSelected(false);
                }
                
                txtObs1.setText(rs.getString(30));
                txtObs2.setText(rs.getString(31));
                txtObs3.setText(rs.getString(32));
                txtObs4.setText(rs.getString(33));
                txtObs5.setText(rs.getString(34));
                txtObs6.setText(rs.getString(35));
                txtObs7.setText(rs.getString(36));
                txtObs8.setText(rs.getString(37));
                txtObs9.setText(rs.getString(38));
                txtObs10.setText(rs.getString(39));
                
                txtCarga1.setText(rs.getString(40));
                txtCarga2.setText(rs.getString(41));
                txtCarga3.setText(rs.getString(42));
                
                txtNota1.setText(rs.getString(43));
                txtNota2.setText(rs.getString(44));
                txtNota3.setText(rs.getString(45));
                
                txtLimpeza1.setText(rs.getString(46));
                txtLimpeza2.setText(rs.getString(47));
                txtLimpeza3.setText(rs.getString(48));
                
                txtLacres.setText(rs.getString(49));
                txtNotaCarga.setText(rs.getString(50));
                txtIdDestino.setText(rs.getString(51));
                lblUser.setText(rs.getString(52));
                if(rs.getString(53).equals("1")){
                    lblStatus.setText("Aberto");
                }else{
                    lblStatus.setText("Finalizado");
                }
                
                //setando dados destino
                consultar_destino();
                
                //mudando botão para finalizar
                
                btnGerarCheckList.setText("Finalizar");
                //Desativando recursos

                //tblPessoas.setVisible(false);
                
                
            } else {
                JOptionPane.showMessageDialog(null, "CheckList não encontrado!");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "CheckList não encontrado!");
        }
        
    }
    
    public void setarCheckList(String id){

        String sql="select c.id,DATE_FORMAT (c.datageracao,'%d/%m/%Y') as datag, v.placa, v.placaCarreta, v.placaCarreta2, v.placaCarreta3, v.uf, v.descricao, v.idvei, v.tipoVei,  p.nome as motorista, p.cpf, p.tipo, p.idpes, p.cnh, p.cat, e.idemp, e.cnpj, e.nome as transportadora, c.item1, c.item2, c.item3, c.item4, c.item5, c.item6, c.item7, c.item8, c.item9, c.item10, c.obs1, c.obs2, c.obs3, c.obs4, c.obs5, c.obs6, c.obs7, c.obs8, c.obs9, c.obs10, c.carga1, c.carga2, c.carga3, c.nota1, c.nota2, c.nota3, c.regimelimpeza1, c.regimelimpeza2, c.regimelimpeza3, c.lacres, c.notacarga, c.iddestino, c.idusuario, c.statuscheck from checklist c\n" +
"join destinos d\n" +
"on c.iddestino = d.id\n" +
"join pessoas p\n" +
"on c.idpes = p.idpes\n" +
"join empresas e\n" +
"on c.idemp= e.idemp\n" +
"join veiculos v \n" +
"on c.idvei= v.idvei\n" +
"where c.id=?" ;
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                //dados do veiculo
                lblIdCheckList.setText(rs.getString(1));
                lblData.setText(rs.getString(2));
                txtVeiPlaca.setText(rs.getString(3));
                txtVeiPlacaCarreta.setText(rs.getString(4));
                txtVeiPlacaCarreta1.setText(rs.getString(5));
                txtVeiPlacaCarreta2.setText(rs.getString(6));
                txtVeiUf.setText(rs.getString(7));
                txtVeiDes.setText(rs.getString(8));
                txtVeiId.setText(rs.getString(9));
                cboVeiTipo.setSelectedItem(rs.getString(10));
                
                //dados da pessoa
                
                txtPesNome.setText(rs.getString(11));
                txtPesCpf.setText(rs.getString(12));
                cboPesTipo.setSelectedItem(rs.getString(13));
                txtPesId.setText(rs.getString(14));
                txtPesCnh.setText(rs.getString(15));
                cboPesCat.setSelectedItem(rs.getString(16));
                //txtPesCpf.setText(rs.getString(17));
                
                //dados da empresa
                
                txtEmpId.setText(rs.getString(17));
                txtEmpCnpj.setText(rs.getString(18));
                txtEmpNome.setText(rs.getString(19));
                
                
                //dados gerais do checklist
                
                String item1=rs.getString(20);
                if (item1.equals("1")) {
                    cbo1.setSelected(true);
                    
                } else {
                    cbo1.setSelected(false);
                }
                
                String item2=rs.getString(21);
                if (item1.equals("1")) {
                    cbo2.setSelected(true);
                    
                } else {
                    cbo2.setSelected(false);
                }
                
                String item3=rs.getString(22);
                if (item3.equals("1")) {
                    cbo3.setSelected(true);
                    
                } else {
                    cbo3.setSelected(false);
                }
                
                String item4=rs.getString(23);
                if (item4.equals("1")) {
                    cbo4.setSelected(true);
                    
                } else {
                    cbo4.setSelected(false);
                }
                
                String item5=rs.getString(24);
                if (item5.equals("1")) {
                    cbo5.setSelected(true);
                    
                } else {
                    cbo5.setSelected(false);
                }
                
                String item6=rs.getString(25);
                if (item6.equals("1")) {
                    cbo6.setSelected(true);
                    
                } else {
                    cbo6.setSelected(false);
                }
                
                String item7=rs.getString(26);
                if (item7.equals("1")) {
                    cbo7.setSelected(true);
                    
                } else {
                    cbo7.setSelected(false);
                }
                
                String item8=rs.getString(27);
                if (item8.equals("1")) {
                    cbo8.setSelected(true);
                    
                } else {
                    cbo8.setSelected(false);
                }
                
                String item9=rs.getString(28);
                if (item9.equals("1")) {
                    cbo9.setSelected(true);
                    
                } else {
                    cbo9.setSelected(false);
                }
                
                String item10=rs.getString(29);
                if (item10.equals("1")) {
                    cbo10.setSelected(true);
                    
                } else {
                    cbo10.setSelected(false);
                }
                
                txtObs1.setText(rs.getString(30));
                txtObs2.setText(rs.getString(31));
                txtObs3.setText(rs.getString(32));
                txtObs4.setText(rs.getString(33));
                txtObs5.setText(rs.getString(34));
                txtObs6.setText(rs.getString(35));
                txtObs7.setText(rs.getString(36));
                txtObs8.setText(rs.getString(37));
                txtObs9.setText(rs.getString(38));
                txtObs10.setText(rs.getString(39));
                
                txtCarga1.setText(rs.getString(40));
                txtCarga2.setText(rs.getString(41));
                txtCarga3.setText(rs.getString(42));
                
                txtNota1.setText(rs.getString(43));
                txtNota2.setText(rs.getString(44));
                txtNota3.setText(rs.getString(45));
                
                txtLimpeza1.setText(rs.getString(46));
                txtLimpeza2.setText(rs.getString(47));
                txtLimpeza3.setText(rs.getString(48));
                
                txtLacres.setText(rs.getString(49));
                txtNotaCarga.setText(rs.getString(50));
                txtIdDestino.setText(rs.getString(51));
                lblUser.setText(rs.getString(52));
                
                if(rs.getString(53).equals("1")){
                    lblStatus.setText("Aberto");
                }else{
                    lblStatus.setText("Finalizado");
                }
                
                
                
                //setando dados destino
                consultar_destino();
                
                //mudando botão para finalizar
                
                btnGerarCheckList.setText("Finalizar");
                //Desativando recursos
                btnAdicionarPessoas.setEnabled(false);
                btnAdicionarVeiculo.setEnabled(false);
                        
                //tblPessoas.setVisible(false);
                
                
            } else {
                JOptionPane.showMessageDialog(null, "CheckList não encontrado!");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "CheckList não encontrado!");
        }
        
    }
    
    private void botao_gera_finaliza(){
        
        if(lblIdCheckList.getText()==null) {
            criar_checklist();
        }else{
            finalizar_checklist();
        }
        
        
    }
    
    
    private void limpar(){
                lblIdCheckList.setText(null);
                lblData.setText(null);
                txtVeiPlaca.setText(null);
                txtVeiPlacaCarreta.setText(null);
                txtVeiPlacaCarreta1.setText(null);
                txtVeiPlacaCarreta2.setText(null);
                txtVeiUf.setText(null);
                txtVeiDes.setText(null);
                txtVeiId.setText(null);
                cboVeiTipo.setSelectedItem(null);
                
                //dados da pessoa
                
                txtPesNome.setText(null);
                txtPesCpf.setText(null);
                cboPesTipo.setSelectedItem(null);
                txtPesId.setText(null);
                txtPesCnh.setText(null);
                cboPesCat.setSelectedItem(null);
                txtPesCpf.setText(null);
                
                //dados da empresa
                
                txtEmpId.setText(null);
                txtEmpCnpj.setText(null);
                txtEmpNome.setText(null);
                
                
                //dados gerais do checklist
                
        
                cbo1.setSelected(true);
                cbo2.setSelected(true);
                cbo3.setSelected(false);
                cbo4.setSelected(false);
                cbo5.setSelected(false);
                cbo6.setSelected(false);
                cbo7.setSelected(false);
                cbo8.setSelected(false);
                cbo9.setSelected(false);
                cbo10.setSelected(false);
                
                
                    
                
                
                txtObs1.setText(null);
                txtObs2.setText(null);
                txtObs3.setText(null);
                txtObs4.setText(null);
                txtObs5.setText(null);
                txtObs6.setText(null);
                txtObs7.setText(null);
                txtObs8.setText(null);
                txtObs9.setText(null);
                txtObs10.setText(null);
                
                txtCarga1.setText(null);
                txtCarga2.setText(null);
                txtCarga3.setText(null);
                
                txtNota1.setText(null);
                txtNota2.setText(null);
                txtNota3.setText(null);
                
                txtLimpeza1.setText(null);
                txtLimpeza2.setText(null);
                txtLimpeza3.setText(null);
                
                txtLacres.setText(null);
                txtNotaCarga.setText(null);
                txtIdDestino.setText("1");
                txtDestino.setText(null);
                txtDestinoCliente.setText(null);
                lblStatus.setText("Aberto");
        
                ((DefaultTableModel) tblPessoas.getModel()).setRowCount(0);
                ((DefaultTableModel) tblVeiculos.getModel()).setRowCount(0);
                ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
                consultar_destino();
                
                btnAdicionarPessoas.setEnabled(true);
                btnAdicionarVeiculo.setEnabled(true);
                btnGerarCheckList.setText("Gerar");
    }
    
   
    private void consultar_destino(){
        
        String sql = "select * from destinos where id = ?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtIdDestino.getText());
            rs=pst.executeQuery();
            if (rs.next()) {
                //txtCadOrigem.setText(rs.getString(2));
                //txtCadCnpj.setText(rs.getString(3));
                txtDestino.setText(rs.getString(4));
                txtDestinoCliente.setText(rs.getString(5));
                //String status=rs.getString(6);
                //if(status.equals("1")){
                    //lbnAtivo.setText("Ativo");
               // }else{
                   // lbnAtivo.setText("Inativo");
                //}
                
                
                
            } else {
                JOptionPane.showMessageDialog(null, "Destino não existente!");
                //as linhas abaixo limpam os campos
                                    
                   
                   
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
     
        private void imprime_checklist(){
        
                 // gera um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(lblIdCheckList.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/checklistfarelo.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                JasperPrint relatorioprint;        
                relatorioprint = JasperFillManager.fillReport(relatoriocompilado,filtro,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                JasperViewer.viewReport(relatorioprint,false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
        }
        
    }
    }                          
        
        private void imprime_checklist_finalizado(){
        
                 // gera um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(lblIdCheckList.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/checklistfarelo_finalizado.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                JasperPrint relatorioprint;        
                relatorioprint = JasperFillManager.fillReport(relatoriocompilado,filtro,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                JasperViewer.viewReport(relatorioprint,false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
        }
        
    }
    }               
        
    private void marcartodos(){
        
        cbo1.setSelected(true);
        cbo2.setSelected(true);
        cbo3.setSelected(true);
        cbo4.setSelected(true);
        cbo5.setSelected(true);
        cbo6.setSelected(true);
        cbo7.setSelected(true);
        cbo8.setSelected(true);
        cbo9.setSelected(true);
        cbo10.setSelected(true);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jDialog1 = new javax.swing.JDialog();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jFrame3 = new javax.swing.JFrame();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblVeiculos = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        txtVeiPesquisar = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtVeiPlaca = new javax.swing.JTextField();
        txtVeiPlacaCarreta = new javax.swing.JTextField();
        txtVeiPlacaCarreta1 = new javax.swing.JTextField();
        txtVeiPlacaCarreta2 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        cboVeiTipo = new javax.swing.JComboBox<>();
        txtVeiId = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        txtVeiDes = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtVeiUf = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPessoas = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        txtPesPesquisar = new javax.swing.JTextField();
        btnAdicionarPessoas = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtPesNome = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtPesCpf = new javax.swing.JFormattedTextField();
        jLabel31 = new javax.swing.JLabel();
        txtPesCnh = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        cboPesTipo = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        cboPesCat = new javax.swing.JComboBox<>();
        txtPesId = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        jLabel33 = new javax.swing.JLabel();
        txtEmpPesquisar = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtEmpCnpj = new javax.swing.JFormattedTextField();
        txtEmpNome = new javax.swing.JTextField();
        txtEmpId = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        cbo1 = new javax.swing.JCheckBox();
        cbo2 = new javax.swing.JCheckBox();
        cbo3 = new javax.swing.JCheckBox();
        cbo4 = new javax.swing.JCheckBox();
        cbo5 = new javax.swing.JCheckBox();
        cbo6 = new javax.swing.JCheckBox();
        cbo7 = new javax.swing.JCheckBox();
        cbo8 = new javax.swing.JCheckBox();
        cbo9 = new javax.swing.JCheckBox();
        cbo10 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        txtObs1 = new javax.swing.JTextField();
        txtObs2 = new javax.swing.JTextField();
        txtObs3 = new javax.swing.JTextField();
        txtObs4 = new javax.swing.JTextField();
        txtObs5 = new javax.swing.JTextField();
        txtObs6 = new javax.swing.JTextField();
        txtObs7 = new javax.swing.JTextField();
        txtObs8 = new javax.swing.JTextField();
        txtObs9 = new javax.swing.JTextField();
        txtObs10 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCarga1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNota1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLimpeza1 = new javax.swing.JTextField();
        txtCarga2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNota2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtLimpeza2 = new javax.swing.JTextField();
        txtCarga3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNota3 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtLimpeza3 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtLacres = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtNotaCarga = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtIdDestino = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtDestinoCliente = new javax.swing.JTextField();
        btnGerarCheckList = new javax.swing.JButton();
        btnAdicionarVeiculo = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblIdCheckList = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame3Layout = new javax.swing.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame3Layout.setVerticalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Check List de Inspecão - Carregamento de Farelo");
        setVisible(true);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblVeiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Placa", "Motorista"
            }
        ));
        tblVeiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVeiculosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblVeiculos);

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel25.setText("Veículo");

        txtVeiPesquisar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtVeiPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVeiPesquisarActionPerformed(evt);
            }
        });
        txtVeiPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVeiPesquisarKeyReleased(evt);
            }
        });

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar.png"))); // NOI18N
        jLabel60.setText("Placa ");

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

        jLabel63.setText("Tipo de Véiculo");

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

        txtVeiId.setEditable(false);

        jLabel64.setText("ID");

        txtVeiDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiDesKeyPressed(evt);
            }
        });

        jLabel62.setText("Descrição do Veículo");

        jLabel13.setText("UF:");

        txtVeiUf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVeiUfKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVeiUf, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtVeiDes, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel64))
                            .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtVeiPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(txtVeiPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlacaCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlacaCarreta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVeiPlacaCarreta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVeiId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel64)
                            .addComponent(jLabel62)
                            .addComponent(txtVeiDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel63)
                                .addComponent(cboVeiTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(txtVeiUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPessoas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF"
            }
        ));
        tblPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPessoasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblPessoas);

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel26.setText("Motorista");

        txtPesPesquisar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPesPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyReleased(evt);
            }
        });

        btnAdicionarPessoas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/adicionar2.png"))); // NOI18N
        btnAdicionarPessoas.setToolTipText("Adicionar");
        btnAdicionarPessoas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarPessoasActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/alterar.png"))); // NOI18N
        jButton6.setToolTipText("Alterar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel27.setText("Nome do Motorista");

        txtPesNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesNomeKeyPressed(evt);
            }
        });

        jLabel28.setText("CPF");

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesCpfKeyTyped(evt);
            }
        });

        jLabel31.setText("CNH");

        txtPesCnh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCnhKeyPressed(evt);
            }
        });

        jLabel29.setText("Tipo");

        cboPesTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Motorista", "Visitante", "Terceiro", "Funcionario" }));
        cboPesTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesTipoKeyPressed(evt);
            }
        });

        jLabel32.setText("Cat");

        cboPesCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AE", "E", "AD", "D", "AC", "C", "AB", "B", "A" }));

        txtPesId.setEditable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(23, 23, 23)
                        .addComponent(txtPesPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPesNome))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(35, 35, 35))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionarPessoas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addGap(22, 22, 22))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel31)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel32)
                                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdicionarPessoas))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jButton6))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Transportadora", "CNPJ"
            }
        ));
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblEmpresas);

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel33.setText("Transportadora");

        txtEmpPesquisar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEmpPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyReleased(evt);
            }
        });

        jLabel34.setText("*Razão");

        jLabel35.setText("*CNPJ");

        txtEmpCnpj.setEditable(false);
        try {
            txtEmpCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtEmpNome.setEditable(false);
        txtEmpNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNomeKeyPressed(evt);
            }
        });

        txtEmpId.setEditable(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(23, 23, 23)
                        .addComponent(txtEmpPesquisar))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dados Gerais", jPanel5);

        cbo1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo1.setText("1 - EPIs");
        cbo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo1ActionPerformed(evt);
            }
        });

        cbo2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo2.setText("2 - Tacógrafo e documentos ");
        cbo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo2ActionPerformed(evt);
            }
        });

        cbo3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo3.setText("3 - Compartimento de carga limpo, sem odores, pragas ou resto de carga anterior");
        cbo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo3ActionPerformed(evt);
            }
        });

        cbo4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo4.setText("4 - Compartimento de carga sem umidade");
        cbo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo4ActionPerformed(evt);
            }
        });

        cbo5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo5.setText("5 - Tampas do compartimento de carga com boa vedação");
        cbo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo5ActionPerformed(evt);
            }
        });

        cbo6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo6.setText("6 - Compartimento de carga sem furos ou danificado");
        cbo6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo6ActionPerformed(evt);
            }
        });

        cbo7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo7.setText("7 - Lona limpa, seca, sem furos e em boas condições");
        cbo7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo7ActionPerformed(evt);
            }
        });

        cbo8.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo8.setText("8 - Pneus em bom estado");
        cbo8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo8ActionPerformed(evt);
            }
        });

        cbo9.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo9.setText("9 - Não possui vazamento de diesel e óleos lubrificantes");
        cbo9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo9ActionPerformed(evt);
            }
        });

        cbo10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbo10.setText("10 - Condições do Pré Cinto em bom estado");
        cbo10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo10ActionPerformed(evt);
            }
        });

        jLabel1.setText("Obs.: O não cumprimento de qualquer dos itens acima impossibilita o carregamento");

        txtObs1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs1KeyPressed(evt);
            }
        });

        txtObs2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs2KeyPressed(evt);
            }
        });

        txtObs3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs3KeyPressed(evt);
            }
        });

        txtObs4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs4KeyPressed(evt);
            }
        });

        txtObs5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs5KeyPressed(evt);
            }
        });

        txtObs6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs6KeyPressed(evt);
            }
        });

        txtObs7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs7KeyPressed(evt);
            }
        });

        txtObs8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs8KeyPressed(evt);
            }
        });

        txtObs9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObs9KeyPressed(evt);
            }
        });

        jButton2.setText("Marcar Todos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo6)
                                .addGap(18, 18, 18)
                                .addComponent(txtObs6, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo4)
                                .addGap(18, 18, 18)
                                .addComponent(txtObs4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtObs1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtObs2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo8)
                                .addGap(18, 18, 18)
                                .addComponent(txtObs8, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo9)
                                .addGap(18, 18, 18)
                                .addComponent(txtObs9, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo10)
                                .addGap(18, 18, 18)
                                .addComponent(txtObs10, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtObs3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtObs5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbo7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtObs7, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addGap(25, 25, 25))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo1)
                    .addComponent(txtObs1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo2)
                    .addComponent(txtObs2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo3)
                    .addComponent(txtObs3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo4)
                    .addComponent(txtObs4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo5)
                    .addComponent(txtObs5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo6)
                    .addComponent(txtObs6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo7)
                    .addComponent(txtObs7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo8)
                    .addComponent(txtObs8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo9)
                    .addComponent(txtObs9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo10)
                    .addComponent(txtObs10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CheckList", jPanel8);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Carga 1");

        jLabel3.setText("Carga 2");

        jLabel4.setText("Carga 3");

        txtCarga1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCarga1KeyPressed(evt);
            }
        });

        jLabel5.setText("IDTF");

        txtNota1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNota1KeyPressed(evt);
            }
        });

        jLabel6.setText("Regime de Limpeza");

        txtLimpeza1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLimpeza1KeyPressed(evt);
            }
        });

        txtCarga2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCarga2KeyPressed(evt);
            }
        });

        jLabel7.setText("IDTF");

        txtNota2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNota2KeyPressed(evt);
            }
        });

        jLabel8.setText("Regime de Limpeza");

        txtLimpeza2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLimpeza2KeyPressed(evt);
            }
        });

        txtCarga3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCarga3KeyPressed(evt);
            }
        });

        jLabel9.setText("IDTF");

        txtNota3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNota3KeyPressed(evt);
            }
        });

        jLabel10.setText("Regime de Limpeza");

        txtLimpeza3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLimpeza3KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtCarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(txtNota2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addComponent(jLabel8)
                            .addGap(18, 18, 18)
                            .addComponent(txtLimpeza2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtCarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)
                            .addComponent(txtNota1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addComponent(jLabel6)
                            .addGap(18, 18, 18)
                            .addComponent(txtLimpeza1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCarga3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtNota3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtLimpeza3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtNota1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtLimpeza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtNota2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtLimpeza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLimpeza3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtNota3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtCarga3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(65, 65, 65))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel23.setText("Lacres:");

        txtLacres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLacresKeyPressed(evt);
            }
        });

        jLabel24.setText("Nota da Carga:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(txtNotaCarga, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(txtLacres, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtLacres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtNotaCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel36.setText("Destino");

        txtIdDestino.setText("1");
        txtIdDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdDestinoKeyPressed(evt);
            }
        });

        txtDestino.setEditable(false);

        jLabel37.setText("Cliente");

        txtDestinoCliente.setEditable(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addComponent(txtIdDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel37)
                .addGap(28, 28, 28)
                .addComponent(txtDestinoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(txtIdDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(txtDestinoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dados Complementares", jPanel1);

        btnGerarCheckList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnGerarCheckList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/1055094_check_select_icon.png"))); // NOI18N
        btnGerarCheckList.setText("Gerar");
        btnGerarCheckList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarCheckListActionPerformed(evt);
            }
        });

        btnAdicionarVeiculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionarVeiculo.setToolTipText("Cadastrar Veículo");
        btnAdicionarVeiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarVeiculoActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        jButton4.setToolTipText("Alterar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar48.png"))); // NOI18N
        jButton1.setToolTipText("Pesquisar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/limpar48.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/printtt.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("CheckList:");

        lblIdCheckList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIdCheckList.setText("0");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Data:");

        lblData.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblData.setText("  /  /   ");

        lblUser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUser.setText("-");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("User:");

        lblStatus.setText("Aberto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblIdCheckList, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblData)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblStatus))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(lblIdCheckList)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lblData))
                    .addComponent(lblStatus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAdicionarVeiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnGerarCheckList)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionarVeiculo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGerarCheckList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        setBounds(0, 0, 900, 593);
    }// </editor-fold>//GEN-END:initComponents

    private void cbo10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo10ActionPerformed

    private void cbo9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo9ActionPerformed

    private void cbo8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo8ActionPerformed

    private void cbo7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo7ActionPerformed

    private void cbo6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo6ActionPerformed

    private void cbo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo5ActionPerformed

    private void cbo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo4ActionPerformed

    private void cbo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo3ActionPerformed

    private void cbo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo2ActionPerformed

    private void cbo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo1ActionPerformed

    private void txtEmpNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNomeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtEmpCnpj.requestFocus();
        }
    }//GEN-LAST:event_txtEmpNomeKeyPressed

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

    private void txtPesCpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtPesCnh.requestFocus();
        }
    }//GEN-LAST:event_txtPesCpfKeyPressed

    private void txtPesCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesCpfActionPerformed

    private void txtPesNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesNomeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtPesCpf.requestFocus();
        }
    }//GEN-LAST:event_txtPesNomeKeyPressed

    private void cboVeiTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboVeiTipoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiPlacaCarreta.requestFocus();
        }
    }//GEN-LAST:event_cboVeiTipoKeyPressed

    private void cboVeiTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVeiTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboVeiTipoActionPerformed

    private void txtVeiPlacaCarreta2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta2KeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta2.getText();
        txtVeiPlacaCarreta2.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarreta2KeyTyped

    private void txtVeiPlacaCarreta2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta2KeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiDes.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaCarreta2KeyPressed

    private void txtVeiPlacaCarreta1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta1KeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta1.getText();
        txtVeiPlacaCarreta1.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarreta1KeyTyped

    private void txtVeiPlacaCarreta1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarreta1KeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiPlacaCarreta2.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaCarreta1KeyPressed

    private void txtVeiPlacaCarretaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarretaKeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlacaCarreta.getText();
        txtVeiPlacaCarreta.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaCarretaKeyTyped

    private void txtVeiPlacaCarretaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaCarretaKeyPressed
        pesquisa_rapida_completa_por_placa();
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiPlacaCarreta1.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaCarretaKeyPressed

    private void txtVeiPlacaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaKeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtVeiPlaca.getText();
        txtVeiPlaca.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtVeiPlacaKeyTyped

    private void txtVeiPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPlacaKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiPlacaCarreta.requestFocus();
        }
    }//GEN-LAST:event_txtVeiPlacaKeyPressed

    private void txtVeiPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVeiPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVeiPesquisarActionPerformed

    private void txtVeiPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiPesquisarKeyReleased
       pesquisar_veiculos();
    }//GEN-LAST:event_txtVeiPesquisarKeyReleased

    private void tblVeiculosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVeiculosMouseClicked
        setar_campos_veiculos();
    }//GEN-LAST:event_tblVeiculosMouseClicked

    private void txtPesPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesPesquisarKeyReleased
        pesquisar_pessoa();
    }//GEN-LAST:event_txtPesPesquisarKeyReleased

    private void tblPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoasMouseClicked
        setar_campos_pessoa();
    }//GEN-LAST:event_tblPessoasMouseClicked

    private void btnAdicionarVeiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarVeiculoActionPerformed
        adicionarVeiculo();
    }//GEN-LAST:event_btnAdicionarVeiculoActionPerformed

    private void btnAdicionarPessoasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarPessoasActionPerformed
        adicionar_pessoa();
    }//GEN-LAST:event_btnAdicionarPessoasActionPerformed

    private void txtEmpPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyReleased
        pesquisar_empresa();
    }//GEN-LAST:event_txtEmpPesquisarKeyReleased

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
        setar_campos_empresa();
    }//GEN-LAST:event_tblEmpresasMouseClicked

    private void btnGerarCheckListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarCheckListActionPerformed
        botao_gera_finaliza();
    }//GEN-LAST:event_btnGerarCheckListActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       pesquisarCheckList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtIdDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdDestinoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             consultar_destino();
       }
    }//GEN-LAST:event_txtIdDestinoKeyPressed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtPesCpfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfKeyTyped
         verifica_cpf_ja_cadastrado(txtPesCpf.getText());
    }//GEN-LAST:event_txtPesCpfKeyTyped

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if(lblIdCheckList.getText()==null) {
            JOptionPane.showMessageDialog(null, "CheckList não gerado ou não selecionado!");
        }else{
            imprime_checklist();
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       alterar_checklist();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtVeiDesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiDesKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtVeiUf.requestFocus();
        }
    }//GEN-LAST:event_txtVeiDesKeyPressed

    private void txtVeiUfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeiUfKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtPesNome.requestFocus();
        }
    }//GEN-LAST:event_txtVeiUfKeyPressed

    private void txtObs1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs1KeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs2.requestFocus();
        }
    }//GEN-LAST:event_txtObs1KeyPressed

    private void txtObs2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs2KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs3.requestFocus();
        }
    }//GEN-LAST:event_txtObs2KeyPressed

    private void txtObs3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs3KeyPressed
               if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs4.requestFocus();
        }
    }//GEN-LAST:event_txtObs3KeyPressed

    private void txtObs4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs4KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs5.requestFocus();
        }
    }//GEN-LAST:event_txtObs4KeyPressed

    private void txtObs5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs5KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs6.requestFocus();
        }
    }//GEN-LAST:event_txtObs5KeyPressed

    private void txtObs6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs6KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs7.requestFocus();
        }
    }//GEN-LAST:event_txtObs6KeyPressed

    private void txtObs7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs7KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs8.requestFocus();
        }
    }//GEN-LAST:event_txtObs7KeyPressed

    private void txtObs8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs8KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs9.requestFocus();
        }
    }//GEN-LAST:event_txtObs8KeyPressed

    private void txtObs9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObs9KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtObs10.requestFocus();
        }
    }//GEN-LAST:event_txtObs9KeyPressed

    private void txtCarga1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarga1KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtNota1.requestFocus();
        }
    }//GEN-LAST:event_txtCarga1KeyPressed

    private void txtNota1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNota1KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtLimpeza1.requestFocus();
        }
    }//GEN-LAST:event_txtNota1KeyPressed

    private void txtLimpeza1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLimpeza1KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtCarga2.requestFocus();
        }
    }//GEN-LAST:event_txtLimpeza1KeyPressed

    private void txtCarga2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarga2KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtNota2.requestFocus();
        }
    }//GEN-LAST:event_txtCarga2KeyPressed

    private void txtNota2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNota2KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtLimpeza2.requestFocus();
        }
    }//GEN-LAST:event_txtNota2KeyPressed

    private void txtLimpeza2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLimpeza2KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtCarga3.requestFocus();
        }
    }//GEN-LAST:event_txtLimpeza2KeyPressed

    private void txtCarga3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarga3KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtNota3.requestFocus();
        }
    }//GEN-LAST:event_txtCarga3KeyPressed

    private void txtNota3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNota3KeyPressed
               if(evt.getKeyCode() == evt.VK_ENTER){
            txtLimpeza3.requestFocus();
        }
    }//GEN-LAST:event_txtNota3KeyPressed

    private void txtLimpeza3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLimpeza3KeyPressed
                if(evt.getKeyCode() == evt.VK_ENTER){
            txtLacres.requestFocus();
        }
    }//GEN-LAST:event_txtLimpeza3KeyPressed

    private void txtLacresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLacresKeyPressed
               if(evt.getKeyCode() == evt.VK_ENTER){
            txtNotaCarga.requestFocus();
        }
    }//GEN-LAST:event_txtLacresKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        marcartodos();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarPessoas;
    private javax.swing.JButton btnAdicionarVeiculo;
    private javax.swing.JButton btnGerarCheckList;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JCheckBox cbo1;
    private javax.swing.JCheckBox cbo10;
    private javax.swing.JCheckBox cbo2;
    private javax.swing.JCheckBox cbo3;
    private javax.swing.JCheckBox cbo4;
    private javax.swing.JCheckBox cbo5;
    private javax.swing.JCheckBox cbo6;
    private javax.swing.JCheckBox cbo7;
    private javax.swing.JCheckBox cbo8;
    private javax.swing.JCheckBox cbo9;
    private javax.swing.JComboBox<String> cboPesCat;
    private javax.swing.JComboBox<String> cboPesTipo;
    private javax.swing.JComboBox<String> cboVeiTipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblIdCheckList;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTable tblPessoas;
    private javax.swing.JTable tblVeiculos;
    private javax.swing.JTextField txtCarga1;
    private javax.swing.JTextField txtCarga2;
    private javax.swing.JTextField txtCarga3;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtDestinoCliente;
    private javax.swing.JFormattedTextField txtEmpCnpj;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpPesquisar;
    private javax.swing.JTextField txtIdDestino;
    private javax.swing.JTextField txtLacres;
    private javax.swing.JTextField txtLimpeza1;
    private javax.swing.JTextField txtLimpeza2;
    private javax.swing.JTextField txtLimpeza3;
    private javax.swing.JTextField txtNota1;
    private javax.swing.JTextField txtNota2;
    private javax.swing.JTextField txtNota3;
    private javax.swing.JTextField txtNotaCarga;
    private javax.swing.JTextField txtObs1;
    private javax.swing.JTextField txtObs10;
    private javax.swing.JTextField txtObs2;
    private javax.swing.JTextField txtObs3;
    private javax.swing.JTextField txtObs4;
    private javax.swing.JTextField txtObs5;
    private javax.swing.JTextField txtObs6;
    private javax.swing.JTextField txtObs7;
    private javax.swing.JTextField txtObs8;
    private javax.swing.JTextField txtObs9;
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
