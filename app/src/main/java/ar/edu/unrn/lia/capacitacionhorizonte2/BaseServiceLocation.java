package ar.edu.unrn.lia.capacitacionhorizonte2;

import android.app.Service;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



/**
 * Created by horaciomunoz on 30/8/16.
 */

public abstract class BaseServiceLocation extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static BaseServiceLocation instance = null;

    protected String TAG;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final int REQUEST_LOCATION = 2;

    protected AppCapacitacion appCapacitacion;


    public static BaseServiceLocation getInstance() {
        return instance;
    }

    public static void setInstance(BaseServiceLocation instance) {
        BaseServiceLocation.instance = instance;
    }

    @Override
    public void onCreate() {
        setInstance(this);
        TAG = this.getClass().getSimpleName();
        appCapacitacion = (AppCapacitacion) getApplicationContext();
        setupGoogleAPIClient();
        mGoogleApiClient.connect();

        Log.i(TAG, "Servicio Creando");
    }

    public static boolean isInstanceCreated() {
        return getInstance() != null;
    }



    protected synchronized void setupGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(appCapacitacion.getTimeInterval());// Update location every second
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(appCapacitacion.getDistancia()); //Set the minimum displacement between location updates in meters
    }



    @Override
    public void onDestroy() {
        setInstance(null);
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.i(getClass().getSimpleName(), "Servicio Destruido");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }


    public abstract void onConnectionFailed(@NonNull ConnectionResult connectionResult) ;


    public abstract void onLocationChanged(Location location);



}
