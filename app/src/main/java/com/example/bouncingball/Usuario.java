package com.example.bouncingball;

import java.io.Serializable;

public class Usuario  implements Serializable {

    private Integer id;
    private String usuario ;
    private String clave ;
    //private String pais;
    private String email ;
    private Integer puntaje;

    public Usuario(){

        this.usuario = "";

    }
    public Usuario(String u){

        this.usuario = u;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Integer getPuntaje(){return this.puntaje;}
    public void setPuntaje(Integer p){

        this.puntaje = p ;
    }



}
