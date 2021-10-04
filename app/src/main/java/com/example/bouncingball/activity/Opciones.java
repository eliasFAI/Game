package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bouncingball.R;
import android.content.Context;

public class Opciones extends AppCompatActivity {

    private TextView mostrar_user ,txtNivelDelJuego;
    private CheckBox c1,c2,c3 ;
    private Switch aSwitchE ,aSwitchS;
    private MediaPlayer mp ;
    private Button b1 ,b2 ,b5;
    private EditText usuario ;
    private EditText clave;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones2);

        recibir_date();
        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        aSwitchS = (Switch) findViewById(R.id.switch3);
        txtNivelDelJuego =(TextView) findViewById(R.id.textView);
        b1 = (Button) findViewById(R.id.button9);
        b2 = (Button) findViewById(R.id.button10);
        b5 = (Button) findViewById(R.id.button11);

        usuario = findViewById(R.id.editUser);
        clave = findViewById(R.id.editPassword);
        actualizarIdioma();

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

    public void onClickIdioma(View v){

        SharedPreferences preferences = getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){


                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idioma","en");
                editor.commit();
                actualizarIdioma();
            }
            else{

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idioma","es");
                editor.commit();
                actualizarIdioma();
            }
        }

    private void actualizarIdioma(){
        SharedPreferences preferences = getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){
            aSwitchS.setText("Sonido");
            txtNivelDelJuego.setText("Nivel del Juego");
            c1.setText("Facil");
            c2.setText("Intermedio");
            c3.setText("Dificil");
            b1.setText("Validar");
            b2.setText("Anterior");
            b5.setText("CAMBIAR IDIOMA");



        }
        else{

            aSwitchS.setText("Sound");
            txtNivelDelJuego.setText("Level of Play");
            c1.setText("Easy");
            c2.setText("Intermediate");
            c3.setText("Hard");
            b1.setText("Validate");
            b2.setText("Previous");
            b5.setText("CHANGE IDIOM");
        }

    }
    public void onClickSound(View v){

        if(v.getId()==R.id.switch3){
            if(aSwitchS.isChecked()){
                System.out.println("Pasas x Aqui ");
                if(mp!=null){

                    mp.release();
                }
                mp = MediaPlayer.create(this,R.raw.soundtheme);
                if(mp==null){
                    System.out.println("Mp es Null ");
                }
                mp.seekTo(0);
                mp.start();
                mp.setLooping(true);
            }
            else{
                if(mp!=null){

                    mp.stop();
                }
            }
        }
    }
    public void volver(View v){
        Intent menu = new Intent(Opciones.this, MainActivity.class);
        startActivity(menu);
    }
}