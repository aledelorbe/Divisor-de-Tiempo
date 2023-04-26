package com.example.divisortiempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtTxtTiempo, edtTxtBloques;
    Button btnComenzar, btnDetener, btnReiniciar;
    int tiempo, bloques, intervaloMin, intervaloMinOrigi, intervaloSegOrigi, contadorBloques = 0, intervaloSeg = 0;
    Timer timer1;
    MediaPlayer mediaPlayer;
    TextView txtMinutosRestantes, txtSegundosRestantes, txtBloques;
    boolean isTimerRunning1 = false, banderaFin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTxtTiempo = findViewById(R.id.tiempoEditText);
        edtTxtBloques = findViewById(R.id.bloquesEditText);
        btnComenzar = findViewById(R.id.comenzarButton);
        btnDetener = findViewById(R.id.detenerButton);
        btnReiniciar = findViewById(R.id.reiniciarButton);
        txtMinutosRestantes = findViewById(R.id.minutosRestantes);
        txtSegundosRestantes = findViewById(R.id.segundosRestantes);
        txtBloques = findViewById(R.id.txtBloques);

        // Se crea una variable que contine la info del audio que sirve como alarma.
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        // Se pone a escuchar los botones.
        btnComenzar.setOnClickListener(this);
        btnDetener.setOnClickListener(this);
        btnReiniciar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        // Comportamiento del boton comenzar
        if( view.getId() == R.id.comenzarButton ){
            // Extraccion de los datos ingresados por el usuario: minutos y bloques.
            tiempo = Integer.parseInt(edtTxtTiempo.getText().toString());
            bloques = Integer.parseInt(edtTxtBloques.getText().toString());

            // Determinar cuantos minutos* dura un bloque de tiempo.
            intervaloMinOrigi = intervaloMin = tiempo / bloques;

            // Mandar a llamar el metodo temporizadorSegundos.
            temporizadorSegundos();
        }
        // Comportamiento del boton detener
        else if( view.getId() == R.id.detenerButton ){
            // Detendra la reproduccion de la alarma.
            mediaPlayer.stop();
        }
        // Comportamiento del boton reniciar
        else if ( view.getId() == R.id.reiniciarButton) {
            // Finalizara el timer y reiniciara la app.
            finalizarTimers();
            reiniciarApp();
        }
    }

    // Cada que pase un segundo, imprimira el nuevo segundo restante.
    public void temporizadorSegundos(){
        // Si ya esta corriendo el timer, quiere decir que el usuario previamente ha solicitado
        // un divisor de tiempo, por lo que se le avisara al usuario que no podra solicitar otro
        // a menos que reinice la app.
        if(isTimerRunning1)
            Toast.makeText(getApplicationContext(), "Ya ha un divisor de tiempo programado. Reinicie la app si desea programar otro.", Toast.LENGTH_LONG).show();
        else {
            // Mandar a llamar el metodo temporizadorSegundos.
            calcularSegundos();

            timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Impresion del valor de los minutos y segundos.
                            txtMinutosRestantes.setText(" " + (intervaloMin));
                            txtSegundosRestantes.setText(" " + (intervaloSeg));

                            // Si ya termino un bloque de tiempo, imprimir que cierto bloque de
                            // tiempo ha concluido y si ya terminaron todos los bloques de tiempo
                            // imprimir que el divisor de tiempo solicitado a concluido.
                            if ( intervaloMin == 0 && intervaloSeg == 0)
                            {
                                txtBloques.append("¡El bloque de tiempo " + (contadorBloques) + " ha terminado!\n");
                                if ( contadorBloques == bloques )
                                {
                                    txtBloques.append("¡Se ha terminado el divisor de tiempo!");
                                }
                            }
                        }
                    });
                    intervaloSeg--;

                    // Si la banderaFin es true, ya debe detenerse el timer.
                    if( banderaFin )
                    {
                        this.cancel();
                    }
                    // Si los minutos no valen 0 y los segundos ya valen -1,
                    // A los segundos se le asigna 59 y se decrementa en un
                    // uno los minutos.
                    else if( intervaloMin != 0 && intervaloSeg == -1 )
                    {
                        intervaloSeg = 59;
                        intervaloMin--;
                    }
                    // Si los minutos valen 0 y los segundos tambien, quiere
                    // decir que un bloque de tiempo ya termino, por lo que debe hacerse sonar
                    // la alarma.
                    else if ( intervaloMin == 0 && intervaloSeg == 0)
                    {
                        reproducirAlarma();
                    }
                    // Si los minutos valen 0 y los segundos -1, volver a comenzar un bloque
                    // de tiempo, asignandoles a los segundo y minutos los valores originales
                    else if( intervaloMin == 0 && intervaloSeg == -1 )
                    {
                        intervaloSeg = intervaloSegOrigi;
                        intervaloMin = intervaloMinOrigi;
                    }


                    // Si los minutos vale 0 y los segundo valen 1, ya casi termina un bloque
                    // de tiempo por lo que se debe incrementar en uno la variable que cuenta
                    // los bloques de tiempo concluidos, y si ya terminaron todos los bloques de
                    // tiempo solicitados por el usuario, entonces asignarle true a la banderaFin.
                    if ( intervaloMin == 0 && intervaloSeg == 1)
                    {
                        contadorBloques++;
                        if ( contadorBloques == bloques )
                        {
                            banderaFin = true;
                        }
                    }
                }
            }, 0, 1000);
            // Para que cambie cada 1 segundo, a partir del instante que
            // se mande a llamar el timer.

            // Esta variable se utiliza como bandera para saber que ya se esta corriendo el timer.
            isTimerRunning1 = true;
        }
    }

    // Metodo que se encarga de reproducir el audio de la alarma.
    private void reproducirAlarma() {
        mediaPlayer.start();
    }

    // Metodo que se encarga de calcular el tiempo en segundos, de los bloques de tiempo solicitados
    public void calcularSegundos() {

        String entrada = String.valueOf( (float)tiempo/(float)bloques );
        int posPto = entrada.indexOf(".");
        System.out.println("posPto = " + posPto);

        // Si al realizar la operacion: tiempo/bloques, no arroja valores decimales entonces...
        if( posPto == -1 ){
            // Los segundos valen 0.
            intervaloSeg = 0;
        }
        // En caso contrario entonces...
        else{
            // Extraer los vales que hay despues del pto.
            String decimales = entrada.substring(posPto + 1, entrada.length());
            System.out.println("decimales = " + decimales);

            // Si la longitud de ese valor es 1, entonces hay que concatenarle un
            // 0. Porque si por ejemplo el valor es 5, en realidad debe valer 50 para la
            // realizar la conversion a segundos.
            if (decimales.length() == 1) {
                decimales += "0";
            }

            // Realizar la conversion de ese valor a segundos.
            int decimalesInt = Integer.parseInt(decimales);
            intervaloSegOrigi = intervaloSeg = decimalesInt * 60 / 100;
        }

        System.out.println("intervaloSeg = " + intervaloSeg);
    }

    // Metodo que se encarga de detener el timer que esta corriendo.
    public void finalizarTimers(){
        timer1.cancel();
    }

    // Metodo que se encarga de ejecutar las instrucciones necesarias para dejar como estaba en
    // un principio la app (reinicio de app)
    public void reiniciarApp(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}




