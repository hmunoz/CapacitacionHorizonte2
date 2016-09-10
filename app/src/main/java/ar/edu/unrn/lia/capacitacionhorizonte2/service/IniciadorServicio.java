package ar.edu.unrn.lia.capacitacionhorizonte2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ar.edu.unrn.lia.capacitacionhorizonte2.maps.MapsActivity;

/**
 * Created by Guillermo on 22/08/2016.
 */
public class IniciadorServicio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent(context,LocationService.class);
        context.startService(serviceIntent);


    }

}
