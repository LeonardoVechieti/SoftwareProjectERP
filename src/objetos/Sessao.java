/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

/**
 *
 * @author Leonardo
 */



public class Sessao {
    
    private String id;
    private String empresa;
    private String cnpj;
    private String ref;
    private String cidade;
    private String cep;
    private String endereco;
    private String fantasia;

    public Sessao(String id, String empresa, String cnpj, String ref, String cidade, String cep, String endereco, String fantasia) {
        this.id = id;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.ref = ref;
        this.cidade = cidade;
        this.cep = cep;
        this.endereco = endereco;
        this.fantasia = fantasia;
    }
    public Sessao(){
        
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getFantasia() {
        return fantasia;
    }

    public String getId() {
        return id;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRef() {
        return ref;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
    
    
}
