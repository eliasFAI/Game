package com.example.bouncingball;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    MediaPlayer mp ;
    Button siguiente ;
    Button ranking ;
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


    }
    public void play(View v){

        Intent jugar = new Intent(MainActivity.this,Main4Activity.class);
        startActivity(jugar);
    }
    public void mostrar(View v){

     Intent mostrarActivity = new Intent(MainActivity.this,Main3Activity.class);
     startActivity(mostrarActivity);


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
