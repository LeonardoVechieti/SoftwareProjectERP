/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;
import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import objetos.Sessao;
import javax.swing.JOptionPane;


/**
 *
 * @author Leonardo
 */
public class TelaLogin extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;
Sessao sessao;
private String idEmpresa;
    
    public void logar(){
        //verifica root
        config();
        
        String slq = "select * from usuarios where login = ? and senha = ? and ativo=1";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, txtUsuario.getText());
            String captura = new String(txtSenha.getPassword());
            pst.setString(2, captura);
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                //a linha abaixo obtem o conteudo do compo perfil da tabela usuarios
                String perfil=rs.getString(5); //o 5 é o n campo da tabela
                //System.out.println(perfil); // serve de apoio para seber se deu certo
                //a estrutura abaixo faz o tratamento do perfil do usuario
                
                
                if(perfil.equals("admin")){
                   
                     //a linha abaixo exibe o conteudo do campo da tabela
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    TelaPrincipal.menRel.setEnabled(true);
                    TelaPrincipal.menCadUsu.setEnabled(true);
                    TelaPrincipal.menParametros.setEnabled(true);
                    //linha abaixo troca o nome de quem está logado na tela principal
                    TelaPrincipal.lblUsuarioId.setText(rs.getString(1));
                    TelaPrincipal.lblUsuario.setText(rs.getString(2));
                    this.dispose(); //garante que o formulario feche
                } else if(perfil.equals("supervisor")){
                   
                     //a linha abaixo exibe o conteudo do campo da tabela
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    TelaPrincipal.menRel.setEnabled(true);
                   
                    TelaPrincipal.menCadUsu.setEnabled(true);
                    //linha abaixo troca o nome de quem está logado na tela principal
                    TelaPrincipal.lblUsuarioId.setText(rs.getString(1));
                    TelaPrincipal.lblUsuario.setText(rs.getString(2));
                    this.dispose(); //garante que o formulario feche
                } else{
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                     //linha abaixo troca o nome de quem está logado na tela principal
                    TelaPrincipal.lblUsuarioId.setText(rs.getString(1));
                    TelaPrincipal.lblUsuario.setText(rs.getString(2));
                    
                    this.dispose(); //garante que o formulario feche
                }
                
                idEmpresa=(rs.getString(6));
                    //conexao.close(); //termina a conexao com o banco
                    sessao();
                    
            }else {
                JOptionPane.showMessageDialog(null, "Usuario ou senha inválidos!");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
                    
        }
        
    }
    
    
    public TelaLogin() {
        initComponents();
        //troca o icone do sistema
        trocaIcone();
        conexao = ModuloConexao.conector();
        //System.out.println(conexao); 
        if (conexao != null){
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/dbok.png")));
            //lblStatus.setText("Conectado");
            
        }else{
            lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/dberror.png")));
            //lblStatus.setText("Não Conectado");
        }
        
    }
    
   
    private void sessao(){ //codigo que busca os dados da sessao do usuario e inicia o objeto sessao 

        
        String slq = "select * from parametrosEmpresa where id = ?";
        try {
            pst  = conexao.prepareStatement(slq);
            pst.setString(1, idEmpresa);  
            rs = pst.executeQuery();
            
            if (rs.next()) {
                
                
                TelaPrincipal.lblEmpId.setText(rs.getString(1));
                TelaPrincipal.lblEmpresa.setText(rs.getString(2));//seta no desktop a lbl onde mostra o nome da empresa logada na sessao
                //Passa todos os dados para o objeto
                //Sessao sessao = new Sessao(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                
                //TelaPrincipal.dados_sessao(sessao); // passa o objeto para a tela principal
                    
            }else {
                JOptionPane.showMessageDialog(null, "e");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
                    
        }
    }
    
    //metodo responsavel pro dar um icone a execução do sistema
    private void trocaIcone(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("iconesistema.png")));       
    }
    
    
    
    private void config(){
        
        if(txtUsuario.getText().equals("root")){
            
            TelaConfig config = new TelaConfig();
            
            config.setVisible(true);
        
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        txtSenha = new javax.swing.JPasswordField();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DEV - Sistema de Gestão");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Usuário");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Senha");

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/login3.png"))); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });

        txtSenha.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(txtSenha))
                        .addGap(61, 61, 61))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(342, 200));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed

        //chamnado o metodo logar
       logar();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER){
            txtSenha.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed
          if(evt.getKeyCode() == evt.VK_ENTER){
            btnLogin.requestFocus();
        }
    }//GEN-LAST:event_txtSenhaKeyPressed

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
           if(evt.getKeyCode() == evt.VK_ENTER){
            logar();
        }
    }//GEN-LAST:event_btnLoginKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
