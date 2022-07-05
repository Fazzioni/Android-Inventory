package com.example.work.Situacao;

public class Tsituacao {
    public long codigo;
    public String nome;
    public long color;
    public Boolean filtred;

    public Tsituacao(long codigo, String nome, long color, Boolean Filtred) {
        this.codigo = codigo;
        this.nome = nome;
        this.color = color;
        this.filtred = Filtred;
    }

    public Tsituacao() {
    }

    public Boolean getFiltred() {
        return filtred;
    }

    public void setFiltred(Boolean filtred) {
        this.filtred = filtred;
    }

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
    public long getColor() {
        return color;
    }
    public void setColor(long color) {
        this.color = color;
    }
}
