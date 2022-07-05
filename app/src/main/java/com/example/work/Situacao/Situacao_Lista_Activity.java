package com.example.work.Situacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.work.MainActivity;
import com.example.work.Pedidos.Pedido_new_Activity;
import com.example.work.R;

import java.util.ArrayList;

public class Situacao_Lista_Activity extends AppCompatActivity {



    ListView grid_situacao_lista;

    private SearchView sv;
    ArrayAdapter<String> adapter;
    private ArrayList<Tsituacao> itens;
    private situacao_adapter adapterListView;


    @Override
    protected void onResume() {
        super.onResume();

        if (sv != null){
            sv.setQuery("", false);
            sv.clearFocus();
            sv.setIconified(true);
            ListarSituacao("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao__lista);
        getSupportActionBar().setTitle("Situação");

        grid_situacao_lista = (ListView) findViewById(R.id.grid_situacao_lista);

        grid_situacao_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.SetPedido_Situacao == true){
                    Tsituacao item = (Tsituacao) grid_situacao_lista.getItemAtPosition(i);

                    Pedido_new_Activity.SetSituacao((int) item.getCodigo());



                    //MainActivity.Alerta(Situacao_Lista_Activity.this,"",item.getNome());
                    /*String str = (String) grid_situacao_lista.getItemAtPosition(i);
                    str = str.substring(0,str.indexOf("."));
                    Pedido_new_Activity.AlteraUser(Integer.parseInt(str));
                    */
                    MainActivity.SetPedido_Situacao  = true;

                    finish();
                }
            }
        });


        grid_situacao_lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tsituacao item = adapterListView.getItem(i);

                //MainActivity.Alerta(Produtos_activity.this,item.getNome(),"");
                Intent intent = new Intent(Situacao_Lista_Activity.this, Situacao_new_Activity.class);
                Situacao_new_Activity.COD_EDIT = item.getCodigo();

                // localizar o cliente
                Situacao_new_Activity.ST_temp = item;

                startActivity(intent);
                return true;
            }
        });
        ListarSituacao("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);

        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
        sv.setQueryHint("Procurar situação");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Log.d("lista",s);
                ListarSituacao("WHERE nome LIKE '%"+s+"%'");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ListarSituacao("WHERE nome LIKE '%"+s+"%'");
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_Insert:
                //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Situacao_Lista_Activity.this, Situacao_new_Activity.class);
                Situacao_new_Activity.COD_EDIT = -1;
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private void ListarSituacao(String param){
         itens = new ArrayList<Tsituacao>();

        Cursor c = MainActivity.db.rawQuery("SELECT Codigo,Nome,cor FROM situacao "+param,null);
        if (c.moveToFirst()){
            do {
                Tsituacao cl = new Tsituacao(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        Integer.parseInt(c.getString(2)),true);
                //         tproduto_items.add(cl);
                itens.add(cl);

            }while(c.moveToNext());
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.cliente_lista );

        adapterListView = new situacao_adapter(this,itens);


        grid_situacao_lista.setAdapter(adapterListView);

        //Cor quando a lista é selecionada para ralagem.
                    //grid_situacao_lista.setCacheColorHint(Color.TRANSPARENT);

        //grid_mercadoria.setAdapter(adapter);

        /*for(Tproduto_Item p: tproduto_items){
          //  Log.d("Lista",p.getCodigo()+": "+p.getNome());
            arrayList.add(p.getCodigo()+".  "+p.getNome());
            adapter.notifyDataSetChanged();
        }*/

    }


}
