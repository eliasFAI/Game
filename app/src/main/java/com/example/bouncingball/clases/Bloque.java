package com.example.bouncingball.clases;


import android.graphics.Paint;
import android.graphics.Rect;

import com.example.bouncingball.logica.Grilla;

import java.util.ArrayList;

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

    public int getLeft(){
        return this.posX ;
    }
    public int getTop(){
        return this.posY;
    }
    public int getRight(){
        return this.posX+this.getAnchoBloque();
    }
    public int getBottom(){
        return this.posY+this.getAltoBloque();
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

    public Rect[] areaDeContacto(){
        int velocidad=13;
        //posX, posY, posX+13, posY+13
        //Esquina 1
        Rect area1=new Rect(posX,posY,posX+velocidad-1,posY+velocidad-1);

        Rect area2=new Rect(posX+velocidad,posY,posX+anchoBloque-velocidad,posY+velocidad);
        //Esquina 2
        Rect area3=new Rect(posX+anchoBloque-velocidad+1,posY,posX+anchoBloque,posY+velocidad-1);

        Rect area4=new Rect(posX,posY+velocidad,posX+velocidad,posY+altoBloque-velocidad);

        Rect area5=new Rect(posX+getAnchoBloque()-velocidad,posY+velocidad,posX+getAnchoBloque(),posY+getAltoBloque()-velocidad);
        //Esquina 3
        Rect area6=new Rect(posX,posY+getAltoBloque()-velocidad-1,posX+velocidad-1,posY+getAltoBloque());

        Rect area7=new Rect(posX+velocidad,posY+getAltoBloque()-velocidad,posX+getAnchoBloque()-velocidad,posY+getAltoBloque());
        //Esquina 4
        Rect area8=new Rect(posX+getAnchoBloque()-velocidad+1,posY+getAltoBloque()-velocidad+1,posX+getAnchoBloque(),posY+getAltoBloque());

        Rect [] areas=new Rect[8];
        areas[0]=area1;
        areas[1]=area2;
        areas[2]=area3;
        areas[3]=area4;
        areas[4]=area5;
        areas[5]=area6;
        areas[6]=area7;
        areas[7]=area8;

        return areas;
    }

    public ArrayList<Bloque> bloquesAdyacente(Grilla m){
          //Rect [] adyacente = new Rect[8];
          ArrayList<Bloque> adyacente = new ArrayList();
          int pos_X=this.getNroFila();
          int pos_Y = this.getNroColumna();

        if (pos_X == 0 && pos_Y == 0) {

            adyacente.add(m.getBloque(pos_X,(pos_Y+1)));
            adyacente.add(m.getBloque((pos_X+1),pos_Y));
            adyacente.add(m.getBloque((pos_X+1),(pos_Y+1)));

        } else {
            if (pos_X == 0 && pos_Y == m.getCantidadColumnas()-1) {

                adyacente.add(m.getBloque(pos_X,(pos_Y-1)));
                adyacente.add(m.getBloque((pos_X+1),pos_Y));
                adyacente.add(m.getBloque((pos_X+1),(pos_Y-1)));

            } else {
                if (pos_Y == 0 && pos_X == m.getCantidadFilas()- 1) {

                    adyacente.add(m.getBloque((pos_X-1),pos_Y));
                    adyacente.add(m.getBloque((pos_X),(pos_Y+1)));
                    adyacente.add(m.getBloque((pos_X-1),(pos_Y+1)));

                } else {
                    if (pos_X == m.getCantidadFilas() - 1 && pos_Y == m.getCantidadColumnas() - 1) {

                        adyacente.add(m.getBloque((pos_X-1),(pos_Y-1)));
                        adyacente.add(m.getBloque((pos_X-1),(pos_Y)));
                        adyacente.add(m.getBloque(pos_X,(pos_Y-1)));

                    } else {
                        if (pos_X == 0 ) {

                            adyacente.add(m.getBloque(pos_X,(pos_Y+1)));
                            adyacente.add(m.getBloque(pos_X,(pos_Y-1)));
                            adyacente.add(m.getBloque((pos_X+1),pos_Y));
                            adyacente.add(m.getBloque((pos_X+1),(pos_Y+1)));
                            adyacente.add(m.getBloque((pos_X+1),(pos_Y-1)));

                        } else {
                            if (pos_X == m.getCantidadFilas()- 1 ) {
                                adyacente.add(m.getBloque(pos_X,(pos_Y-1)));
                                adyacente.add(m.getBloque(pos_X,(pos_Y+1)));
                                adyacente.add(m.getBloque((pos_X+1),(pos_Y-1)));
                                adyacente.add(m.getBloque((pos_X+1),pos_Y));
                                adyacente.add(m.getBloque((pos_X+1),(pos_Y+1)));

                            } else {

                                if (pos_Y == 0) {

                                    adyacente.add(m.getBloque((pos_X-1),pos_Y));
                                    adyacente.add(m.getBloque((pos_X+1),pos_Y));
                                    adyacente.add(m.getBloque((pos_X-1),(pos_Y+1)));
                                    adyacente.add(m.getBloque(pos_X,(pos_Y+1)));
                                    adyacente.add(m.getBloque((pos_X+1),(pos_Y+1)));


                                } else {

                                    if (pos_Y == m.getCantidadColumnas()-1 ) {

                                        adyacente.add(m.getBloque((pos_X-1),pos_Y));
                                        adyacente.add(m.getBloque((pos_X+1),pos_Y));
                                        adyacente.add(m.getBloque((pos_X-1),(pos_Y-1)));
                                        adyacente.add(m.getBloque(pos_X,(pos_Y-1)));
                                        adyacente.add(m.getBloque((pos_X+1),(pos_Y-1)));


                                    } else {

                                        adyacente.add(m.getBloque((pos_X-1),(pos_Y-1)));
                                        adyacente.add(m.getBloque((pos_X-1),pos_Y));
                                        adyacente.add(m.getBloque((pos_X-1),(pos_Y+1)));
                                        adyacente.add(m.getBloque(pos_X,(pos_Y-1)));
                                        adyacente.add(m.getBloque(pos_X,(pos_Y+1)));
                                        adyacente.add(m.getBloque((pos_X+1),(pos_Y-1)));
                                        adyacente.add(m.getBloque((pos_X+1),pos_Y));
                                        adyacente.add(m.getBloque((pos_X+1),(pos_Y+1)));

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return adyacente ;
    }
}