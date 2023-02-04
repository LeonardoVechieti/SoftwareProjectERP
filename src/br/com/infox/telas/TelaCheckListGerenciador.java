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
import static br.com.infox.telas.TelaPrincipal.desktop;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;


public class TelaCheckListGerenciador extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaCheckListGerenciador() {
        initComponents();
        conexao = ModuloConexao.conector();
        
        buscar_checklist_aberto();
        
        java.util.Date data = new java.util.Date();
        //DateFormat formatador;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        //formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        txtData.setText(formato.format(data));
        buscar_checklist_finalizados_data();
        
    }
    
    private void buscar_checklist_aberto(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select c.id as Nº,DATE_FORMAT (c.datageracao,'%d/%m/%Y') as Data, v.placa as Placa, p.nome as Motorista, e.nome as Transportadora, d.destino as Destino from checklist c\n" +
"join destinos d\n" +
"on c.iddestino = d.id\n" +
"join pessoas p\n" +
"on c.idpes = p.idpes\n" +
"join empresas e\n" +
"on c.idemp= e.idemp\n" +
"join veiculos v \n" +
"on c.idvei= v.idvei\n" +
"where c.statusCheck =1";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            //pst.setString(1, txtVeiPesquisar.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblCheckList.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void buscar_checklist_finalizados_data(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select c.id as Nº,DATE_FORMAT (c.datageracao,'%d/%m/%Y') as Data, v.placa as Placa, p.nome as Motorista, e.nome as Transportadora, d.destino as Destino from checklist c\n" +
"join destinos d\n" +
"on c.iddestino = d.id\n" +
"join pessoas p\n" +
"on c.idpes = p.idpes\n" +
"join empresas e\n" +
"on c.idemp= e.idemp\n" +
"join veiculos v \n" +
"on c.idvei= v.idvei\n" +
"where c.statusCheck =0 and date(`datageracao`)=?";
        try {
            pst=conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            String dataRecebida = txtData.getText(); //passa a String para a variavel string  
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); //cria o perfil brasileiro
            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd"); //cria o perfil americano
            java.util.Date dataFormatada =  formato.parse(dataRecebida); //formata no padrão brasileiro
            String dataInsert=formato2.format(dataFormatada);
            pst.setString(1, dataInsert);
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblCheckListFinalizados.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void buscar_checklist_finalizados_placa(){ //Atencão SQL tambem coloca apelido no nome dos campos
        String sql="select c.id as Nº,DATE_FORMAT (c.datageracao,'%d/%m/%Y') as Data, v.placa as Placa, p.nome as Motorista, e.nome as Transportadora, d.destino as Destino from checklist c\n" +
"join destinos d\n" +
"on c.iddestino = d.id\n" +
"join pessoas p\n" +
"on c.idpes = p.idpes\n" +
"join empresas e\n" +
"on c.idemp= e.idemp\n" +
"join veiculos v \n" +
"on c.idvei= v.idvei\n" +
"where c.statusCheck =0 and v.placa like ? \n"
                + "order by  c.id desc";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1, txtPlacaFinalizados.getText() + "%");
            rs=pst.executeQuery();
            // a linha abaixo usa a bliblioteca rs2xml.jar para preenceher a tabela
            tblCheckListFinalizados.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void seta_checklist(){ 
        String id;
        int setar = tblCheckList.getSelectedRow();
        id=tblCheckList.getModel().getValueAt(setar,0).toString();
        abrir_checklist(id);
        
    }
    
    
    
    private void abrir_checklist(String id){
        TelaCheckList checklist = new TelaCheckList();
        checklist.setVisible(true);
        desktop.add(checklist);
        checklist.setarCheckList(id);
        checklist.moveToFront();
    }
    
    private void seta_checklist_finalizado(){ 
        String id;
        int setar = tblCheckListFinalizados.getSelectedRow();
        id=tblCheckListFinalizados.getModel().getValueAt(setar,0).toString();
        abrir_checklist_finalizado(id);
        
    }
    
    
    
    private void abrir_checklist_finalizado(String id){
        TelaCheckList checklist = new TelaCheckList();
        checklist.setVisible(true);
        desktop.add(checklist);
        checklist.setarCheckList(id);
        checklist.moveToFront();
    }
    
    private void pesquisa_avancada(){
        TelaPesquisa pesquisa = new TelaPesquisa();
        pesquisa.setVisible(true);
        pesquisa.setTitle("Pesquisa (CheckList)");
        pesquisa.seleciona_botao_pesquisa("checklist");
        pesquisa.cboCampo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 - Nome", "2 - CPF", "3 - Placa", "4 - Transportadora" }));
        
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCheckList = new javax.swing.JTable();
        btnAtualizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCheckListFinalizados = new javax.swing.JTable();
        btnAtualizar1 = new javax.swing.JButton();
        txtData = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPlacaFinalizados = new javax.swing.JTextField();
        txtPlaca = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnPesquisaAvancada = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Demonstrativo - RQ_SLG1002");
        setPreferredSize(new java.awt.Dimension(700, 440));
        setRequestFocusEnabled(false);

        tblCheckList = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCheckList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblCheckList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCheckList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCheckListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCheckList);

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

        jLabel1.setText("Obs: Duplo clique para vizualizar e gerenciar os dados.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(btnAtualizar)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Não Finalizados", jPanel1);

        tblCheckListFinalizados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCheckListFinalizados.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblCheckListFinalizados.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCheckListFinalizados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCheckListFinalizadosMouseClicked(evt);
            }
        });
        tblCheckListFinalizados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCheckListFinalizadosKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblCheckListFinalizados);

        btnAtualizar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/refresh_icon48.png"))); // NOI18N
        btnAtualizar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAtualizar1MouseClicked(evt);
            }
        });
        btnAtualizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizar1ActionPerformed(evt);
            }
        });

        txtData.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtData.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txtData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataActionPerformed(evt);
            }
        });
        txtData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N
        jLabel2.setText("Data");

        txtPlacaFinalizados.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txtPlacaFinalizados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtPlacaFinalizados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPlacaFinalizadosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlacaFinalizadosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPlacaFinalizadosKeyTyped(evt);
            }
        });

        txtPlaca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPlaca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N
        txtPlaca.setText("Placa");

        jLabel5.setText("Obs: Duplo clique para vizualizar e gerenciar os dados.");

        btnPesquisaAvancada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPesquisaAvancada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/pesquisar.png"))); // NOI18N
        btnPesquisaAvancada.setText("Pesquisar Avançada");
        btnPesquisaAvancada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaAvancadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPlaca)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPlacaFinalizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnPesquisaAvancada, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)))
                .addGap(18, 18, 18)
                .addComponent(btnAtualizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAtualizar1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPesquisaAvancada))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPlaca)
                                    .addComponent(txtPlacaFinalizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 7, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Finalizados ", jPanel2);

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

    private void tblCheckListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCheckListMouseClicked
       seta_checklist();
    }//GEN-LAST:event_tblCheckListMouseClicked

    private void tblCheckListFinalizadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCheckListFinalizadosMouseClicked
       seta_checklist_finalizado();
    }//GEN-LAST:event_tblCheckListFinalizadosMouseClicked

    private void btnAtualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtualizarMouseClicked

    }//GEN-LAST:event_btnAtualizarMouseClicked

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        buscar_checklist_aberto();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnAtualizar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtualizar1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtualizar1MouseClicked

    private void btnAtualizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizar1ActionPerformed
        buscar_checklist_finalizados_data();
    }//GEN-LAST:event_btnAtualizar1ActionPerformed

    private void txtDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataActionPerformed

    private void txtDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataKeyPressed
          if(evt.getKeyCode() == evt.VK_ENTER){
              buscar_checklist_finalizados_data();
          }
    }//GEN-LAST:event_txtDataKeyPressed

    private void txtPlacaFinalizadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaFinalizadosKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            //txtVeiPlacaCarreta.requestFocus();
        }
    }//GEN-LAST:event_txtPlacaFinalizadosKeyPressed

    private void txtPlacaFinalizadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaFinalizadosKeyTyped
        //deixar o que foi digitado em letra maiuscula
        String temp = txtPlacaFinalizados.getText();
        txtPlacaFinalizados.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtPlacaFinalizadosKeyTyped

    private void txtPlacaFinalizadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaFinalizadosKeyReleased
        buscar_checklist_finalizados_placa();
    }//GEN-LAST:event_txtPlacaFinalizadosKeyReleased

    private void tblCheckListFinalizadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCheckListFinalizadosKeyReleased
        buscar_checklist_finalizados_placa();
    }//GEN-LAST:event_tblCheckListFinalizadosKeyReleased

    private void btnPesquisaAvancadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaAvancadaActionPerformed
        //abre tela de pesquisa
        pesquisa_avancada();
    }//GEN-LAST:event_btnPesquisaAvancadaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnAtualizar1;
    private javax.swing.JButton btnPesquisaAvancada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblCheckList;
    private javax.swing.JTable tblCheckListFinalizados;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JLabel txtPlaca;
    private javax.swing.JTextField txtPlacaFinalizados;
    // End of variables declaration//GEN-END:variables
}
