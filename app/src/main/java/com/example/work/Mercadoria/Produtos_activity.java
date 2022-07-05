package com.example.work.Mercadoria;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import java.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.work.MainActivity;
import com.example.work.Pedidos.Pedido_new_produto;
import com.example.work.R;

import java.util.ArrayList;

public class Produtos_activity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    private SearchView sv;
    private ArrayList<Tproduto_Item> itens;
    private Produto_Adapter adapterListView;

    public static Boolean ShowTeclado = false;




    private ListView grid_mercadoria;


    @Override
    protected void onResume() {
        super.onResume();

        if (sv != null) {
         //   sv.setQuery("", false);
         //   sv.clearFocus();
         //   sv.setIconified(true);
         if (sv.getQuery().toString() == "") {
             ListarProdutos("");
         }else{
             if (MainActivity.isLongNumeric(sv.getQuery().toString())) {
                 ListarProdutos("WHERE codigo = "+sv.getQuery().toString());
             }else
                 ListarProdutos("WHERE nome LIKE '%" + sv.getQuery().toString() + "%'");
         }
         }
   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);
        getSupportActionBar().setTitle("Produtos");

        grid_mercadoria = (ListView) findViewById(R.id.grid_produto);

        grid_mercadoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.SetPedido_Mercadoria == true){

                    Tproduto_Item item = adapterListView.getItem(i);
                    Pedido_new_produto.AlteraProduto(item);
                    MainActivity.SetPedido_Mercadoria = false;
                    finish();
                }
            }
        });


        grid_mercadoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tproduto_Item item = adapterListView.getItem(i);

                //MainActivity.Alerta(Produtos_activity.this,item.getNome(),"");
                Intent intent = new Intent(Produtos_activity.this, Produtos_Add_Activity.class);
                Produtos_Add_Activity.COD_EDIT = item.getCodigo();

                // localizar o cliente
                Produtos_Add_Activity.PT_temp = item;
                startActivity(intent);


                return true;
            }
        });


    /*    if (sv != null) {
            sv.setQuery("", false);
            sv.clearFocus();
            sv.setIconified(true);
        };
        */
      ListarProdutos("");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ListarProdutos(String param){
     //   List<Tproduto_Item> tproduto_items = new ArrayList<Tproduto_Item>();
        itens = new ArrayList<Tproduto_Item>();

        Cursor c = MainActivity.db.rawQuery("SELECT Codigo,Nome,preco_ci,preco_si,Item_especial FROM mercadoria "+param,null);
        if (c.moveToFirst()){
            do {
                Tproduto_Item cl = new Tproduto_Item(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                         new BigDecimal(c.getString(2)),
                         new BigDecimal(c.getString(3)),
                        Boolean.parseBoolean(c.getString(4))
                    );


       //         tproduto_items.add(cl);
                itens.add(cl);

            }while(c.moveToNext());
        }


        //arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.cliente_lista);

        adapterListView = new Produto_Adapter(this,itens);


        /*grid_mercadoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String str = (String) grid_mercadoria.getItemAtPosition(pos);
                str = str.substring(0,str.indexOf("."));

                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                Cursor c2 = MainActivity.db.rawQuery("SELECT Codigo, nome, preco_ci, preco_si FROM mercadoria WHERE codigo = "+str,null);
                if (c2.moveToFirst()){
                    Intent intent = new Intent(Produtos_activity.this, Produtos_Add_Activity.class);
                    Produtos_Add_Activity.COD_EDIT = Integer.parseInt(str);

                    // localizar o cliente
                    Produtos_Add_Activity.PT_temp.setCodigo( Integer.parseInt(c2.getString(0)));
                    Produtos_Add_Activity.PT_temp.setNome(    c2.getString(1));
                    Produtos_Add_Activity.PT_temp.setPreco_ci(Double.parseDouble(c2.getString(2)));
                    Produtos_Add_Activity.PT_temp.setPreco_si(Double.parseDouble(c2.getString(3)));
                    startActivity(intent);
                }
                return true;}
        });*/
        grid_mercadoria.setAdapter(adapterListView);

        //Cor quando a lista é selecionada para ralagem.
        grid_mercadoria.setCacheColorHint(Color.TRANSPARENT);

        //grid_mercadoria.setAdapter(adapter);

        /*for(Tproduto_Item p: tproduto_items){
          //  Log.d("Lista",p.getCodigo()+": "+p.getNome());
            arrayList.add(p.getCodigo()+".  "+p.getNome());
            adapter.notifyDataSetChanged();
        }*/

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);

        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
        sv.setQueryHint("Procurar mercadoria");
        //if (sv != null) {
            sv.setQuery("", false);
            sv.clearFocus();
            sv.setIconified(true);

            if (ShowTeclado){
                //sv.setQuery("",true);
                sv.setFocusable(true);
                sv.setIconified(false);
                sv.requestFocusFromTouch();
                ShowTeclado = false;
            }

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Log.d("lista",s);
                if (MainActivity.isLongNumeric(s)) {
                    ListarProdutos("WHERE codigo = "+s);
                }else
                    ListarProdutos("WHERE nome LIKE '%" + s + "%'");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (MainActivity.isLongNumeric(s)) {
                    ListarProdutos("WHERE codigo = "+s);
                }else
                    ListarProdutos("WHERE nome LIKE '%" + s + "%'");

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
                Intent intent = new Intent(Produtos_activity.this, Produtos_Add_Activity.class);
                Produtos_Add_Activity.COD_EDIT = -1;
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
