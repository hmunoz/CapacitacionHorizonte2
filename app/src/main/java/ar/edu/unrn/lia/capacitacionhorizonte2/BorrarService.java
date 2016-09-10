package ar.edu.unrn.lia.capacitacionhorizonte2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BorrarService extends Service {

    static final String TAG = BorrarService.class.getSimpleName();

    private static BorrarService instance = null;

    public BorrarService() {
    }

    @Override
    public void onCreate() {
        setInstance(instance);
        super.onCreate();
        Log.i(TAG,"onCreate");
    }

    public static BorrarService getInstance() {
        return instance;
    }

    public static void setInstance(BorrarService instance) {
        BorrarService.instance = instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        setInstance(null);
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}
