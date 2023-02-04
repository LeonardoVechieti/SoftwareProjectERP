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
public class TelaIntegracao extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaIntegracao
     */
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    String idemp;
    private String dataInsert="1000/10/10"; 
    
    public TelaIntegracao() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_integracao_gerenciador();
    }
    
        
    private void altera_integracao(){
            
            
            String idinteg=txtIntegracaoIdG.getText();
            String sql="update integracao set setor=?, idusuario=?, validade=?, datacontratacao=? where statusInteg= true and idinteg=?";
        try {
            pst=conexao.prepareStatement(sql);
           
            pst.setString(1, cboSetorIntegracaoG.getSelectedItem().toString());
            pst.setString(2, TelaPrincipal.lblUsuarioId.getText());   //pega o id da tela pricipal
           
            if(txtValidadeIntegracaoG.getText().equals("  /  /    ")){
                txtValidadeIntegracaoG.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida = txtValidadeIntegracaoG.getText(); //passa a String para a variavel string
            
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padão brasileiro
            dataInsert=formato2.format(dataFormatada);
            pst.setString(3, dataInsert); //Passa a data do formato no formato2 americano
            
            if(txtContratacaoIntegracaoG.getText().equals("  /  /    ")){
                txtContratacaoIntegracaoG.setText("10/10/1000");
            }
            
            //As linhas abaixo convertem String em data, data padão brasileiro em americano
            
            String dataRecebida2 = txtContratacaoIntegracaoG.getText(); //passa a String para a variavel string
            
            formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada2 =  formato.parse(dataRecebida); //formata no padão brasileiro
            dataInsert=formato2.format(dataFormatada2);
            pst.setString(4, dataInsert); //Passa a data do formato no formato2 americano
            pst.setString(5, idinteg);
            
            //validacão do campos
            if ((txtIntegracaoIdG.getText().isEmpty()) || txtIntegAtivo.getText().equals("Inativado")) {
                JOptionPane.showMessageDialog(null, "Integração não selecionada ou Inativada, favor verifique!");
            } else {
            
                // a linha abaixo atualiza a tabela com os dados do form
                // a estrutura abaixo e usada para confirmar a inserção dos dados na tabela
                int adicionado= pst.executeUpdate();
            
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Integração alterada com sucesso!");
                    imprime_integracao(); //chama a função para impressao  
                    
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
            
        
        
        
    }
    private void pesquisar_empresa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select idemp as ID, nome as Razão, cnpj as CNPJ, ncontrato as Número, DATE_FORMAT (`validade`,'%d/%m/%Y') AS `Válidade` from empresas where statusInteg =true and nome like ?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, txtPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblEmpresas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //mtodo para setar os campos do formulario com o conteudo da tabela
    
    private void setar_campos(){
        int setar = tblEmpresas.getSelectedRow();
        txtEmpId.setText(tblEmpresas.getModel().getValueAt(setar,0).toString());
        txtEmpNome.setText(tblEmpresas.getModel().getValueAt(setar,1).toString());
        txtEmpCnpj.setText(tblEmpresas.getModel().getValueAt(setar,2).toString());
        txtEmpContrato.setText(tblEmpresas.getModel().getValueAt(setar,3).toString());
        txtEmpValidade.setText(tblEmpresas.getModel().getValueAt(setar,4).toString());
        
        
    }
    
    private void setar_pessoas_tabela(){
        
        idemp=txtEmpId.getText();
        String idempresa=TelaPrincipal.lblEmpId.getText();
       
        String sql="select i.idinteg as IDINTEG, nome as Nome, cpf as CPF, cidade as Cidade, DATE_FORMAT (`datainteg`,'%d/%m/%Y') as Integracão , DATE_FORMAT (`validade`,'%d/%m/%Y') as Válidade from pessoas p\n" +
"join integracao i\n" +
"on p.idpes = i.idpes\n" +
"where idempresa=? and i.statusInteg='1' and i.idemp="+idemp;
        
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            pst.setString(1, idempresa);
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblPessoas.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    
    }
    
    private void setar_campos_pessoa(){
        int setar = tblPessoas.getSelectedRow();
        txtEmpIntegD.setText(tblPessoas.getModel().getValueAt(setar,0).toString());
        txtPesNome.setText(tblPessoas.getModel().getValueAt(setar,1).toString());
        txtPesCpf.setText(tblPessoas.getModel().getValueAt(setar,2).toString());
        txtPesCidade.setText(tblPessoas.getModel().getValueAt(setar,3).toString());
        txtPesContratacao.setText(tblPessoas.getModel().getValueAt(setar,4).toString());
        txtPesValidade.setText(tblPessoas.getModel().getValueAt(setar,5).toString());
        //a linha abixo desabilita o botão adicionar
        //btnAdicionar.setEnabled(false); 
        setar_campos_restantes_integracao();
        
    }
    private void setar_campos_restantes_integracao(){
        //Codigo abaixo seta o restante dos dados
        String idinteg=txtEmpIntegD.getText();
        String sql="select statusInteg, idpes, setor from integracao where idinteg="+ idinteg;
        
       
        try {
            pst=conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            
            if (rs.next()) {

                String ativo=rs.getString(1);
                if (ativo.equals("1")) {
                    cboIntegracaoD.setSelected(true);
                    
                } else {
                    cboIntegracaoD.setSelected(false);
                }
                txtPesId.setText(rs.getString(2));
                cboPesSetorD.setSelectedItem(rs.getString(3).toString());
                
               
            
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    private void pesquisar_integracao_gerenciador(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String idempresa=TelaPrincipal.lblEmpId.getText();
        String sql="select i.idinteg as IDINTEG, p.nome as Nome, cpf as CPF, e.nome as Empresa, DATE_FORMAT (`datainteg`,'%d/%m/%Y') as Integracão , DATE_FORMAT (i.validade,'%d/%m/%Y') as Válidade from pessoas p\n" +
        "join integracao i\n" +
        "on p.idpes = i.idpes\n" +
        "join empresas e\n" +
        "on e.idemp = i.idemp\n" +
        "where i.idempresa=?\n" +
        "order by p.nome";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, idempresa);
            
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblInteg.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void seta_id_integracao(){
        int setar = tblInteg.getSelectedRow();
        txtIntegracaoIdG.setText(tblInteg.getModel().getValueAt(setar,0).toString());
        setar_todos_os_campos_g();
    }
    private void setar_todos_os_campos_g(){
        String id=txtIntegracaoIdG.getText();
        String sql="select p.idpes, p.nome, p.cpf, p.tipo, e.idemp, e.nome, e.cnpj, e.ncontrato, DATE_FORMAT (i.datainteg,'%d/%m/%Y'), DATE_FORMAT (i.validade,'%d/%m/%Y'), i.statusInteg, i.setor from pessoas p\n" +
        "join integracao i\n" +
        "on p.idpes = i.idpes\n" +
        "join empresas e\n" +
        "on e.idemp = i.idemp\n" +
        "where i.idinteg=?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, id);
            
            rs=pst.executeQuery();
           if (rs.next()) {
                txtPesIdG.setText(rs.getString(1));
                txtPesNomeG.setText(rs.getString(2));
                txtPesCpfG.setText(rs.getString(3));
                txtPesTipoG.setText(rs.getString(4));
                txtEmpIdG.setText(rs.getString(5));
                txtEmpNomeG.setText(rs.getString(6));
                txtEmpCnpjG.setText(rs.getString(7));
                txtEmpContratoG.setText(rs.getString(8));
                txtContratacaoIntegracaoG.setText(rs.getString(9));
                txtValidadeIntegracaoG.setText(rs.getString(10));
                String ativo=rs.getString(11);
                if (ativo.equals("1")) {
                    txtIntegAtivo.setText("Ativo");
                    
                } else {
                    txtIntegAtivo.setText("Inativado");
                }
                cboSetorIntegracaoG.setSelectedItem(rs.getString(12).toString());
                
                
               
            
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void pesquisar_integracao_nome(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String idempresa=TelaPrincipal.lblEmpId.getText();
        String sql="select i.idinteg as IDINTEG, p.nome as Nome, cpf as CPF, e.nome as Empresa, DATE_FORMAT (`datainteg`,'%d/%m/%Y') as Integracão , DATE_FORMAT (i.validade,'%d/%m/%Y') as Válidade from pessoas p\n" +
        "join integracao i\n" +
        "on p.idpes = i.idpes\n" +
        "join empresas e\n" +
        "on e.idemp = i.idemp\n" +
        "where i.idempresa=? and p.nome like ?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, idempresa);
            pst.setString(2, txtPesquisarIntegNome.getText() + "%");
            
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblInteg.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    
     private void inativar(){
        String idinteg=txtIntegracaoIdG.getText();
        String status= null;
        String ativo=txtIntegAtivo.getText();
        if(ativo.equals("Ativo")){
            status="0"; //O staus é invertido, pela logica da funcao
            
        }else{
            status="1";
        }
        
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja ativar ou inativar?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma==JOptionPane.YES_OPTION){
            String sql="update integracao set statusInteg=? where idinteg= ?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1, status);
                pst.setString(2, idinteg);  
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
        private void imprime_integracao(){
        
                 // gera um relatório de clientes
        int confirma = JOptionPane.showConfirmDialog(null, "Deseja imprimir a integração?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(txtEmpIntegD.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/myreports/integracao_terceiro.jrxml"));
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmpresas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmpValidade = new javax.swing.JFormattedTextField();
        txtEmpCnpj = new javax.swing.JFormattedTextField();
        txtEmpNome = new javax.swing.JTextField();
        txtEmpId = new javax.swing.JTextField();
        txtEmpContrato = new javax.swing.JTextField();
        txtPesNome = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPesCpf = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        txtPesContratacao = new javax.swing.JFormattedTextField();
        txtPesId = new javax.swing.JTextField();
        txtPesValidade = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPesCidade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoas = new javax.swing.JTable();
        cboIntegracaoD = new javax.swing.JRadioButton();
        cboPesSetorD = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        txtEmpIntegD = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtEmpNomeG = new javax.swing.JTextField();
        txtPesIdG = new javax.swing.JTextField();
        txtPesCpfG = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        txtPesNomeG = new javax.swing.JTextField();
        txtEmpIdG = new javax.swing.JTextField();
        txtEmpCnpjG = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        txtEmpContratoG = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPesTipoG = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtValidadeIntegracaoG = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        txtIntegracaoIdG = new javax.swing.JTextField();
        cboSetorIntegracaoG = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        txtContratacaoIntegracaoG = new javax.swing.JFormattedTextField();
        btnAlterar = new javax.swing.JButton();
        btnInativar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtIntegAtivo = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtPesquisarIntegNome = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblInteg = new javax.swing.JTable();

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

        setClosable(true);
        setIconifiable(true);
        setTitle("Integração de Terceiros ");

        tblEmpresas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Razão", "CNPJ", "Contrato"
            }
        ));
        tblEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpresasMouseClicked(evt);
            }
        });
        tblEmpresas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblEmpresasKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmpresas);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search-file-icon32.png"))); // NOI18N
        jLabel1.setText("Pesquisar");

        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
        });

        jLabel2.setText("Razão");

        jLabel3.setText("CNPJ");

        jLabel4.setText("Validade");

        txtEmpValidade.setEditable(false);

        txtEmpCnpj.setEditable(false);

        txtEmpNome.setEditable(false);

        txtEmpId.setEditable(false);

        txtEmpContrato.setEditable(false);

        txtPesNome.setEditable(false);

        jLabel6.setText("Nome");

        jLabel7.setText("CPF");

        txtPesCpf.setEditable(false);

        jLabel9.setText("Contratação");

        txtPesContratacao.setEditable(false);

        txtPesId.setEditable(false);

        txtPesValidade.setEditable(false);

        jLabel10.setText("Válidade");

        txtPesCidade.setEditable(false);

        jLabel8.setText("Cidade");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        jButton1.setText("Integrados Empresa");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        jButton2.setText(" Integração");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Contrato");

        tblPessoas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Cidade", "Data Contratação", "Data Válidade"
            }
        ));
        tblPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPessoasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPessoas);

        cboIntegracaoD.setText("Integração Ativa");
        cboIntegracaoD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntegracaoDActionPerformed(evt);
            }
        });

        cboPesSetorD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manutenção", "Administrativo", "Amox", "Transportes" }));
        cboPesSetorD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPesSetorDActionPerformed(evt);
            }
        });
        cboPesSetorD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPesSetorDKeyPressed(evt);
            }
        });

        jLabel23.setText("N Integraçao");

        txtEmpIntegD.setEditable(false);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        jButton3.setText("Empresas");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        jButton4.setText("Integrados");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtPesNome)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel9))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPesContratacao)
                                    .addComponent(txtPesValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtEmpContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton4)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtEmpIntegD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cboIntegracaoD)
                                        .addGap(33, 33, 33)
                                        .addComponent(cboPesSetorD, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtEmpNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtEmpContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPesNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIntegracaoD)
                    .addComponent(cboPesSetorD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPesCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(txtPesContratacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(txtEmpIntegD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(txtPesValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Demonstrativo", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setText("Nome");

        jLabel18.setText("CPF");

        txtEmpNomeG.setEditable(false);

        txtPesIdG.setEditable(false);

        txtPesCpfG.setEditable(false);
        try {
            txtPesCpfG.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPesCpfG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesCpfGKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesCpfGKeyTyped(evt);
            }
        });

        jLabel20.setText("Empresa");

        txtPesNomeG.setEditable(false);

        txtEmpIdG.setEditable(false);

        txtEmpCnpjG.setEditable(false);
        try {
            txtEmpCnpjG.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtEmpCnpjG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpCnpjGKeyPressed(evt);
            }
        });

        jLabel21.setText("CNP-J");

        txtEmpContratoG.setEditable(false);
        txtEmpContratoG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpContratoGKeyPressed(evt);
            }
        });

        jLabel22.setText("Contrato:");

        jLabel11.setText("Tipo");

        txtPesTipoG.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesNomeG, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesIdG, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesCpfG, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesTipoG))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpNomeG, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpIdG, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpCnpjG, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpContratoG, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPesIdG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesNomeG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtPesCpfG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtPesTipoG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtEmpNomeG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpIdG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtEmpCnpjG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtEmpContratoG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        jLabel12.setText("Geral");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setText("Válidade");

        try {
            txtValidadeIntegracaoG.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtValidadeIntegracaoG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValidadeIntegracaoGActionPerformed(evt);
            }
        });

        jLabel17.setText("N Integraçao");

        txtIntegracaoIdG.setEditable(false);

        cboSetorIntegracaoG.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manutenção", "Administrativo", "Amox", "Transportes" }));
        cboSetorIntegracaoG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSetorIntegracaoGActionPerformed(evt);
            }
        });
        cboSetorIntegracaoG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboSetorIntegracaoGKeyPressed(evt);
            }
        });

        jLabel19.setText("Contratação");

        try {
            txtContratacaoIntegracaoG.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtContratacaoIntegracaoG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContratacaoIntegracaoGActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/alterar.png"))); // NOI18N
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnInativar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/inativar.png"))); // NOI18N
        btnInativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInativarActionPerformed(evt);
            }
        });

        jLabel16.setText("Status");

        txtIntegAtivo.setEditable(false);

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N

        txtPesquisarIntegNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarIntegNomeKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIntegAtivo))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(txtValidadeIntegracaoG, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(txtIntegracaoIdG, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboSetorIntegracaoG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtContratacaoIntegracaoG, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPesquisarIntegNome, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(btnAlterar)
                .addGap(18, 18, 18)
                .addComponent(btnInativar)
                .addGap(45, 45, 45))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboSetorIntegracaoG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(txtIntegAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtValidadeIntegracaoG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIntegracaoIdG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContratacaoIntegracaoG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInativar)
                            .addComponent(btnAlterar))
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPesquisarIntegNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(24, 24, 24))))
        );

        jLabel14.setText("Empresa/Pessoa");

        tblInteg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CPF", "Cidade", "Data Contratação", "Data Válidade"
            }
        ));
        tblInteg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblIntegMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblInteg);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel14)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)))
                .addContainerGap(19, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gerenciador", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        setBounds(0, 0, 900, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void tblEmpresasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpresasMouseClicked
        setar_campos();
        setar_pessoas_tabela();
    }//GEN-LAST:event_tblEmpresasMouseClicked

    private void tblEmpresasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmpresasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblEmpresasKeyReleased

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar_empresa();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        objetos.GeradorRelatorios relatorio = new objetos.GeradorRelatorios();
        relatorio.relatoriointegracaointegradosempresa(txtEmpId.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoasMouseClicked
        // TODO add your handling code here:
        setar_campos_pessoa();
    }//GEN-LAST:event_tblPessoasMouseClicked

    private void txtValidadeIntegracaoGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValidadeIntegracaoGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValidadeIntegracaoGActionPerformed

    private void cboSetorIntegracaoGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSetorIntegracaoGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSetorIntegracaoGActionPerformed

    private void cboSetorIntegracaoGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboSetorIntegracaoGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSetorIntegracaoGKeyPressed

    private void txtContratacaoIntegracaoGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContratacaoIntegracaoGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContratacaoIntegracaoGActionPerformed

    private void txtPesCpfGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfGKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
           // txtPesCnh.requestFocus();
        }
    }//GEN-LAST:event_txtPesCpfGKeyPressed

    private void txtPesCpfGKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesCpfGKeyTyped
        //verifica_cpf_ja_cadastrado(txtPesCpf.getText());
    }//GEN-LAST:event_txtPesCpfGKeyTyped

    private void txtEmpCnpjGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpCnpjGKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            //txtEmpEndereco.requestFocus();
        }
    }//GEN-LAST:event_txtEmpCnpjGKeyPressed

    private void txtEmpContratoGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpContratoGKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtEmpValidade.requestFocus();
        }
    }//GEN-LAST:event_txtEmpContratoGKeyPressed

    private void cboIntegracaoDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntegracaoDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIntegracaoDActionPerformed

    private void cboPesSetorDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPesSetorDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesSetorDActionPerformed

    private void cboPesSetorDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPesSetorDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPesSetorDKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       imprime_integracao();
      
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblIntegMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIntegMouseClicked
        seta_id_integracao();
    }//GEN-LAST:event_tblIntegMouseClicked

    private void btnInativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInativarActionPerformed
         inativar();
         pesquisar_integracao_gerenciador();
    }//GEN-LAST:event_btnInativarActionPerformed

    private void txtPesquisarIntegNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarIntegNomeKeyReleased
        pesquisar_integracao_nome();
    }//GEN-LAST:event_txtPesquisarIntegNomeKeyReleased

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        altera_integracao();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        objetos.GeradorRelatorios relatorio = new objetos.GeradorRelatorios();
        relatorio.relatoriointegracaoempresas();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnInativar;
    private javax.swing.JRadioButton cboIntegracaoD;
    private javax.swing.JComboBox<String> cboPesSetorD;
    private javax.swing.JComboBox<String> cboSetorIntegracaoG;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
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
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblEmpresas;
    private javax.swing.JTable tblInteg;
    private javax.swing.JTable tblPessoas;
    private javax.swing.JFormattedTextField txtContratacaoIntegracaoG;
    private javax.swing.JFormattedTextField txtEmpCnpj;
    private javax.swing.JFormattedTextField txtEmpCnpjG;
    private javax.swing.JTextField txtEmpContrato;
    private javax.swing.JTextField txtEmpContratoG;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEmpIdG;
    private javax.swing.JTextField txtEmpIntegD;
    private javax.swing.JTextField txtEmpNome;
    private javax.swing.JTextField txtEmpNomeG;
    private javax.swing.JFormattedTextField txtEmpValidade;
    private javax.swing.JTextField txtIntegAtivo;
    private javax.swing.JTextField txtIntegracaoIdG;
    private javax.swing.JTextField txtPesCidade;
    private javax.swing.JFormattedTextField txtPesContratacao;
    private javax.swing.JFormattedTextField txtPesCpf;
    private javax.swing.JFormattedTextField txtPesCpfG;
    private javax.swing.JTextField txtPesId;
    private javax.swing.JTextField txtPesIdG;
    private javax.swing.JTextField txtPesNome;
    private javax.swing.JTextField txtPesNomeG;
    private javax.swing.JTextField txtPesTipoG;
    private javax.swing.JFormattedTextField txtPesValidade;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtPesquisarIntegNome;
    private javax.swing.JFormattedTextField txtValidadeIntegracaoG;
    // End of variables declaration//GEN-END:variables
}
