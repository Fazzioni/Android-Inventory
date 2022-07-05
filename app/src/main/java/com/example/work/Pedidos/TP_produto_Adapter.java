package com.example.work.Pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.work.R;

import java.util.ArrayList;

public class TP_produto_Adapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private ArrayList<TP_produto_item> itens;

    public TP_produto_Adapter(Context context, ArrayList<TP_produto_item> itens)
    {   //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }
    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public TP_produto_item getItem(int position)
    {
        return itens.get(position);
    }
    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        //Pega o item de acordo com a posção.
        TP_produto_item item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.lista_mercadoria_in_pedido, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.



        ((TextView) view.findViewById(R.id.Lista_mercadoria_in_pedido_nome)).setText(item.getCodigo_mercadoria()+". "+item.getNome_mercadoria());

        ((TextView) view.findViewById(R.id.Lista_mercadoria_in_pedido_qtd)).setText(""+item.getQuantidade());
        ((TextView) view.findViewById(R.id.Lista_mercadoria_in_pedido_preco)).setText("$ "+item.getPreco());

        return view;
    }
}
