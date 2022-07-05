package com.example.work.Pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.work.R;


import java.util.ArrayList;

public class Pedido_Adapter extends  BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Tpedido> itens;


    public Pedido_Adapter(Context context, ArrayList<Tpedido> itens)
    {        //Itens que preencheram o listview
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
    public Tpedido getItem(int position)
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

    public View getView(int position, View view, ViewGroup parent)
    {


        //Pega o item de acordo com a posção.
        Tpedido item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.pedidos_lista, null);





        ((TextView) view.findViewById(R.id.Lista_Pedido_Nome)).setText(item.getNome_Cliente());
        ((TextView) view.findViewById(R.id.Lista_Pedido_codigo)).setText( ""+item.getCodigo() );

        ((TextView) view.findViewById(R.id.Lista_Pedido_data)).setText( item.getData_dia() );


        ((TextView) view.findViewById(R.id.Lista_Pedido_preco)).setText("R$ "+item.getPreco_total());
        ((TextView) view.findViewById(R.id.Lista_Pedido_Situacao_nome)).setText(item.getSituacao_nome());
        ((LinearLayout) view.findViewById(R.id.Lista_Pedido_layout)).setBackgroundColor((int) item.getSituacao_color());
        ((LinearLayout) view.findViewById(R.id.Lista_Pedido_layout)).getBackground().setAlpha(80);


        //lbl_data.setText(formataData.format(Pedido_Data));


        return view;
    }
}
