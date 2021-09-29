package com.example.bouncingball.clases;

public class Pelota {
    private int x;
    private int y;
    private int tamanio;
    private int direccionEnX;
    private int direccionEnY;
    private int velocidad;
    private int radio;
    private int posAnteriorX;
    private int posAnteriorY;
    private int centroX ;
    private int centroY ;

    private int posSiguienteX;
    private int posSiguienteY;
    private int [][] esquinasPelota;

    public Pelota(){

    }

    public Pelota(int posX, int posY, int tamanio, int velocidad) {
        this.x = posX;
        this.y = posY;
        this.tamanio = tamanio;
        this.velocidad = velocidad;
        this.direccionEnX= - velocidad;
        this.direccionEnY=-velocidad;
        //Para trabajar con los rebotes
        this.posAnteriorX=posX;
        this.posAnteriorY=posY;
        this.posSiguienteX=posX;
        this.posSiguienteY=posY;
        this.centroX = posX+18;
        this.centroY = posY+18;
        esquinasPelota = new int[4][2];
    }

    public int getCentroX(){
        return centroX ;
    }
    public int getCentroY(){
        return centroY ;
    }
    public int getPosX() {
        return x;
    }

    public int getPosY() {
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
    public int[][] devolverPuntos(){
        esquinasPelota[0][0]=this.x;
        esquinasPelota[0][1]=this.y;

        esquinasPelota[1][0]=this.x+this.tamanio;
        esquinasPelota[1][1]=this.y;

        esquinasPelota[2][0]=this.x;
        esquinasPelota[2][1]=this.y+this.tamanio;

        esquinasPelota[3][0]=this.x+this.tamanio;
        esquinasPelota[3][1]=this.y+this.tamanio;

        return esquinasPelota;
    }
    public String getDireccion(){
        String salida = "";
        if(this.posAnteriorY<this.getPosY()){
            salida = "abajo";
        }else{
            salida = "arriba";
        }
        if(this.posAnteriorX<this.getPosX()){
            salida += "Derecha";
        }else{
            salida += "Izquierda";
        }
        return salida;
    }
}
