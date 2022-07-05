package com.example.work.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.work.MainActivity;
import com.example.work.R;
import com.example.work.Situacao.Tsituacao;
import com.example.work.Situacao.situacao_adapter_filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FiltroActivity extends AppCompatActivity  implements  DatePickerDialog.OnDateSetListener{

    TextView Filtro_lbl_dini, Filtro_lbl_dend;
    Toolbar tool_situacao;
    private ListView grid_list_situacao;

    private ArrayList<Tsituacao> itens;
    private situacao_adapter_filter adapterListView;

    String Dini,Dend = "NULO";

    TextView LblData;

     private void showDatePickerDialog(){
         int Data_day;
         int Data_month;
         int data_Year;

         if (LblData.getText() == "NULO"){
             java.util.Date data = new Date();
             Data_day   = Integer.parseInt((String) android.text.format.DateFormat.format("dd",   data));
             Data_month = Integer.parseInt((String) android.text.format.DateFormat.format("MM",   data));
             data_Year  = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", data));
         }else{
             SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
             java.util.Date data = null;
             try {
                 data = format.parse(LblData.getText().toString());
                 Data_day   = Integer.parseInt((String) android.text.format.DateFormat.format("dd",   data));
                 Data_month = Integer.parseInt((String) android.text.format.DateFormat.format("MM",   data));
                 data_Year  = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", data));
             } catch (ParseException e) {
                 e.printStackTrace();
                 LblData.setText("NULO");
                 showDatePickerDialog();
                 return;
             }
           }
        DatePickerDialog datePickerDialog = new DatePickerDialog(FiltroActivity.this,
                FiltroActivity.this,
                data_Year, Data_month-1, Data_day);
        datePickerDialog.show();
    }


    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
       int Data_day = i2;
       int Data_month = i1+1;
       int Year = i;
        //Pedido_Data = getDate(i,i1,i2);
        LblData.setText(Data_day+"/"+Data_month+"/"+Year);
    }



    @Override
    protected void onResume() {
        super.onResume();
        ListarSituacao();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tool_situacao = (Toolbar) findViewById(R.id.Filtro_Tool_Bar_2);
        tool_situacao.inflateMenu(R.menu.situacao_all_itens);

        tool_situacao.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.S_L_M_All:
                            for (int i=0; i< adapterListView.getCount();i++){
                            adapterListView.getItem(i).setFiltred(true);
                            adapterListView.notifyDataSetChanged();
                           }



                        break;
                    case
                      R.id.S_L_M_null:
                        for (int i=0; i< adapterListView.getCount();i++){
//                            itens.get(i).setFiltred(false);
                            adapterListView.getItem(i).setFiltred(false);
                            adapterListView.notifyDataSetChanged();
                        }


                        break;
                }
                return false;
            }
        });



        grid_list_situacao = (ListView) findViewById(R.id.Filtro_list_view);
        Filtro_lbl_dini = (TextView) findViewById(R.id.Filtro_lbl_dini);
        Filtro_lbl_dend = (TextView) findViewById(R.id.Filtro_dend);


        Filtro_lbl_dini.setText(MainActivity.LoadCfg(1));
        Filtro_lbl_dend.setText(MainActivity.LoadCfg(2));

        if (Filtro_lbl_dini.getText().toString() == ""){Filtro_lbl_dini.setText("NULO");}
        if (Filtro_lbl_dend.getText().toString() == ""){Filtro_lbl_dend.setText("NULO");}

        Filtro_lbl_dini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_datapicker(Filtro_lbl_dini);
            }
        });

        Filtro_lbl_dend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_datapicker(Filtro_lbl_dend);
            }
        });


        grid_list_situacao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               adapterListView.getItem(i).setFiltred(!adapterListView.getItem(i).getFiltred());
               adapterListView.notifyDataSetChanged();
           }
        });



    }

    private void show_datapicker(TextView lbl) {
            LblData = lbl;
            showDatePickerDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Menu_item_FILTER_post:
                    PostFilter();


                // MainActivity.Show_info= "Item salvo";
             break;

            case R.id.Menu_item_FILTER_clear:
                for (int i=0; i< adapterListView.getCount();i++){
                    adapterListView.getItem(i).setFiltred(true);
                    adapterListView.notifyDataSetChanged();}
                Dini = "NULO";
                Dend = Dini;

                ((TextView) findViewById(R.id.Filtro_lbl_dini)).setText(Dini);
                ((TextView) findViewById(R.id.Filtro_dend)).setText(Dend);
             break;

            case android.R.id.home:
            finish();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void PostFilter() {
        Log.d("TESTE","FILTRO POSTING");
         Boolean b = true;
         String sql = "";

         for (int i=0; i <itens.size();i++){
             if (itens.get(i).getFiltred() == false){
             b = false;
             }else{
             sql = sql + ", "+itens.get(i).getCodigo();
             }

         MainActivity.ExecutaSql("UPDATE situacao SET Filtred = '"+itens.get(i).getFiltred()+"' WHERE codigo = "+itens.get(i).getCodigo());
         }

        Log.d("TESTE","FILTRO Saving sql");
        if (b){
            MainActivity.UpdateCfg(3,"");
        } else {
            if (sql != ""){
                MainActivity.UpdateCfg(3, "WHERE A.cod_situacao IN (" + sql.substring(1) + ")");
                }else{
                MainActivity.UpdateCfg(3, "");}

        }


        Log.d("TESTE","FILTRO sql saved");

        if (Filtro_lbl_dend.getText().toString() == "NULO"){Filtro_lbl_dend.setText("");}
        if (Filtro_lbl_dini.getText().toString() == "NULO"){Filtro_lbl_dini.setText("");}

        MainActivity.UpdateCfg(1,Filtro_lbl_dini.getText().toString());
        MainActivity.UpdateCfg(2,Filtro_lbl_dend.getText().toString());


        finish();
        Log.d("TESTE","FILTRO FINISH");
       //se b= true entao, nenhuma situacao filtrada
        /*
        sql = "WHERE (A.data_pedido between '"+convertStringToDate(((TextView) findViewById(R.id.Filtro_lbl_dini)).getText().toString())+
                "' and '"+convertStringToDate(((TextView) findViewById(R.id.Filtro_dend)).getText().toString())+"') " +
                "" +
                "" +
                " AND A.cod_situacao IN "+sql+"";
*/
         //sql = "WHERE A.cod_situacao IN "+sql+"";
        //MainActivity.Alerta(FiltroActivity.this,"",sql);
        //Pedidos_Lista_Activity.Param = sql;

       // salvar os itens

    }

    public static String convertStringToDate(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date datetmp = null;
        try {
            datetmp = format.parse(dtStart);

            format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(datetmp);


        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }




    private void ListarSituacao(){
        itens = new ArrayList<Tsituacao>();

        Cursor c = MainActivity.db.rawQuery("SELECT Codigo,Nome,cor,Filtred FROM situacao",null);
        if (c.moveToFirst()){
            do {
                Tsituacao cl = new Tsituacao(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        Integer.parseInt(c.getString(2)),Boolean.parseBoolean(c.getString(3)));

                //         tproduto_items.add(cl);
                itens.add(cl);

            }while(c.moveToNext());
        }

        adapterListView = new situacao_adapter_filter(this,itens);
        grid_list_situacao.setAdapter(adapterListView);

        //Cor quando a lista é selecionada para ralagem.
        //grid_situacao_lista.setCacheColorHint(Color.TRANSPARENT);

        //grid_mercadoria.setAdapter(adapter);

        /*for(Tproduto_Item p: tproduto_items){
          //  Log.d("Lista",p.getCodigo()+": "+p.getNome());
           arrayList.add(p.getCodigo()+".  "+p.getNome());
        }*/
    }

}
