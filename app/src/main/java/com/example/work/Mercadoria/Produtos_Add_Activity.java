
package com.example.work.Mercadoria;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import com.example.work.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.work.R;

import java.math.BigDecimal;

public class Produtos_Add_Activity extends AppCompatActivity {
    public static long COD_EDIT = -1;

    public EditText edit_codigo, edit_nome, edit_preco_ci, edit_preco_si;

    public static Tproduto_Item PT_temp = new Tproduto_Item();

    public Switch Id_Produto_switch;
    private Boolean Registro_Editado = false;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos__add_);
        getSupportActionBar().setTitle("Editando Produto");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_codigo = (EditText) findViewById(R.id.edit_codigo);
        edit_nome = (EditText) findViewById(R.id.edit_nome);
        edit_preco_ci = (EditText) findViewById(R.id.edit_preco_ci);
        edit_preco_si = (EditText) findViewById(R.id.edit_preco_si);

        Id_Produto_switch = (Switch) findViewById(R.id.Id_Produto_switch_item);

        edit_codigo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        edit_nome.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        edit_preco_ci.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        edit_preco_si.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                 Registro_Editado = true;
                Log.d("TESTE", "1");
                try {
                    BigDecimal tmp = new BigDecimal(edit_preco_si.getText().toString());
                    Log.d("TESTE", "2");
                    tmp = tmp.divide(new BigDecimal("1.06"), 10, BigDecimal.ROUND_HALF_UP);
                    Log.d("TESTE", "3");
                    tmp = tmp.multiply(new BigDecimal("1.16"));
                    tmp = tmp.setScale(2, BigDecimal.ROUND_HALF_UP);
                    edit_preco_ci.setText(tmp + "");
                } catch (Exception e) { edit_preco_ci.setText(""); } }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });





        if (COD_EDIT != -1) {
            edit_codigo.setText("" + PT_temp.getCodigo());
            edit_nome.setText(PT_temp.getNome());
            edit_preco_si.setText("" + PT_temp.getPreco_si());

            edit_preco_ci.setText("" + PT_temp.getPreco_ci());


            //MainActivity.Alerta(Produtos_Add_Activity.this,"",PT_temp.getItem_especial().toString());
            Id_Produto_switch.setChecked(PT_temp.getItem_especial());
        }

        Registro_Editado = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table, menu);
//        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
//        sv.setQueryHint("Procurar mercadoria");
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.item_del);
        menuItem.setVisible(COD_EDIT != -1);

        ((MenuItem) menu.findItem(R.id.Share)).setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_post:
                // MainActivity.Show_info= "Item salvo";
                post_click();
                break;

            case R.id.item_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ATENÇÃO!!!");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setMessage("O produto será removido permanentemente, deseja continuar?").setPositiveButton("SIM, eu desejo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Excluiproduto();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplication(),"Exclução cancelada pelo usuário",Toast.LENGTH_SHORT);
                    }
                });
                builder.show();
                break;



            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void post_click() {

        if (edit_codigo.getText().length() == 0) {
            if (COD_EDIT != -1) {
                MainActivity.Alerta(Produtos_Add_Activity.this, "Erro!!", "Verifique o código do produto\n o valor precisa ser um inteiro e não nulo");
                return;
            }
        } else if (MainActivity.isLongNumeric(edit_codigo.getText().toString()) == false) {
            MainActivity.Alerta(Produtos_Add_Activity.this, "Erro!!", "Código inválido \n Informe um número do tipo inteiro");
            return;
        }


        if (edit_nome.getText().toString().length() == 0) {
            MainActivity.Alerta(Produtos_Add_Activity.this, "Erro!!", "Informe um nome ao produto");
            return;
        }

        if (edit_preco_si.getText().length() == 0) edit_preco_si.setText("0");
        if (edit_preco_ci.getText().length() == 0) edit_preco_ci.setText("0.0");


        String str = "";
        String msg = "";
        if (COD_EDIT == -1) {
            if (edit_codigo.getText().length() == 0) {
                str = "INSERT INTO mercadoria (Nome, preco_ci, preco_si, Item_especial) VALUES ('" +
                        edit_nome.getText() + "'," +
                        edit_preco_ci.getText() + " , " +
                        edit_preco_si.getText() + ", '" +
                        Id_Produto_switch.isChecked() + "' )";
                msg = "Registro inserido";
            } else {
                str = "INSERT INTO mercadoria (Codigo, Nome, preco_ci, preco_si, Item_especial) VALUES (" +
                        edit_codigo.getText() + ",'" +
                        edit_nome.getText() + "', " +
                        edit_preco_ci.getText() + " , " +
                        edit_preco_si.getText() + ", '" +
                        Id_Produto_switch.isChecked() + "' )";

                msg = "Código gerado, Registro inserido";
            }
        } else {
            str = "UPDATE mercadoria SET" +
                    " codigo = " + edit_codigo.getText() + "," +
                    " nome = '" + edit_nome.getText() + "'," +
                    " preco_ci = " + edit_preco_ci.getText() + ", " +
                    " preco_si = " + edit_preco_si.getText() + "," +
                    " Item_especial = '" + Id_Produto_switch.isChecked() + "' " +
                    " WHERE codigo = " + COD_EDIT;
            msg = "Registro alterado";
        }

        //ainActivity.ExecutaSql(getApplicationContext(),"UPDATE cliente SET codigo = 24 WHERE codigo = 23");


        try {
            MainActivity.ExecutaSql(str);

            if (COD_EDIT != -1)
                if (COD_EDIT != Integer.parseInt(edit_codigo.getText().toString())) {
                    try {
                        MainActivity.ExecutaSql("UPDATE livro set cod_produto = " + edit_codigo.getText() + " WHERE cod_produto = " + COD_EDIT);

                    } catch (Exception e) {
                        MainActivity.Alerta(this, "EXCEPTION", "ERRO CRÍTICO AO ATUALIZAR O CÓDIGO DA MERCADORIA NOS PEDIDOS \n antigo: " + COD_EDIT + "\n CODIGO NOVO (N FOI ATUALIZADO): " + edit_codigo.getText());
                        return;
                    }
                }


            finish();

        } catch (Exception e) {
            int i = e.getMessage().toLowerCase().indexOf("code 1555");

            if (i != -1) {
                MainActivity.Alerta(this, "EXCEPTION", "Já existe um produto com esse código");
            } else
                MainActivity.Alerta(this, "EXCEPTION", e.getClass() + ":  " + e.getMessage() + "\n\n\n" + i

                );
        }
    }

    public void Excluiproduto() {
    Cursor c = MainActivity.db.rawQuery("SELECT codigo FROM livro WHERE cod_produto = " + COD_EDIT, null);
                if(c.moveToFirst()==false)
    {
        try {
            MainActivity.ExecutaSql("DELETE FROM mercadoria WHERE codigo = " + COD_EDIT);
            Toast.makeText(getApplication(),"Produto excluído",Toast.LENGTH_LONG);

            // MainActivity.Alerta(Produtos_Add_Activity.this, "Item removido","");
            //MainActivity.Show_info = "Item Removido";
            finish();
        } catch (Exception e) {
            MainActivity.Alerta(Produtos_Add_Activity.this, "EXCEPTION", e.getClass() + ": " + e.getMessage());
        }


    }else
            MainActivity.Alerta(Produtos_Add_Activity .this,"O produto não pode ser removido","Existe um pedido relacionado");
    }


    @Override
    public void onBackPressed() {
        if (Registro_Editado) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Atenção")
                    .setMessage("O produto não foi salvo, deseja salvar?")
                    .setPositiveButton("Sim, salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            post_click();
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

    }

}

