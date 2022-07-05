package com.example.work.Situacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.work.MainActivity;
import com.example.work.R;

public class Situacao_new_Activity extends AppCompatActivity {
    public static long COD_EDIT = -1;

    public EditText edit_nome, edit_color;
    public static Tsituacao ST_temp = new Tsituacao();

    @Override
    protected void onStart() {
        super.onStart();
        if (COD_EDIT != -1) {
            edit_nome.setText(ST_temp.getNome());
            edit_color.setText(""+ST_temp.getColor());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao_new_);
        getSupportActionBar().setTitle("Editando situação");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_color= (EditText) findViewById(R.id.situacao_new_edit_color);
        edit_nome = (EditText) findViewById(R.id.situacao_new_edit_nome);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table,menu);
//        sv = (SearchView) menu.findItem(R.id.item_search).getActionView();
//        sv.setQueryHint("Procurar mercadoria");
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.item_del);
        menuItem.setVisible( COD_EDIT != -1 );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_post:
                post_click();
                finish();
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void post_click(){
        if (edit_color.getText().length() == 0 ) edit_color.setText("0");

        String str = "";
        String msg = "";
        if (COD_EDIT == -1) {
              str = "INSERT INTO situacao (Nome, cor, Filtred) VALUES ('" +
                        edit_nome.getText() + "', " +
                        edit_color.getText() + "," +
                      "'true' )";
                msg = "Código gerado, Registro inserido";
        }else{
            str = "UPDATE situacao SET" +
                    " nome = '" + edit_nome.getText() + "'," +
                    " cor = " + edit_color.getText()+
                    "  WHERE codigo = " + COD_EDIT;
            msg = "Registro alterado";
        }
        //ainActivity.ExecutaSql(getApplicationContext(),"UPDATE cliente SET codigo = 24 WHERE codigo = 23");

        try{
            MainActivity.Alerta(this,"VERIFICAR O TAMANHO DO CAMPO COR ","az");

            MainActivity.ExecutaSql(str);
             finish();


        } catch (Exception e){
            MainActivity.Alerta(this,"EXCEPTION",e.getClass()+":  "+e.getMessage());
        }


    }


}