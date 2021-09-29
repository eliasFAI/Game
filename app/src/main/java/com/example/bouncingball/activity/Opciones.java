package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bouncingball.R;

public class Opciones extends AppCompatActivity {

    private TextView mostrar_user ;
    private CheckBox c1,c2,c3 ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones2);
        recibir_date();
        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
    }

    public void recibir_date(){
        Bundle extra = getIntent().getExtras();
        String name_user = extra.getString("id_user2");
        mostrar_user = (TextView) findViewById(R.id.TexViewPlayers);
        mostrar_user.setText(name_user);
    }
    public void onClick(View v){

         if(v.getId()==R.id.button9){
            validar();
         }

    }
    private void validar(){

        String cad ="Seleccionado: \n";

        if(c1.isChecked()){
          cad+="Opcion1";
        }
        if(c2.isChecked()){
            cad+="Opcion2";
        }
        if(c3.isChecked()){
            cad+="Opcion3";
        }
        Toast.makeText(getApplicationContext(),cad,Toast.LENGTH_SHORT).show();
    }
}