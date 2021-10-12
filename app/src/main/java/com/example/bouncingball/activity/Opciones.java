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
import android.widget.ImageButton;
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
    private ImageButton btn_sonido ,btn_idioma ;
    private EditText usuario ;
    private EditText clave;
    private MediaPlayer mpclic ;
    private boolean encendida  ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones2);
        mpclic = MediaPlayer.create(this,R.raw.clic);

        recibir_date();
        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        txtNivelDelJuego =(TextView) findViewById(R.id.textView);
        b1 = (Button) findViewById(R.id.button10);
        b2 = (Button) findViewById(R.id.button9);
        btn_sonido = (ImageButton) findViewById(R.id.imageButton);
        btn_idioma = (ImageButton) findViewById(R.id.imageButton2);

        encendida = false ;
        usuario = findViewById(R.id.editUser);
        clave = findViewById(R.id.editPassword);
        actualizarIdioma();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void recibir_date(){
        Bundle extra = getIntent().getExtras();
        String name_user = extra.getString("id_user2");
        mostrar_user = (TextView) findViewById(R.id.TexViewPlayers);
        mostrar_user.setText(name_user);
    }
    public void onClick(View v){
         mpclic.start();
         if(v.getId()==R.id.button10){
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

        mpclic.start();
        SharedPreferences preferences = getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){
            btn_idioma.setImageResource(R.drawable.estadosunidos);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idioma","en");
                editor.commit();
                actualizarIdioma();
            }
            else{
            btn_idioma.setImageResource(R.drawable.argentina);
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


            txtNivelDelJuego.setText("Nivel del Juego");
            c1.setText("Facil");
            c2.setText("Intermedio");
            c3.setText("Dificil");
            b1.setText("Validar");
            b2.setText("Volver");

        }
        else{


            txtNivelDelJuego.setText("Level of Play");
            c1.setText("Easy");
            c2.setText("Intermediate");
            c3.setText("Hard");
            b1.setText("Validate");
            b2.setText("Previous");

        }

    }
    public void onClickSound(View v){

        if(encendida){
            apagarMusica();
        }else{
            enciendeMusica();
        }

    }
    private void enciendeMusica(){
        btn_sonido.setImageResource(R.drawable.consonido);
        Intent miReproductor = new Intent(this,ServicioMusica.class);
        this.startService(miReproductor);
        encendida =!encendida;

    }
    private void apagarMusica(){
        btn_sonido.setImageResource(R.drawable.sinsonido);
        Intent miReproductor = new Intent(this,ServicioMusica.class);
        this.stopService(miReproductor);
        encendida = !encendida;

    }
    public void volver(View v){
        mpclic.start();
        Intent menu = new Intent(Opciones.this, MainActivity.class);
        startActivity(menu);
    }
}