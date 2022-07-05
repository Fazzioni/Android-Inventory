package com.example.work.Pedidos;

import java.math.BigDecimal;

public class Tpedido {
 public long codigo;
 public long cod_cliente;
// public long cod_situacao;
 public boolean preco_com_imposto;
 public BigDecimal Preco_total;
 public String Data_dia;
// public String Descricao;

 public int situacao_color;
 public String situacao_nome;
 public String nome_Cliente;


    public Tpedido(long codigo, long cod_cliente, BigDecimal preco_total, String data_dia, int situacao_color, String _situacao_nome, String nome_Cliente, boolean preco_com_imposto) {
        this.codigo = codigo;
        this.cod_cliente = cod_cliente;
     //   this.cod_situacao = cod_situacao;
        this.preco_com_imposto = preco_com_imposto;
        Preco_total = preco_total;
        Data_dia = data_dia;
     //   Descricao = descricao;
        this.situacao_color = situacao_color;
        this.nome_Cliente = nome_Cliente;
        this.situacao_nome = _situacao_nome;
    }
    public Tpedido() {
    }

    public int getSituacao_color() {
        return situacao_color;
    }

    public String getSituacao_nome() {
        return situacao_nome;
    }

    public void setSituacao_nome(String situacao_nome) {
        this.situacao_nome = situacao_nome;
    }

    public void setSituacao_color(int situacao_color) {
        this.situacao_color = situacao_color;
    }

    public String getNome_Cliente() {
        return nome_Cliente;
    }

    public void setNome_Cliente(String nome_Cliente) {
        this.nome_Cliente = nome_Cliente;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public long getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(long cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public BigDecimal getPreco_total() {
        return Preco_total;
    }

    public void setPreco_total(BigDecimal preco_total) {
        Preco_total = preco_total;
    }

    public String getData_dia() {
        return Data_dia;
    }

    public void setData_dia(String data_dia) {
        Data_dia = data_dia;
    }

    public boolean isPreco_com_imposto() {
        return preco_com_imposto;
    }

    public void setPreco_com_imposto(boolean preco_com_imposto) {
        this.preco_com_imposto = preco_com_imposto;
    }
}
