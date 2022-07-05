package com.example.work.Mercadoria;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.work.MainActivity;
import com.example.work.R;

import java.util.ArrayList;

public class Produto_Adapter  extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<Tproduto_Item> itens;




    public Produto_Adapter(Context context, ArrayList<Tproduto_Item> itens)
    {
        //Itens que preencheram o listview
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
    public Tproduto_Item getItem(int position)
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
        Tproduto_Item item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.produto_lista, null);


        if (position % 2 != 0){
        ((LinearLayout) view.findViewById(R.id.Lista_Produto_layout)).setBackgroundColor(Color.argb(80,255,0,0));
        }else
        ((LinearLayout) view.findViewById(R.id.Lista_Produto_layout)).setBackgroundColor(Color.TRANSPARENT);


        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.lbl_produto_codigo)).setText(""+item.getCodigo());
        ((TextView) view.findViewById(R.id.lbl_produto_nome)).setText(item.getNome());

        switch (MainActivity.SetPedido_Mercadoria_Show_Preco){
            case 0: ((TextView) view.findViewById(R.id.lbl_produto_preco)).setText("R$ "+item.getPreco_ci()+"\nR$"+item.getPreco_si());
            break;
            case 1: ((TextView) view.findViewById(R.id.lbl_produto_preco)).setText("R$ "+item.getPreco_ci());
            break;
            case 2: ((TextView) view.findViewById(R.id.lbl_produto_preco)).setText("R$ "+item.getPreco_si());
            break;
        }




        return view;
    }
}
