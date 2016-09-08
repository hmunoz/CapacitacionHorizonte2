package ar.edu.unrn.lia.capacitacionhorizonte2.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;

import java.text.DateFormat;
import java.util.Date;

import ar.edu.unrn.lia.capacitacionhorizonte2.BaseActivityLocation;
import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import ar.edu.unrn.lia.capacitacionhorizonte2.maps.MapsActivity;
import ar.edu.unrn.lia.capacitacionhorizonte2.preference.SettingsActivity;
import ar.edu.unrn.lia.capacitacionhorizonte2.service.MyService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivityLocation {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @BindView(R.id.latitude)
    TextView latitude;
    @BindView(R.id.longitude)
    TextView longitude;

    private String mLastUpdateTime;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_main)
    RelativeLayout container;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.imgPreview)
    ImageView imageView;
    @BindView(R.id.latitude_textview)
    TextView latitudeTextview;
    @BindView(R.id.longitude_textview)
    TextView longitudeTextview;
    LocalBroadcastManager bm;

    // handler for received data from service
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyService.BROADCAST_ACTION_SERVICE)) {
                final String param = intent.getStringExtra(MyService.EXTRA_PARAM);
                Snackbar.make(container, "Form Service: "+param, Snackbar.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Reciver
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyService.BROADCAST_ACTION_SERVICE);
        bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mBroadcastReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        bm.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_map) {
            startActivity(new Intent(this, MapsActivity.class));
            return true;
        } else if (id == R.id.action_start_stope) {
            //Iniciar/Stop Servicios
            Intent intent = new Intent(this, MyService.class);
            if (MyService.isInstanceCreated()) {
                stopService(intent);
            } else {
                startService(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(container, connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        latitudeTextview.setText(String.valueOf(location.getLatitude()));
        longitudeTextview.setText(String.valueOf(location.getLongitude()));
        Snackbar.make(container, "Updated: " + mLastUpdateTime, Snackbar.LENGTH_SHORT).show();

    }


    //Camara Hardware


    @OnClick(R.id.fab)
    public void onClick() {
        dispatchTakePictureIntent();
        Snackbar.make(container, "Tomar Foto", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }


}
