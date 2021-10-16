package com.example.bouncingball.logica;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.getIntent;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import androidx.core.math.MathUtils;

import com.example.bouncingball.R;
import com.example.bouncingball.activity.EventoActivity;
import com.example.bouncingball.activity.LoginScreen;
import com.example.bouncingball.activity.MainActivity;
import com.example.bouncingball.clases.Bloque;
import com.example.bouncingball.clases.Jugador;
import com.example.bouncingball.clases.Pelota;
import com.example.bouncingball.database.dbConexion;
import com.example.bouncingball.hilos.GameThread;

import java.util.ArrayList;


public class GameView extends SurfaceView {
    //Pinceles
    private Paint pincelIndicadores = new Paint();
    private Paint pincelPelota = new Paint();
    private Paint pincelJugador = new Paint();
    private Paint pincelDureza =  new Paint();
    private dbConexion dao ;
    //Indicadores
    private int vidas;
    private int puntaje;
    private int nivel;
    private boolean cayo;
    //Elementos
    private Grilla grilla;
    private Pelota pelota;
    private Jugador jugador;
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

    private boolean futuroContacto=false;
    private MediaPlayer mp ;

    public GameView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                setWillNotDraw(false);
                nivel = 1;
                vidas = 1;
                puntaje = 0;
                cayo = false;
                xMax = getWidth();
                yMax = getHeight();
                SharedPreferences preferences = getContext().getSharedPreferences("myidiom", Context.MODE_PRIVATE);
                String user = preferences.getString("user","vacio");
                System.out.println("Usuario Recibido :"+user);
                //Ubicacion del jugador
                jugador = new Jugador((getWidth() / 2) - (150 / 2), getHeight() - 200, 150,20,13);
               //  jugador=new Bloque(165,300,250,40);
                //pelota = new Pelota(jugador.getPosX(),jugador.getPosY()-15,15, 15);
                pelota = new Pelota(jugador.getPosX(),jugador.getPosY()-40,45, 7);
                //grilla = new Grilla(xMax, yMax, 7, 10, 25);
                /*
                * Aqui deberia recuperarse del sharedPreferences
                * */
                int level = preferences.getInt("level",1);
                grilla = new Grilla(xMax, yMax, 7, 10, 80,level ,context);

                // Base de Datos
                dao = new dbConexion(getContext());

                gameThread = new GameThread(GameView.this);
                gameThread.play();


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
        pincelDureza.setColor(Color.RED);

        //Controles
        verificarContactoPantalla();

        //pelota.actualizarPosicion();


        if (vidas >= 0) {


            //Es el ultimo bloque??
            boolean gano=this.grilla.getCantidadBloquesPintados()==0;
            if(gano){
                this.nivelSuperado(canvas);
            }else{
                //actualizamos las posiciones
                if (!cayo) {
                    //Verifico contacto si la posicion es menor que

                    this.verificarContactosMultiples(canvas);
                    this.pintarGrilla(canvas);


                    //Aca va el metodo pintar grilla
                    this.controlDelJuego(canvas);

                } else {
                    //La pelota pasa al jugador
                    this.restarVida();
                }
                //Pinta los indicadores de vida y puntaje
                this.dibujarIndicadores(canvas);
                //Jugador
                //Jugador
                Rect l2 = jugador.getRect(0);
                Rect l3 = jugador.getRect(1);
                Rect l4 = jugador.getRect(2);
                Rect l5 = jugador.getRect(3);
                Rect l6 = jugador.getRect(4);
                canvas.drawRect(jugador.getPosX(), jugador.getPosY(), jugador.getPosX() + jugador.getAncho(), jugador.getPosY() + jugador.getAlto(), pincelJugador);
                canvas.drawRect(l2, pincelPelota);
                canvas.drawRect(l3, pincelIndicadores);
                canvas.drawRect(l4, pincelPelota);
                canvas.drawRect(l5, pincelIndicadores);
                canvas.drawRect(l6, pincelPelota);

                //**canvas.drawRect(jugador.getPosX(), jugador.getPosY(), jugador.getPosX() + jugador.getAnchoBloque(), jugador.getPosY() + jugador.getAltoBloque(), pincelJugador);
            }

        } else {
            //Si pierdo todas las vidas
            //AlertDialog.Builder builder = new AlertDialog.Builder();
            this.reiniciarJuego(canvas);
        }
        //Posición del boton siguiente
        Rect l = new Rect(500, 500, 600, 900);
        canvas.drawRect(xMax-100, yMax-100, xMax+50, yMax+50, pincelPelota);
        canvas.drawRect(0, yMax-100, 50, yMax-50, pincelPelota);
        //canvas.drawRect(xMax-100, yMax-100, xMax+50, yMax+50, pincelPelota);
        //canvas.drawRect(0, yMax-100, 50, yMax-50, pincelPelota);
        if(siguienteFotograma){
            gameThread.pause();
            siguienteFotograma=false;
        }
    }

    //Metodos del Juego
    private ArrayList<Bloque> obtenerBloquesChocados(Canvas canvas){
        boolean unContacto = false;
        int contarContactos=0;
       // Bloque [] arreBloqueContacto=new Bloque[3];

        ArrayList<Bloque> listaBloque = new ArrayList<Bloque>();
        //Recorre todas las filas verificando si la pelota tuvo algun contacto
        for (int i=0;i<this.grilla.getCantidadFilas();i++){
            for (int j=0;j<this.grilla.getCantidadColumnas();j++){
                //Verifica que el bloque todavia no se rompio
                if(this.grilla.getBloque(i,j).getDureza()==1 || this.grilla.getBloque(i,j).getDureza()==2){
                    //optimizar: Para que si ya detecto un contacto, no siga verificando los otros bloques
                    unContacto=verificarContacto(this.grilla.getBloque(i,j));
                    if(unContacto){
                        // Para hacer pruebas
                        //Guardo el bloque que hizo contacto
                        listaBloque.add(this.grilla.getBloque(i,j));
                       // arreBloqueContacto[contarContactos]=this.grilla.getBloque(i,j);
                        if(this.grilla.getBloque(i,j).getDureza()==1) {
                            this.puntaje = this.puntaje + this.grilla.getBloque(i, j).getPuntaje();
                        }else{
                            this.grilla.getBloque(i,j).setPincel(pincelDureza);
                        }
                        contarContactos++;
                    }
                }

            }
        }

        return listaBloque;
    }

    private void verificarContactosMultiples(Canvas canvas){
        Bloque [] arreBloqueContacto;
        ArrayList<Bloque> listaBloque;
        listaBloque=this.obtenerBloquesChocados(canvas);


        if(listaBloque.size()==1){
            System.out.println("Contacto 1");
            Bloque b=listaBloque.get(0);
            contactoUnBloque(b);
        }
        if(listaBloque.size()==2){
            System.out.println("Contacto 2");
            Bloque b1=listaBloque.get(0);
            Bloque b2=listaBloque.get(1);
            contactoDosBloques(b1,b2);
        }
        if(listaBloque.size()==3){
            System.out.println("Contacto 3");
            Bloque b1=listaBloque.get(0);
            Bloque b2=listaBloque.get(1);
            Bloque b3=listaBloque.get(2);
            contactoTresBloques(b1,b2,b3);
        }

    }
    private void contactoUnBloque(Bloque b){

        actualizarDireccion2(b);
        //  colisionBloqueAux(b);
        if(b.getDureza()==1) {
            b.setDureza(0);
            this.grilla.restarBloquesPintados();
        }
        else{
            b.setDureza(1);
        }

    }
    private void contactoDosBloques(Bloque b1, Bloque b2){
        //Estan horizontal
        if(b1.getNroFila()==b2.getNroFila()){
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            if(b1.getDureza()==1 && b2.getDureza()==1) {
                b1.setDureza(0);
                b2.setDureza(0);
                this.grilla.restarBloquesPintados();
                this.grilla.restarBloquesPintados();
            }
            else{
                if(b1.getDureza()==2 && b2.getDureza()==2) {
                    b1.setDureza(1);
                    b2.setDureza(1);

                }
                else{
                    if(b1.getDureza()==1){
                        b1.setDureza(0);
                        b2.setDureza(1);
                        this.grilla.restarBloquesPintados();

                    }
                    else{
                        b2.setDureza(0);
                        b1.setDureza(1);
                        this.grilla.restarBloquesPintados();
                    }
                }
            }
           // b1.setDureza(0);
          //  b2.setDureza(0);
            System.out.println("Bloques estan horizontal");
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
        }
        //Estan vertical
        if(b1.getNroColumna()==b2.getNroColumna()){
            System.out.println("Bloques estan Vertical");
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            b1.setDureza(0);
            b2.setDureza(0);
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            this.grilla.restarBloquesPintados();
            this.grilla.restarBloquesPintados();
        }
        //Estan en diagonal
        if(b1.getNroColumna()!=b2.getNroColumna()&&b1.getNroFila()!=b2.getNroFila()){
            System.out.println("Bloques estan diagonal");
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            b1.setDureza(0);
            b2.setDureza(0);
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            this.grilla.restarBloquesPintados();
            this.grilla.restarBloquesPintados();
        }

    }
    private void contactoTresBloques(Bloque b1, Bloque b2, Bloque b3){
        System.out.println("Contacto Tres Bloques ");
        //Caso 1
        if(b2.getNroColumna()!=b3.getNroColumna()&&b2.getNroFila()!=b3.getNroFila()){
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            System.out.println("Caso 1 ");
            b2.setDureza(0);
            b3.setDureza(0);
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            this.grilla.restarBloquesPintados();
            this.grilla.restarBloquesPintados();
        }
        //Caso 2 y caso 4
        if(b1.getNroColumna()!=b3.getNroColumna()&&b1.getNroFila()!=b3.getNroFila()){
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            System.out.println("Caso 2 y 4 ");
            b1.setDureza(0);
            b3.setDureza(0);
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            this.grilla.restarBloquesPintados();
            this.grilla.restarBloquesPintados();
        }
        //Caso 3
        if(b1.getNroColumna()!=b2.getNroColumna()&&b1.getNroFila()!=b2.getNroFila()){
            //Tiene que desaparecer los dos y la dirección solo cambia eje Y
            System.out.println("Caso 3 ");
            b1.setDureza(0);
            b2.setDureza(0);
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            this.grilla.restarBloquesPintados();
            this.grilla.restarBloquesPintados();
        }

    }
    private void actualizarBloqueContacto(Bloque b){

        actualizarDireccion2(b);
        //  colisionBloqueAux(b);
        b.setDureza(0);
        this.grilla.restarBloquesPintados();
    }

    private void pintarGrilla(Canvas canvas){
        for (int i = 0; i < this.grilla.getCantidadFilas(); i++){
            for (int j = 0; j < this.grilla.getCantidadColumnas(); j++){
                Bloque bloque = this.grilla.getBloque(i, j);
                //Este metodo dibuja la grilla creada anteriormente.
                if(bloque.getDureza() == 1){
                    canvas.drawBitmap(bloque.getImagen(), bloque.getPosX(), bloque.getPosY(), null);
//					canvas.drawRect(bloque.getModelo(), bloque.getPincel());
                }
            }
        }
    }

    private void nivelSuperado(Canvas canvas){
        imgSuperoNivel=true;
        gameThread.pause();
        this.reubicarPelota();

        /*
        * Handler , Runnable
        * */
        SharedPreferences preferences = getContext().getSharedPreferences("myidiom", Context.MODE_PRIVATE);
        //int level = preferences.getInt("level",1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("level",2);
        editor.commit();
        new Handler().post(new Runnable() {

            @Override
            public void run() {
            Intent i = new Intent(getContext(),EventoActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(getContext(),i,null);
            }

        });


        this.grilla.avanzarUnNivel();
        //bmp= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ganaste),xMax,yMax,false);
        //canvas.drawBitmap(bmp, 0, 0, null);
        preferences = getContext().getSharedPreferences("myidiom", Context.MODE_PRIVATE);
        // String user = preferences.getString("user","vacio");
        editor = preferences.edit();
        editor.putInt("user_puntaje",this.puntaje);
        editor.commit();
    }

    private void controlDelJuego(Canvas canvas){

        if (inicioJuego && (pelota.getY() < jugador.getPosY()) && (pelota.getY() > (jugador.getPosY() - 50))) {
            //verificarContactoJugador();
            verificarContactoJugador2();
        }

        //actualizamos las posiciones
        if (pantallaPulsada) {
            //Actualizar posicion del jugador
            //posXJugador=posDedoX-(anchoJugador/2);
            transicionEnX();
            jugador.inicializarAreas();
        }
        if (inicioJuego) {
            pelota.actualizarPosicion();
            canvas.drawRect(pelota.getX(), pelota.getY(), pelota.getX() + pelota.getTamanio(), pelota.getY() + pelota.getTamanio(), pincelPelota);
        } else {
            //La pelota esta en la posicion del jugador
            pelota.setX(jugador.getPosX() + (jugador.getAncho() / 2));

            canvas.drawRect(pelota.getX(), pelota.getY(), pelota.getX() + pelota.getTamanio(), pelota.getY() + pelota.getTamanio(), pincelPelota);
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

        SharedPreferences preferences = getContext().getSharedPreferences("myidiom", Context.MODE_PRIVATE);

        String idioma_user = preferences.getString("idioma","es");

        if(idioma_user.equalsIgnoreCase("es")){
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
        else{

            //Nivel
            canvas.drawText("Level:" + String.valueOf(this.grilla.getNivelActual()), (xMax * 10) / 100, 70f, pincelIndicadores);
            //Puntaje
            canvas.drawText("Score:" + String.valueOf(this.puntaje), (xMax * 40) / 100, 70f, pincelIndicadores);
            //Vidas
            if(vidas>0){

                canvas.drawText("Life:" + String.valueOf(this.vidas), (xMax * 75) / 100, 70f, pincelIndicadores);
            }else{

                canvas.drawText("Life:" + String.valueOf(0), (xMax * 75) / 100, 70f, pincelIndicadores);
            }

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
        pelota.setX(jugador.getPosX() + (jugador.getAncho() / 2));
        pelota.setY(jugador.getPosY()- pelota.getTamanio()-2);
        pelota.setDireccionEnX(-pelota.getVelocidad());
        pelota.setDireccionEnY(-pelota.getVelocidad());
    }

    public boolean onTouchEvent(MotionEvent evento) {
        //if (evento.getAction() == MotionEvent.ACTION_DOWN) {}
        //Pulso la pantalla
        pantallaPulsada = true;
        posDedoX = (int) evento.getX();
        posDedoY = (int) evento.getY();

        //invalidate();

        //    if(evento.getAction() == MotionEvent.ACTION_UP){}
        // MotionEvent.ACTION_DOWN
        if(imgFinJuego || imgSuperoNivel){
            if (evento.getAction() == MotionEvent.ACTION_UP) {
                //Si perdio o gano
                if(posDedoX>50&&posDedoX<(xMax-50)){
                    if(posDedoY>600&&posDedoY<(yMax-300)){
                        gameThread.continuar();
                        imgFinJuego=false;
                        imgSuperoNivel=false;
                    }
                }
            }
        }else{
            if(evento.getAction() == MotionEvent.ACTION_UP){
                //System.out.println("Pulso la pantalla *********************");
                inicioJuego = true;
                pantallaPulsada = false;
            }
        }


        //Boton continuar
        if(evento.getAction() == MotionEvent.ACTION_UP){
            if(posDedoX>50&&posDedoX<(xMax-50)){
                if(posDedoY>10&&posDedoY<300){
                    gameThread.continuar();
                    System.out.println("Continuar jugando Boton Segui jugando #0# ");

                }
            }
        }
        if(juegoEnPausa && !siguienteFotograma){
            if (evento.getAction() == MotionEvent.ACTION_UP) {
                if(posDedoX>xMax-100){
                    if(posDedoY>yMax-100){
                        gameThread.continuar();
                        System.out.println("Continuar jugando Boton Segui jugando [1] ");
                        siguienteFotograma=true;
                    }
                }
            }

        }

        if(juegoEnPausa && !siguienteFotograma){
            if (evento.getAction() == MotionEvent.ACTION_UP) {
                if(posDedoX<100){
                    if(posDedoY>yMax-100){
                        gameThread.continuar();
                        System.out.println("Continuar jugando Boton Segui jugando *2* ");

                    }
                }
            }

        }
        return true;
    }

    //Controlar rebote
    public void transicionEnX() {
        int posNueva = posDedoX - (jugador.getAncho() / 2);
        //int posNueva=posDedoX;
        int velocidadTransicion = 50;
        int rangoMovimiento = 50;
        int posJugadorCentro = jugador.getPosX() + (jugador.getAncho() / 2);
        //Verifica que el jugador no salga del limite izquierdo ni del derecho
        if(posNueva<0 || (posNueva+jugador.getAncho())>xMax){
            if(posNueva<0){
                //posNueva=-20;
                posNueva=0;
                jugador.setPosX(posNueva);

            }else{
                //posNueva=-20;
                posNueva=xMax-jugador.getAncho();
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

    public void controlarChoques(){
        gameThread.pause();//Para verificar contactos
        juegoEnPausa=true;
        if(siguienteFotograma){

        }
    }

    //La pelota verifica si toco algun borde de la pantalla
    public void verificarContactoPantalla() {

        //Derecha
        if (pelota.getX() + pelota.getTamanio() >= xMax) {
            pelota.setDireccionEnX(-pelota.getVelocidad());
        }

        //Izquierda
        if (pelota.getX() <= 0) {
            pelota.setDireccionEnX(pelota.getVelocidad());
        }

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
    public boolean verificarContacto(Bloque bloque){
        boolean salida=false;
        int areaDeContacto = -1;
//		int x=pelota.getX();
//		int y=pelota.getY();
//
//		int cx=pelota.getX()+(pelota.getTamanio()/2);
//		int cy=pelota.getY()+(pelota.getTamanio()/2);
//
//		int anchoPelota=pelota.getTamanio();
//		int alto=pelota.getTamanio();
//
//		Rect rec1=new Rect(x,y,x+anchoPelota,y+alto);
//		int bX=(int)b.getPosX();
//		int by=(int)b.getPosY();
//		int bAncho=(int)(b.getPosX()+b.getAnchoBloque());
//		int bAlto=(int)(b.getPosY()+b.getAltoBloque());
//		Rect rec2=new Rect(bX, (int)b.getPosY(), (int)(b.getPosX()+b.getAnchoBloque()), (int)(b.getPosY()+b.getAltoBloque()));

        //if(rec1.intersect(rec2)){
        // if(rec1.intersect(rec2,x,y,anchoPelota/2)){
        //if(intersect(rec2,x,y,anchoPelota/2)){
        if(this.pelota.interseccion(bloque.getModelo())){
            System.out.println("---------Se detecto una intersección con el bloque: "+bloque.getId());
            areaDeContacto = bloque.getAreaDeContacto(this.pelota);
//			actualizarDireccion2(bloque);
            this.pelota.setDireccion3(areaDeContacto, "Bloque");
            bloque.setDureza(0);
            this.grilla.restarBloquesPintados();
            salida = true;
        }
//		if(interseccion(cx,cy,anchoPelota/2,rec2)){
//			System.out.println("---------Se detecto una intersección con el bloque: "+b.getId());
//			actualizarDireccion2(b);
//			//  colisionBloqueAux(b);
//			b.setDureza(0);
//			this.grilla.restarBloquesPintados();
//			salida=true;
//		}
        return salida;
    }

    public void actualizarDireccion2(Bloque b) {
        Rect [] areasBloque=b.areaDeContacto();

        int posArea=0;
        boolean salir=false;

        while(posArea<areasBloque.length && !salir){
            Rect auxRect=areasBloque[posArea];
            if(pelota.interseccion(auxRect)){
                salir=true;
            }else{
                posArea++;
            }
        }
        System.out.println(" *************** Area despues del while es: "+posArea);

        if(posArea == 0){
            Rect auxRect = areasBloque[1];
            Rect auxRect2 = areasBloque[3];

            if(pelota.interseccion(auxRect)){
                posArea = 1;
            }else if(pelota.interseccion(auxRect2)){
                posArea = 3;
            }
        }

        if(posArea == 2){
            Rect auxRect = areasBloque[1];
            Rect auxRect2 = areasBloque[4];

            if(pelota.interseccion(auxRect)){
                posArea = 1;
            }else if(pelota.interseccion(auxRect2)){
                posArea = 4;
            }
        }

        if(posArea == 5){
            Rect auxRect = areasBloque[6];
            Rect auxRect2 = areasBloque[3];

            if(pelota.interseccion(auxRect)){
                posArea = 6;
            }else if(pelota.interseccion(auxRect2)){
                posArea = 3;
            }
        }

        if(posArea == 7){
            Rect auxRect = areasBloque[6];
            Rect auxRect2 = areasBloque[4];

            if(pelota.interseccion(auxRect)){
                posArea = 6;
            }else if(pelota.interseccion(auxRect2)){
                posArea = 4;
            }
        }

        System.out.print("Area al salir del metodo actualizarDireccion2 : "+posArea);
        System.out.println();
        updateDireccion(posArea);

    }
    private void updateDireccion(int area){
        if(area == 1 || area == 6){
            System.out.println("choco en el area "+area+" en uno de los centros");
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
        }
        else if(area == 3 || area == 4){
            System.out.println("choco en el area "+area+" en uno de los laterales");
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
        }//Aca comienzan las esquinas
        else if(area == 0 && !pelota.getDireccion().equals("abajoDerecha")){
            if(pelota.getDireccion().equals("arribaDerecha")){
                System.out.println("choco en el area "+area+" en la direccion arriba a la derecha");
                pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            }else if(pelota.getDireccion().equals("abajoIzquierda")){
                System.out.println("choco en el area "+area+" en la direccion abajo a la izquierda");
                pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            }
        }else if(area == 2 && !pelota.getDireccion().equals("abajoIzquierda")){
            if(pelota.getDireccion().equals("arribaIzquierda")){
                System.out.println("choco en el area "+ area +" en la direccion arriba la izquierda ");
                pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            }else if(pelota.getDireccion().equals("abajoDerecha")){
                pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            }
        }else if(area == 5 && !pelota.getDireccion().equals("arribaDerecha")){
            if(pelota.getDireccion().equals("abajoDerecha")){
                System.out.println("choco en el area "+area+" en la direccion abajo a la derecha");
                pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            }else if(pelota.getDireccion().equals("arribaIzquierda")){
                System.out.println("choco en el area "+ area +" en la direccion arriba la izquierda ");
                pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            }
        }else if(area == 7 && !pelota.getDireccion().equals("arribaIzquierda")){
            if(pelota.getDireccion().equals("arribaDerecha")){
                System.out.println("choco en el area "+area+" en la direccion arriba a la derecha");
                pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
            }else if(pelota.getDireccion().equals("abajoIzquierda")){
                System.out.println("choco en el area "+area+" en la direccion abajo a la izquierda");
                pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            }
        }else{
            System.out.println("choco en el area "+area+" sale en direccion correcta");
            pelota.setDireccionEnX(-1 * pelota.getDireccionEnX());
            pelota.setDireccionEnY(-1 * pelota.getDireccionEnY());
        }
    }

    public boolean verificarContactoJugador(){
        boolean salida=false;
        //int x=pelota.getX();
        //int y=pelota.getY();

        // Aca verificar *****
        int x=pelota.getPosSiguienteX()-7;
        int y=pelota.getPosSiguienteY()-7;
        int anchoPelota=pelota.getTamanio();
        int alto=pelota.getTamanio();
        Rect rec1=new Rect(x,y,x+anchoPelota,y+alto);
        Rect rec2=new Rect(jugador.getPosX(), jugador.getPosY(), jugador.getPosX()+jugador.getAncho(), jugador.getPosY()+jugador.getAncho());

        if(rec1.intersect(rec2)/*||(y+alto)==posYJugador*/){

            //Colisiono
            //System.out.println("----------------------------Choco valor Pelota x: "+x+" valor y "+ y);
            //System.out.println("----------------------------Choco valor Jugador x: "+jugador.getPosX()+" valor y "+ jugador.getPosY());
            actualizarDireccionPelota();

            salida=true;
            //gameThread.pause();
        }else{
            //System.out.println("No choco valor Pelota x: "+x+" valor y "+ y);
            //System.out.println("No choco valor Jugador x: "+jugador.getPosX()+" valor y "+ jugador.getPosY());
            //gameThread.pause();
        }
        return salida;
    }

    public void actualizarDireccionPelota(){
        //contacto y

        //Verifico la posicion del jugador donde choca, porque eso me da la inclinación de salida

        int cuadro1 = jugador.getAncho() / 5;
        if (pelota.getX() < (jugador.getPosX() + (cuadro1 * 1))) {
            //choco en el primer cuadrante
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
            //Verifico en cual angulo venia
            //en el cuadrante del centro solo actualizo el y
            pelota.setDireccionEnY(-1*pelota.getVelocidad());

        }
        if (pelota.getX() > (jugador.getPosX() + (cuadro1 * 3)) && pelota.getX() < (jugador.getPosX() + (cuadro1 * 4))) {
            //choco en el cuarto cuadrante
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

    private void manejadorDeIntersecciones(){

    }

    private void manejadorDelJugador(){

    }

    private void verificarContactoJugador2(){
        int areaDeContacto = this.jugador.getAreaDeContacto(this.pelota);
        this.pelota.setDireccion3(areaDeContacto, "Jugador");
    }
}
