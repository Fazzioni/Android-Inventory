package com.example.work.Pedidos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.MainActivity;
import com.example.work.Mercadoria.Produtos_activity;
import com.example.work.Mercadoria.Tproduto_Item;
import com.example.work.R;

import java.math.BigDecimal;

import static android.app.PendingIntent.getActivity;

public class Pedido_new_produto extends AppCompatActivity {

    private static TextView lbl_mercadoria, lbl_preco;
    private static EditText edit_qtd, edit_preco;
    private Button btn_cancel, btn_post, btn_mais, btn_menos;

    public static long codigo_livro;
    public static long cod_mercadoria;
    public static BigDecimal preco = BigDecimal.ZERO;
    public static BigDecimal quantidade = BigDecimal.ZERO;

    private int ignore_close = 0;

    @Override
    protected void onStart() {
        super.onStart();

        ignore_close = ignore_close+1;

        Boolean b = true;

        if (ignore_close >= 2)
        if (Pedido_new_Activity.First_insert == true){
            b = false;
            finish();

        }
        if ((b) & (ignore_close == 2)){
        try {
            Log.d("TESTE","SHOW TECLADO");

            edit_qtd.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit_qtd.requestFocus();
            imm.showSoftInput(edit_qtd, InputMethodManager.SHOW_FORCED);

            //edit_qtd.focu
           // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
           // imm.showSoftInput(edit_qtd, InputMethodManager.SHOW_IMPLICIT);

        } catch (Exception e){
            Log.d("TESTE",e.getMessage());
        }}


        Log.d("TESTE","start: "+ignore_close);
    }

    public static void AlteraProduto(Tproduto_Item item){
        lbl_mercadoria.setText(item.getCodigo()+". "+item.getNome());

        if (Pedido_new_Activity.Usar_preco_imposto == true){       preco = item.getPreco_ci();
        }else{  preco = item.getPreco_si(); }

        lbl_preco.setText("R$ "+preco);

        cod_mercadoria = item.getCodigo();

        quantidade = BigDecimal.ZERO;
        edit_qtd.setText("");

        Pedido_new_Activity.First_insert = false;




    }



    //=======================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("teste","START ON CREATE PEDIDO_NEW_PRODUTO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_new_produto);

        codigo_livro = -1;
        cod_mercadoria = -1;


        lbl_mercadoria = (TextView) findViewById(R.id.P_L_M_lbl_Mercadoria);
        lbl_preco = (TextView) findViewById(R.id.P_L_M_lbl_preco);

        edit_preco = (EditText) findViewById(R.id.P_L_M_Edit_Preco);
        edit_qtd = (EditText) findViewById(R.id.P_L_M_Edit_quantidade);

        btn_cancel = (Button) findViewById(R.id.P_L_M_btn_cancel);
        btn_mais = (Button) findViewById(R.id.P_L_M_btn_mais);
        btn_menos = (Button) findViewById(R.id.P_L_M_btn_menos);
        btn_post = (Button) findViewById(R.id.P_L_M_btn_salvar);

        ignore_close = 0;
        Log.d("TESTE","Pedido_new -> 1.Loaded Values");


        edit_qtd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()){
                    //MainActivity.Alerta(Pedido_new_produto.this,"ok","ok");
                    Log.d("TESTE","BUTTO OK PRESSED");
                    btn_post.requestFocus();

                }


                return false;
            }
        });

        btn_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {

                    BigDecimal tmp =  new BigDecimal(edit_qtd.getText().toString());
                    tmp = tmp.add(BigDecimal.ONE);
                    edit_qtd.setText(tmp+"");

                    tmp = tmp.multiply(preco);
                    edit_preco.setText(tmp+"");

                    hideKeyboard();


                }catch (Exception e){
                    edit_qtd.setText("1");
                    edit_preco.setText(preco+"");
                } }});

        btn_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                    BigDecimal tmp =  new BigDecimal(edit_qtd.getText().toString());
                    tmp = tmp.subtract(BigDecimal.ONE);

                    if (tmp.compareTo(BigDecimal.ZERO) <= 0){tmp = BigDecimal.ONE;};


                    edit_qtd.setText(tmp+"");

                    tmp = tmp.multiply(preco);
                    edit_preco.setText(tmp+"");

                hideKeyboard();

                }catch (Exception e){
                    edit_qtd.setText("1");
                    edit_preco.setText(preco+"");
                } }     });

        lbl_mercadoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clickmercadoria();
            }
        });

        edit_qtd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    BigDecimal tmp =  new BigDecimal(edit_qtd.getText().toString());
                    tmp = tmp.multiply(preco);
                    edit_preco.setText(tmp+"");
                }catch (Exception e){
                }


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pedido_new_produto.this);
                builder.setTitle("ATENÇÃO!!!");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setMessage("O item "+lbl_mercadoria.getText()+" será removido do pedido,\n deseja continuar?").setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Removeproduto_pedido();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplication(), "Remoção cancelada pelo usuário", Toast.LENGTH_SHORT);
                    }
                });
                builder.show();

                }});


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (quantidade.compareTo(BigDecimal.ZERO) <= 0){tmp = BigDecimal.ONE;};
                String sql = "";
                if (edit_qtd.getText().length() == 0){
                    sql = edit_preco.getText().toString();
                    edit_qtd.setText("0"); // ao mudar a quantidade o campo muda o preco, salvar temporariamente
                    edit_preco.setText(sql);
                    sql = "";  }

                // quantidade nao pode ser igual a zero
                try{
                   BigDecimal tst = new BigDecimal(edit_qtd.getText().toString());
                   if (tst.compareTo(BigDecimal.ZERO)== 0){
                       MainActivity.Alerta(Pedido_new_produto.this,"Error","O campo quantidade é inválido");
                       return;
                   }

                }catch (Exception e){
                   MainActivity.Alerta(Pedido_new_produto.this,"Error","O campo quantidade é inválido");
                    return;
                }


                if (edit_preco.getText().length() == 0){
                    edit_preco.setText("0");
                }

                if (cod_mercadoria == -1){
                    MainActivity.Alerta(Pedido_new_produto.this,"Ação impossível","por favor adicione novamente a mercadoria! \n\n Motivo do erro: codigo_mercadoria = -1");
                    return;
                }

                // precisamos do código do pedido
                if (Pedido_new_Activity.Pedido_codigo == -1){
                    // precisamos salvar o pedido
                    Pedido_new_Activity.PostPedido();
                    if (Pedido_new_Activity.Pedido_codigo == -1){
                        MainActivity.Alerta(Pedido_new_produto.this,"Ação impossível","por favor salve o pedido \n\n Motivo do erro: codigo do pedido = -1");
                        return;
                    }
                }
                try{ quantidade = new BigDecimal(edit_qtd.getText().toString());} catch (Exception e){ quantidade = BigDecimal.ZERO;     }
                try{ preco = new BigDecimal(edit_preco.getText().toString());} catch (Exception e){ preco = BigDecimal.ZERO;     }


                if (codigo_livro == -1){
                    sql = "INSERT INTO livro (cod_pedido, cod_produto, qtd, preco) VALUES ("+Pedido_new_Activity.Pedido_codigo+", "+cod_mercadoria+", "+quantidade+", "+preco+")";
                } else {
                    sql = "UPDATE livro set" +
                            " cod_produto = "+cod_mercadoria+", "+
                            " qtd = "+quantidade+", "+
                            " preco = "+preco+" " +
                            " WHERE codigo = "+codigo_livro;
                }
                try{
                    MainActivity.ExecutaSql(sql);
                    finish();
                    Pedido_new_Activity.ListarMercadoriasInProdutoNew(getApplicationContext());
                    Pedido_new_Activity.adicionaNovoProduto = true;

                } catch (Exception e){
                    Log.d("LOG_ERROR", e.getClass()+":  "+e.getMessage());
                    // MainActivity.Alerta(this ,"EXCEPTION",e.getClass()+":  "+e.getMessage());
                }


            } });


        //facilitar a vida do usuário
        if (Pedido_new_Activity.First_insert == true)
        { Clickmercadoria(); }
        else{
            Log.d("TESTE","Pedido_new -> Start read: Pedido_new_activity.first_insert == false");
            codigo_livro = Pedido_new_Activity.Tmp_long_itemClick.getCodigo_livro();
            cod_mercadoria = Pedido_new_Activity.Tmp_long_itemClick.getCodigo_mercadoria();

            Cursor c = MainActivity.db.rawQuery("SELECT Codigo,Nome,preco_ci,preco_si,Item_especial FROM mercadoria WHERE codigo = "+cod_mercadoria,null);
            if (c.moveToFirst()){
                Tproduto_Item tmp = new Tproduto_Item(cod_mercadoria,c.getString(1),new BigDecimal(c.getString(2).toString()),new BigDecimal(c.getString(3).toString()),Boolean.parseBoolean(c.getString(4)));


                AlteraProduto(tmp);


                quantidade = Pedido_new_Activity.Tmp_long_itemClick.getQuantidade();
                preco = Pedido_new_Activity.Tmp_long_itemClick.getPreco().divide(quantidade,2,BigDecimal.ROUND_HALF_UP);

                 BigDecimal preco_db = BigDecimal.ZERO;

                  if (Pedido_new_Activity.Usar_preco_imposto)
                  {preco_db = tmp.preco_ci;}
                  else{preco_db = tmp.preco_si;}


                  if (preco_db.compareTo(preco) != 0){
                      MainActivity.Alerta(Pedido_new_produto.this,"Preço unitário diferente da lista de preço","Preço da lista: "+preco_db+"\nPreço em uso: "+preco);

                  //Toast.makeText(Pedido_new_produto.this,"Preço unitário diferente da lista de preço",Toast.LENGTH_LONG);
                  }


                lbl_preco.setText("R$ "+preco);


                edit_qtd.setText(""+quantidade);
                edit_preco.setText(""+Pedido_new_Activity.Tmp_long_itemClick.getPreco());


            }else
            {finish();}
        }

        Log.d("TESTE","FINISH CREATE PEDIDO_NEW_PRODUTO");
    }




    public void Clickmercadoria(){

        Intent intent = new Intent(Pedido_new_produto.this, Produtos_activity.class);
        MainActivity.SetPedido_Mercadoria = true;
        if (Pedido_new_Activity.Usar_preco_imposto == true) {
            MainActivity.SetPedido_Mercadoria_Show_Preco = 1;
        } else {MainActivity.SetPedido_Mercadoria_Show_Preco = 2;}

        Produtos_activity.ShowTeclado = true;
        startActivity(intent);
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void Removeproduto_pedido(){
    if (codigo_livro == -1){
        finish();
    } else {
        String  sql = "DELETE FROM livro" +
                " WHERE codigo = "+codigo_livro;
        try{
            MainActivity.ExecutaSql(sql);
            finish();
            Pedido_new_Activity.ListarMercadoriasInProdutoNew(getApplicationContext());

        } catch (Exception e){
            Log.d("LOG_ERROR", e.getClass()+":  "+e.getMessage());
            // MainActivity.Alerta(this ,"EXCEPTION",e.getClass()+":  "+e.getMessage());
        }}}

}
