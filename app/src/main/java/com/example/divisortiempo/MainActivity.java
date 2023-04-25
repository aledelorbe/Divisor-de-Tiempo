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

    EditText edtTxtTiempo, edtTxtBloques;
    Button btnComenzar, btnDetener, btnReiniciar;
    int tiempo, bloques, intervaloMin, intervaloOriginal, contadorBloques = 0, intervaloSeg = 0;
    Timer timer, timer2, timer3;
    MediaPlayer mediaPlayer;
    TextView txtMinutosRestantes, txtSegundosRestantes, txtBloques;
    boolean isTimerRunning = false, isTimerRunning2 = false, isTimerRunning3 = false;

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

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        btnComenzar.setOnClickListener(this);
        btnDetener.setOnClickListener(this);
        btnReiniciar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        if( view.getId() == R.id.comenzarButton ){
            tiempo = Integer.parseInt(edtTxtTiempo.getText().toString());
            bloques = Integer.parseInt(edtTxtBloques.getText().toString());
            intervaloOriginal = intervaloMin = tiempo / bloques;
            intervaloSeg = 59;

            temporizadorMinutos();
            temporizadorSegundos();
        }
        else if( view.getId() == R.id.detenerButton ){
            mediaPlayer.stop();
        } /*else if ( view.getId() == R.id.reiniciarButton) {
            finalizarTimers();
            reiniciarApp();
        }*/
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
                    intervaloMin--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtMinutosRestantes.setText(" " + (intervaloMin));
                        }
                    });
                    if( intervaloMin == -1 )
                        intervaloMin = intervaloOriginal;
                }
            }, 0, 60*1000);
            // Para que cambie cada 60 segundos (un minuto), a partir del instante que
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
                    intervaloSeg--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtSegundosRestantes.setText(" " + (intervaloSeg));

                            if ( intervaloMin == 0 && intervaloSeg == 0)
                            {
                                txtBloques.append("¡El bloque de tiempo " + (contadorBloques) + " ha terminado!\n");
                            }
                        }
                    });
                    if( intervaloSeg == -1 )
                        intervaloSeg = 59;
                    else if ( intervaloMin == 0 && intervaloSeg == 1)
                    {
                        contadorBloques++;
                    }
                    else if ( intervaloMin == 0 && intervaloSeg == 0)
                    {
                        reproducirAlarma();
                    }
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
        txtMinutosRestantes.setText( " " + (intervaloMin--) );
        if( intervaloMin == -1 ) // Porque cuando llegue al minuto 0, todavia quedarian 60 segundos mas.
            intervaloMin = intervaloOriginal;
    }

    public void tiempoSegRestante() {
        txtSegundosRestantes.setText( " " + (intervaloSeg--) );
        if( intervaloSeg == -1 ) // Porque cuando llegue al minuto 0, todavia quedarian 60 segundos mas.
            intervaloSeg = 59;
    }

}
