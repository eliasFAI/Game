package com.example.bouncingball;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.sql.SQLOutput;
import java.util.ArrayList;


public class GameView extends SurfaceView {
    //Pinceles
    private Paint pincelIndicadores = new Paint();
    private Paint pincelPelota = new Paint();
    private Paint pincelJugador = new Paint();
    //Indicadores
    private int vidas;
    private int puntaje;
    private int nivel;
    private boolean cayo;
    //Elementos
    private Grilla grilla;
    private Pelota pelota;
    private Bloque jugador;
    private GameThread gameThread;
    private int xMax, yMax;
    //Dedo
    private int posDedoX;
    private int posDedoY;
    //Inicio del juego
    private boolean inicioJuego = false;

    private boolean pantallaPulsada = false;

    //imagen Game over
    private boolean imgFinJuego=false;

    //imagen Gano
    private boolean imgSuperoNivel=false;
    private Bitmap bmp;
    private Bitmap pelotaImg;

    private boolean juegoEnPausa=false;
    private boolean siguienteFotograma=false;

    public GameView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                setWillNotDraw(false);
                nivel = 1;
                vidas = 2;
                puntaje = 0;
                cayo = false;
                xMax = getWidth();
                yMax = getHeight();
                System.out.println("Ancho de la pantalla:"+xMax);
                //Ubicacion del jugador
                jugador=new Bloque(getWidth() / 2 - (150/2),getHeight()-200,150,20);
                pelota = new Pelota(jugador.getPosX(),jugador.getPosY()-15,20, 13);
                grilla = new Grilla(xMax, yMax, 7, 10, 30);
                gameThread = new GameThread(GameView.this);
                gameThread.play();

                //gameThread.setRunning(true);

                //gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //System.out.println("Entro a onDraw");
        //Propiedades del canvas
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);

        //Pinceles
        pincelPelota.setColor(Color.BLUE);
        pincelJugador.setColor(Color.GREEN);
        pincelIndicadores.setColor(Color.WHITE);
        pincelIndicadores.setTextSize(40f);




            //Controles
            verificarContactoPantalla();

            //pelota.actualizarPosicion();


            if (vidas >= 0) {
                this.recorrerBloquesYPintar(canvas);

                //Es el ultimo bloque??
                boolean gano=this.grilla.getCantidadBloquesPintados()==0;
                if(gano){
                    this.nivelSuperado(canvas);
                }else{
                    //actualizamos las posiciones
                    if (!cayo) {
                        //Verifico contacto si la posicion es menor que
                        this.controlDelJuego(canvas);
                    } else {
                        //La pelota pasa al jugador
                        this.restarVida();
                    }
                    //Pinta los indicadores de vida y puntaje
                    this.dibujarIndicadores(canvas);
                    //Jugador
                    canvas.drawRect(jugador.getPosX(), jugador.getPosY(), jugador.getPosX() + jugador.getAnchoBloque(), jugador.getPosY() + jugador.getAltoBloque(), pincelJugador);
                }

            } else {
                //Si pierdo todas las vidas
                //AlertDialog.Builder builder = new AlertDialog.Builder();
                this.reiniciarJuego(canvas);
            }

            //Posición del boton siguiente

            canvas.drawRect(xMax-100, yMax-100, xMax+50, yMax+50, pincelPelota);
            canvas.drawRect(0, yMax-100, 50, yMax-50, pincelPelota);
            if(siguienteFotograma){
                gameThread.pause();
                siguienteFotograma=false;
            }

    }

    //Metodos del Juego
    private void recorrerBloquesYPintar(Canvas canvas){
        boolean unContacto = false;
        //Recorre todas las filas verificando si la pelota tuvo algun contacto
        for (int i=0;i<this.grilla.getCantidadFilas();i++){
            for (int j=0;j<this.grilla.getCantidadColumnas();j++){

                //Verifica que el bloque todavia no se rompio
                if(this.grilla.getBloque(i,j).getDureza()==1){
                    //falta optimizar: Para que si ya detecto un contacto, no siga verificando los otros bloques
                    if(!unContacto){
                        verificarContacto2(this.grilla.getBloque(i,j));// Para hacer pruebas
                        unContacto=verificarContacto(this.grilla.getBloque(i,j));
                        verificarContacto(this.grilla.getBloque(i,j));
                        if(unContacto){
                            this.puntaje=this.puntaje+this.grilla.getBloque(i,j).getPuntaje();

                        }
                    }
                    //Este metodo dibuja la grilla creada anteriormente.
                    canvas.drawRect(this.grilla.getBloque(i,j).getPosX(),this.grilla.getBloque(i,j).getPosY(),
                            this.grilla.getBloque(i,j).getPosX()+this.grilla.getBloque(i,j).getAnchoBloque(),
                            this.grilla.getBloque(i,j).getPosY()+this.grilla.getBloque(i,j).getAltoBloque(),this.grilla.getBloque(i,j).getPincel());
                }
            }
        }
    }

    private void nivelSuperado(Canvas canvas){
        imgSuperoNivel=true;
        System.out.println("********************************");
        System.out.println("***************GANASTE*****************");
        System.out.println("********************************");
        gameThread.pause();
        this.reubicarPelota();
        this.grilla.avanzarUnNivel();
        bmp= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ganaste),xMax,yMax,false);
        canvas.drawBitmap(bmp, 0, 0, null);
    }

    private void controlDelJuego(Canvas canvas){
        pelotaImg= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pelota2),pelota.getTamanio(),pelota.getTamanio(),false);

        if (inicioJuego && (pelota.getY() < jugador.getPosY()) && (pelota.getY() > (jugador.getPosY() - 50))) {
            verificarContactoJugador();
        }

        //actualizamos las posiciones
        if (pantallaPulsada) {
            //Actualizar posicion del jugador
            //posXJugador=posDedoX-(anchoJugador/2);
            transicionEnX();
        }
        if (inicioJuego) {
            //Pelota en movimiento
            pelota.actualizarPosicion();


            canvas.drawBitmap(pelotaImg, pelota.getX(), pelota.getY(), null);

            //canvas.drawRect(pelota.getX(), pelota.getY(), pelota.getX() + pelota.getTamanio(), pelota.getY() + pelota.getTamanio(), pincelPelota);
            //canvas.drawCircle((pelota.getX()+(pelota.getTamanio()/2)),(pelota.getX()+(pelota.getTamanio()/2)), pelota.getTamanio(),pincelPelota);
        } else {
            //La pelota esta en la posicion del jugador
            pelota.setX(jugador.getPosX() + (jugador.getAnchoBloque() / 2));

            canvas.drawBitmap(pelotaImg, pelota.getX(), pelota.getY(), null);
            //canvas.drawRect(pelota.getX(), pelota.getY(), pelota.getX() + pelota.getTamanio(), pelota.getY() + pelota.getTamanio(), pincelPelota);
            //canvas.drawCircle((pelota.getX()+(pelota.getTamanio()/2)),(pelota.getX()+(pelota.getTamanio()/2)), pelota.getTamanio(),pincelPelota);
        }
    }

    private void restarVida(){
        vidas += -1;
        //Reubicar la pelota cuando se cae
       this.reubicarPelota();
        //canvas.drawRect(pelota.getPosX(), pelota.getPosY(), pelota.getPosX() + pelota.getTamanio(), pelota.getPosY() + pelota.getTamanio(), pincelPelota);
        cayo = false;
        inicioJuego=false;
                /*
                AlertDialog.Builder alerta=new AlertDialog.Builder(getContext());
                alerta.setMessage("Hola 2");
                alerta.create();
                alerta.show();
                System.out.println("Alerta creada 2");
                */
    }

    private void dibujarIndicadores(Canvas canvas){
        //Nivel
        canvas.drawText("Nivel:" + String.valueOf(this.grilla.getNivelActual()), (xMax * 10) / 100, 70f, pincelIndicadores);
        //Puntaje
        canvas.drawText("Puntaje:" + String.valueOf(this.puntaje), (xMax * 40) / 100, 70f, pincelIndicadores);
        //Vidas
        if(vidas>0){

            canvas.drawText("Vidas:" + String.valueOf(this.vidas), (xMax * 75) / 100, 70f, pincelIndicadores);
        }else{

            canvas.drawText("Vidas:" + String.valueOf(0), (xMax * 75) / 100, 70f, pincelIndicadores);
        }
    }

    private void reiniciarJuego(Canvas canvas){
        System.out.println("Vuelve a iniciar el juego");
        vidas = 2;
        puntaje=0;
        imgFinJuego=true;
        this.grilla.setCantidadBloquesPintados(0);
        this.grilla.reiniciarGrilla();
        gameThread.pause();
        bmp= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.finjuego),xMax,yMax,false);
        canvas.drawBitmap(bmp, 0, 0, null);
        System.out.println("Despues de la pausa");
    }

    private void reubicarPelota(){
        pelota.setX(jugador.getPosX() + (jugador.getAnchoBloque() / 2));
        pelota.setY(jugador.getPosY()- pelota.getTamanio()-2);
        pelota.setDireccionEnX(pelota.getVelocidad());
        pelota.setDireccionEnY(-pelota.getVelocidad());
    }

    public boolean onTouchEvent(MotionEvent evento) {
        //if (evento.getAction() == MotionEvent.ACTION_DOWN) {}
        //Pulso la pantalla
        pantallaPulsada = true;
        posDedoX = (int) evento.getX();
        posDedoY = (int) evento.getY();
        //System.out.println("Pulso la pantalla en la posicion x: "+posDedoX+" Y: "+posDedoY);

        //invalidate();

        //    if(evento.getAction() == MotionEvent.ACTION_UP){}
        // MotionEvent.ACTION_DOWN
        if(imgFinJuego || imgSuperoNivel){
            if (evento.getAction() == MotionEvent.ACTION_UP) {
                System.out.println("pulso la pantalla 111111");
                //Si perdio o gano
                if(posDedoX>50&&posDedoX<(xMax-50)){
                    if(posDedoY>600&&posDedoY<(yMax-300)){
                        gameThread.continuar();
                        System.out.println("Continuar jugando");
                        imgFinJuego=false;
                        imgSuperoNivel=false;
                    }
                }
            }
        }else{
            if(evento.getAction() == MotionEvent.ACTION_UP){
                System.out.println("Pulso la pantalla *********************");
                inicioJuego = true;
                pantallaPulsada = false;
            }
        }


        //Boton continuar
        if(evento.getAction() == MotionEvent.ACTION_UP){
            if(posDedoX>50&&posDedoX<(xMax-50)){
                if(posDedoY>10&&posDedoY<300){
                    gameThread.continuar();
                    System.out.println("Continuar jugando Boton Segui jugando");

                }
            }
        }

        if(juegoEnPausa && !siguienteFotograma){
            if(posDedoX>xMax-100){
                if(posDedoY>yMax-100){
                    gameThread.continuar();
                    System.out.println("Continuar jugando Boton Segui jugando");
                    siguienteFotograma=true;
                }
            }
        }

        if(juegoEnPausa && !siguienteFotograma){
            if(posDedoX<100){
                if(posDedoY>yMax-100){
                    gameThread.continuar();
                    System.out.println("Continuar jugando Boton Segui jugando");

                }
            }
        }


        return true;
    }
    public void controlarChoques(){
        gameThread.pause();//Para verificar contactos
        juegoEnPausa=true;
        if(siguienteFotograma){

        }
    }

    //Controlar rebote
    public void transicionEnX() {
        int posNueva = posDedoX - (jugador.getAnchoBloque() / 2);
        //int posNueva=posDedoX;
        int velocidadTransicion = 50;
        int rangoMovimiento = 50;
        int posJugadorCentro = jugador.getPosX() + (jugador.getAnchoBloque() / 2);
        //Verifica que el jugador no salga del limite izquierdo ni del derecho
        if(posNueva<0 || (posNueva+jugador.getAnchoBloque())>xMax){
            if(posNueva<0){
                //posNueva=-20;
                posNueva=0;
                jugador.setPosX(posNueva);

            }else{
                //posNueva=-20;
                posNueva=xMax-jugador.getAnchoBloque();
                jugador.setPosX(posNueva);
            }

        }else{
            if (((posJugadorCentro < (posNueva + rangoMovimiento)) && ((posNueva - rangoMovimiento) < posJugadorCentro))) {
                jugador.setPosX(posNueva);
            } else {
                if (jugador.getPosX() < posNueva) {
                    //El bloque esta a la izquierda
                    //verifico que si sumo la velocidad de transicion no supere la posicion que tiene que llegar
                    if ((jugador.getPosX() + velocidadTransicion) < posNueva) {
                        jugador.setPosX(jugador.getPosX()+velocidadTransicion);
                    } else {
                        jugador.setPosX(posNueva);
                    }
                } else {
                    //El bloque esta a la derecha
                    if ((jugador.getPosX() - velocidadTransicion) > posNueva) {
                        jugador.setPosX(jugador.getPosX()- velocidadTransicion);
                    } else {
                        jugador.setPosX(posNueva);
                    }
                }
            }
        }

    }

    //La pelota verifica si toco algun borde de la pantalla
    public void verificarContactoPantalla() {

        if (pelota.getX() + pelota.getTamanio() >= xMax) {
            pelota.setDireccionEnX(-pelota.getVelocidad());
            //Derecha
        }
        if (pelota.getX() <= 0) {
            pelota.setDireccionEnX(pelota.getVelocidad());
            //Izquierda
        }
        //¿Porque le restamos una vida aca dijo Lucas?
        //Se respondio solo, dijo cuando toca el fondo amigo :)
        //Fondo
        if (pelota.getY() + pelota.getTamanio() >= yMax) {
            pelota.setDireccionEnY(-pelota.getVelocidad());
            cayo=true;

        }
        //Techo
        if (pelota.getY() <= 100) {
            pelota.setDireccionEnY(pelota.getVelocidad());
        }


    }

    //Contacto de los bloques
    public boolean verificarContacto(Bloque b){
        boolean salida=false;
        int x=pelota.getX();
        int y=pelota.getY();
        int anchoPelota=pelota.getTamanio();
        int alto=pelota.getTamanio();

        Rect rec1=new Rect(x,y,x+anchoPelota,y+alto);
        int bX=(int)b.getPosX();
        int by=(int)b.getPosY();
        int bAncho=(int)(b.getPosX()+b.getAnchoBloque());
        int bAlto=(int)(b.getPosY()+b.getAltoBloque());
        Rect rec2=new Rect(bX, (int)b.getPosY(), (int)(b.getPosX()+b.getAnchoBloque()), (int)(b.getPosY()+b.getAltoBloque()));

        if(rec1.intersect(rec2)){
            //Colisiono
            actualizarDireccion(b);
            b.setDureza(0);
            this.grilla.restarBloquesPintados();
            System.out.println("Bloques pintados restantes:"+this.grilla.getCantidadBloquesPintados());
            salida=true;
        }

        return salida;
    }
    public boolean verificarContacto2(Bloque b){
        boolean salida=false;
        int x=pelota.getPosSiguienteX();
        int y=pelota.getPosSiguienteY();
        int anchoPelota=pelota.getTamanio();
        int alto=pelota.getTamanio();

        Rect rec1=new Rect(x,y,x+anchoPelota,y+alto);
        int bX=(int)b.getPosX();
        int by=(int)b.getPosY();
        int bAncho=(int)(b.getPosX()+b.getAnchoBloque());
        int bAlto=(int)(b.getPosY()+b.getAltoBloque());
        Rect rec2=new Rect(bX, (int)b.getPosY(), (int)(b.getPosX()+b.getAnchoBloque()), (int)(b.getPosY()+b.getAltoBloque()));

        if(rec1.intersect(rec2)){

            controlarChoques();
            Paint colorChoque = new Paint();
            colorChoque.setColor(Color.RED);
            b.setPincel(colorChoque);
            System.out.println("Bloques pintados restantes:"+this.grilla.getCantidadBloquesPintados());
            salida=true;
        }

        return salida;
    }


    public void actualizarDireccion(Bloque b){
        int puntoMedioY=pelota.getY()+(pelota.getTamanio()/2);
        int puntoMedioX=pelota.getX()+(pelota.getTamanio()/2);
        boolean chocoCostado=false;
        boolean chocoDerechaBloque=false;
        boolean chocoIzquierdaBloque=false;

        System.out.println("-----------------------------");
        System.out.println("Actualizando Dirección");
        //Choca en algun costado
        if(b.getPosY()<puntoMedioY && puntoMedioY<(b.getPosY()+b.getAltoBloque())&& (puntoMedioX<b.getPosX() || puntoMedioX>(b.getPosX()+b.getAnchoBloque()))){
            //Choco en algun costado
            chocoCostado=true;
            System.out.println("****Choco en uno de los costados******");
            if(b.getPosX()>pelota.getX()){
                System.out.println("############Entro true b.getPosX "+b.getPosX()+" > "+pelota.getX()+" pelota.getX()");
                //choco en la izquierda
                chocoIzquierdaBloque=true;
                System.out.println("=========choco en la izquierda."+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
            }else{
                System.out.println("############Entro FALSE b.getPosX "+b.getPosX()+" > "+pelota.getX()+" pelota.getX()");
                //choco en la derecha
                chocoDerechaBloque=true;
                System.out.println("=========choco en la derecha"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
            }
        }
            //Si el punto medio esta dentro de y <puntoMedio<(y+altoBloque)
            //De acuerdo a la direccion que lleva la pelota, es donde voy a tener un contacto
            if (pelota.getX() > pelota.getPosAnteriorX()) {
                System.out.println("****Choco en el medio ------ Se esta moviendo a la derecha******");
                //Se esta moviendo a la derecha
                if (pelota.getY() > pelota.getPosAnteriorY()) {

                    //hacia abajo y a la derecha
                    if(chocoIzquierdaBloque||(pelota.getY()>b.getPosY())){
                        pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
                        System.out.println("======== hacia abajo y a la derecha 1"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }else{
                        pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
                        System.out.println("======== hacia abajo y a la derecha 2"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }
                } else {
                    //Hacia arriba y a la derecha
                    if(chocoIzquierdaBloque||(pelota.getY()<b.getPosY())){
                        pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
                        System.out.println("======== Hacia arriba y a la derecha 1"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }else{
                        pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
                        System.out.println("======== Hacia arriba y a la derecha 2"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }

                }
            } else {
                System.out.println("****Choco en el medio ------ Se mueve a la izquierda******");
                //Se mueve a la izquierda
                if (pelota.getY() > pelota.getPosAnteriorY()) {
                    //hacia abajo y a la izquierda
                    //(pelota.getPosY()>b.getPosY()) Significa que paso el bloque
                    if(chocoDerechaBloque||(pelota.getY()>b.getPosY())){
                        pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
                        System.out.println("======== hacia abajo y a la izquierda 1"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }else{
                        pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
                        System.out.println("======== hacia abajo y a la izquierda 2"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }
                } else {
                    //Hacia arriba y a la izquierda
                    if(chocoDerechaBloque||(pelota.getY()<b.getPosY())){
                        pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
                        System.out.println("======== Hacia arriba y a la izquierda 1"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }else{
                        pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
                        System.out.println("======== Hacia arriba y a la izquierda 2"+" Bloque posX: "+b.getNroColumna()+" posY:"+b.getNroFila());
                    }
                }

            }



    }

    public boolean verificarContactoJugador(){
        boolean salida=false;
        //int x=pelota.getX();
        //int y=pelota.getY();

        // Aca verificar *****
        //
        int x=pelota.getPosSiguienteX()-7;
        int y=pelota.getPosSiguienteY()-7;
        int anchoPelota=pelota.getTamanio();
        int alto=pelota.getTamanio();
        System.out.println("entro a verificar");
        Rect rec1=new Rect(x,y,x+anchoPelota,y+alto);
        Rect rec2=new Rect(jugador.getPosX(), jugador.getPosY(), jugador.getPosX()+jugador.getAnchoBloque(), jugador.getPosY()+jugador.getAltoBloque());

        if(rec1.intersect(rec2)/*||(y+alto)==posYJugador*/){

            //Colisiono
            System.out.println("----------------------------Choco valor Pelota x: "+x+" valor y "+ y);
            System.out.println("----------------------------Choco valor Jugador x: "+jugador.getPosX()+" valor y "+ jugador.getPosY());
            actualizarDireccionPelota();

            salida=true;
            //gameThread.pause();
        }else{
            System.out.println("No choco valor Pelota x: "+x+" valor y "+ y);
            System.out.println("No choco valor Jugador x: "+jugador.getPosX()+" valor y "+ jugador.getPosY());
            //gameThread.pause();
        }
        return salida;
    }

    public void actualizarDireccionPelota(){
        //contacto y

        //Verifico la posicion del jugador donde choca, porque eso me da la inclinación de salida

        int cuadro1 = jugador.getAnchoBloque() / 5;
        if (pelota.getX() < (jugador.getPosX() + (cuadro1 * 1))) {
            //choco en el primer cuadrante
            System.out.println("choco en el primer cuadrante");
            //Verifico en cual angulo venia
            if (pelota.getX() < pelota.getPosAnteriorX()) {
                //venia de derecha a izquierda
                //actualizo solo el y
                System.out.println("Actualizo solo Y");
                int nuevaDireccion=(pelota.getVelocidad()*33)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));

            } else {
                //venia de izquierda a derecha
                //Como esta en el primer cuadrante vuelve en esa misma direccion
                //Actualizao valor X y Y
                System.out.println("Actualizo X e Y");
                int nuevaDireccion=(pelota.getVelocidad()*33)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
                pelota.setDireccionEnX(-1*pelota.getVelocidad());
            }
        }
        if (pelota.getX() > (jugador.getPosX() + (cuadro1 * 1)) && pelota.getX() < (jugador.getPosX()+ (cuadro1 * 2))) {
            //choco en el segundo cuadrante
            //Verifico en cual angulo venia
            System.out.println("choco en el segundo cuadrante");
            if (pelota.getX() < pelota.getPosAnteriorX()) {
                //venia de derecha a izquierda
                //66%
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
            } else {
                //venia de izquierda a derecha
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
                pelota.setDireccionEnX(-1*pelota.getVelocidad());
            }

        }
        if (pelota.getX() > (jugador.getPosX() + (cuadro1 * 2)) && pelota.getX() < (jugador.getPosX() + (cuadro1 * 3))) {
            //choco en el tercer cuadrante
            System.out.println("choco en el tercer cuadrante");
            //Verifico en cual angulo venia
            //en el cuadrante del centro solo actualizo el y
            pelota.setDireccionEnY(-1*pelota.getVelocidad());

        }
        if (pelota.getX() > (jugador.getPosX() + (cuadro1 * 3)) && pelota.getX() < (jugador.getPosX() + (cuadro1 * 4))) {
            //choco en el cuarto cuadrante
            System.out.println("choco en el cuarto cuadrante");
            //Verifico en cual angulo venia

            if (pelota.getX() < pelota.getPosAnteriorX()) {
                //venia de derecha a izquierda
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
                pelota.setDireccionEnX(-1*pelota.getVelocidad());
            } else {
                //venia de izquierda a derecha
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
            }

        }
        if (pelota.getX() > (jugador.getPosX() + (cuadro1 * 4)) && pelota.getX() < (jugador.getPosX() + (cuadro1 * 5))) {
            //choco en el quinto cuadrante
            System.out.println("choco en el quinto cuadrante");
            //Verifico en cual angulo venia

            if (pelota.getX() < pelota.getPosAnteriorX()) {
                //venia de derecha a izquierda
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
                pelota.setDireccionEnX(-1*pelota.getVelocidad());

            } else {
                //venia de izquierda a derecha
                int nuevaDireccion=(pelota.getVelocidad()*66)/100;
                pelota.setDireccionEnY(-1*(nuevaDireccion));
            }
        }

        /*
        if(pelota.getDireccionEnY()>0){
            pelota.setDireccionEnY(-1*(pelota.getVelocidad()));
        }
        */

    }





}
