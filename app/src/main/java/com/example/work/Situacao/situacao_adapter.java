package com.example.work.Situacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.work.R;

import java.util.ArrayList;

public class situacao_adapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<Tsituacao> itens;

    public situacao_adapter(Context context, ArrayList<Tsituacao> itens)
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
    public Tsituacao getItem(int position)
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
        Tsituacao item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.situacao_lista, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        TextView lbl =  (TextView) view.findViewById(R.id.Situacao_Layout_lista_text);
        lbl.setText(""+item.getNome());

        lbl.setBackgroundColor( (int) item.getColor() );

        //view.setBackgroundColor((int) item.getColor());

        ((LinearLayout) view.findViewById(R.id.Situacao_Layout_lista_linear)).setBackgroundColor((int) item.getColor() );
        //


        return view;
    }
}
