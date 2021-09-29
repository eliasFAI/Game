package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bouncingball.R;

public class MainActivity extends AppCompatActivity {


    MediaPlayer mp ;
    Button siguiente ;
    Button ranking ;
    TextView mostrar_user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* ranking =(Button) findViewById(R.id.btnRanking);
        ranking.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent rank = new Intent(MainActivity.this,Main3Activity.class);
                startActivity(rank);


            }
        });*/
       recibirDatos();

    }
    public void recibirDatos(){

        Bundle extra = getIntent().getExtras();
        String name_user = extra.getString("id_user");
        mostrar_user = (TextView) findViewById(R.id.textV);
        mostrar_user.setText(name_user);

    }
    public void play(View v){

        Intent jugar = new Intent(MainActivity.this, Main4Activity.class);
        startActivity(jugar);
    }
    public void mostrar(View v){

     Intent mostrarActivity = new Intent(MainActivity.this, Main3Activity.class);
        mostrarActivity.putExtra("id_user2",mostrar_user.getText().toString());
     startActivity(mostrarActivity);


    }
    public void opciones(View v){
        Intent k = new Intent(MainActivity.this, Opciones.class);
        k.putExtra("id_user2",mostrar_user.getText().toString());
        startActivity(k);

    }
    public void exit(View v){
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
    public void sonido(View v){

        if(mp!=null){

            mp.release();
        }
        mp = MediaPlayer.create(this,R.raw.play);
        mp.seekTo(0);
        mp.start();
        mp.setLooping(true);
    }
    public void silencio (View v){
        /*Por ahora funciona */
        if(mp!=null){

            mp.stop();
        }

      //  mp.setLooping(false);
    }


}
