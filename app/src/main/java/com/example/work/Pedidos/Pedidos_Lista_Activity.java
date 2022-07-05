package com.example.work.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.work.MainActivity;
import com.example.work.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pedidos_Lista_Activity extends AppCompatActivity {

    ListView grid_pedido_lista;
    TextView lbl_count;
    RelativeLayout Pedidos_Lista_lt_count;

    private SearchView sv;
    ArrayAdapter<String> adapter;
    private ArrayList<Tpedido> itens;
    private Pedido_Adapter adapterListView;
    public static long Set_codigo_pedido = -1;

  //  private Boolean isFiltred;

//    public static String Param = "";

    @Override
    protected void onStart() {
        Log.d("TESTE","PEDIDO START");
        super.onStart();
     //   Listarpedidos(null,true);
    }

    //ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TESTE","PEDIDO RESUME");

        //Listarpedidos(null,true);


        /*final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(2); // Incremented By Value 2
            }
        }; */

         final ProgressDialog  progressDialog = new ProgressDialog(Pedidos_Lista_Activity.this);
           // progressDialog.setMax(100); // Progress Dialog Max Value
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle("ProgressDialog"); // Setting Title
           // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

            //new Thread(new Runnable() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {

                        Listarpedidos("",true);



                    progressDialog.dismiss();

/*                    try
                    { while (progressDialog.getProgress() <= progressDialog.getMax()) {
                            Thread.sleep(20);
                            handle.sendMessage(handle.obtainMessage());
                            if (progressDialog.getProgress() == progressDialog.getMax()) { progressDialog.dismiss();}
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            //}).start();
            });
        }
















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_lista);
        //isFiltred = false;

        Set_codigo_pedido = -1;
        grid_pedido_lista = (ListView) findViewById(R.id.Lista_Pedido_view);
        lbl_count = (TextView) findViewById(R.id.Pedidos_Lista_lbl_count);
        Pedidos_Lista_lt_count = (RelativeLayout) findViewById(R.id.Pedidos_Lista_lt_count);

        lbl_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (isFiltred != true){isFiltred = true;}else{isFiltred =false;}
                //Listarpedidos(null,true);

                clickFIlter();
            }
        });




        grid_pedido_lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tpedido item = adapterListView.getItem(i);
                Set_codigo_pedido = item.getCodigo();

                //MainActivity.Alerta(Produtos_activity.this,item.getNome(),"");
                Intent intent = new Intent(Pedidos_Lista_Activity.this, Pedido_new_Activity.class);
                startActivity(intent);
                return true;
            }
        });

        grid_pedido_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tpedido item = adapterListView.getItem(i);
                Set_codigo_pedido = item.getCodigo();
                //MainActivity.Alerta(Produtos_activity.this,item.getNome(),"");
                Intent intent = new Intent(Pedidos_Lista_Activity.this, Pedido_new_Activity.class);
                startActivity(intent);


            }
        });
        //Listarpedidos("");
    }


    private Date StrToDate(String formaInt,String Value){
      try {
          //"dd/MM/yyyy"
          SimpleDateFormat sdf = new SimpleDateFormat(formaInt);
          return  sdf.parse(Value);
      }catch (Exception e){
          return null;
      }

    }

    private void Listarpedidos(String param,boolean ParamZero) {
        itens = new ArrayList<Tpedido>();

        /*Select  A.*, B.nome_razao, B.nome, B.UF, B.Municiopio, B.cpf from
        Venda_info A inner join Cliente_cadastro B on
        A.id_cliente = b.codigo
         */


        Date strDate = StrToDate("dd/MM/yyyy",MainActivity.LoadCfg(1));
        Date endDate = StrToDate("dd/MM/yyyy",MainActivity.LoadCfg(2));

//        f (getCurrentDateTime.compareTo(getMyTime) < 0)
//        O método CompareTo deve retornar um número negativo se o objeto atual for menor que outro objeto, número positivo se o objeto atual for maior que outro objeto e zero se ambos os objetos forem iguais entre si.


        boolean isFiltred = false;

        if (ParamZero == true){
           param =  MainActivity.LoadCfg(3);

           if (param != "")
           if (param.length() > 0)
           isFiltred = true;

        }{
            Log.d("TESTE",param);
        //  ParamZero = false;
        //  codigo do pedido, like nome
        }

        if (strDate != null) isFiltred = true;
        if (endDate != null) isFiltred = true;

        if (strDate == null)
        if (endDate == null) {
            ParamZero = false; //acelerar loop abaixo
        }


        Cursor c = MainActivity.db.rawQuery(
                "SELECT A.codigo, A.cod_cliente, A.preco_total, A.data_pedido, " +
                " B.nome, C.Nome, C.cor, A.preco_com_imposto FROM pedido A " +
                " INNER JOIN cliente B on A.cod_cliente = B.codigo " +
                " INNER JOIN situacao C on A.cod_situacao = C.codigo "+param+"    ORDER BY A.codigo DESC",null);
// +"
        if (c.moveToFirst()){
            do {

                Boolean Show_Red = true;


           if (ParamZero == true) {
               Date dreg = StrToDate("yyyy-MM-dd", c.getString(3));
               if (strDate != null)
               if (dreg != null)
               if (strDate.compareTo(dreg) > 0)
                   Show_Red = false;

               if (endDate != null)
               if (dreg != null)
               if (endDate.compareTo(dreg) < 0)
               Show_Red = false;
           }


                if (Show_Red == true) {
                    Tpedido cl = new Tpedido(
                            c.getLong(0),       /* codigo */
                            c.getLong(1),       /* cod_cliente*/
                            new BigDecimal(c.getString(2)),       /* preco_total*/
                            MainActivity.convertStringToDate(c.getString(3)),     /* data_dia*/
                            c.getInt(6),        /* situacao_color*/
                            c.getString(5),     /* situacao_nome*/
                            c.getString(4),      /* nome_Cliente*/
                            Boolean.parseBoolean(c.getString(7))
                    );
                    itens.add(cl);
                }


            }while(c.moveToNext());
        }

       adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.cliente_lista );
       adapterListView = new Pedido_Adapter(this,itens);
       grid_pedido_lista.setAdapter(adapterListView);


        lbl_count.setText("Pedidos encontrados: "+ adapterListView.getCount());
        if (isFiltred){
            Pedidos_Lista_lt_count.setBackgroundColor(getResources().getColor(R.color.bakcground_imposto_si));
            lbl_count.setText(lbl_count.getText()+"\n OS PEDIDOS ESTÃO FILTRADOS");
        }else{
            Pedidos_Lista_lt_count.setBackgroundColor(getResources().getColor(R.color.Background_imposto_ci));
        }


    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pedidos_lista,menu);

//        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
//        sv.setQueryHint("Procurar mercadoria");
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.item_del);
        menuItem.setVisible(false );
        getSupportActionBar().setTitle("Pedidos");

        sv = (SearchView) menu.findItem(R.id.iten_Filter_SV_Pedido).getActionView();
        sv.setQueryHint("Procurar pedido");
        sv.setQuery("", false);
        sv.clearFocus();
        sv.setIconified(true);


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Log.d("lista",s);
                if (MainActivity.isLongNumeric(s)) {
                    Listarpedidos("WHERE A.codigo = "+s,false);
                }else
                    Listarpedidos("WHERE B.nome LIKE '%" + s + "%'",false);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (MainActivity.isLongNumeric(s)) {
                    Listarpedidos("WHERE A.codigo = "+s,false);
                }else
                    Listarpedidos("WHERE B.nome LIKE '%" + s + "%'",false);

                return true;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_add:
                Set_codigo_pedido = -1;
                Intent intent = new Intent( Pedidos_Lista_Activity.this, Pedido_new_Activity.class);
                startActivity(intent);
                break;

            case R.id.item_Lista_Pedidos_ITEM_ESPECIAL:
                if (!MainActivity.permissed(Pedidos_Lista_Activity.this)){
                    break;
                }

                final ProgressDialog ringProgressDialog = ProgressDialog.show(Pedidos_Lista_Activity.this, "Aguarde um momento", "A lista está sendo gerada", true);
                //you usually don't want the user to stop the current process, and this will make sure of that
                ringProgressDialog.setCancelable(false);
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ListaITENSESPECIAIS(true);
                        //SharePedido(Pedido_codigo);
                        //after he logic is done, close the progress dialog
                        ringProgressDialog.dismiss();
                    }
                });
                th.start();
                break;

            case R.id.item_Lista_Pedidos_ITE_With_Cliente:
                if (!MainActivity.permissed(Pedidos_Lista_Activity.this)){
                    break;
                }

                final ProgressDialog ringProgressDialog2 = ProgressDialog.show(Pedidos_Lista_Activity.this, "Aguarde um momento", "A lista está sendo gerada", true);
                //you usually don't want the user to stop the current process, and this will make sure of that
                ringProgressDialog2.setCancelable(false);
                Thread th0 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ListarEspeciais_With_Clientes();
                        //ListaITENSESPECIAIS(true);
                        //SharePedido(Pedido_codigo);
                        //after he logic is done, close the progress dialog
                        ringProgressDialog2.dismiss();
                    }
                });
                th0.start();
                break;




            case R.id.item_Lista_filter:
                clickFIlter();
                break;

            case R.id.item_Relatorio:

                if (!MainActivity.permissed(Pedidos_Lista_Activity.this)){
                    break;
                }

                final ProgressDialog ringProgressDialog20 = ProgressDialog.show(Pedidos_Lista_Activity.this, "Aguarde um momento", "A lista está sendo gerada", true);
                //you usually don't want the user to stop the current process, and this will make sure of that
                ringProgressDialog20.setCancelable(false);
                Thread th3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clickrelatorio6();
                        //SharePedido(Pedido_codigo);
                        //after he logic is done, close the progress dialog
                        ringProgressDialog20.dismiss();
                    }
                });
                th3.start();
                break;





            case R.id.item_Lista_Pedidos_todos:
                if (!MainActivity.permissed(Pedidos_Lista_Activity.this)){
                    break;
                }

                final ProgressDialog ringProgressDialog23 = ProgressDialog.show(Pedidos_Lista_Activity.this, "Aguarde um momento", "A lista está sendo gerada", true);
                //you usually don't want the user to stop the current process, and this will make sure of that
                ringProgressDialog23.setCancelable(false);
                Thread th4 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ListaITENSESPECIAIS(false);
                        //SharePedido(Pedido_codigo);
                        //after he logic is done, close the progress dialog
                        ringProgressDialog23.dismiss();
                    }
                });
                th4.start();
                break;


            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ListarEspeciais_With_Clientes() {
                        // a.qtd = quantidade de itens do pedido
                        // a.cod_produto = codigo da mercadoria, verificar ao mudar
                        // b.Nome = nome da mercadoria
                        // c.nome = nome do Cliente
        String sql = "SELECT A.qtd, B.NOME, C.Nome, A.cod_produto FROM livro A " +
                    " INNER JOIN mercadoria B on A.cod_produto = B.codigo " +
                    " INNER JOIN pedido     D on A.cod_pedido  = D.codigo "+
                    " INNER JOIN cliente    C on D.cod_cliente = C.codigo "+
                    "";
        String sql2 = "";
        if (itens.size() == 0) return;
        for (int i =0; i < itens.size();i++ ){
            sql2 = sql2 + ", "+itens.get(i).getCodigo();
        }


        sql = sql + " WHERE A.cod_pedido IN (" + sql2.substring(1) + ") AND B.Item_especial = 'true'";
        sql = sql + " ORDER by B.NOME DESC";

        //Log.d("TESTE",sql);

        // ========================================================================================
        //                              BORA FAZER A IMPRESSAO
        // ========================================================================================
        int Border = 75;
        int Size_title = 55;
        int Size_table = 45;

        int Width_C1  = 826;
        long Pagina = 1;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ArrayList<Uri> uris = new ArrayList<Uri>();

        int y = 0;
        int x = Border;

        Paint Line_paint = new Paint();
        Line_paint.setColor(Color.BLACK);
        Line_paint.setStrokeWidth(2);

        Paint paint = new Paint();
        Paint pBackground = new Paint();
        paint.setFakeBoldText(true);
        pBackground.setColor(Color.WHITE);

        y = (Border*2)+10;

        // criando imagem
        Bitmap bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
      //  canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint);

        Log.d("TESTE","SEARCH OK");

        // ========================================================================================
        //                              PESQUISA
        // ========================================================================================
        long Cod_last_Reg = 0;
        Cursor c2 = MainActivity.db.rawQuery(sql,null);
        if (c2.moveToFirst()){do {
                            if (y > 3300) { //mudar a coluna
                                if (x > 1500) { // nova folha
                                    Log.d("TESTE","INICIANDO NOVA PAGINA "+Pagina);
                                    MainActivity.SetPage(Pagina, canvas, Border);
                                    Pagina += 1;


                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Titulo da Imagem", null);
                                    uris.add(Uri.parse(path2));

                                    canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
                                    y = Border + 50;
                                    x = Border;
                                }else{ //nova coluna
                                    y =  (Border*2)+10;
                                    x = x + Width_C1;
                                    //desenhar um quadrado para evitar que haja cliente com o nome muito grande
                                    canvas.drawRect(x - 15, y, canvas.getWidth(), canvas.getHeight(), pBackground);
                                }
                            }


            //Log.d("TESTE", c2.getString(1)+": "+c2.getString(0)+"x "+c2.getString(2));
            if (Cod_last_Reg != c2.getLong(3)){ // desenha o nome da mercadoria
                Cod_last_Reg = c2.getLong(3);
                //canvas.drawLine(x, y,x+Width_C1, y, Line_paint);
                y += 50;
                canvas.drawLine(x, y,x+Width_C1, y, Line_paint);y += 3;
                paint.setTextSize(Size_title);
                y += GetRectText("A",paint).height()+15;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(c2.getString(1),   x,y,paint);
                y += +7;
                //canvas.drawLine(x, y,x+Width_C1, y, Line_paint);y +=7;
            }

            if (y > 3300) { //mudar a coluna
                if (x > 1500) { // nova folha
                    Log.d("TESTE","INICIANDO NOVA PAGINA "+Pagina);
                    MainActivity.SetPage(Pagina, canvas, Border);
                    Pagina += 1;

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Titulo da Imagem", null);
                    uris.add(Uri.parse(path2));

                    canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
                    y = Border + 50;
                    x = Border;
                }else{ //nova coluna
                    y =  (Border*2)+10;
                    x = x + Width_C1;
                    //desenhar um quadrado para evitar que haja cliente com o nome muito grande
                    canvas.drawRect(x - 15, y, canvas.getWidth(), canvas.getHeight(), pBackground);
                }
            }



            // adiciona o cliente
            paint.setTextSize(Size_table);
            y += GetRectText("A",paint).height()+10;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(c2.getString(0)+"x "+c2.getString(2),   x,y,paint);
            y += +10;
        }while(c2.moveToNext());}

        // ========================================================================================
        //                              SHARE
        // ========================================================================================

        paint.setTextSize(Size_title);
        MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;

        File file;
        String path = Environment.getExternalStorageDirectory().toString();
        // Create a file to save the image
        file = new File(path, "UniqueFileName"+".jpg");
        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Titulo da Imagem", null);
            uris.add(Uri.parse(path2));

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





        ///share.putExtra(Intent.EXTRA_STREAM, imageUri);


        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivityForResult(Intent.createChooser(share, "Sending multiple attachment"),12345);



        // share.putExtra(Intent.EXTRA_STREAM, imageUri);
        //share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(Intent.createChooser(share, "Compartilhando pedido"));




    }




    private void clickFIlter() {
        Intent intent = new Intent(Pedidos_Lista_Activity.this, FiltroActivity.class);
        startActivity(intent);
    }








    private static Rect GetRectText(String txt, Paint paint){
        if (txt.length() == 0){
            return new Rect(0,0,10,10);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), bounds);
        return bounds;
    }

    private void ListaITENSESPECIAIS(Boolean USAR_APENAS_ITEM_ESPECIAL) {
    // preparar lista de mercadorias
        ArrayList<TP_produto_item> List_Print = new ArrayList<TP_produto_item>();

        String sql = "SELECT A.cod_produto, A.qtd, B.NOME, A.preco, B.Item_especial FROM livro A INNER JOIN mercadoria B on A.cod_produto = B.codigo " +
                " WHERE ";

        if (itens.size() == 0) return;

        for (int i =0; i < itens.size();i++ ){
            sql += "( cod_pedido = "+itens.get(i).getCodigo()+") or ";
        }
        sql = sql.substring(0,sql.length()-4);

        // MainActivity.Alerta(Pedidos_Lista_Activity.this,"",sql);

        Cursor c2 = MainActivity.db.rawQuery(sql,null);

        if (c2.moveToFirst()){
            do {
                boolean adicionar = false;
                if (USAR_APENAS_ITEM_ESPECIAL == false){ adicionar = true;}else{ adicionar = (Boolean.parseBoolean(c2.getString(4)));}

                if (adicionar) {
                    TP_produto_item temp_mercadoria = new TP_produto_item(0, c2.getLong(0), c2.getString(2), new BigDecimal(c2.getString(1)), new BigDecimal(c2.getString(3)));

                    boolean b = false;
                    for (int i = 0; i < List_Print.size(); i++) {
                        if (List_Print.get(i).getCodigo_mercadoria() == temp_mercadoria.getCodigo_mercadoria()) {

                            List_Print.get(i).setQuantidade(List_Print.get(i).getQuantidade().add(temp_mercadoria.getQuantidade()));
                            List_Print.get(i).setPreco((List_Print.get(i).getPreco().add(temp_mercadoria.getPreco())));

                            b = true;
                            break;
                        }
                    }
                    if (b == false) {
                        List_Print.add(temp_mercadoria);
                    }

                }

            }while(c2.moveToNext());}


        //MainActivity.Alerta(Pedidos_Lista_Activity.this,"",List_Print.size()+"");
        // ========================================================================================
        //                              BORA FAZER A IMPRESSAO
        // ========================================================================================



            Paint Line_paint = new Paint();
            Line_paint.setColor(Color.BLACK);
            Line_paint.setStrokeWidth(2);

            int Border = 75;

            int Size_title = 55;
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
            y = y + 30;
            // DESENHANDO  HEAD


            //////////DESENHAR TABELA
            paint.setColor(Color.BLACK);
            //paint.setStyle(Paint.Style.STROKE);
            //paint.setStrokeWidth(2);
            paint.setAntiAlias(true);
            //paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint);


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
            //if (c.moveToFirst()){
             //   do {// 5 colunas
                BigDecimal Money_total = BigDecimal.ZERO;

           for (int i = 0; i < List_Print.size();i++){
               Money_total = Money_total.add(List_Print.get(i).getPreco());

                    // se nao tiver espaco
                    if (y > 3300) {
                        MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;
                        //ArrayList<Bitmap> ArrayBmp =new ArrayList<Bitmap>();
                        ArrayBmp.add(bitmap);
                        bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);
                        canvas = new Canvas(bitmap);
                        canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
                        // DESENHANDO  HEAD
                        //y = DesenharHead(canvas, P_cod_cliente, P_Nome_CLiente, P_endereco, P_Localizacao, P_Data, Border);
                        y = Border + 50;
                    }

                    y += GetRectText("A",paint).height()+20;

                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(List_Print.get(i).getCodigo_mercadoria()+"" ,   Border+ (Widh_codigo/2)  ,y,paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(List_Print.get(i).getNome_mercadoria(),Border+10+Widh_codigo,y,paint);

                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(List_Print.get(i).getQuantidade()+"",canvas.getWidth()-Border-Width_preco_total -Width_preco_unitario -(Width_quantidade/2),y,paint); // quantidade

                    /*BigDecimal unitario_temp = new BigDecimal(c.getString(1));
                    BigDecimal total_tmo = new BigDecimal(c.getString(2));
                    */

                    paint.setTextAlign(Paint.Align.RIGHT);
                    //canvas.drawText( money.format( total_tmo.divide(unitario_temp) ) ,canvas.getWidth()-Border-Width_preco_total-10,y,paint); //preco unitario
                    canvas.drawText( money.format(List_Print.get(i).getPreco()),canvas.getWidth()-Border-3,y,paint); //preco total

                    y = y + 10;

                    canvas.drawLine(Border, y,canvas.getWidth() - Border, y, Line_paint);
                }



        paint.setTextSize(Size_title);
        y = y+20+ GetRectText("A",paint).height();
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Valor total   R$ "+ money.format(Money_total),canvas.getWidth()-Border-10,y,paint); //preco total



        paint.setTextSize(Size_title);
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






 private void clickrelatorio6() {

     if (itens.size() == 0)
         return;

     // preparar lista de mercadorias
     ArrayList<TP_produto_item> List_Print = new ArrayList<TP_produto_item>();
// ========================================================================================
//                              DECLARANDO CONSTANTES
// ========================================================================================


     BigDecimal Fator_Correcao = new BigDecimal("1.06");
     if (MainActivity.LoadCfg(4) != "") Fator_Correcao =  new BigDecimal(MainActivity.LoadCfg(4));



     Paint Line_paint = new Paint();
     Line_paint.setColor(Color.BLACK);
     Line_paint.setStrokeWidth(2);

     int Border = 75;
     int Size_mercadorias = 25;
     int Size_table = 30;
     int Widh_codigo = 250;
     int Width_preco_total = 350;
     int Width_preco_unitario = 350;
     int Width_quantidade = 200;

     int Pagina = 1;
// ========================================================================================
//                              BORA FAZER A IMPRESSAO
// ========================================================================================
     ArrayList<Bitmap> ArrayBmp = new ArrayList<Bitmap>();
     int y = Border * 2;
     int x2 = 0;
     int x = Border;
     Paint paint = new Paint();
     Paint pBackground = new Paint();
     pBackground.setColor(Color.WHITE);
     DecimalFormat money = new DecimalFormat("###,##0.00");


     // criando imagem
     Bitmap bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);
     Canvas canvas = new Canvas(bitmap);
     canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);

     y = y + 30;
     // DESENHANDO  HEAD
     // DESENHAR TABELA
     paint.setColor(Color.BLACK);
     paint.setAntiAlias(true);
  //   canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);

     /*
     paint.setTextSize(Size_title);
     y += GetRectText("A",paint).height()+15;
         paint.setTextAlign(Paint.Align.CENTER);
         canvas.drawText("Código", Border + (Widh_codigo / 2), y, paint);
         canvas.drawText("Descrição", Border + Widh_codigo + ((canvas.getWidth() - (Border * 2) - Widh_codigo - Width_preco_total - Width_preco_unitario - Width_quantidade) / 2), y, paint);
         canvas.drawText("Qtd", canvas.getWidth() - Border - Width_preco_total - Width_preco_unitario - (Width_quantidade / 2), y, paint);
         canvas.drawText("Unitário", canvas.getWidth() - Border - Width_preco_total - (Width_preco_unitario / 2), y, paint);
         canvas.drawText("Total", canvas.getWidth() - Border - (Width_preco_total / 2), y, paint);
     */


     paint.setTextAlign(Paint.Align.LEFT);
     y = y + 10;
     canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);


     BigDecimal Money_pedidos = BigDecimal.ZERO;
     BigDecimal R_money_Total = BigDecimal.ZERO;
     BigDecimal R_Money_Total_Sem_Desconto = BigDecimal.ZERO;



     for (int ID_pedido = 0; ID_pedido < itens.size(); ID_pedido++) {
// ========================================================================================
//                              Carregar pedido
// ========================================================================================
         if (itens.get(ID_pedido).isPreco_com_imposto()){
            Fator_Correcao = new BigDecimal("1.16");
         } else{
             Fator_Correcao = new BigDecimal("1.06");
         }



         R_Money_Total_Sem_Desconto = R_Money_Total_Sem_Desconto.add(itens.get(ID_pedido).getPreco_total());

         //vamos imprimir o pedido, cliente e a data -> uma linha

        // canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);
         y += GetRectText("A", paint).height() + 20;
         paint.setTextSize(Size_table);
         paint.setFakeBoldText(true);
         paint.setTextAlign(Paint.Align.LEFT);
         canvas.drawText("Pedido: " + itens.get(ID_pedido).getCodigo() + " - " + itens.get(ID_pedido).getCod_cliente() + ". " + itens.get(ID_pedido).getNome_Cliente(), Border, y, paint);
         paint.setTextAlign(Paint.Align.RIGHT);
         canvas.drawText("Data " + itens.get(ID_pedido).getData_dia(), canvas.getWidth() - Border - 10, y, paint);

         y += 10;
         canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);


         Money_pedidos = BigDecimal.ZERO;

         String sql = "SELECT A.cod_produto, B.NOME, A.preco, A.qtd FROM livro A INNER JOIN mercadoria B on A.cod_produto = B.codigo " +
                      "WHERE A.cod_pedido = " + itens.get(ID_pedido).getCodigo();

         //Log.d("TESTE",sql);

         Cursor c = MainActivity.db.rawQuery(sql, null);
         // =====================================================================
         //                              Print mercadorias
         // =====================================================================



         Log.d("TESTE","REG COUNT "+c.getCount());
         if (c.moveToFirst()){ do {
             // se nao tiver espaco
             if (y > 3250 - Border) {

                 //coloca a pagina no fim
                 MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;


                 //ArrayList<Bitmap> ArrayBmp =new ArrayList<Bitmap>();
                 ArrayBmp.add(bitmap);
                 bitmap = Bitmap.createBitmap(2480/*width*/, 3508/*height*/, Bitmap.Config.ARGB_8888);
                 canvas = new Canvas(bitmap);
                 canvas.drawRect(1, 1, canvas.getWidth(), canvas.getHeight(), pBackground);
                 // DESENHANDO  HEAD
                 //y = DesenharHead(canvas, P_cod_cliente, P_Nome_CLiente, P_endereco, P_Localizacao, P_Data, Border);
                 y = Border + 50;
             }


             BigDecimal total_tmp = new BigDecimal(c.getString(2));
             total_tmp = total_tmp.divide(Fator_Correcao, 2, RoundingMode.HALF_UP); ///-----------------------------------------------------------------------

             Money_pedidos = Money_pedidos.add(total_tmp);

             //R_money_Total = Relatorio_money_Total.add(Money_total);


             BigDecimal unitario_temp = total_tmp.divide((new BigDecimal(c.getString(3))), 2, RoundingMode.HALF_UP);

             paint.setTextSize(Size_mercadorias);
             paint.setFakeBoldText(true);


             y += GetRectText("A", paint).height() + 10;
             // codigo
             paint.setTextAlign(Paint.Align.CENTER);
             canvas.drawText(c.getString(0) + "", Border + (Widh_codigo / 2), y, paint);
             // nome
             paint.setTextAlign(Paint.Align.LEFT);
             canvas.drawText(c.getString(1), Border + 10 + Widh_codigo, y, paint);
             // qtd
             paint.setTextAlign(Paint.Align.CENTER);
             canvas.drawText(c.getString(3), canvas.getWidth() - Border - Width_preco_total - Width_preco_unitario - (Width_quantidade / 2), y, paint); // quantidade
             // unitário
             paint.setTextAlign(Paint.Align.RIGHT);
             canvas.drawText(money.format(unitario_temp), canvas.getWidth() - Border - Width_preco_total - 10, y, paint); //preco unitario
             //total
             canvas.drawText(money.format(total_tmp), canvas.getWidth() - Border - 3, y, paint);
             y = y + 5;
             canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);


         }
         while (c.moveToNext());}

         paint.setTextSize(Size_table);
         paint.setFakeBoldText(true);

         canvas.drawLine(Border, y, canvas.getWidth() - Border, y, Line_paint);
         y = y + 5 + GetRectText("A", paint).height();
         paint.setTextAlign(Paint.Align.RIGHT);
         canvas.drawText("Valor total   R$ " + money.format(Money_pedidos), canvas.getWidth() - Border - 10, y, paint); //preco total


         R_money_Total = R_money_Total.add(Money_pedidos);
         y = y +5 + GetRectText("A", paint).height();

     } // id_pedido


     y = y+20 + GetRectText("A", paint).height();
     paint.setTextAlign(Paint.Align.RIGHT);
     canvas.drawText("Valor total   R$ " + money.format(R_money_Total), canvas.getWidth() - Border - 10, y, paint); //preco total

     paint.setTextAlign(Paint.Align.LEFT);
     canvas.drawText("Valor total R$ " + money.format(R_Money_Total_Sem_Desconto.divide(Fator_Correcao,2, RoundingMode.HALF_UP)), Border + 10, y, paint); //preco total

     y = y + 20 + GetRectText("A", paint).height();
     canvas.drawText("Valor total S/D  R$ " + money.format(R_Money_Total_Sem_Desconto), Border + 10, y, paint); //preco total


     y = y + 20 + GetRectText("A", paint).height();
     canvas.drawText("Diferença R$ " + money.format(R_Money_Total_Sem_Desconto.subtract(R_money_Total)), Border + 10, y, paint);






























     MainActivity.SetPage(Pagina,canvas,Border); Pagina += 1;

     File file;
         String path = Environment.getExternalStorageDirectory().toString();
         // Create a file to save the image

         file = new File(path, "UniqueFileName" + ".jpg");
         try {
             OutputStream stream = null;

             stream = new FileOutputStream(file);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
             stream.flush();
             stream.close();
         } catch (IOException e) // Catch the exception
         {
             e.printStackTrace();
         }

         // Parse the saved image path to uri
         Uri savedImageURI = Uri.parse(file.getAbsolutePath());
         // Display the saved image to ImageView
         //iv_saved.setImageURI(savedImageURI);
         // Display saved image uri to TextView
         //tv_saved.setText("Image saved in external storage.\n" + savedImageURI);

         Log.d("compart", "Image saved in external storage.\n" + savedImageURI);


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
         startActivityForResult(Intent.createChooser(share, "Sending multiple attachment"), 12345);


         // share.putExtra(Intent.EXTRA_STREAM, imageUri);
         //share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         //startActivity(Intent.createChooser(share, "Compartilhando pedido"));




 } //end


}














/*    Select  A.*, B.nome_razao, B.nome, B.UF, B.Municiopio, B.cpf from
      Venda_info A inner join Cliente_cadastro B on
      A.id_cliente = b.codigo
*/