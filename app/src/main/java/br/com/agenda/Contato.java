package br.com.agenda;

public class Contato {
    private String id;
    private String nomecontato;
    private String email;
    private String endereco;
    private String genero;
    private String celular;

    @Override
    public String toString() {
        return nomecontato +
                "\n" + celular;
    }

    public String toString2() {
        return nomecontato +
                "\n" + email;
    }

    public Contato(){

    }

    public Contato(String id, String nomecontato, String email, String endereco, String genero, String celular) {
        this.id = id;
        this.nomecontato = nomecontato;
        this.email = email;
        this.endereco = endereco;
        this.genero = genero;
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNomecontato() {
        return nomecontato;
    }

    public void setNomecontato(String nomecontato) {
        this.nomecontato = nomecontato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
