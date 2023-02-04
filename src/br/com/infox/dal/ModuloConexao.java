/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.dal;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import propiedades.config;

/**
 *
 * @author Leonardo
 */
public class ModuloConexao {
    
    
    public static Connection conector() {
        
        config confi = new config();
        try {
            confi.lerarquivo();
        } catch (IOException ex) {
            Logger.getLogger(ModuloConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Connection conexao = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url= confi.getUrl();
        //System.out.println("Nome = "+url);
        String user=confi.getUser();
        String password=confi.getPassword();
            try {
                Class.forName(driver);
                conexao = DriverManager.getConnection(url, user, password);
                return conexao;
            } catch (Exception e) {
                System.out.println(e);
                return null ;
            }
        
        
    }
    
    
    
    
}
        //String url= "jdbc:mysql://localhost:3306/dbinfox";
       // String user="dev";
       // String password=  "dev123c