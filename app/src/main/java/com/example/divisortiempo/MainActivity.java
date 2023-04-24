package com.example.divisortiempo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText tiempoEditText, bloquesEditText, txtBloques;
    Button comenzarButton, detenerButton;
    int tiempo, bloques, intervalo, intervaloOriginal, contadorBloques = 0, intervaloSeg;
    Timer timer, timer2, timer3;
    MediaPlayer mediaPlayer;
    TextView minutosRestantes, segundosRestantes;
    boolean isTimerRunning = false, isTimerRunning2 = false, isTimerRunning3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tiempoEditText = findViewById(R.id.tiempoEditText);
        bloquesEditText = findViewById(R.id.bloquesEditText);
        comenzarButton = findViewById(R.id.comenzarButton);
        detenerButton = findViewById(R.id.detenerButton);
        minutosRestantes = findViewById(R.id.minutosRestantes);
        segundosRestantes = findViewById(R.id.segundosRestantes);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        comenzarButton.setOnClickListener(this);
        detenerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if( view.getId() == R.id.comenzarButton ){
            tiempo = Integer.parseInt(tiempoEditText.getText().toString());
            bloques = Integer.parseInt(bloquesEditText.getText().toString());
            intervaloOriginal = intervalo = tiempo / bloques;
            intervaloSeg = 59;

            temporizadorBloques();
            temporizadorMinutos();
            //temporizadorSegundos();
        }
        else if( view.getId() == R.id.detenerButton ){
            mediaPlayer.stop();
        }/* else if ( view.getId() == R.id.reiniciarButton) {

        }*/
    }

    // Cada que termine un bloque sonara la alarma y se imprimira el texto
    // de bloque numero i terminado.
    public void temporizadorBloques(){
        if( isTimerRunning )
            timer.cancel();
        else {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reproducirAlarma();
                    imprimirBloqueTerminado();
                }
            }, intervaloOriginal * 60 * 1000, intervaloOriginal * 60 * 1000);
            // Para que suene cada intervaloOriginal minutos, a partir de que termina el primer
            // bloque de tiempo.

            isTimerRunning = true;
        }
    }

    // Cada que pase un minuto, imprimira el nuevo minuto restante.
    public void temporizadorMinutos(){
        if( isTimerRunning2 )
            timer2.cancel();
        else {
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    tiempoMinRestante();
                }
            }, 0, 60 * 1000);
            // Para que cambie cada 60 segundos (1 minuto), a partir del instante que
            // se mande a llamar el timer.

            isTimerRunning2 = true;
        }
    }

    // Cada que pase un segundo, imprimira el nuevo segundo restante.
    public void temporizadorSegundos(){
        if( isTimerRunning3 )
            timer3.cancel();
        else {
            timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                @Override
                public void run() {
                    tiempoSegRestante();
                }
            }, 0, 1000);
            // Para que cambie cada 1 segundo, a partir del instante que
            // se mande a llamar el timer.

            isTimerRunning3 = true;
        }
    }

    private void reproducirAlarma() {
        mediaPlayer.start();
    }

    public void tiempoMinRestante() {
        minutosRestantes.setText( " " + (intervalo--) );
        if( intervalo == -1 ) // Porque cuando llegue al minuto 0, todavia quedarian 60 segundos mas.
            intervalo = intervaloOriginal;
    }

    public void tiempoSegRestante() {
        segundosRestantes.setText( " " + (intervaloSeg--) );
        /*
        if( intervaloSeg == -1 ) // Porque cuando llegue al minuto 0, todavia quedarian 60 segundos mas.
            intervaloSeg = 59;

         */
    }

    public void imprimirBloqueTerminado() {
        txtBloques.append("¡El bloque de tiempo " + (++contadorBloques) + " ha terminado!\n");
    }
}
