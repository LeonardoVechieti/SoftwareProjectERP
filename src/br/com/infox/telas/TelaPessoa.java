/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author Leonardo
 */
public class TelaPessoa extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private String nr35;
    private String nr10;
    private String nr33;
    private String nr12;
    private String outroscertificados;
    private String statusInteg;
    private String dataInsert="1000/10/10";   
    
    
    public TelaPessoa() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void adicionar(){
        String sql="insert into pessoas (nome, cpf, cnh, cat, tipo, cidade, datacontratacao, listaequipamento, listaepi, nr35, nr10, nr33, nr12, outroscertificados, certificados, idemp, razao, iud) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtPesNome.getText());
            pst.setString(2, txtPesCpf.getText());
            pst.setString(3, txtPesCnh.getText());
            pst.setString(4, cboPesCat.getSelectedItem().toString());
            pst.setString(5, cboPesTipo.getSelectedItem().toString());
            pst.setString(6, txtPesCidade.getText());
            if(txtPesDataContratacao.getText().equals("  /  /    ")){
                txtPesDataContratacao.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtPesDataContratacao.getText(); //passa a String para a variavel string
            
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padão brasileiro
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato.format(dataFormatada));
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato2.format(dataFormatada)); 
           
            
            dataInsert=formato2.format(dataFormatada);
            
            pst.setString(7, dataInsert); //Passa a data do formato no formato2 americano
            pst.setString(8, txtPesListaEquipamento.getText());
            pst.setString(9, txtPesListaEpi.getText());
                if(cboPesNR35.isSelected()){
                    nr35="1";      
                 } else{
                    nr35="0";
                 }
            pst.setString(10, nr35);
                 if(cboPesNR10.isSelected()){
                    nr10="1";      
                 } else{
                    nr10="0";
                 }    
            pst.setString(11, nr10);
                if(cboPesNR33.isSelected()){
                    nr33="1";      
                 } else{
                    nr33="0";
                 }   
            pst.setString(12, nr33);
                 if(cboPesNR12.isSelected()){
                    nr12="1";      
                 } else{
                    nr12="0";
                 }
            pst.setString(13, nr12);
                 if(cboPesOutros.isSelected()){
                    outroscertificados="1";      
                 } else{
                    outroscertificados="0";
                 }
            pst.setString(14, outroscertificados);              
            pst.setString(15, txtPesOutros.getText());
            if(txtEmpPesId.getText().isEmpty()){
                txtEmpPesId.setText("1");
            }
            pst.setString(16, txtEmpPesId.getText());
            pst.setString(17, txtEmpPesNome.getText());  
            pst.setString(18, txtPesRfid.getText());
            
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
                    //Limpa as Caixas
                    //limpar();
                }
            }
            
        } catch (Exception e) {
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
    
    private void setar_campos(){
        
        int setar = tblPessoas.getSelectedRow();
        txtPesId.setText(tblPessoas.getModel().getValueAt(setar,0).toString());
        txtPesNome.setText(tblPessoas.getModel().getValueAt(setar,1).toString());
        txtPesCpf.setText(tblPessoas.getModel().getValueAt(setar,2).toString());
        cboPesTipo.setSelectedItem(tblPessoas.getModel().getValueAt(setar,3).toString());
        //a linha abixo desabilita o botão adicionar
        btnAdicionar.setEnabled(false); 
        
        
    }
    
    private void setar_campos_restantes(){
        //Codigo abaixo seta o restante dos dados
        String idpes=txtPesId.getText();
        String sql="select cidade, DATE_FORMAT (`datacontratacao`,'%d/%m/%Y') AS `Contratação`, listaequipamento, listaepi, nr35, nr10, nr33, nr12, outroscertificados, certificados, idemp, razao, iud, cnh, cat, idinteg from pessoas where idpes="+ idpes;
        
        //String sql="select cidade, DATE_FORMAT (`datacontratacao`,'%d/%m/%Y') AS `Contratação`, listaequipamento, listaepi, nr35, nr10, nr33, nr12, outroscertificados, certificados, iud, cnh, cat from pessoas where idpes="+ idpes;
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                txtPesCidade.setText(rs.getString(1));
                txtPesDataContratacao.setText(rs.getString(2));
                if(txtPesDataContratacao.getText().equals("10/10/1000")){
                    txtPesDataContratacao.setText(null);
                }
                txtPesListaEquipamento.setText(rs.getString(3));
                txtPesListaEpi.setText(rs.getString(4));
                //setando o campo dos RadioButtons
                String nr35=rs.getString(5);
                if (nr35.equals("1")) {
                    cboPesNR35.setSelected(true);
                    
                } else {
                    cboPesNR35.setSelected(false);
                }
                String nr10=rs.getString(6);
                if (nr10.equals("1")) {
                    cboPesNR10.setSelected(true);
                    
                } else {
                    cboPesNR10.setSelected(false);
                }
                String nr33=rs.getString(7);
                if (nr33.equals("1")) {
                    cboPesNR33.setSelected(true);
                    
                } else {
                    cboPesNR33.setSelected(false);
                }
                String nr12=rs.getString(8);
                if (nr12.equals("1")) {
                    cboPesNR12.setSelected(true);
                    
                } else {
                    cboPesNR12.setSelected(false);
                }
                String outros=rs.getString(9);
                if (outros.equals("1")) {
                    cboPesOutros.setSelected(true);
                    
                } else {
                    cboPesOutros.setSelected(false);
                }
                txtPesOutros.setText(rs.getString(10));
                txtEmpPesId.setText(rs.getString(11));
                txtEmpPesNome.setText(rs.getString(12));
                //String statusInteg=rs.getString(13);
                //if (statusInteg.equals("1")) {
                    //cboIntegracao.setSelected(true);
                    
                //} else {
                  //  cboIntegracao.setSelected(false);
                //}

                //txtEmpValidade.setText(rs.getString(14));
                
                //if(txtEmpValidade.getText().equals("10/10/1000")){
                    //txtEmpValidade.setText(null);
               // }
                
                txtPesRfid.setText(rs.getString(13));
                txtPesCnh.setText(rs.getString(14));
                cboPesCat.setSelectedItem(rs.getString(15));
                //txtEmpInteg.setText(rs.getString(14));
                
                
                
              
                busca_integracao(); //procura integracao válida para empresa do usuario logado
                
                
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    private void alterar(){
        String idpes= txtPesId.getText();
        if (idpes.equals(null)){
            JOptionPane.showMessageDialog(null, "Nenhum cadastro selecionado. Verifique!");
        } else {
        String sql="update pessoas set nome=?, cpf=?, cnh=?, cat=?, tipo=?, cidade=?, datacontratacao=?, listaequipamento=?, listaepi=?, nr35=?, nr10=?, nr33=?, nr12=?, outroscertificados=?, certificados=?, idemp=?, razao=?, iud=? where idpes="+idpes;
            
            
        
        try {
            pst=conexao.prepareStatement(sql);

            pst.setString(1, txtPesNome.getText());
            pst.setString(2, txtPesCpf.getText());
            pst.setString(3, txtPesCnh.getText());
            pst.setString(4, cboPesCat.getSelectedItem().toString());
            pst.setString(5, cboPesTipo.getSelectedItem().toString());
            pst.setString(6, txtPesCidade.getText());
            //s linhas abaixo verificam o campo de data e caso não contenha valor atribui valor padrao
                        if(txtPesDataContratacao.getText().equals("  /  /    ")){
                txtPesDataContratacao.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtPesDataContratacao.getText(); //passa a String para a variavel string
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padão brasileiro
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato.format(dataFormatada));
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato2.format(dataFormatada)); 
                    
            pst.setString(7, formato2.format(dataFormatada)); //Passa a data do formato no formato2 americano
            pst.setString(8, txtPesListaEquipamento.getText());
            pst.setString(9, txtPesListaEpi.getText());
                if(cboPesNR35.isSelected()){
                    nr35="1";      
                 } else{
                    nr35="0";
                 }
            pst.setString(10, nr35);
                 if(cboPesNR10.isSelected()){
                    nr10="1";      
                 } else{
                    nr10="0";
                 }    
            pst.setString(11, nr10);
                if(cboPesNR33.isSelected()){
                    nr33="1";      
                 } else{
                    nr33="0";
                 }   
            pst.setString(12, nr33);
                 if(cboPesNR12.isSelected()){
                    nr12="1";      
                 } else{
                    nr12="0";
                 }
            pst.setString(13, nr12);
                 if(cboPesOutros.isSelected()){
                    outroscertificados="1";      
                 } else{
                    outroscertificados="0";
                 }
            pst.setString(14, outroscertificados);              
            pst.setString(15, txtPesOutros.getText());
            pst.setString(16, txtEmpPesId.getText());
            pst.setString(17, txtEmpPesNome.getText());
                //if(cboIntegracao.isSelected()){
                    //statusInteg="1";      
                // } else{
                //    statusInteg="0";
             //    }
            //pst.setString(18, statusInteg);
            
            // if(txtEmpValidade.getText().equals("  /  /    ")){
                //txtEmpValidade.setText("10/10/1000");
           // }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            //String dataRecebida2 = txtEmpValidade.getText(); //passa a String para a variavel string   
           // java.util.Date dataFormatada2 =  formato.parse(dataRecebida2); //formata no padão brasileiro
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato.format(dataFormatada));
            //System.out.println("TESTEDEDATA"+dataFormatada);
            //System.out.println(formato2.format(dataFormatada)); 
           // pst.setString(19, formato2.format(dataFormatada2));
            //pst.setString(18, txtEmpId.getText());
            
            pst.setString(18, txtPesRfid.getText());
            //validação dos campos
            
            if ((txtPesNome.getText().isEmpty()) || (txtPesCpf.getText().isEmpty()) ) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso!");
                    //Limpa as Caixa
                    //limpar();
                    //cboUsuPerfil.setSelectedItem(null);
                    //btnAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        }
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
    
    //metodo remover uma pessoa do banco
    
    private void remover(){
        
        //a estrutura confima a remoção do usuário
        
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta pessoa do cadastro", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma==JOptionPane.YES_OPTION){
            String sql="delete from pessoas where idpes=?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, txtPesId.getText());
               // pst.executeUpdate();  //executa a sql
                int apagado = pst.executeUpdate();
                if (apagado>0){
                    JOptionPane.showMessageDialog(null, "Removido com sucesso");
                    limpar();
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não é possível exclusão, pois já possui movimentacão relacionada!");
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
        
    }
    
        private void limpar(){
            //limpa os dados
            txtPesPesquisar.setText(null);
            txtPesId.setText(null);
            txtPesNome.setText(null);
            txtPesCpf.setText(null);
            txtPesCnh.setText(null);
            
            txtPesCidade.setText(null);
            txtPesDataContratacao.setText(null);
            txtPesListaEpi.setText(null);
            txtPesListaEquipamento.setText(null);
            txtEmpId.setText(null);
            txtEmpNome.setText(null);
            txtEmpPesquisar.setText(null);
            txtEmpValidade.setText(null);
            cboPesTipo.setSelectedItem(null);
            cboIntegracao.setSelected(false);
            cboPesNR10.setSelected(false); 
            cboPesNR12.setSelected(false); 
            cboPesNR33.setSelected(false); 
            cboPesNR35.setSelected(false); 
            cboPesOutros.setSelected(false); 
            txtEmpId.setText(null);
            txtEmpNome.setText(null);
            txtEmpValidade.setText(null);
            txtPesRfid.setText(null);
            txtEmpInteg.setText(null);
            txtEmpPesId.setText(null);
            txtEmpPesNome.setText(null);
         
             btnAdicionar.setEnabled(true); 
             
             txtEmpValidade.setEditable(true);
            
            ((DefaultTableModel) tblPessoas.getModel()).setRowCount(0);
            ((DefaultTableModel) tblEmpresas.getModel()).setRowCount(0);
            ((DefaultTableModel) tblPesEmpresas.getModel()).setRowCount(0);
        }
        
        private void pesquisar_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ from empresas where statusInteg= true and nome like ?";
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
    
    private void setar_campos_empresa(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
       
        
    }
    
    private void pesquisar_pes_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ from empresas where nome like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtEmpPesPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPesEmpresas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //mtodo para setar os campos do formulario com o conteudo da tabela
    
    private void setar_campos_pes_empresa(){
        int setar = tblPesEmpresas.getSelectedRow();
        txtEmpPesId.setText(tblPesEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpPesNome.setText(tblPesEmpresas.getModel().getValueAt(setar,1).toString());
       
        
    }
    
    //metodo para capturar rfid do arduino
    
    private void captura_rfid(){
         
         String id = null, iud = null, dataIud = null;

               String sql="select * from arduino where processado=false";
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                id=rs.getString(1);
                iud= rs.getString(2);
                dataIud=rs.getString(3);
               
                txtPesRfid.setText(iud);
               
                finaliza_iud(id);
            } 
        } catch (Exception e){
            
            JOptionPane.showMessageDialog(null, e);
        
            //System.out.println("estou aqui erro controle arduino");
           
        }
     }
    private void finaliza_iud(String id){
         
        String sql="update arduino set processado= '1' where id="+id;
        try {
           
                pst=conexao.prepareStatement(sql);
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                   
                  
                }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
     }
    
    private boolean verifica_requisitos_integracao(){
        if(txtEmpInteg.getText().isEmpty()){ //se casp já possui integraçao válida, ou seja campo atribuido
            return true;
        }
        return false;
    }
    
    private void gerar_integracao(){
        
        if (verifica_requisitos_integracao()==true){
            String sql="insert into integracao (statusInteg, setor, idempresa, idusuario, idemp, empnome, idpes, validade, datacontratacao) values(?,?,?,?,?,?,?,?,?)";
        try {
            pst=conexao.prepareStatement(sql);
            //if(cboIntegracao.isSelected()){
                    statusInteg="1";      
                 //} else{
                   // statusInteg="0";
                // }
            pst.setString(1, statusInteg);
            pst.setString(2, cboPesSetor.getSelectedItem().toString());
            pst.setString(3, TelaPrincipal.lblEmpId.getText());   //pega o id da tela pricipal
            pst.setString(4, TelaPrincipal.lblUsuarioId.getText());   //pega o id da tela pricipal
            pst.setString(5, txtEmpId.getText());
            pst.setString(6, txtEmpNome.getText());
            pst.setString(7, txtPesId.getText());
            if(txtEmpValidade.getText().equals("  /  /    ")){
                txtEmpValidade.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtEmpValidade.getText(); //passa a String para a variavel string
            
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padão brasileiro
            dataInsert=formato2.format(dataFormatada);
            pst.setString(8, dataInsert); //Passa a data do formato no formato2 americano
            
            if(txtPesDataContratacao.getText().equals("  /  /    ")){
                txtPesDataContratacao.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida2 = txtPesDataContratacao.getText(); //passa a String para a variavel string
            
            formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada2 =  formato.parse(dataRecebida); //formata no padão brasileiro
            dataInsert=formato2.format(dataFormatada2);
            pst.setString(9, dataInsert); //Passa a data do formato no formato2 americano
            
            
            //validacão do campos
            if ((txtPesId.getText().isEmpty()) || (txtEmpId.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abaixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Integração gerada com sucesso!");
                    atribui_integracao();  //salva a integracao no cadastro da pessoa
                    imprime_integracao(); //chama a função para impressao  
                    protege_campos_integracao();
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
            
        }else{
            JOptionPane.showMessageDialog(null, "Não é possivel gerar integracão, pois já há integracão gerada!");
        }
        
        
    }
    
    private String recuperaIdIntegracao(){
        String sql ="select max(idinteg) from integracao"; //busca no banco o ultimo id criado
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
    
    private void atribui_integracao(){
        String idpes= txtPesId.getText();                           //Busca id da pessoa no campo do txt
        String idinteg= recuperaIdIntegracao();
        String sql="update pessoas set idinteg= ? where idpes=?";
        try {
           
                pst=conexao.prepareStatement(sql);
                pst.setString(1, idinteg);
                pst.setString(2, idpes);
                
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                   JOptionPane.showMessageDialog(null, "Integraçao número (" + recuperaIdIntegracao() + ") atribuida com sucesso ao cadastro!");
                   txtEmpInteg.setText(recuperaIdIntegracao());  //atribui id da integracao ao campo pretendido
                   alterar(); //salva todos os dados alterados. 
                }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }

    private void protege_campos_integracao(){
        if(txtEmpInteg.getText().isEmpty()){
            
        }else{
            txtEmpPesquisar.setEditable(false);
            txtEmpValidade.setEditable(false);
            //cboIntegracao.setEnabled(false);
            //cboPesSetor.setEnabled(false);
            }
            
       
    }
    
    private void busca_integracao(){
        String idpes = txtPesId.getText(), idempresa = TelaPrincipal.lblEmpId.getText();

               String sql="select idinteg, idemp, empnome, setor, statusInteg, DATE_FORMAT (`validade`,'%d/%m/%Y')  from integracao where idpes=? and idempresa=? and statusInteg=true";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, idpes);
            pst.setString(2, idempresa);
            rs=pst.executeQuery();
            
            if (rs.next()) {
                
                txtEmpInteg.setText(rs.getString(1));
                txtEmpId.setText(rs.getString(2));
                txtEmpNome.setText(rs.getString(3));
                cboPesSetor.setSelectedItem(rs.getString(4).toString());
                String statusInteg=rs.getString(5);
                if (statusInteg.equals("1")) {
                    cboIntegracao.setSelected(true);
                    
                } else {
                    cboIntegracao.setSelected(false);
                }

                txtEmpValidade.setText(rs.getString(6));
                
                if(txtEmpValidade.getText().equals("10/10/1000")){
                    txtEmpValidade.setText(null);
                }
                
                
                
               
                
            } 
        } catch (Exception e){
            
            JOptionPane.showMessageDialog(null, e);
           
        }
        
    }
    
    private void imprime_integracao(){
        
                 // gera um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Deseja imprimir a integração?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(txtEmpInteg.getText()));
                //Compila o relatorio
                //JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/checkexemplo.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                //JasperPrint relatorioprint;  
                //JasperPrint relatorioprint2;
                //relatorioprint = JasperFillManager.fillReport(relatoriocompilado,filtro,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                //JasperViewer.viewReport(relatorioprint,false);
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/MyReports/integracao_terceiro.jasper"), filtro, conexao);
                JasperPrint print2 = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/MyReports/integracao_terceiro_subreport1.jasper"), filtro, conexao);
                JasperViewer.viewReport(print, false);
                JasperViewer.viewReport(print2, false);
                conexao.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
        }
        
    }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jMenu3 = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jSeparator1 = new javax.swing.JSeparator();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        guiaCertificados = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPesNome = new javax.swing.JTextField();
        txtPesCpf = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPesId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cboPesTipo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtPesCidade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPesDataContratacao = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoas = new javax.swing.JTable();
        txtPesPesquisar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtPesCnh = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cboPesCat = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPesListaEquipamento = new javax.swing.JTextField();
        txtPesListaEpi = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        cboPesNR35 = new javax.swing.JCheckBox();
        cboPesNR10 = new javax.swing.JCheckBox();
        cboPesNR33 = new javax.swing.JCheckBox();
        cboPesNR12 = new javax.swing.JCheckBox();
        cboPesOutros = new javax.swing.JCheckBox();
        txtPesOutros = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtEmpPesPesquisar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPesEmpresas = new javax.swing.JTable();
        txtEmpPesId = new javax.swing.JTextField();
        txtEmpPesNome = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtEmpPesquisar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        txtEmpId = new javax.swing.JTextField();
        txtEmpNome = new javax.swing.JTextField();
        cboIntegracao = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        txtEmpValidade = new javax.swing.JFormattedTextField();
        btnPesImpContrato = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtPesRfid = new javax.swing.JTextField();
        btnCapturar = new javax.swing.JButton();
        cboPesSetor = new javax.swing.JComboBox<>();
        txtEmpInteg = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Pessoas");
        getContentPane().setLayout(null);

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setBorder(null);
        btnAdicionar.setMaximumSize(new java.awt.Dimension(60, 60));
        btnAdicionar.setMinimumSize(new java.awt.Dimension(60, 60));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(81, 57));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdicionar);
        btnAdicionar.setBounds(189, 369, 81, 60);

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnRemover.setToolTipText("Remover");
        btnRemover.setMaximumSize(new java.awt.Dimension(60, 60));
        btnRemover.setMinimumSize(new java.awt.Dimension(60, 60));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemover);
        btnRemover.setBounds(448, 369, 81, 60);

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/limpar.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar");
        btnLimpar.setMaximumSize(new java.awt.Dimension(60, 60));
        btnLimpar.setMinimumSize(new java.awt.Dimension(60, 60));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpar);
        btnLimpar.setBounds(578, 369, 81, 60);

        jLabel1.setText("* Nome");

        jLabel2.setText("* CPF");

        jLabel7.setText("Cidade");

        txtPesNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesNomeKeyPressed(evt);
            }
        });

        try {
            txtPesCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPesCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCpfKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesCpfKeyTyped(evt);
            }
        });

        jLabel5.setText("* Campos Obrigatórios");

        txtPesId.setEnabled(false);

        jLabel4.setText("Id Pessoa");

        cboPesTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Motorista", "Visitante", "Terceiro", "Funcionario" }));
        cboPesTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPesTipoActionPerformed(evt);
            }
        });
        cboPesTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesTipoKeyPressed(evt);
            }
        });

        jLabel3.setText("* Tipo");

        txtPesCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCidadeKeyPressed(evt);
            }
        });

        jLabel8.setText("Data de Contratação");

        try {
            txtPesDataContratacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPesDataContratacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesDataContratacaoActionPerformed(evt);
            }
        });
        txtPesDataContratacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesDataContratacaoKeyPressed(evt);
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

        txtPesPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesPesquisarActionPerformed(evt);
            }
        });
        txtPesPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesPesquisarKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel11.setText("Pesquisar");

        txtPesCnh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCnhKeyPressed(evt);
            }
        });

        jLabel15.setText("Cat");

        jLabel14.setText("CNH");

        cboPesCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AE", "E", "AD", "D", "AC", "C", "AB", "B", "A" }));
        cboPesCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesCatKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addGap(28, 28, 28)
                .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesDataContratacao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(348, 348, 348))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cboPesTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel14)
                    .addComponent(txtPesCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPesCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPesDataContratacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );

        guiaCertificados.addTab("Dados Pessoais", jPanel4);

        jLabel9.setText("Lista de Equipamentos");

        jLabel6.setText("Lista de EPIs");

        txtPesListaEquipamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesListaEquipamentoKeyPressed(evt);
            }
        });

        txtPesListaEpi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesListaEpiKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cboPesNR35.setText("NR35");
        cboPesNR35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPesNR35ActionPerformed(evt);
            }
        });

        cboPesNR10.setText("NR10");

        cboPesNR33.setText("NR33");

        cboPesNR12.setText("NR12");

        cboPesOutros.setText("Outros");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(cboPesNR35)
                .addGap(18, 18, 18)
                .addComponent(cboPesNR10)
                .addGap(26, 26, 26)
                .addComponent(cboPesNR33)
                .addGap(33, 33, 33)
                .addComponent(cboPesNR12)
                .addGap(26, 26, 26)
                .addComponent(cboPesOutros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPesOutros, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboPesNR35)
                    .addComponent(cboPesNR10)
                    .addComponent(cboPesNR33)
                    .addComponent(cboPesNR12)
                    .addComponent(cboPesOutros)
                    .addComponent(txtPesOutros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesListaEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesListaEpi, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 73, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPesListaEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPesListaEpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        guiaCertificados.addTab("Certificados e EPIs", jPanel5);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel18.setText("Pesquisar");

        txtEmpPesPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesPesquisarKeyReleased(evt);
            }
        });

        tblPesEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Razão", "CNPJ"
            }
        ));
        tblPesEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesEmpresasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPesEmpresas);

        txtEmpPesId.setEditable(false);

        txtEmpPesNome.setEditable(false);

        jLabel20.setText("Obs: Todas as empresas cadastradas são habilitadas na tabela.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(txtEmpPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtEmpPesPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmpPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEmpPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        guiaCertificados.addTab("Empresa", jPanel2);

        txtEmpPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpPesquisarKeyReleased(evt);
            }
        });

        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Razão", "CNPJ"
            }
        ));
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmpresas);

        txtEmpId.setEditable(false);

        txtEmpNome.setEditable(false);

        cboIntegracao.setText("Integração Ativa");
        cboIntegracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntegracaoActionPerformed(evt);
            }
        });

        jLabel10.setText("Válidade");

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

        btnPesImpContrato.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPesImpContrato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        btnPesImpContrato.setText("Imprimir Contrato");
        btnPesImpContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesImpContratoActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/integracao.png"))); // NOI18N
        jButton1.setText("Gerar Integração");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel12.setText("Pesquisar");

        jLabel13.setText("RFID");

        btnCapturar.setText("Capturar");
        btnCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapturarActionPerformed(evt);
            }
        });

        cboPesSetor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manutenção", "Administrativo", "Amox", "Transportes" }));
        cboPesSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPesSetorActionPerformed(evt);
            }
        });
        cboPesSetor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesSetorKeyPressed(evt);
            }
        });

        txtEmpInteg.setEditable(false);

        jLabel16.setText("N Integraçao");

        jLabel17.setText("Obs: Somente empresas habiliatas com integração são listadas na tebela");

        jLabel19.setText("Obs: Após integração gerada não é possível alterar os dados");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesRfid, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCapturar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesImpContrato)
                        .addGap(54, 54, 54)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 28, Short.MAX_VALUE)
                                .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEmpInteg, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(cboIntegracao)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cboPesSetor, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(38, 38, 38))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(33, 33, 33))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboIntegracao)
                            .addComponent(cboPesSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmpInteg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtPesRfid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCapturar)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel17)
                        .addGap(31, 31, 31)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPesImpContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(82, 82, 82))
        );

        guiaCertificados.addTab("Integração", jPanel6);

        getContentPane().add(guiaCertificados);
        guiaCertificados.setBounds(0, 0, 816, 351);

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setMaximumSize(new java.awt.Dimension(60, 60));
        btnAlterar.setMinimumSize(new java.awt.Dimension(60, 60));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAlterar);
        btnAlterar.setBounds(320, 369, 81, 60);

        setBounds(0, 0, 832, 490);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // Chama o metodo
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Chamando o metodo para alterar clientes
        
        alterar(); 
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        //chamando o metodo remover
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnPesImpContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesImpContratoActionPerformed
          // gera um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão do Relatório de Clientes?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                HashMap filtro = new HashMap();
                filtro.put("ID", Integer.parseInt(txtPesId.getText()));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/MyReports/contrato_acesso.jasper"), filtro, conexao);
                JasperViewer.viewReport(print, false);
                conexao.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
        }
        
    }
    }//GEN-LAST:event_btnPesImpContratoActionPerformed

    private void cboPesNR35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPesNR35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesNR35ActionPerformed

    private void txtEmpValidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpValidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpValidadeActionPerformed

    private void cboIntegracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntegracaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIntegracaoActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtEmpPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesquisarKeyReleased
       pesquisar_empresa();
    }//GEN-LAST:event_txtEmpPesquisarKeyReleased

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
        setar_campos_empresa();
    }//GEN-LAST:event_tblEmpresasMouseClicked

// o evento abixo é do tipo, "enquanto for digitado"
    private void txtPesPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesPesquisarKeyReleased

        pesquisar_pessoa();
        protege_campos_integracao();
    }//GEN-LAST:event_txtPesPesquisarKeyReleased

    private void txtPesPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesPesquisarActionPerformed

    // evento que sera usado para setar os campos da tabela
    private void tblPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoasMouseClicked
        // chamando o metodo setar campos
        
        setar_campos();
        setar_campos_restantes();
    }//GEN-LAST:event_tblPessoasMouseClicked

    private void txtPesDataContratacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesDataContratacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesDataContratacaoActionPerformed

    private void cboPesTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPesTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesTipoActionPerformed

    private void btnCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapturarActionPerformed
        //chama a função capturar
        captura_rfid();
    }//GEN-LAST:event_btnCapturarActionPerformed

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

    private void cboPesTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPesTipoKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesCpf.requestFocus();
       }
    }//GEN-LAST:event_cboPesTipoKeyPressed

    private void txtPesCidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCidadeKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesDataContratacao.requestFocus();
       }
    }//GEN-LAST:event_txtPesCidadeKeyPressed

    private void txtPesDataContratacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesDataContratacaoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             //guiaCertificados.requestFocus();
             txtPesListaEquipamento.requestFocus();
             
       }
    }//GEN-LAST:event_txtPesDataContratacaoKeyPressed

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

    private void txtPesListaEquipamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesListaEquipamentoKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesListaEpi.requestFocus();
       }
    }//GEN-LAST:event_txtPesListaEquipamentoKeyPressed

    private void txtPesListaEpiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesListaEpiKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
             txtPesOutros.requestFocus();
       }
    }//GEN-LAST:event_txtPesListaEpiKeyPressed

    private void cboPesSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPesSetorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesSetorActionPerformed

    private void cboPesSetorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPesSetorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesSetorKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(txtPesId.getText().isEmpty()){
            adicionar();
            txtPesId.setText(recuperaIdCadastro());
        }
        gerar_integracao();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPesCpfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfKeyTyped
        verifica_cpf_ja_cadastrado(txtPesCpf.getText());
    }//GEN-LAST:event_txtPesCpfKeyTyped

    private void txtEmpPesPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpPesPesquisarKeyReleased
        pesquisar_pes_empresa();
    }//GEN-LAST:event_txtEmpPesPesquisarKeyReleased

    private void tblPesEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesEmpresasMouseClicked
        setar_campos_pes_empresa();
    }//GEN-LAST:event_tblPesEmpresasMouseClicked
     

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCapturar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesImpContrato;
    private javax.swing.JButton btnRemover;
    private javax.swing.JRadioButton cboIntegracao;
    private javax.swing.JComboBox<String> cboPesCat;
    private javax.swing.JCheckBox cboPesNR10;
    private javax.swing.JCheckBox cboPesNR12;
    private javax.swing.JCheckBox cboPesNR33;
    private javax.swing.JCheckBox cboPesNR35;
    private javax.swing.JCheckBox cboPesOutros;
    private javax.swing.JComboBox<String> cboPesSetor;
    private javax.swing.JComboBox<String> cboPesTipo;
    private javax.swing.JTabbedPane guiaCertificados;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTable tblPesEmpresas;
    private javax.swing.JTable tblPessoas;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpInteg;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpPesId;
    private javax.swing.JTextField txtEmpPesNome;
    private javax.swing.JTextField txtEmpPesPesquisar;
    private javax.swing.JTextField txtEmpPesquisar;
    private javax.swing.JFormattedTextField txtEmpValidade;
    private javax.swing.JTextField txtPesCidade;
    private javax.swing.JTextField txtPesCnh;
    private javax.swing.JFormattedTextField txtPesCpf;
    private javax.swing.JFormattedTextField txtPesDataContratacao;
    private javax.swing.JTextField txtPesId;
    private javax.swing.JTextField txtPesListaEpi;
    private javax.swing.JTextField txtPesListaEquipamento;
    private javax.swing.JTextField txtPesNome;
    private javax.swing.JTextField txtPesOutros;
    private javax.swing.JTextField txtPesPesquisar;
    private javax.swing.JTextField txtPesRfid;
    // End of variables declaration//GEN-END:variables
}
