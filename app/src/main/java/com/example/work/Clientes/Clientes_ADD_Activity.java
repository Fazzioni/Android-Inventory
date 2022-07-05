package com.example.work.Clientes;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.R;


public class Clientes_ADD_Activity extends AppCompatActivity {

    public static long COD_EDIT = -1;
    public static Cliente_Item CL_temp = new Cliente_Item();


    private Boolean Registro_Editado = false;


    public EditText edit_codigo, edit_nome, edit_cidade, edit_endereco;
    public TextView Lbl_CpfValid;

    protected void onStart() {
        super.onStart();
      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes__add_);

        getSupportActionBar().setTitle("Editando cliente");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Lbl_CpfValid = (TextView)  findViewById(R.id.lbl_Clientes_add_cpf_confirm);
        edit_codigo = (EditText) findViewById(R.id.Edit_Codigo);
        edit_nome = (EditText) findViewById(R.id.Edit_Nome);
        edit_cidade = (EditText) findViewById(R.id.Edit_Cidade);
        edit_endereco = (EditText) findViewById(R.id.Edit_endereco);


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
        edit_cidade.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        edit_endereco.addTextChangedListener(CpfCnpjMaks.insert(edit_endereco));


        edit_endereco.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {Registro_Editado = true;
            // calcula cpf e cnpj
                try{
                if (isValid(CpfCnpjMaks.unmask(edit_endereco.getText().toString()))){
                    if (isValidCPF(CpfCnpjMaks.unmask(edit_endereco.getText().toString()))){Lbl_CpfValid.setText("CPF Válido"); }else{ Lbl_CpfValid.setText("CNPJ Válido"); }
                    Lbl_CpfValid.setTextColor(Color.GREEN);
                }else{
                    Lbl_CpfValid.setText("CPF/CNPJ inválido: "+ CpfCnpjMaks.unmask(edit_endereco.getText().toString()).length());
                    Lbl_CpfValid.setTextColor(Color.RED);
                }
            }catch (Exception e){
                    Lbl_CpfValid.setText("CPF/CNPJ inválido: "+ CpfCnpjMaks.unmask(edit_endereco.getText().toString()).length());
                    Lbl_CpfValid.setTextColor(Color.RED);
                }}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });





        if (COD_EDIT != -1) {
            edit_codigo.setText("" + CL_temp.getCodigo());
            edit_nome.setText(CL_temp.getNome());
            edit_cidade.setText(CL_temp.getCidade().toString());
            edit_endereco.setText(CL_temp.getEnderecao().toString());
        }

        Registro_Editado = false;
    };


    public void Click_post() {

        Log.d("teste", "click");


        if (edit_codigo.getText().length() == 0) {
            if (COD_EDIT != -1) {
                MainActivity.Alerta(Clientes_ADD_Activity.this, "Erro!!", "Verifique o código do cliente\n o valor precisa ser um inteiro e não nulo");
                return;
            }
        } else if (MainActivity.isLongNumeric(edit_codigo.getText().toString()) == false) {
            MainActivity.Alerta(Clientes_ADD_Activity.this, "Erro!!", "Código inválido \n Informe um número do tipo inteiro");
            return;
        }

        Log.d("teste", "click2");

        if (edit_nome.getText().toString().length() == 0) {
            MainActivity.Alerta(Clientes_ADD_Activity.this, "Erro!!", "Informe um nome ao cliente");
            return;
        }


        String str = "";
        String msg = "";
        if (COD_EDIT == -1) {
            if (edit_codigo.getText().length() == 0) {
                str = "INSERT INTO cliente (Nome, Endereco, cidade) VALUES ('" +
                        edit_nome.getText() + "','" +
                        edit_endereco.getText().toString() + "','" +
                        edit_cidade.getText().toString() + "')";
                msg = "Registro inserido";
            } else {
                str = "INSERT INTO cliente (Codigo, Nome, Endereco, cidade) VALUES (" +
                        edit_codigo.getText() + ",'" +
                        edit_nome.getText() + "','" +
                        edit_endereco.getText().toString() + "','" +
                        edit_cidade.getText().toString() + "')";
                msg = "Código gerado, Registro inserido";
            }
        } else {
            str = "UPDATE cliente SET" +
                    " codigo = " + edit_codigo.getText() + "," +
                    " nome = '" + edit_nome.getText() + "'," +
                    " endereco = '" + edit_endereco.getText().toString() + "'," +
                    " cidade = '" + edit_cidade.getText().toString() + "'" +
                    " WHERE codigo = " + COD_EDIT;
            msg = "Registro alterado";
        }
        //ainActivity.ExecutaSql(getApplicationContext(),"UPDATE cliente SET codigo = 24 WHERE codigo = 23");

        try {
            Log.d("TESTE",str);

            MainActivity.ExecutaSql(str);


            if (COD_EDIT != -1)
                if (COD_EDIT != Integer.parseInt(edit_codigo.getText().toString())) {
                    try {
                        MainActivity.ExecutaSql("UPDATE pedido set cod_cliente = " + edit_codigo.getText() + " WHERE cod_cliente = " + COD_EDIT);
                    } catch (Exception e) {
                        MainActivity.Alerta(Clientes_ADD_Activity.this, "EXCEPTION", "ERRO CRÍTICO AO ATUALIZAR O CÓDIGO DO CLIENTE NOS PEDIDOS \n antigo: " + COD_EDIT + "\n CODIGO NOVO (N FOI ATUALIZADO): " + edit_codigo.getText());
                        return;
                    }
                }
            finish();
        } catch (Exception e) {
            int i = e.getMessage().toLowerCase().indexOf("code 1555");

            if (i != -1) {
                MainActivity.Alerta(Clientes_ADD_Activity.this, "EXCEPTION", "Já existe um cliente com esse código");
            } else
                MainActivity.Alerta(Clientes_ADD_Activity.this, "EXCEPTION", e.getClass() + ":  " + e.getMessage() + "\n\n\n" + i);


        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table, menu);

        MenuItem menuItem = (MenuItem) menu.findItem(R.id.item_del);
        menuItem.setVisible(COD_EDIT != -1);
        ((MenuItem) menu.findItem(R.id.Share)).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_post:
                Click_post();
                break;

            case R.id.item_del:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ATENÇÃO!!!");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("O Cliente "+edit_nome.getText()+" será removido permanentemente, deseja continuar?").setPositiveButton("SIM, eu desejo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExcluiUsuario();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplication(), "Exclução cancelada pelo usuário", Toast.LENGTH_SHORT);
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

public void ExcluiUsuario(){
    Cursor c = MainActivity.db.rawQuery("SELECT codigo FROM pedido WHERE cod_cliente = " + COD_EDIT, null);
                if(c.moveToFirst()==false)
    {
        try {
            MainActivity.ExecutaSql("DELETE FROM cliente WHERE codigo = " + COD_EDIT);
            // MainActivity.Alerta(Produtos_Add_Activity.this, "Item removido","");
            //MainActivity.Show_info = "Item Removido";
            finish();
        } catch (Exception e) {
            MainActivity.Alerta(Clientes_ADD_Activity.this, "EXCEPTION", e.getClass() + ": " + e.getMessage());
        }


    }else
            MainActivity.Alerta(Clientes_ADD_Activity .this,"O cliente não pode ser removido","Existe um pedido relacionado");
}



    @Override
    public void onBackPressed() {
        if (Registro_Editado) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Atenção")
                    .setMessage("O cliente não foi salvo, deseja salvar?")
                    .setPositiveButton("Sim, salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Click_post();
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

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cpfCnpj) {
        return (isValidCPF(cpfCnpj) || isValidCNPJ(cpfCnpj));
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    private static String padLeft(String text, char character) {
        return String.format("%11s", text).replace(' ', character);
    }

    private static boolean isValidCPF(String cpf) {
        cpf = cpf.trim().replace(".", "").replace("-", "");
        if ((cpf==null) || (cpf.length()!=11)) return false;

        for (int j = 0; j < 10; j++)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
                return false;

        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
    }

    private static boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.trim().replace(".", "").replace("-", "");
        if ((cnpj==null)||(cnpj.length()!=14)) return false;

        Integer digito1 = calcularDigito(cnpj.substring(0,12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0,12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0,12) + digito1.toString() + digito2.toString());
    }


}
