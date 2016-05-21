package com.example.antoniolategano.mensapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText inserimento_pasti;
    TextView pasti_consumati;
    TextView pasti_rimanenti;
    Button conferma;
    Button intero;
    Button ridotto;
    float num_pasti_consumati;
    float num_pasti_rimanenti;
    static Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        num_pasti_consumati = 0;
        inserimento_pasti = (EditText) findViewById(R.id.insPastiEditText);
        pasti_consumati = (TextView) findViewById(R.id.PastiConsTextView);
        pasti_rimanenti = (TextView) findViewById(R.id.PastiRimTextView);
        intero = (Button) findViewById(R.id.PlusOneButton);
        ridotto = (Button) findViewById(R.id.PlusHalfButton);
        conferma = (Button) findViewById(R.id.confermaButton);
        inserimento_pasti.setText("0");

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        num_pasti_consumati = sharedPref.getFloat("pasti_cons", 0);
        num_pasti_rimanenti = sharedPref.getFloat("pasti_rim", 0);

        pasti_consumati.setText(String.valueOf(num_pasti_consumati));
        pasti_rimanenti.setText(String.valueOf(num_pasti_rimanenti));
        inserimento_pasti.setText(String.valueOf(num_pasti_consumati));

        intero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_pasti_rimanenti <= 0) {
                    pasti_rimanenti.setText("0.0");
                    Toast.makeText(MainActivity.this, "Sei fottuto, ma puoi scroccare la tessera a qualcuno", Toast.LENGTH_LONG).show();
                } else {
                    num_pasti_consumati += 1;
                    num_pasti_rimanenti -= 1;
                    pasti_consumati.setText(String.valueOf(num_pasti_consumati));
                    pasti_rimanenti.setText(String.valueOf(num_pasti_rimanenti));
                }

            }
        });

        ridotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_pasti_rimanenti <= 0) {
                    pasti_rimanenti.setText("0.0");
                    Toast.makeText(MainActivity.this, "Sei fottuto, ma puoi scroccare la tessera a qualcuno", Toast.LENGTH_LONG).show();
                } else {
                    num_pasti_consumati += 0.5;
                    num_pasti_rimanenti -= 0.5;
                    pasti_consumati.setText(String.valueOf(num_pasti_consumati));
                    pasti_rimanenti.setText(String.valueOf(num_pasti_rimanenti));
                }
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num_pasti_consumati = Float.parseFloat(inserimento_pasti.getText().toString());
                if(num_pasti_consumati > 310) {
                    num_pasti_consumati = 310;
                }
                if(num_pasti_consumati < 0) {
                    num_pasti_consumati = 0;
                }
                num_pasti_rimanenti = 310 - num_pasti_consumati;
                pasti_consumati.setText(String.valueOf(num_pasti_consumati));
                pasti_rimanenti.setText(String.valueOf(num_pasti_rimanenti));
                inserimento_pasti.setVisibility(View.INVISIBLE);
                conferma.setVisibility(View.INVISIBLE);
            }
        });

        myIntent = new Intent(this , MyReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 39);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("pasti_cons", num_pasti_consumati);
        editor.putFloat("pasti_rim", num_pasti_rimanenti);
        editor.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pasti_setting) {
            inserimento_pasti.setVisibility(View.VISIBLE);
            conferma.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
