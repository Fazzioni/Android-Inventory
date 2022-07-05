package com.example.work.Clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import android.widget.SearchView.OnQueryTextListener;


public class ClientesActivity extends AppCompatActivity {



    ListView grid;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    SearchView sv;

    @Override
    protected void onResume() {
        super.onResume();

                    if (sv != null) {
                //   sv.setQuery("", false);
                //   sv.clearFocus();
                //   sv.setIconified(true);
                if (sv.getQuery().toString() == "") {
                    ListarClientes("");
                }else{
                    if (MainActivity.isLongNumeric(sv.getQuery().toString())) {
                        ListarClientes("WHERE codigo = "+sv.getQuery().toString());
                    }else
                        ListarClientes("WHERE nome LIKE '%" + sv.getQuery().toString() + "%'");
                }
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);

        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
        sv.setQueryHint("Procurar cliente");

           sv.setQuery("", false);
           sv.clearFocus();
           sv.setIconified(true);

        sv.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Log.d("lista",s);
                if (MainActivity.isLongNumeric(s)) {
                    ListarClientes("WHERE codigo = "+s);
                }else
                    ListarClientes("WHERE nome LIKE '%" + s + "%'");


                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (MainActivity.isLongNumeric(s)) {
                    ListarClientes("WHERE codigo = "+s);
                }else
                    ListarClientes("WHERE nome LIKE '%" + s + "%'");


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
                Intent intent = new Intent(ClientesActivity.this, Clientes_ADD_Activity.class);
                Clientes_ADD_Activity.COD_EDIT = -1;
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        getSupportActionBar().setTitle("Clientes");

        // SetPedido = false;



        grid = (ListView) findViewById(R.id.Grid);
        ListarClientes("");

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.SetPedido_Cliente == true){

                    String str = (String) grid.getItemAtPosition(i);
                    str = str.substring(0,str.indexOf("."));

                    Pedido_new_Activity.AlteraUser(Integer.parseInt(str));
                    MainActivity.SetPedido_Cliente = false;
                    finish();
                }
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String str = (String) grid.getItemAtPosition(pos);
                str = str.substring(0,str.indexOf("."));

                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                Cursor c2 = MainActivity.db.rawQuery("SELECT Codigo,Nome,endereco,cidade FROM cliente WHERE codigo = "+str,null);
                if (c2.moveToFirst()){
                    Intent intent = new Intent(ClientesActivity.this, Clientes_ADD_Activity.class);
                    Clientes_ADD_Activity.COD_EDIT = Integer.parseInt(str);

                    // localizar o cliente
                    Clientes_ADD_Activity.CL_temp.setCodigo( Integer.parseInt(c2.getString(0)));
                    Clientes_ADD_Activity.CL_temp.setNome(c2.getString(1));
                    Clientes_ADD_Activity.CL_temp.setEnderecao(c2.getString(2));
                    Clientes_ADD_Activity.CL_temp.setCidade(c2.getString(3));
                    if (c2.isNull(2) == false) { Log.d("TESTE", c2.getString(2)); }

                    startActivity(intent);
                    return  true;
                }
                return true;
            }
        });


    }


    public List<Cliente_Item> Lista_todosClientes(String Param){
        List<Cliente_Item> listcliente = new ArrayList<Cliente_Item>();

        Cursor c = MainActivity.db.rawQuery("SELECT Codigo,Nome,endereco,cidade FROM cliente "+Param,null);
        if (c.moveToFirst()){
            do {
                Cliente_Item cl = new Cliente_Item();
                cl.setCodigo(Integer.parseInt(c.getString(0)));
                cl.setNome(c.getString(1));
                cl.setEnderecao(c.getString(2));
                cl.setCidade(c.getString(3));
                listcliente.add(cl);
            }while(c.moveToNext());
        }
        return listcliente;
    }

    public void ListarClientes(String param){
        List<Cliente_Item> cliente_items = Lista_todosClientes(param);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.cliente_lista,arrayList);


        grid.setAdapter(adapter);


        for(Cliente_Item c: cliente_items){
            //Log.d("Lista",c.getCodigo()+": "+c.getNome());
            arrayList.add(c.getCodigo()+".  "+c.getNome());
            adapter.notifyDataSetChanged();
        }

    }


}