package com.example.divisortiempo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText tiempoEditText, bloquesEditText;
    Button comenzarButton, detenerButton;
    int tiempo, bloques, intervalo, intervaloOriginal;

    Timer timer, timer2;
    NotificationManager notificationManager;
    MediaPlayer mediaPlayer;
    TextView minutosRestantes;
    boolean isTimerRunning = false;
    boolean isTimerRunning2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tiempoEditText = findViewById(R.id.tiempoEditText);
        bloquesEditText = findViewById(R.id.bloquesEditText);
        comenzarButton = findViewById(R.id.comenzarButton);
        detenerButton = findViewById(R.id.detenerButton);
        minutosRestantes = findViewById(R.id.minutosRestantes);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

            temporizador1();
            temporizador2();
        }
        else if( view.getId() == R.id.detenerButton ){
            timer.cancel();
            timer2.cancel();
            notificationManager.cancelAll();
            mediaPlayer.stop();
        }
    }

    public void temporizador1(){
        if( isTimerRunning )
            timer.cancel();
        else {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    reproducirAlarma();
                }
            }, intervaloOriginal * 60 * 1000, intervaloOriginal * 60 * 1000);
            // Para que suene cada intervaloOriginal minutos, a partir de que termina el primer
            // bloque de tiempo.

            isTimerRunning = true;
        }
    }

    public void temporizador2(){
        if( isTimerRunning2 )
            timer2.cancel();
        else {
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    tiempoRestante();
                }
            }, 0, 60 * 1000);
            // Para que cambie cada 60 segundos (1 minuto), a partir del instante que
            // se mande a llamar el timer.

            isTimerRunning2 = true;
        }
    }

    private void reproducirAlarma() {
        mediaPlayer.start();
    }

    public void tiempoRestante() {
        minutosRestantes.setText( " " + (intervalo--) );
        if( intervalo == -1 ) // Porque cuando llegue al minuto 0, todavia quedarian 60 segundos mas.
            intervalo = intervaloOriginal;
    }
}
