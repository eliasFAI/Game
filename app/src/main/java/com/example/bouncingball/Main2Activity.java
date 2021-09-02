package com.example.bouncingball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private EditText usuario ;
    private EditText clave;
    private EditText email ;
    private UsuarioDB dao ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usuario = findViewById(R.id.editUsuario);
        clave = findViewById(R.id.editClave);
        email = findViewById(R.id.editEmail);
        dao = new UsuarioDB(this);
    }

    public void guardar(View view){

        Usuario a = new Usuario();
        a.setUsuario(usuario.getText().toString());
        a.setClave(clave.getText().toString());
        a.setEmail(email.getText().toString());
        a.setPuntaje(1);

        if(!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
        {

            dao.insertar(a);

            Toast.makeText(this, "Registro Exitoso" , Toast.LENGTH_SHORT).show();
            usuario.setText("");
            clave.setText("");
            email.setText("");
        }
        else{

            Toast.makeText(this, "Debe llenar todos los campos " , Toast.LENGTH_SHORT).show();
        }
        Intent menu = new Intent(Main2Activity.this,MainActivity.class);
        startActivity(menu);

    }
}
