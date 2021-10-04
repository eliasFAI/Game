package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bouncingball.R;
import com.example.bouncingball.clases.Usuario;
import com.example.bouncingball.database.dbConexion;

public class Main2Activity extends AppCompatActivity {

    private EditText usuario ;
    private EditText clave;
    private EditText email ;
    private dbConexion dao ;
    private Button btn_aceptar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usuario = findViewById(R.id.editUsuario);
        clave = findViewById(R.id.editClave);
        email = findViewById(R.id.editEmail);
        btn_aceptar =(Button)findViewById(R.id.button4);
        dao = new dbConexion(this);
    }
    protected void onResume() {
        super.onResume();

        //Carga Activity.
        actualizarIdioma();

    }
    private void actualizarIdioma(){
        SharedPreferences preferences = getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){

            usuario.setText("Usuario");
            clave.setText("Clave");
            email.setText("Correo");
            btn_aceptar.setText("ACEPTAR");

        }
        else{
            usuario.setText("User");
            clave.setText("Password");
            email.setText("Email");
            btn_aceptar.setText("ACCEPT");

        }

    }
    public void guardar(View view){


       Usuario a = new Usuario (usuario.getText().toString(),clave.getText().toString(),email.getText().toString(),"0");

        if(!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
        {

           long  id = dao.insertarUser(usuario.getText().toString(),clave.getText().toString(),email.getText().toString());
            if(id>0) {
                Toast.makeText(this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                Intent menu = new Intent(Main2Activity.this, MainActivity.class);
                menu.putExtra("id_user",a.getUsuario());
                startActivity(menu);
                usuario.setText("");
                clave.setText("");
                email.setText("");
            }
            else{
                Toast.makeText(this, "Error al GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();
            }
        }
        else{

            Toast.makeText(this, "Debe llenar todos los campos " , Toast.LENGTH_SHORT).show();
        }


    }

}
