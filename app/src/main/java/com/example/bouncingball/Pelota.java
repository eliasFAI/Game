package com.example.bouncingball;

public class Pelota {
    private int x;
    private int y;
    private int tamanio;
    private int direccionEnX;
    private int direccionEnY;
    private int velocidad;

    private int posAnteriorX;
    private int posAnteriorY;

    private int posSiguienteX;
    private int posSiguienteY;

    public Pelota(){

    }

    public Pelota(int posX, int posY, int tamanio, int velocidad) {
        this.x = posX;
        this.y = posY;
        this.tamanio = tamanio;
        this.velocidad = velocidad;
        this.direccionEnX=velocidad;
        this.direccionEnY=-velocidad;
        //Para trabajar con los rebotes
        this.posAnteriorX=posX;
        this.posAnteriorY=posY;
        this.posSiguienteX=posX;
        this.posSiguienteY=posY;
    }

    public int getPosX() {
        return x;
    }

    public float getPosY() {
        return y;
    }

    public int getTamanio() {
        return tamanio;
    }

    public int getDireccionEnX() {
        return direccionEnX;
    }

    public int getDireccionEnY() {
        return direccionEnY;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getPosAnteriorX() {
        return posAnteriorX;
    }

    public int getPosAnteriorY() {
        return posAnteriorY;
    }

    public void setPosAnteriorY(int posAnteriorY) {
        this.posAnteriorY = posAnteriorY;
    }

    public void setPosAnteriorX(int posAnteriorX) {
        this.posAnteriorX = posAnteriorX;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setDireccionEnY(int direccionEnY) {
        this.direccionEnY = direccionEnY;
    }

    public void setDireccionEnX(int direccionEnX) {
        this.direccionEnX = direccionEnX;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Metodos
    public void actualizarPosicion(){
        this.posAnteriorX=this.x;
        this.posAnteriorY=this.y;

        this.x=this.x+this.direccionEnX;
        this.y=this.y+this.direccionEnY;

        this.posSiguienteX=this.x+this.direccionEnX;
        this.posSiguienteY=this.y+this.direccionEnY;
    }
    public int getPosSiguienteX() {
        return posSiguienteX;
    }

    public int getPosSiguienteY() {
        return posSiguienteY;
    }


}
