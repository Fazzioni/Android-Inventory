package com.example.work.Pedidos;

import java.math.BigDecimal;

public class TP_produto_item {
    public long codigo_livro;
    public long codigo_mercadoria;
    public String Nome_mercadoria;
    public BigDecimal quantidade;
    public BigDecimal preco;

    public TP_produto_item(long codigo_livro, long codigo_mercadoria, String nome_mercadoria, BigDecimal quantidade, BigDecimal preco) {
        this.codigo_livro = codigo_livro;
        this.codigo_mercadoria = codigo_mercadoria;
        this.Nome_mercadoria = nome_mercadoria;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getNome_mercadoria() {
        return Nome_mercadoria;
    }

    public void setNome_mercadoria(String nome_mercadoria) {
        Nome_mercadoria = nome_mercadoria;
    }

    public TP_produto_item() {
    }

    public long getCodigo_livro() {
        return codigo_livro;
    }

    public void setCodigo_livro(long codigo_livro) {
        this.codigo_livro = codigo_livro;
    }

    public long getCodigo_mercadoria() {
        return codigo_mercadoria;
    }

    public void setCodigo_mercadoria(long codigo_mercadoria) {
        this.codigo_mercadoria = codigo_mercadoria;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
