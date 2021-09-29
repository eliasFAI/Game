package com.example.bouncingball.clases;

import java.io.Serializable;

public class Usuario  implements Serializable {


    private String usuario ;
    private String clave ;
    private String email ;
    private String puntaje;

    public Usuario(){
        this.usuario = null;
        this.clave = null ;
        this.email = null ;
        this.puntaje = "";
    }
    public Usuario(String us,String pas){
        this.usuario = us ;
        this.clave = pas ;
    }
    public Usuario(String us ,String pas,String em,String p){

        this.usuario = us;
        this.clave = pas ;
        this.email = em ;
        this.puntaje = p ;
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String u) {
        this.usuario = u;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String em) {
        this.email = em;
    }
    public String getPuntaje(){return this.puntaje;}
    public void setPuntaje(String p){

        this.puntaje = p ;
    }
 //   public String toString(){
       // return this.usuario ;
  //  }



}
