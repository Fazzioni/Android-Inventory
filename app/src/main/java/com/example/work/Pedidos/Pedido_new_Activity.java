package com.example.work.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.work.Clientes.ClientesActivity;
import com.example.work.MainActivity;
import com.example.work.R;
import com.example.work.Situacao.Situacao_Lista_Activity;

public class Pedido_new_Activity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    public static TextView lbl_nome, lbl_info, lbl_menu_imposto, lbl_situacao;
    public static LinearLayout layout_situacao;
    public static boolean adicionaNovoProduto = false;

    private static LinearLayout ctlayout, btn_menu_imposto;
    private static Toolbar tool_produto;


    String Lastnome, lastinfo;

    public static long Cliente_Codigo = -1;
    public static long Situacao_codigo = -1;
    public static long Pedido_codigo = -1;

    private static BigDecimal Pedido_Preco = BigDecimal.valueOf(0);
    private static int Pedido_Data_day, Pedido_Data_month, Pedido_data_Year;
    public static boolean Usar_preco_imposto = true;
    public static boolean First_insert;

    private static TextView lbl_data;
    private static EditText Edit_Pedido_descricao;

    private static Boolean NotPost = false; // if it true then not post because load de default values

    private static ActionBar Barra_top;

    private static boolean Registro_Editado = false;
    static ProgressDialog progressDoalog;


    //======================================================
 //   private static com.example.finish.NonScrollListView list_grid_mercadorias_inProduto;
    private static ListView Lista_Pedido_New_Grid_Mercadoria_2;

    //private static ArrayAdapter<String> adapter;
    private static ArrayList<TP_produto_item> itens;
    private static TP_produto_Adapter adapterListView;
//======================================================

    public static TP_produto_item Tmp_long_itemClick;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void someTimeConsumingWork(long milisToWork){
        SystemClock.sleep(milisToWork);
    }



    @Override
    protected void onResume() {
     super.onResume();
     hideKeyboard();

  /*   if (adicionaNovoProduto){     //ao inserir um novo produto o prox é automático
         adicionaNovoProduto = false;
         AdicionaNovoProduto();
     }
*/


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_new);

       Lista_Pedido_New_Grid_Mercadoria_2 = (ListView) findViewById(R.id.Lista_Pedido_New_Grid_Mercadoria_2);
       Lista_Pedido_New_Grid_Mercadoria_2.addHeaderView(getLayoutInflater().inflate(R.layout.header_listview,null));
       Lista_Pedido_New_Grid_Mercadoria_2.addFooterView(getLayoutInflater().inflate(R.layout.footerlistview,null));
       Lista_Pedido_New_Grid_Mercadoria_2.setAdapter(null);


        Log.d("teste","START ONCREATE");



        Barra_top = getSupportActionBar();

        NotPost = true;

        Edit_Pedido_descricao = (EditText) findViewById(R.id.edit_pedido_descricao);
        Edit_Pedido_descricao.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        tool_produto = (Toolbar) findViewById(R.id.tool_produtos);
        tool_produto.inflateMenu(R.menu.menu_pedidos_lista);

        for (int i=0; i < tool_produto.getMenu().size(); i++){
              tool_produto.getMenu().getItem(i).setVisible( tool_produto.getMenu().getItem(i).getItemId() == R.id.item_add );

        }
       /* ((MenuItem) tool_produto.getMenu().findItem(R.id.item_del)).setVisible(false);
        ((MenuItem) tool_produto.getMenu().findItem(R.id.iten_Filter_SV_Pedido)).setVisible(false);
        ((MenuItem) tool_produto.getMenu().findItem(R.id.item_Lista_Pedidos_ITEM_ESPECIAL)).setVisible(false);
        ((MenuItem) tool_produto.getMenu().findItem(R.id.item_Lista_Pedidos_todos)).setVisible(false);
        ((MenuItem) tool_produto.getMenu().findItem(R.id.item_Lista_filter)).setVisible(false);
        */







        // ((MenuItem) tool_produto.getMenu().findItem(R.id.item_search)).setVisible(true);

        lbl_data = (TextView) findViewById(R.id.pedido_lbl_data);
        // java.util.Date dataUtil = new java.util.Date();
        //Pedido_Data = new java.sql.Date(dataUtil.getDate());
        //SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        //Pedido_Data = new Date();
        //lbl_data.setText(formataData.format(Pedido_Data));

        Date data = new Date();
        Pedido_Data_day   = Integer.parseInt((String) android.text.format.DateFormat.format("dd",   data));
        Pedido_Data_month = Integer.parseInt((String) android.text.format.DateFormat.format("MM",   data));
        Pedido_data_Year  = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", data));

        lbl_data.setText(Pedido_Data_day+"/"+Pedido_Data_month+"/"+Pedido_data_Year);



        lbl_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        findViewById(R.id.pedidos_data_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        tool_produto.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_add:
                        First_insert = true;
                        Intent intent = new Intent( Pedido_new_Activity.this, Pedido_new_produto.class);

                        //.SetPedido_Mercadoria_com_imposto
                        //Intent intent = new Intent(Pedido_new_Activity.this, Pedido_new_produto.class);
                        startActivity(intent);
                        break;

             }


                return false;
            }
        });

        lastinfo ="";
        Lastnome = "";

        lbl_situacao = (TextView) findViewById(R.id.pedido_lbl_situacao);
        layout_situacao = (LinearLayout) findViewById(R.id.pedidos_situacao_layout);

        lbl_nome = (TextView) findViewById(R.id.Pedido_lbl_cliente);
        lbl_info = (TextView) findViewById(R.id.pedido_lbl_info);
        ctlayout = (LinearLayout) findViewById(R.id.pedidos_Ctl2);

        ctlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {ClicksetUser();return false;
            }
        });
        lbl_info.setOnLongClickListener(new View.OnLongClickListener() {
            @Override  public boolean onLongClick(View view) { ClicksetUser(); return false;
            }
        });
        lbl_nome.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) { ClicksetUser(); return false;
            }
        });

        ctlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ClicksetUser();
            }
        });
        lbl_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ClicksetUser();
            }
        });
        lbl_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ClicksetUser();
            }
        });

        Pedido_new_Activity.lbl_nome.setText("");
        Pedido_new_Activity.lbl_info.setText("");

       // list_grid_mercadorias_inProduto = (com.example.finish.NonScrollListView) findViewById(R.id.Lista_Pedido_New_Grid_Mercadoria);





        Log.d("teste","breake 10");


        Lista_Pedido_New_Grid_Mercadoria_2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("teste","START ITEM LONG CLICK");

                if (i == 0){ return false;}

                Log.d("Teste","position: "+(i-1)+" de "+adapterListView.getCount());

                if (i-1 >= adapterListView.getCount()){ return  false;}

                Tmp_long_itemClick = (TP_produto_item) adapterListView.getItem(i-1);
                //item.getCodigo_livro()
                First_insert = false;
                Intent intent = new Intent( Pedido_new_Activity.this, Pedido_new_produto.class);
                startActivity(intent);
                Log.d("teste","FINISH ITEM LONG CLICK");
                return false;
            }
    });


        btn_menu_imposto = (LinearLayout) findViewById(R.id.pedidos_imposto_lt);
        btn_menu_imposto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuExample();
            }
        });
        lbl_menu_imposto = (TextView) findViewById(R.id.pedido_lbl_imposto_preco);
        lbl_menu_imposto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuExample();
            }
        });

        lbl_situacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClicksetSotiacao();
            }
        });
        layout_situacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ClicksetSotiacao(); }
        });


        Pedido_codigo = -1;
        Cliente_Codigo = -1;
        Situacao_codigo = -1;
        Pedido_Preco = BigDecimal.valueOf(0);
        Edit_Pedido_descricao.setText("");
        Usar_preco_imposto = false;


/*        progressDoalog = new ProgressDialog(Pedido_new_Activity.this);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setProgress(0);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("ProgressDialog bar example");
        progressDoalog.show();
*/

//SetPedido(Pedidos_Lista_Activity.Set_codigo_pedido,getApplicationContext());
        NotPost = false;




        //DEFINING A NEW THREAD WHICH WOULD DO SOME WORK AND THEN DISMISS THE DIALOG
        Thread workThread=new Thread(new Runnable(){
            @Override
            public void run() {
//@ albertsmus :call your getAllEntos() here!
                someTimeConsumingWork(10);



                //ONLY ONE THREAD CAN HANDLE UI, INCLUDES DISSMISSAL OF
                //PROGRESS DIALOG. runOnUiThread WILL DO ITS MAGIC HERE!
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Log.d("TESTE","START SETPEDIDO");
                        SetPedido(Pedidos_Lista_Activity.Set_codigo_pedido,Pedido_new_Activity.this);
                        Log.d("TESTE","NEXT LINE FROM SETPEDIDO");

                        progressDoalog.dismiss();
                        //Toast.makeText(Pedido_new_Activity.this, "Work done!", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        workThread.start();
        progressDoalog=ProgressDialog.show(this, "Carregando banco de dados", "buscando mercadorias...");




       /* list_grid_mercadorias_inProduto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScrollView scrollView = findViewById(R.id.SCROOL);

                scrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break; }

                return false;
            }
        });
        */

        Log.d("teste","ONCREATE OK");
        Registro_Editado = false;
    }



    public static void SetPedido(long cod_new,final Context ctx) {

        Log.d("teste","START SetPedido()");

        if (cod_new != -1) {


            Cursor c = MainActivity.db.rawQuery("SELECT cod_cliente, preco_total, data_pedido, preco_com_imposto, " +
                    "           cod_situacao, descricao FROM pedido WHERE codigo = "+cod_new, null);
            if (c.moveToFirst()) {
                Pedido_codigo = cod_new;


                Cliente_Codigo = Long.parseLong(c.getString(0));
                Pedido_Preco = new BigDecimal(c.getString(1));

                SetStringBDCToDatedays(c.getString(2));


                Usar_preco_imposto = Boolean.parseBoolean(c.getString(3));
                Situacao_codigo = Long.parseLong(c.getString(4));
                Edit_Pedido_descricao.setText(c.getString(5));


                AlteraUser(Cliente_Codigo);



                ListarMercadoriasInProdutoNew(ctx);



















            }
        } else{
            Cliente_Codigo = -1;
            Situacao_codigo = -1;
            Pedido_codigo = -1;
            Pedido_Preco = BigDecimal.valueOf(0);
            Edit_Pedido_descricao.setText("");
            Usar_preco_imposto = false;

        }

        tool_produto.setTitle("Preço total R$ "+Pedido_Preco);
        Atualiza_informacao_imposto(ctx);
        SetSituacao(Situacao_codigo);
        Barra_top.setTitle("Pedido: "+Pedido_codigo);


        Log.d("TESTE","FINISH SETPEDIDO");

    }


    public static void SetStringBDCToDatedays(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date datetmp = null;
        try {
            datetmp = format.parse(dtStart);

            Pedido_Data_day    = Integer.parseInt((String) android.text.format.DateFormat.format("dd",  datetmp));
            Pedido_Data_month  = Integer.parseInt((String) android.text.format.DateFormat.format("MM",  datetmp));
            Pedido_data_Year   = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy",datetmp));

            Pedido_new_Activity.lbl_data.setText(Pedido_Data_day+"/"+Pedido_Data_month+"/"+Pedido_data_Year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog(){



    DatePickerDialog datePickerDialog = new DatePickerDialog(Pedido_new_Activity.this,
            Pedido_new_Activity.this,
            Pedido_data_Year, Pedido_Data_month-1, Pedido_Data_day);
        datePickerDialog.show();
}

    private void popupMenuExample() {
        PopupMenu popup = new PopupMenu(Pedido_new_Activity.this, btn_menu_imposto);
        popup.getMenuInflater().inflate(R.menu.menu_pedidos_preco, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_pedido_preco_ci:{
                        Usar_preco_imposto = true;
                        Atualiza_informacao_imposto(getApplicationContext());

                        MainActivity.Alerta(Pedido_new_Activity.this,"","preco com imposto, verificar se precisa atualizar as mercadorias");
                        break;
                    }
                    case R.id.menu_pedido_preco_si:{
                        Usar_preco_imposto = false;
                        Atualiza_informacao_imposto(getApplicationContext());

                        MainActivity.Alerta(Pedido_new_Activity.this,"","preco sem imposto, verificar se precisa atualizar as mercadorias");
                        break;
                    }


                }

                PostPedido();
                //Toast.makeText(Pedido_new_Activity.this,item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    private void ClicksetSotiacao(){
        MainActivity.SetPedido_Situacao  = true;
        Intent intent = new Intent(Pedido_new_Activity.this, Situacao_Lista_Activity.class);
        startActivity(intent);
    }

    private void ClicksetUser(){
       // Toast.makeText(getApplicationContext(),"Get user",Toast.LENGTH_LONG);
        MainActivity.SetPedido_Cliente  = true;
        Intent intent = new Intent(Pedido_new_Activity.this, ClientesActivity.class);

        startActivity(intent);
    };

    public static void AlteraUser(long cod_new){
        Cursor c2 = MainActivity.db.rawQuery("SELECT Codigo,Nome,endereco,cidade FROM cliente WHERE codigo = "+cod_new,null);
        if (c2.moveToFirst()){
            Cliente_Codigo = cod_new;

            Pedido_new_Activity.lbl_nome.setText(c2.getString(0)+". "+c2.getString(1));

            String str = "";
            if (c2.getString(2) != null){ str = c2.getString(2);};

            if (c2.getString(3) != null){
                if (str.length() != 0) str = str + "\n";
                str = str+c2.getString(3);
            }
            Pedido_new_Activity.lbl_info.setText(str);

            if (NotPost == false){
            PostPedido();
            }

        }
    };

    public static void SetSituacao(long cod_new){
        Cursor c2;
        if (cod_new == -1){
            Pedido_new_Activity.lbl_situacao.setText("");
            c2 = MainActivity.db.rawQuery("SELECT Codigo,Nome,cor FROM situacao", null);
        }else {
        c2 = MainActivity.db.rawQuery("SELECT Codigo,Nome,cor FROM situacao WHERE codigo = " + cod_new, null);
        }

        if (c2.moveToFirst()){
            Situacao_codigo = Long.parseLong(c2.getString(0));
            Pedido_new_Activity.lbl_situacao.setText(""+c2.getString(1));
            Pedido_new_Activity.layout_situacao.setBackgroundColor( (int) (Integer.parseInt(c2.getString(2))));
            if (NotPost == false){ PostPedido(); }

        }
    };

    private static void Atualiza_informacao_imposto(Context ctx){
        if (Usar_preco_imposto == true){
            lbl_menu_imposto.setText(ctx.getString(R.string.Usar_Lista_Imposto_ci));
           btn_menu_imposto.setBackgroundColor(ctx.getResources().getColor(R.color.Background_imposto_ci));

        }else {
            lbl_menu_imposto.setText(ctx.getString(R.string.Usar_Lista_Imposto_si));
            btn_menu_imposto.setBackgroundColor(ctx.getResources().getColor(R.color.bakcground_imposto_si));
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table,menu);
//        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
//        sv.setQueryHint("Procurar mercadoria");
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.item_del);
        menuItem.setShowAsAction(0);

        //((MenuItem) menu.findItem(R.id.item_search)).setVisible(true);

adicionaNovoProduto = false;

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_post:
                PostPedido();
                finish();
                break;

            case R.id.item_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ATENÇÃO!!!");
                builder.setMessage("O pedido "+Pedido_codigo+" será removido permanentemente, deseja continuar?").setPositiveButton("SIM, eu desejo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RemoverPedido();;
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplication(), "Exclução cancelada pelo usuário", Toast.LENGTH_SHORT);
                    }
                });
                builder.show();
                break;





            case R.id.Share:
                if (Pedido_codigo != -1){

                    //atualizar para salvar o campo descricao
                    PostPedido();

                    if (!MainActivity.permissed(Pedido_new_Activity.this)){
                        break;
                    }

                    final ProgressDialog ringProgressDialog = ProgressDialog.show(Pedido_new_Activity.this, "Aguarde um momento", "O pedido está sendo gerado", true);
                    //you usually don't want the user to stop the current process, and this will make sure of that
                    ringProgressDialog.setCancelable(false);
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            SharePedido(Pedido_codigo);
                            //after he logic is done, close the progress dialog
                            ringProgressDialog.dismiss();
                        }
                    });
                    th.start();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RemoverPedido() {
        if (Pedido_codigo == -1) {  finish(); return;}

        //Cursorc3 = MainActivity.db.rawQuery("SELECT codigo FROM livro WHERE Cod_pedido = "+Pedido_codigo,null);

        ListarMercadoriasInProdutoNew(Pedido_new_Activity.this);



        if (itens.size() != 0 ){
            MainActivity.Alerta(Pedido_new_Activity.this,"O pedido não pode ser excluido","Para remover este pedido é necessário remover todas as mercadorias abaixo.");
        }else {
            try {
                MainActivity.ExecutaSql("DELETE FROM pedido WHERE codigo = " + Pedido_codigo);
                MainActivity.Alerta(Pedido_new_Activity.this, "SUCESSO!", "O PEDIDO FOI REMOVIDO");
                finish();
            } catch (Exception e) {
                MainActivity.Alerta(Pedido_new_Activity.this, "ERRO AO REMOVER O PEDIDO", e.getClass() + ": " + e.getMessage());
            }}
    }




    public Date convertStringToDate(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date datetmp = null;
        try {
        datetmp = format.parse(dtStart);

        } catch (ParseException e) {
            e.printStackTrace();
        }

            return datetmp;
    }

    public static void PostPedido(){



        //Pedido_Data = convertStringToDate(DateEdit.getText().toString());



        String sql = "";
        boolean inserted = false;
        if (Pedido_codigo == -1){
            inserted = true;
            sql = "INSERT INTO pedido (cod_cliente, cod_situacao, preco_com_imposto, preco_total, data_pedido, descricao) " +
                  "Values ("+Cliente_Codigo+", "+Situacao_codigo+", '"+Usar_preco_imposto+"', "+Pedido_Preco+", '"+Pedido_data_Year+"-"+Pedido_Data_month+"-"+Pedido_Data_day+"', '"+Edit_Pedido_descricao.getText()+"' )";
            }else{
            sql = "UPDATE pedido SET" +
                    " cod_cliente = "+Cliente_Codigo+", " +
                    " cod_situacao = "+Situacao_codigo+", "+
                    " preco_com_imposto = '"+Usar_preco_imposto+"', "+
                    " preco_total = "+Pedido_Preco+" , "+
                    " data_pedido = '"+Pedido_data_Year+"-"+Pedido_Data_month+"-"+Pedido_Data_day+"', " +
                    " descricao = '"+Edit_Pedido_descricao.getText()+"' " +
                    "WHERE codigo = "+Pedido_codigo;
             }

        Log.d("REG_Info","POST");
        Log.d("REG_SLQ",sql);

            try{
                MainActivity.ExecutaSql(sql);
                //MainActivity.Alerta(Pedido_new_Activity.this,"SUCCESS!",sql);
                //Toast.makeText(Pedido_new_Activity.this,"posted",Toast.LENGTH_SHORT);


                if (Pedido_codigo == -1){
                    Cursor c2 = MainActivity.db.rawQuery("select codigo from pedido where codigo = (select max(codigo) from pedido)",null);
                        if (c2.moveToFirst()){
                        Pedido_codigo = Long.parseLong(c2.getString(0));
                            Barra_top.setTitle("Pedido: "+Pedido_codigo);
                        }};

                Registro_Editado = false;

            } catch (Exception e){
                Log.d("LOG_ERROR", e.getClass()+":  "+e.getMessage());

               // MainActivity.Alerta(this ,"EXCEPTION",e.getClass()+":  "+e.getMessage());
                 }
    }


    private void atualizaCod_Pedido(){


    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Pedido_Data_day = i2;
        Pedido_Data_month = i1+1;
        Pedido_data_Year = i;
        //Pedido_Data = getDate(i,i1,i2);

        //SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        //lbl_data.setText(formataData.format(Pedido_Data));
        lbl_data.setText(Pedido_Data_day+"/"+Pedido_Data_month+"/"+Pedido_data_Year);
        PostPedido();
    }



    public static void ListarMercadoriasInProdutoNew(final Context ctx){
        Log.d("TESTE","ATUALIZANDO LISTA DE PRODUTOS");
        Log.d("TESTE",  new Date().toString());

        itens = new ArrayList<TP_produto_item>();
        Cursor c = MainActivity.db.rawQuery(
                "SELECT A.codigo, A.cod_produto, A.qtd, A.preco, " +
                        " B.Nome FROM livro A " +
                        " INNER JOIN mercadoria B on A.cod_produto = B.codigo " +
                        " WHERE cod_pedido = "+Pedido_codigo
                        ,null);

        BigDecimal ValorTotal = BigDecimal.ZERO;
     /*   final ProgressDialog ringProgressDialog = ProgressDialog.show(ctx, "Aguarde um momento", "Estamos trabalhando no banco de dados", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
*/
        if (c.moveToFirst()){
            do {
                TP_produto_item prod = new TP_produto_item(
                        Integer.parseInt(c.getString(0)),
                        Integer.parseInt(c.getString(1)),
                        c.getString(4),
                        new BigDecimal(c.getString(2)),
                        new BigDecimal(c.getString(3)));
                ValorTotal = ValorTotal.add(prod.getPreco());
                itens.add(prod);
            }while(c.moveToNext());
        }
       // ringProgressDialog.dismiss();

        Log.d("TESTE", new Date().toString());


        if (ValorTotal.compareTo(Pedido_Preco) != 0 ){ // valores diferentes
            Pedido_Preco = ValorTotal;
            PostPedido();
        }
        tool_produto.setTitle("Preço total R$ "+Pedido_Preco);

        //arrayList = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(ctx,R.layout.lista_mercadoria_in_pedido);

        adapterListView = new TP_produto_Adapter(ctx,itens);




        //list_grid_mercadorias_inProduto.setAdapter(adapterListView);


        Lista_Pedido_New_Grid_Mercadoria_2.setAdapter(adapterListView);
       // Lista_Pedido_New_Grid_Mercadoria_2.setAdapter(null);

        //Lista_Pedido_New_Grid_Mercadoria_2.setAdapter(null);




        Log.d("TESTE","FINISH LOAD MERCADORIAS");
    }



    private static Rect GetRectText(String txt, Paint paint){
        if (txt.length() == 0){
            return new Rect(0,0,10,10);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), bounds);
        return bounds;
    }

    private int DesenharHead(Canvas canvas, Long P_Cod_Cliente, String P_Nome_CLiente,String P_endereco, String P_Localizacao,String P_Data,int Border, String Descricao)
    {
        int Size_head  = 45;
        int y = Border;

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(Size_head);

        paint.setTextSize(Size_head + 10);

        y  = y + GetRectText("Cliente: " + P_Cod_Cliente + ".  " + P_Nome_CLiente, paint).height() + 15;
        canvas.drawText("Cliente: "+P_Cod_Cliente+".  "+P_Nome_CLiente,Border, y, paint);

        int x2 = GetRectText("Cliente: ",paint).width() + 5;

        paint.setTextSize(Size_head);
        if (P_endereco.length() > 0) {
            y = y + GetRectText(P_endereco + " ", paint).height()+15;
            canvas.drawText(P_endereco + " ", Border + x2, y, paint);
        }

        if (P_Localizacao.length() > 0) {
            y = y + GetRectText(P_Localizacao + " ", paint).height()+15;
            canvas.drawText(P_Localizacao + " ", Border + x2, y, paint);
        }


        int y2 = Border;
        paint.setTextSize(Size_head + 15);
        paint.setTextAlign(Paint.Align.RIGHT);

        y2 = y2 +  GetRectText(P_Data,paint).height()+5;
        canvas.drawText(P_Data,canvas.getWidth()- (Border*2), y2, paint);

        paint.setTextSize(Size_head-10);
        y2 = y2 +20+GetRectText("A",paint).height();
        canvas.drawText("Código do Pedido: "+Pedido_codigo, canvas.getWidth() - (Border*2), y2, paint);

    if (y2 > y){y = y2;}

        if (Descricao != "") {
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setFakeBoldText(true);
            y += 20 + GetRectText("A", paint).height();
            canvas.drawText(Descricao, Border*3, y, paint);
        }


        return y + 10;
    }

    public void SharePedido(long P_codigo){
      //  ProgressDialog pd = new ProgressDialog(Pedido_new_Activity.this);
      //  pd.setMessage("Definindo variáveis");
      //  pd.show();
     //  this.setFinishOnTouchOutside(false);

        long       P_cod_cliente;
        BigDecimal P_preco_total = BigDecimal.ZERO;
        String     P_Data = "";
        String     P_descricao = "";
        String     P_Nome_CLiente="";
        String     P_Localizacao = "";
        String     P_endereco = "";
        String     P_Nome_Situacao="";

        Paint Line_paint = new Paint();
        Line_paint.setColor(Color.BLACK);
        Line_paint.setStrokeWidth(2);

        Log.d("teste","jump -1");
        Cursor c = MainActivity.db.rawQuery(
                "SELECT A.cod_cliente, A.preco_total, A.data_pedido, A.descricao," +
                        " B.nome, B.Endereco, B.cidade, " +
                        " C.Nome FROM pedido A " +
                        " INNER JOIN cliente B on A.cod_cliente = B.codigo " +
                        " INNER JOIN situacao C on A.cod_situacao = C.codigo " +
                        "WHERE A.codigo = "+P_codigo,null);

        if (c.moveToFirst() == false){
            MainActivity.Alerta(Pedido_new_Activity.this,"Error","Pedido não encontrado");
            return;}

        Log.d("teste","jump -1");
        P_cod_cliente = c.getLong(0);
        P_preco_total = new BigDecimal(c.getString(1));
        P_Data = MainActivity.convertStringToDate(c.getString(2));
        P_descricao = c.getString(3);

        if (c.isNull(4) ==false){P_Nome_CLiente = c.getString(4);};
        if (c.isNull(5) ==false){ P_Localizacao = c.getString(5);};
        if (c.isNull(6) ==false){P_endereco = c.getString(6);};

        //P_Nome_Situacao = c.getString(7);

        Log.d("teste","jump -2");
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String texto = "Olá sou um texto compartilhado";
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                */


        int Border = 75;
        int Size_title = 45;
        int Size_table = 45;

        int Widh_codigo = 250;
        int Width_preco_total = 350;
        int Width_preco_unitario = 350;
        int Width_quantidade = 200;
        int Pagina = 1;


        ArrayList<Bitmap> ArrayBmp =new ArrayList<Bitmap>();


        int y = Border*2;
        int x2 = 0;
        int x = Border;


        Paint paint = new Paint();
        Paint pBackground = new Paint();
        pBackground.setColor(Color.WHITE);

        // criando imagem
        Bitmap bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
        // DESENHANDO  HEAD
         y = DesenharHead(canvas,P_cod_cliente,P_Nome_CLiente,P_endereco,P_Localizacao,P_Data,Border,P_descricao);

         //////////DESENHAR TABELA
        paint.setColor(Color.BLACK);
        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
       //paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint);


        c = MainActivity.db.rawQuery(
                "SELECT A.cod_produto, A.qtd, A.preco," +
                        " B.Nome FROM livro A " +
                        " INNER JOIN mercadoria B on A.cod_produto = B.codigo " +
                        " WHERE A.cod_pedido = "+P_codigo,null);


/*       if (c.moveToFirst() == false){
            MainActivity.Alerta(Pedido_new_Activity.this,"Error","produtos nao encontrado");
            return;}
*/


        y += 10;

        //paint.setStrokeWidth(4);

        paint.setTextSize(Size_title);
        y += GetRectText("A",paint).height()+15;

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Código",   Border + (Widh_codigo/2),y,paint);
        canvas.drawText("Descrição",Border + Widh_codigo+ ((canvas.getWidth()-(Border*2)-Widh_codigo-Width_preco_total-Width_preco_unitario-Width_quantidade)/2)  ,y,paint);
        canvas.drawText("Qtd"     ,canvas.getWidth()-Border -Width_preco_total -Width_preco_unitario - (Width_quantidade/2),y,paint);
        canvas.drawText("Unitário",canvas.getWidth()-Border- Width_preco_total -(Width_preco_unitario/2),y,paint);
        canvas.drawText("Total"   ,canvas.getWidth()-Border- (Width_preco_total/2),y,paint);


        paint.setTextAlign(Paint.Align.LEFT);
        y = y + 10;
        //paint.setStrokeWidth(3);
        canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint );



        paint.setTextSize(Size_table);
        paint.setFakeBoldText(true);
        DecimalFormat money = new DecimalFormat("###,##0.00");


        Log.d("teste","jump 2");
        if (c.moveToFirst()){
            do {// 5 colunas
                // se nao tiver espaco
                if (y > 3300) {
                    MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;

                    //ArrayList<Bitmap> ArrayBmp =new ArrayList<Bitmap>();
                    ArrayBmp.add(bitmap);
                    bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);
                    canvas = new Canvas(bitmap);
                    canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
                    // DESENHANDO  HEAD
                    y = DesenharHead(canvas, P_cod_cliente, P_Nome_CLiente, P_endereco, P_Localizacao, P_Data, Border,P_descricao);
                }

                y += GetRectText("A",paint).height()+20;

                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(c.getString(0) ,   Border+ (Widh_codigo/2)  ,y,paint);

                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(c.getString(3),Border+10+Widh_codigo,y,paint);

                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(c.getString(1),canvas.getWidth()-Border-Width_preco_total -Width_preco_unitario -(Width_quantidade/2),y,paint); // quantidade

                BigDecimal unitario_temp = new BigDecimal(c.getString(1));
                BigDecimal total_tmo = new BigDecimal(c.getString(2));

                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText( money.format( total_tmo.divide(unitario_temp,2, RoundingMode.HALF_UP) ) ,canvas.getWidth()-Border-Width_preco_total-10,y,paint); //preco unitario
                canvas.drawText( money.format( total_tmo),canvas.getWidth()-Border-3,y,paint); //preco total


                y = y + 10;

                canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint);
            }while(c.moveToNext());}
        Log.d("teste","jump 3");



        paint.setTextSize(Size_title);
        y = y+20+ GetRectText("A",paint).height();
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Valor total   R$ "+ money.format(P_preco_total),canvas.getWidth()-Border-10,y,paint); //preco total


        MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;
        File file;
        String path = Environment.getExternalStorageDirectory().toString();
        // Create a file to save the image

        file = new File(path, "UniqueFileName"+".jpg");
        try{
            OutputStream stream = null;

            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the saved image path to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
        // Display the saved image to ImageView
        //iv_saved.setImageURI(savedImageURI);
        // Display saved image uri to TextView
        //tv_saved.setText("Image saved in external storage.\n" + savedImageURI);

        Log.d("compart","Image saved in external storage.\n" + savedImageURI);


        // compartilhando imagem
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ArrayList<Uri> uris = new ArrayList<Uri>();
        ArrayBmp.add(bitmap);

        for (int i = 0; i < ArrayBmp.size(); i++) {
            ArrayBmp.get(i).compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), ArrayBmp.get(i), "Titulo da Imagem", null);
            uris.add(Uri.parse(path2));
        }

        ///share.putExtra(Intent.EXTRA_STREAM, imageUri);


        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivityForResult(Intent.createChooser(share, "Sending multiple attachment"),12345);



       // share.putExtra(Intent.EXTRA_STREAM, imageUri);
        //share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(Intent.createChooser(share, "Compartilhando pedido"));

    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //===============================================================================================



    @Override
    public void onBackPressed() {
        if (Registro_Editado) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Atenção")
                    .setMessage("A descrição do pedido foi alterada, deseja salvar?")
                    .setPositiveButton("Sim, salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           PostPedido();
                           finish();
                        }

                    })
                    .setNegativeButton("Não, Voltar sem salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }else
            finish();

    };



  public void AdicionaNovoProduto(){
      First_insert = true;
      Intent intent = new Intent( Pedido_new_Activity.this, Pedido_new_produto.class);
      //.SetPedido_Mercadoria_com_imposto
      //Intent intent = new Intent(Pedido_new_Activity.this, Pedido_new_produto.class);
      startActivity(intent);
  }





}

