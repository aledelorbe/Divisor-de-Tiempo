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
    int tiempo, bloques, intervalo;

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
            intervalo = tiempo / bloques;

            /*
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mostrarNotificacion();
                    reproducirAlarma();
                }
            }, 0, intervalo * 60 * 1000);*/

            minutosRestantes.setText( intervalo );
            /*
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    minutosRestantes.setText( intervalo );
                    //mostrarTiempoRestante();
                }
            }, 0, 60 * 1000);*/
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

    /*
    private void mostrarTiempoRestante() {
        minutosRestantes.setText( intervalo - 1 );
    }*/


}
