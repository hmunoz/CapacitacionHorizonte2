package ar.edu.unrn.lia.capacitacionhorizonte2.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.Format;
import java.util.concurrent.TimeUnit;

import ar.edu.unrn.lia.capacitacionhorizonte2.AppCapacitacion;
import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import ar.edu.unrn.lia.capacitacionhorizonte2.gps.MainActivity;



public class MyService extends Service {

    static final String TAG = MyService.class.getSimpleName();


    private NotificationManager myNotificationManager;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private AppCapacitacion appCapacitacion;

    private static MyService instance = null;

    public static boolean isInstanceCreated() {
        return instance != null;
    }



    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            if (appCapacitacion.isActiveNotification()) {
                displayNotification("Servicio Capacitacion", "Tiempo entre notificacion:" + appCapacitacion.getTimeNotificacion()/1000);
            }
            this.postDelayed(msg.getCallback(),appCapacitacion.getTimeNotificacion());
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }







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

        instance = this;
        appCapacitacion = (AppCapacitacion) getApplicationContext();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);



        Log.i(TAG, "Servicio Creando");
        Toast.makeText(MyService.this, "Servicio Creando", Toast.LENGTH_SHORT).show();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG, "Intent received");
        Toast.makeText(MyService.this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();

        Message  msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);



        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
        instance = null;
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
