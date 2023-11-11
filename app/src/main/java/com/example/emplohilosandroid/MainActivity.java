/*d- lo que ocurre en este caso es que directamente aparecera el terminado en
la app debido ha que no hay una actualizacion en tiempo real debido hasta que no termina el
bubcle while este no actualizara la main_Activity

e-directamente recibirás una excepción porque intentas modificar
la interfaz de usuario desde un hilo que no es el principal.*/

package com.example.emplohilosandroid;

/*

//Aqui tienes el f donde si funciona por fin el codigo descomentalo papara probarlo
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private TextView tvCrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final TextView tvCrono = (TextView)findViewById(R.id.tvCrono);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int remaining = 10;
                while(remaining > 0) {
                    final int finalRemaining = remaining;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCrono.setText("" + finalRemaining);
                        }
                    });
                    remaining--;
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        // Manejar excepción
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCrono.setText("Terminado");
                    }
                });
            }
        });
        t.start();





    }
}*/

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter;
    private Button btnStart, btnStop, btnResume, btnReset;
    private CounterTask counterTask;
    private int count = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tvCrono);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnResume = findViewById(R.id.btnResume);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    counterTask = new CounterTask();
                    counterTask.execute(count);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    counterTask.cancel(true);
                    isRunning = false;
                }
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    counterTask = new CounterTask();
                    counterTask.execute(count); // Aquí count mantiene el último valor antes de pausar
                }
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                tvCounter.setText(String.valueOf(count));
            }
        });
    }

    private class CounterTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int start = integers[0];
            while (!isCancelled()) {
                try {
                    Thread.sleep(1000);
                    publishProgress(++start);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            count = values[0];
            tvCounter.setText(String.valueOf(count));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isRunning = false;
        }
    }

}