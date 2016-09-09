package ar.edu.unrn.lia.capacitacionhorizonte2;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by horaciomunoz on 26/8/16.
 */

public class AppCapacitacion extends Application {
    static final String TAG = AppCapacitacion.class.getSimpleName();

    private SharedPreferences sharedPref;
    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    }


    public String username(){
        return sharedPref.getString("user_name", getString(R.string.ref_user_name_default));
    }

    public boolean isActiveNotification(){
        return sharedPref.getBoolean("alert_noti", true);
    }

    public Integer getTimeNotificacion(){
        return Integer.parseInt(sharedPref.getString("alert_noti_time", "10"))*1000;
    }


    public Integer getDistancia(){
        return Integer.parseInt(sharedPref.getString("map_distance", "20"));
    }

    public Integer getTimeInterval(){
        return Integer.parseInt(sharedPref.getString("map_time_interval", "1"))*1000;
    }

}
