package ar.edu.unrn.lia.capacitacionhorizonte2.maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ar.edu.unrn.lia.capacitacionhorizonte2.BaseActivityLocation;
import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import ar.edu.unrn.lia.capacitacionhorizonte2.service.LocationService;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    static  final String TAG = MapsActivity.class.getSimpleName();

    private Context context;
    private GoogleMap mMap;
    LocalBroadcastManager bm;

    public static final int REQUEST_LOCATION = 2;

    // handler for received data from service
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationService.BROADCAST_ACTION_SERVICE)) {
                final LatLng latLngCurrent = new LatLng(intent.getDoubleExtra(LocationService.EXTRA_PARAM_LAT, 0D),
                        intent.getDoubleExtra(LocationService.EXTRA_PARAM_LNG, 0D));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngCurrent, 17);
                mMap.animateCamera(cameraUpdate);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        //Reciver filter Java o XML
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationService.BROADCAST_ACTION_SERVICE);
        bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mBroadcastReceiver, filter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onDestroy() {
        bm.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Android 6.0 permisos
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-40.8064022,-62.9976497);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Horizonte"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
