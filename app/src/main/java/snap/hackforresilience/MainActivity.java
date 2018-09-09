package snap.hackforresilience;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    ImageView chat, visib1,visib2;
 int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

    }

    private void initialize() {
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        chat = (ImageView) findViewById(R.id.chat);
        visib1 = (ImageView) findViewById(R.id.visibility1);
        visib2 = (ImageView) findViewById(R.id.visibility2);
        final CardView card = (CardView) findViewById(R.id.card);
        final CardView card2 = (CardView) findViewById(R.id.card2);
        RelativeLayout rel =  (RelativeLayout) findViewById(R.id.visi);


        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x%2==0)
                {
                    visib1.setVisibility(View.GONE);
                    visib2.setVisibility(View.VISIBLE);
                    x++;
                    card.setVisibility(View.GONE);

                }
                else
                {  visib2.setVisibility(View.GONE);
                visib1.setVisibility(View.VISIBLE);
                    card.setVisibility(View.VISIBLE);
                x++;}

            }
        });

       chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        map = mapFragment.getMap();
                       addVictimMarker(39.9524966,-75.1905943);
                       addVictimMarker(39.9523778,-75.1903640);
                       addDroneMarker(39.9521136,-75.1906577);
                       //addDroneMarker(12.964264,79.154938);

                                map.setCenter(new GeoCoordinate(39.9524966, -75.1905943, 0.0),
                                Map.Animation.NONE);
                        map.setZoomLevel((2*map.getMaxZoomLevel() + map.getMinZoomLevel()) );
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
        MapMarker myMapMarker =
                new MapMarker(new GeoCoordinate(lo,ll), myImage);
        map.addMapObject(myMapMarker);
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