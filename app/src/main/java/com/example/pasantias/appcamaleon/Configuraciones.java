package com.example.pasantias.appcamaleon;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Configuraciones extends AppCompatActivity {
    private Switch swModotrabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        swModotrabajo = (Switch) findViewById(R.id.sw_modotrabajo);

        SharedPreferences sharedPreferences = getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        int modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);
        int estadoDescargas = sharedPreferences.getInt("estadoDescargas", 0);
        Log.d("MODO TRABAJO ANTES" , String.valueOf(modoTrabajo));

        if (modoTrabajo == 1) {
            swModotrabajo.setChecked(false);
        } else swModotrabajo.setChecked(true);

        swModotrabajo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(),
                            "Modo Offline Selecionado", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("datosAplicacion", MODE_PRIVATE);
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putInt("modoDeTrabajo",0 );
                    e.commit();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Modo Online Seleccionado", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("datosAplicacion", MODE_PRIVATE);
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putInt("modoDeTrabajo",1 );
                    e.commit();
                }
            }
        });





    }
}
