package snap.hackforresilience;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.PositionIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Map map = null;
    private MapFragment mapFragment = null;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private PositioningManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

    }

    private void initialize() {
        setContentView(R.layout.activity_main);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        map = mapFragment.getMap();
                       addVictimMarker(12.971604,79.165172);
                       addVolunteerMarker(12.968864,79.161095);
                       addDroneMarker(12.964264,79.154938);

                                map.setCenter(new GeoCoordinate(12.971604, 79.165172, 0.0),
                                Map.Animation.NONE);
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });


    }

    public void addVictimMarker(double lo, double ll){
        com.here.android.mpa.common.Image myImage =
                new com.here.android.mpa.common.Image();
        try {
            String uri = "@drawable/victim";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            myImage.setImageResource(imageResource);
        } catch (IOException e) {

        }
        MapMarker myMapMarker =
                new MapMarker(new GeoCoordinate(lo,ll), myImage);
        map.addMapObject(myMapMarker);
    }

    public void addVolunteerMarker(double lo, double ll){
        com.here.android.mpa.common.Image myImage =
                new com.here.android.mpa.common.Image();
        try {
            String uri = "@drawable/volunteer";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            myImage.setImageResource(imageResource);
        } catch (IOException e) {

        }
        MapMarker myMapMarker =
                new MapMarker(new GeoCoordinate(lo,ll), myImage);
        map.addMapObject(myMapMarker);
    }
    public void addDroneMarker(double lo, double ll){
        com.here.android.mpa.common.Image myImage =
                new com.here.android.mpa.common.Image();
        try {
            String uri = "@drawable/drone";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            myImage.setImageResource(imageResource);
        } catch (IOException e) {

        }
    }
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                initialize();
                break;
        }
    }

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };
}