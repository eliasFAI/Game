package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bouncingball.R;
import com.example.bouncingball.clases.Usuario;
import com.example.bouncingball.database.dbConexion;

public class LoginScreen extends AppCompatActivity {

    private EditText usuario ;
    private EditText clave;
    private dbConexion dao ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usuario = findViewById(R.id.editUser);
        clave = findViewById(R.id.editPassword);
        dao = new dbConexion(this);
    }

    public void logins(View v){

        Usuario us = new Usuario(usuario.getText().toString(),clave.getText().toString());

       if(dao.consultarUsuario(us)!=null) {

            Intent menu = new Intent(LoginScreen.this, MainActivity.class);
            menu.putExtra("id_user",us.getUsuario());
            startActivity(menu);

      }
      else{
            Toast.makeText(this, "User/Password Incorrectos" , Toast.LENGTH_SHORT).show();
        }
    }
    public void register(View v){
        Intent menu = new Intent(LoginScreen.this, Main2Activity.class);
        startActivity(menu);
    }
}