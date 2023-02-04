/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

import br.com.infox.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Leonardo
 */
public class GeradorRelatorios {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public GeradorRelatorios(){
        
        //instacia 
        conexao = ModuloConexao.conector();
            
        }
    
    
    public void relatoriointegracaoempresas() {                                                  
                
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                //filtro.put("ID",Integer.parseInt(lblIdCheckList.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/relatoriointegracaoempresas.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                JasperPrint relatorioprint;        
                relatorioprint = JasperFillManager.fillReport(relatoriocompilado,null,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                JasperViewer.viewReport(relatorioprint,false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
            }
        
        }
    }     
    
    public void relatoriointegracaoempresastercerizadas() {                                                  
                
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                //filtro.put("ID",Integer.parseInt(lblIdCheckList.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/relatoriointegracaoempresastercerizadas.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                JasperPrint relatorioprint;        
                relatorioprint = JasperFillManager.fillReport(relatoriocompilado,null,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                JasperViewer.viewReport(relatorioprint,false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
            }
        
        }
    } 
    
    
    public void relatoriointegracaointegradosgeral() {                                                  
                
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                //filtro.put("ID",Integer.parseInt(lblIdCheckList.getText()));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/relatoriointegracaointegradosgeral.jrxml"));
                //Usando a classe JasperPrint para preparar a impressão do relatório
                JasperPrint relatorioprint;        
                relatorioprint = JasperFillManager.fillReport(relatoriocompilado,null,conexao );
                //Exibe o relatorio usando a classe JasperViewer;
                JasperViewer.viewReport(relatorioprint,false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                //System.out.println(e);
            
            }
        
        }
    } 
    
     public void relatoriointegracaointegradosempresa() {     
         
         String idemp=JOptionPane.showInputDialog("Digite um ID válido (Empresa)");
         
                
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(idemp));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/relatoriointegracaointegradosempresa.jrxml"));
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
     
     public void relatoriointegracaointegradosempresa(String idemp) {     
         
                
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?","Atenção",JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION){
            //Imprime relatorio com o framework JasperReport
            try {
                //passa um parametro para o relatorio
                HashMap filtro = new HashMap(); 
                filtro.put("ID",Integer.parseInt(idemp));
                //Compila o relatorio
                JasperReport relatoriocompilado = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/MyReports/relatoriointegracaointegradosempresa.jrxml"));
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
    
    
}


                                           