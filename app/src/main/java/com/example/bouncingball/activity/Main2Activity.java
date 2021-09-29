package com.example.bouncingball.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usuario = findViewById(R.id.editUsuario);
        clave = findViewById(R.id.editClave);
        email = findViewById(R.id.editEmail);
        dao = new dbConexion(this);
    }

    public void guardar(View view){


       Usuario a = new Usuario (usuario.getText().toString(),clave.getText().toString(),email.getText().toString(),"0");

        if(!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
        {

           long  id = dao.insertarUser(usuario.getText().toString(),clave.getText().toString(),email.getText().toString());
            if(id>0) {
                Toast.makeText(this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                Intent menu = new Intent(Main2Activity.this, MainActivity.class);
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
