package com.example.work.Mercadoria;


import java.math.BigDecimal;

public class Tproduto_Item {
    public long codigo;
    public String nome;
    public java.math.BigDecimal preco_ci;
    public java.math.BigDecimal preco_si;
    public Boolean Item_especial;

    //========================================

    public Tproduto_Item() {
    }


    public Tproduto_Item(long codigo, String nome, BigDecimal preco_ci, BigDecimal preco_si, Boolean _Item_especial) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco_ci = preco_ci;
        this.preco_si = preco_si;
        this.Item_especial = _Item_especial;
    }

    public Boolean getItem_especial() {
        return Item_especial;
    }

    public void setItem_especial(Boolean item_especial) {
        Item_especial = item_especial;
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

    public BigDecimal getPreco_ci() {
        return preco_ci;
    }

    public void setPreco_ci(BigDecimal preco_ci) {
        this.preco_ci = preco_ci;
    }

    public BigDecimal getPreco_si() {
        return preco_si;
    }

    public void setPreco_si(BigDecimal preco_si) {
        this.preco_si = preco_si;
    }
}
