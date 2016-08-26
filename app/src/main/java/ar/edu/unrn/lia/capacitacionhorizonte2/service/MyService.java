package ar.edu.unrn.lia.capacitacionhorizonte2.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import ar.edu.unrn.lia.capacitacionhorizonte2.gps.MainActivity;

public class MyService extends Service {

    private NotificationManager myNotificationManager;
    private Timer timer;
    private SharedPreferences sharedPref;
    private boolean notiPref;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(getClass().getSimpleName(), "Servicio Creando");
        Toast.makeText(MyService.this, "Servicio Creando", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

  /*      displayNotification(

                "Creating service",
                "Servicio Creado"
        );*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getClass().getSimpleName(), "Intent received");
        Toast.makeText(MyService.this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();

        super.onStartCommand(intent, flags, startId);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notiPref = sharedPref.getBoolean("alert_noti", true);
                if (notiPref){
                displayNotification(

                        "Servicio Creado",
                        "Servicio corriendo"
                );}

            }
        }, 0, 60000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i(getClass().getSimpleName(), "Servicio Destruido");
        Toast.makeText(MyService.this, "Servicio Destruido", Toast.LENGTH_SHORT).show();
    }

    protected void displayNotification(String titulo, String contenido) {

        // Invoking the default notification service
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(titulo);
        mBuilder.setContentText(contenido);
        mBuilder.setTicker("Nueva notificacion");//es el que se muestra en la barrita
        mBuilder.setSmallIcon(R.drawable.ic_info_black_24dp);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_info_black_24dp));
        mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSound(uri);
        // Increase notification number every time a new notification arrives
        //mBuilder.setNumber(++numMessagesOne);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        // resultIntent.putExtra("notificationId", notificationIdOne);
        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);//can only be used once

        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

        // pass the Notification object to the system
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        myNotificationManager.notify(1, mBuilder.build());


    }


}
