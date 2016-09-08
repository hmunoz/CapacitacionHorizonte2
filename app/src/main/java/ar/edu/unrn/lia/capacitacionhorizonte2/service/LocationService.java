package ar.edu.unrn.lia.capacitacionhorizonte2.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import java.text.Format;
import java.util.concurrent.TimeUnit;

import ar.edu.unrn.lia.capacitacionhorizonte2.AppCapacitacion;
import ar.edu.unrn.lia.capacitacionhorizonte2.BaseServiceLocation;
import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import ar.edu.unrn.lia.capacitacionhorizonte2.gps.MainActivity;

//http://stackoverflow.com/questions/33000742/why-is-onlocationchanged-not-called-in-android-service

public class LocationService extends BaseServiceLocation {


    private NotificationManager myNotificationManager;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private static LocationService instance = null;

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
                String msgContenido = String.format(getString(R.string.service_content_txt), appCapacitacion.getTimeNotificacion().toString());
                displayNotification("Servicio Capacitacion", msgContenido);
            }
            this.postDelayed(msg.getCallback(),appCapacitacion.getTimeNotificacion());
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
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
        Toast.makeText(LocationService.this, "Servicio Creando", Toast.LENGTH_SHORT).show();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG, "Intent received");
        Toast.makeText(LocationService.this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(LocationService.this, "Servicio Destruido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"Location error:" +connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
            Log.i(TAG,"Location:" +location.getLatitude() + "");
            broadcastActionLocation(location);
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



    //Reciver

    public static final String BROADCAST_ACTION_SERVICE = "ar.edu.unrn.lia.capacitacionhorizonte2.broadcast_action.LOCATION";
    public static final String EXTRA_PARAM_LAT = "ar.edu.unrn.lia.capacitacionhorizonte2.extra.PARAM_LAT";
    public static final String EXTRA_PARAM_LNG = "ar.edu.unrn.lia.capacitacionhorizonte2.extra.PARAM_LNG";

    // called to send data to Activity
    public static void broadcastActionLocation(Location location) {
        Intent intent = new Intent(BROADCAST_ACTION_SERVICE);
        intent.putExtra(EXTRA_PARAM_LAT,location.getLatitude());
        intent.putExtra(EXTRA_PARAM_LNG,location.getLongitude());
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(instance);
        bm.sendBroadcast(intent);
    }

}
