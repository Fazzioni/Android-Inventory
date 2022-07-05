package com.example.work.Clientes;

public class Cliente_Item {
    public long codigo;
    public String nome;
    public String cidade;
    public String enderecao;

    public Cliente_Item(long _codigo, String _nome, String _cidade, String _endereco) {
        this.codigo = _codigo;
        this.nome = _nome;
        this.cidade = _cidade;
        this.enderecao = _endereco;

    }

    public Cliente_Item() {

    }

    //====================================================================
    public long getCodigo() {
        return codigo;
    }
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getEnderecao() {
        return enderecao;
    }
    public void setEnderecao(String enderecao) {
        this.enderecao = enderecao;
    }
}