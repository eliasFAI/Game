package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bouncingball.R;

public class MainActivity extends AppCompatActivity {


    MediaPlayer mp ;
    Button jugar,ranking ,opciones,salir;
    TextView mostrar_user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this,R.raw.clic);

       recibirDatos();


    }
    @Override
    protected void onResume() {
        super.onResume();

        //Carga Activity.
        actualizarIdioma();

    }
    public void recibirDatos(){

        Bundle extra = getIntent().getExtras();
        String name_user = extra.getString("id_user");
        mostrar_user = (TextView) findViewById(R.id.textV);
        mostrar_user.setText(name_user);
        jugar = (Button) findViewById(R.id.button5);
        ranking = (Button) findViewById(R.id.button6);
        opciones = (Button) findViewById(R.id.button7);
        salir = (Button) findViewById(R.id.button8);

    }
    public void play(View v){
        mp.start();
        Intent jugar = new Intent(MainActivity.this, Main4Activity.class);
        startActivity(jugar);
    }
    public void mostrar(View v){
     mp.start();
     Intent mostrarActivity = new Intent(MainActivity.this, Main3Activity.class);
        mostrarActivity.putExtra("id_user2",mostrar_user.getText().toString());
     startActivity(mostrarActivity);


    }
    public void opciones(View v){
        mp.start();
        Intent k = new Intent(MainActivity.this, Opciones.class);
        k.putExtra("id_user2",mostrar_user.getText().toString());
        startActivity(k);

    }
    public void exit(View v){
        mp.start();
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
    private void actualizarIdioma(){
        SharedPreferences preferences = getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){

          jugar.setText("JUGAR");
          ranking.setText("RANKING");
          opciones.setText("OPCIONES");
          salir.setText("SALIR");


        }
        else{

            jugar.setText("PLAY");
            ranking.setText("RANKING");
            opciones.setText("OPTION");
            salir.setText("EXIT");

        }

    }


}
