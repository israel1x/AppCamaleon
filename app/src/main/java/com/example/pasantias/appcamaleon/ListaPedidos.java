package com.example.pasantias.appcamaleon;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.Util.FileUploadNotification;
import com.example.pasantias.appcamaleon.Util.ServioEnvioPedidos;

public class ListaPedidos extends AppCompatActivity {
    private Button btnEjecutar;
    private ProgressBar pbarProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        btnEjecutar = (Button) findViewById(R.id.button2);
        pbarProgreso = (ProgressBar)findViewById(R.id.pbarProgreso);
        btnEjecutar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (!isMyServiceRunning(ServioEnvioPedidos.class)) {
                    ServioEnvioPedidos mSensorService = new ServioEnvioPedidos(getApplicationContext());
                    Intent msgIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                    //  msgIntent.putExtra("iteraciones", 10);
                    startService(msgIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Servicio Activo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(ServioEnvioPedidos.ACTION_PROGRESO);
        filter.addAction(ServioEnvioPedidos.ACTION_FIN);
        ProgressReceiver rcv = new ProgressReceiver();
        registerReceiver(rcv, filter);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ServioEnvioPedidos.ACTION_PROGRESO)) {
                int prog = intent.getIntExtra("progreso", 0);
                pbarProgreso.setProgress(prog);
            } else if (intent.getAction().equals(ServioEnvioPedidos.ACTION_FIN)) {
                Toast.makeText(ListaPedidos.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
