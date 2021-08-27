package com.example.bouncingball;


import android.graphics.Paint;

public class Bloque {

    private Paint pincel;
    private int anchoBloque;
    private int altoBloque;
    private int posX;
    private int posY;
    private int dureza;
    private int id;
    private int nroFila;
    private int nroColumna;
    private int puntaje;

    public Bloque (int posX, int posY, int ancho, int alto){
        this.posX=posX;
        this.posY=posY;
        this.anchoBloque=ancho;
        this.altoBloque=alto;
        this.pincel=pincel;
        this.id=id;
        this.puntaje=100;
    }

    public Bloque (int posX, int posY, int ancho, int alto, Paint pincel, int est){
        this.posX=posX;
        this.posY=posY;
        this.anchoBloque=ancho;
        this.altoBloque=alto;
        this.pincel=pincel;
        this.dureza=est;
        this.id=id;
        this.puntaje=100;
    }

    public int getNroColumna() {
        return nroColumna;
    }

    public void setNroColumna(int nroColumna) {
        this.nroColumna = nroColumna;
    }

    public int getNroFila() {
        return nroFila;
    }

    public int getDureza() {
        return dureza;
    }

    public void setDureza(int dureza) {
        this.dureza = dureza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paint getPincel() {
        return pincel;
    }

    public void setPincel(Paint pincel) {
        this.pincel = pincel;
    }

    public int getAnchoBloque() {
        return anchoBloque;
    }

    public int getAltoBloque() {
        return altoBloque;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPuntaje() {
        return puntaje;
    }
}