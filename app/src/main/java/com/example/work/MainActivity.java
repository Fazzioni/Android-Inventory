package com.example.work;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.work.Clientes.ClientesActivity;
import com.example.work.Mercadoria.Produtos_activity;
import com.example.work.Pedidos.Pedidos_Lista_Activity;
import com.example.work.Situacao.Situacao_Lista_Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String NOME_DO_BANCO = "systemdb2";

    private Button btn_add_cliente, btn_clientes, btn_produto, btn_pedido, btn_situacao;
    public static SQLiteDatabase db;

    public static boolean SetPedido_Cliente = false;
    public static boolean SetPedido_Situacao = false;
    public static boolean SetPedido_Mercadoria = false;
    public static byte    SetPedido_Mercadoria_Show_Preco = 0;

    private static boolean resultValue;


    Handler Manipulador = new Handler()
    {
        public void handleMessage(Message msg){
            String s = "";

            switch (msg.what){
                case 0: s = "Banco de dados não encontrado"; break;
                case 1: s = "Backup realizado com sucesso"; break;
                case 2: s = "Exception found"; break;

                case 10: s = "Banco de dados não encontrado \n verifique se o arquivo está no diretório interno com o nome de 'BANCO_RESTORE.db'"; break;
                case 11: s = "Arquivos trocados"; break;
                case 12: s = "Erro"; break;
                case 13: s = "fail"; break;
            }

            MainActivity.Alerta(MainActivity.this,"",s);

            //Toast.makeText(MainActivity.this, "Exception found", Toast.LENGTH_SHORT).show();




        }
    };

    public static boolean permissed(Activity ctx) {

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                    return false;
            }

            permissionCheck = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                    return false;
            }}

            return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            //criar banco de dados
            //db = openOrCreateDatabase("systemdb2",MODE_PRIVATE,null);
            db = openOrCreateDatabase(NOME_DO_BANCO,MODE_PRIVATE,null);

            // criar tabela
            db.execSQL("CREATE TABLE IF NOT EXISTS cliente ("+
                    "Codigo    INTEGER      PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE,"+
                    "Nome      STRING  (255),"+
                    "Endereco  VARCHAR (255),"+
                    "cidade    STRING  (255)"+
                    ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS mercadoria ("+
                    "Codigo    INTEGER      PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE,"+
                    "Nome      STRING (255),"+
                    "preco_ci  DECIMAL,"+
                    "preco_si  DECIMAL,"+
                    "Item_especial BOOLEAN"+
                    ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS pedido ("+
                    "Codigo    INTEGER      PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE,"+

                    "cod_cliente   INTEGER,"+
                    "cod_situacao  INTEGER,"+
                    "preco_com_imposto BOOLEAN,"+
                    "preco_total  DECIMAL,"+
                    "data_pedido  DATETIME,"+
                    "descricao    STRING (255)"+

                    //sqlite not found
                   // " FOREIGN KEY (cod_cliente)      REFERENCES cliente (Codigo)"+
                   //    " CONSTRAINT FK_Pedido_CLIENTE FOREIGN KEY (cod_cliente) REFERENCES cliente (codigo) ON UPDATE CASCADE"+
                    ");"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS situacao ("+
                    "Codigo    INTEGER      PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE,"+
                    "Nome      STRING (255),"+
                    "cor       INTEGER," +
                    "Filtred   BOOLEAN"+
                    ")"
            );


            db.execSQL("CREATE TABLE IF NOT EXISTS livro ("+
                    "Codigo      INTEGER      PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE,"+
                    "cod_pedido  INTEGER,"+
                    "cod_produto INTEGER," +
                    "qtd         DECIMAL,"+
                    "preco       DECIMAL"+")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS configuration ("+
                    "Codigo INTEGER      PRIMARY KEY,"+
                    "valor  STRING (255) );"
                    );



            //db.execSQL("INSERT INTO cliente (Nome,Endereco) VALUES ('Antonio','concordia')");


            //verificar se existe um usuario com código = -1
            Cursor cursor = db.rawQuery("SELECT Nome FROM cliente WHERE codigo = -1",null);
            //int indiceCodigo = cursor.getColumnIndex("codigo");
            if (cursor.moveToFirst() == false){
                db.execSQL("INSERT INTO cliente (codigo, nome) VALUES (-1,'SEM CLIENTE')");
            }

        } catch (Exception e){

            e.printStackTrace();
        }



        this.btn_clientes = (Button) findViewById(R.id.Btn_Client);
        this.btn_clientes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (db.isOpen() == false){ Alerta(MainActivity.this,"ERROR","FECHE O APLICATIVO E ABRA NOVAMENTE"); return;}

                MainActivity.SetPedido_Cliente = false;
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);

            }
        });


        this.btn_produto = (Button) findViewById(R.id.Btn_Mercadoria);
        this.btn_produto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (db.isOpen() == false){ Alerta(MainActivity.this,"ERROR","FECHE O APLICATIVO E ABRA NOVAMENTE"); return;}

                MainActivity.SetPedido_Mercadoria  = false;
                MainActivity.SetPedido_Mercadoria_Show_Preco = 0;

                Intent intent = new Intent(MainActivity.this, Produtos_activity.class);
                startActivity(intent);
            }
        });

        this.btn_pedido = (Button) findViewById(R.id.Btn_Pedido);
        this.btn_pedido.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (db.isOpen() == false){ Alerta(MainActivity.this,"ERROR","FECHE O APLICATIVO E ABRA NOVAMENTE"); return;}

                Intent intent = new Intent(MainActivity.this, Pedidos_Lista_Activity.class);
                startActivity(intent);
            }
        });

        this.btn_situacao = (Button) findViewById(R.id.Btn_situacao);
        this.btn_situacao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (db.isOpen() == false){ Alerta(MainActivity.this,"ERROR","FECHE O APLICATIVO E ABRA NOVAMENTE"); return;}

                Intent intent = new Intent(MainActivity.this, Situacao_Lista_Activity.class);
                startActivity(intent);
            }
        });


        this.btn_situacao = (Button) findViewById(R.id.Btn_situacao);
        this.btn_situacao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               if (db.isOpen() == false){ Alerta(MainActivity.this,"ERROR","FECHE O APLICATIVO E ABRA NOVAMENTE"); return;}
                Intent intent = new Intent(MainActivity.this, Situacao_Lista_Activity.class);
                startActivity(intent);
            }
        });




        ((Button) findViewById(R.id.Btn_backup)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                /////

                if (Build.VERSION.SDK_INT >= 23) {
                    int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                            return;
                    }

                }

                final ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this, "Aguarde um momento", "Estamos trabalhando no banco de dados", true);
                //you usually don't want the user to stop the current process, and this will make sure of that
                ringProgressDialog.setCancelable(false);
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (db.isOpen() == false){  db.close();}
                        int s = CopyBDC();
                        //after he logic is done, close the progress dialog
                       ringProgressDialog.dismiss();
                      // Alerta(MainActivity.this,"",s);

                        if (s == 1) {
                           // ShareBDC();
                            db = openOrCreateDatabase(NOME_DO_BANCO,MODE_PRIVATE,null);

                        }else
                            Manipulador.sendEmptyMessage(s);



                    }
                });
                th.start();

                //Toast.makeText(MainActivity.this,"TESTE",Toast.LENGTH_LONG);

               // Alerta(MainActivity.this,"","s");
            }
        });


        ((Button) findViewById(R.id.Btn_restore)).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                            return;
                    }

                }




                final ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this, "Aguarde um momento", "Estamos trabalhando no banco de dados", true);
            ringProgressDialog.setCancelable(false);
              Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (db.isOpen()){
                            db.close();}

                        int s = restaurarBDC();

                        ringProgressDialog.dismiss();
                        Manipulador.sendEmptyMessage(s);

                        if (s == 11 || s == 10) {
                         db = openOrCreateDatabase(NOME_DO_BANCO, MODE_PRIVATE, null);
                        }


                    }
                });
                th.start();
            }});

}


    public static void ExecutaSql(String SQL){
      db.execSQL(SQL);
      // Toast.makeText(context, SQL, Toast.LENGTH_SHORT).show();
    }




    public static boolean getDialogValueBack(Context context, String title, String msg) {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (title.length() != 0) alert.setTitle(title);

        alert.setMessage(msg);
        resultValue = false;

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        /* alert.setNegativeButton("Return False", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        }); */
        alert.show();

        try{
            if (resultValue == false)

            Looper.loop(); }
        catch(RuntimeException e){}
        return resultValue;
    }


 public static void Alerta(Context context, String Title, String msg) {
     AlertDialog.Builder builder = new AlertDialog.Builder(context);
     if (Title != "") builder.setTitle(Title);

     builder.setMessage(msg);
     /*builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
             Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
             startActivity(intentLogout);
             finish();
         }
     });
*/
     builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
         }
     });
     builder.create().show();
    }


    public static String convertStringToDate(String dtStart){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date datetmp = null;
        try {
            datetmp = format.parse(dtStart);

            format = new SimpleDateFormat("dd/MM/yy");
            return format.format(datetmp);


        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }




    private int restaurarBDC(){
        int i = 10;


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {
            // Caminho do Backup Banco de Dados
            File file = new File(Environment.getExternalStorageDirectory()+ "//BANCO_RESTORE.db");
            if (file.exists()==false){ return 10;};


            Log.d("TESTE","A");
            InputStream in = new FileInputStream(file);
            // Caminho de Destino do Backup do Seu Banco de Dados
            OutputStream out = new FileOutputStream(new File(
                    Environment.getDataDirectory(), "//data//".concat(getPackageName()).concat("//databases//"+NOME_DO_BANCO)));


            Log.d("TESTE","B");
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            i = 11;

        } catch (FileNotFoundException e) {
            Log.d("TESTE",e.getClass().toString()+": "+  e.getMessage());
            e.printStackTrace();
            i = 12;
        } catch (IOException e) {
            Log.d("TESTE",e.getMessage());
            e.printStackTrace();
            i = 13;
            }

        return i;
    }


    private int CopyBDC(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


            int s= 0;
        //File f=new File(Environment.getDataDirectory() + "/data/com.example.finish/databases/"+NOME_DO_BANCO+".db");

        File f = new File(Environment.getDataDirectory(), "//data//".concat(getPackageName()).concat("//databases//"+NOME_DO_BANCO));

        if(f.exists() == false){
            return 0; }

        FileInputStream fis=null;
        FileOutputStream fos=null;

        Log.d("TESTE","0");

        try
        {
            fis=new FileInputStream(f);
            fos=new FileOutputStream(Environment.getExternalStorageDirectory()+ "//finish.db");
            while(true)
            {
                int i=fis.read();
                if(i!=-1)
                {fos.write(i);}
                else
                {break;}
            }
            fos.flush();
            s = 1;




            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("*/*");
            File fnew = new File(Environment.getExternalStorageDirectory()+ "//finish.db");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fnew));
            startActivity(Intent.createChooser(share, "123"));


        }
        catch(Exception e)
        {
            //MainActivity.Alerta(MainActivity.this,"",e.getMessage());
            Log.d("TESTE",e.getMessage());
            e.printStackTrace();
            s = 2;
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(IOException ioe)
            {}
        }

        return s;
    }

    private void ShareBDC(){

       File fos=new File(Environment.getExternalStorageDirectory()+ "finish.db");

       if (fos.exists() == false){
           return; }

    }

    private void backup (){
        try {
            // Caminho de Origem do Seu Banco de Dados
            InputStream in = new FileInputStream(
                    new File(Environment.getDataDirectory()
                            + "/data/com.example.finish/databases/"+NOME_DO_BANCO+".db"));

            // Caminho de Destino do Backup do Seu Banco de Dados
            OutputStream out = new FileOutputStream(new File(
                    Environment.getExternalStorageDirectory()
                            + "/seuBanco.db"));

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static boolean isLongNumeric(String s){
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false; }

    }


    public static void SetPage(long pagina, Canvas canvas,int Border) {

        int Size_head  = 45;
        int y = Border;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(Size_head);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Página: "+pagina,Border, canvas.getHeight()-Border, paint);
    }


    public static String LoadCfg(int cod){
        Cursor c = db.rawQuery("select valor from configuration WHERE codigo = "+cod,null);
        if (c.moveToFirst()){
            return  c.getString(0).toString();
        }else return "";
    }

    public static void UpdateCfg(int cod,String valor){
        Cursor c = db.rawQuery("select valor from configuration WHERE codigo = "+cod,null);
        if (c.moveToFirst()){
          ExecutaSql("UPDATE configuration SET valor = '"+valor+"' WHERE codigo = "+cod);
        }else{
            ExecutaSql("INSERT INTO configuration  (codigo, valor) VALUES ("+cod+", '"+valor+"')");
        };
    }








}


/*
 Codigo
  1: valor = data_Start_filtro, se diferente de "" então está ativado
  2: valor = data_end_filtro


  // load cod situacao
  3: type of filtro{ ""=false, ja salva o texto sql }


  4: fator de correção -- 106 -> lista relatorio pedidos

 */
