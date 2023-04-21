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


        /*
        detenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                notificationManager.cancelAll();
                mediaPlayer.stop();
            }
        });
        */




    }

    @Override
    public void onClick(View view){

        if( view.getId() == R.id.comenzarButton ){
            tiempo = Integer.parseInt(tiempoEditText.getText().toString());
            bloques = Integer.parseInt(bloquesEditText.getText().toString());
            intervaloOriginal = intervalo = tiempo / bloques;


            /*
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mostrarNotificacion();
                    reproducirAlarma();
                }
            }, intervaloOriginal * 60 * 1000, intervaloOriginal * 60 * 1000);*/

            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    tiempoRestante();
                }
            }, 0, 60 * 1000);
            // Para que cambie cada 60 segundos (1 minuto), a partir del instante que
            // se mande a llamar el timer.
        }
        else if( view.getId() == R.id.comenzarButton ){
            timer.cancel();
            timer2.cancel();
            notificationManager.cancelAll();
            mediaPlayer.stop();
        }
    }

    private void mostrarNotificacion() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Bloque completado")
                .setContentText("¡Un bloque de " + intervalo + " minutos ha sido completado!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
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
