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
    boolean isTimerRunning1 = false;

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
            intervaloMinOrigi = intervaloMin = tiempo / bloques;

            calcularSegundos();

            temporizadorSegundos();
        }
        else if( view.getId() == R.id.detenerButton ){
            mediaPlayer.stop();
        } else if ( view.getId() == R.id.reiniciarButton) {
            finalizarTimers();
            reiniciarApp();
        }
    }

    // Cada que pase un segundo, imprimira el nuevo segundo restante.
    public void temporizadorSegundos(){
        if(isTimerRunning1)
            Toast.makeText(getApplicationContext(), "Ya ha un divisor de tiempo programado. Reinicie la app si desea programar otro.", Toast.LENGTH_LONG).show();
        else {
            timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtMinutosRestantes.setText(" " + (intervaloMin));
                            txtSegundosRestantes.setText(" " + (intervaloSeg));

                            if ( intervaloMin == 0 && intervaloSeg == 0)
                            {
                                txtBloques.append("¡El bloque de tiempo " + (contadorBloques) + " ha terminado!\n");
                            }

                            if ( contadorBloques == bloques )
                            {
                                txtBloques.append("¡Se ha terminado el divisor de tiempo!");
                            }
                        }
                    });
                    intervaloSeg--;

                    if( intervaloSeg == -1 && intervaloMin != 0)
                    {
                        intervaloSeg = 59;
                        intervaloMin--;
                    }
                    else if ( intervaloMin == 0 && intervaloSeg == 1)
                    {
                        contadorBloques++;
                    }
                    else if ( intervaloMin == 0 && intervaloSeg == 0)
                    {
                        reproducirAlarma();
                        intervaloSeg = intervaloSegOrigi;
                        intervaloMin = intervaloMinOrigi;
                    }
                }
            }, 0, 1000);
            // Para que cambie cada 1 segundo, a partir del instante que
            // se mande a llamar el timer.

            isTimerRunning1 = true;
        }
    }

    private void reproducirAlarma() {
        mediaPlayer.start();
    }

    public void calcularSegundos() {

        String entrada = String.valueOf( (float)tiempo/(float)bloques );
        int posPto = entrada.indexOf(".");
        System.out.println("posPto = " + posPto);

        if( posPto == -1 ){
            intervaloSeg = 0;
        }
        else{
            String decimales = entrada.substring(posPto + 1, entrada.length());
            System.out.println("decimales = " + decimales);

            if (decimales.length() == 1) {
                decimales += "0";
            }

            int decimalesInt = Integer.parseInt(decimales);
            intervaloSegOrigi = intervaloSeg = decimalesInt * 60 / 100;
        }

        System.out.println("intervaloSeg = " + intervaloSeg);
    }

    public void finalizarTimers(){
        timer1.cancel();
    }

    public void reiniciarApp(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
