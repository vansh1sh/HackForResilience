package snap.hackforresilience;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.PositionIndicator;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import snap.hackforresilience.rest.ApiClient;
import snap.hackforresilience.rest.ApiInterface;

public class MainActivity extends AppCompatActivity {
    private Map map = null;
    private MapFragment mapFragment = null;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private PositioningManager pm;
    LinearLayout comm,assi;
    String count;
    ImageView chat, visib1,visib2;
    int x=0;
    ArrayList<Double> longitude;
    ArrayList<Double> lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        customTest();
        request();
    }

    public void request(){
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        final Call<Count> loginVerifyCall = apiInterface.getCounter();

        loginVerifyCall.enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                 count= response.body().getCount();
                int foo = Integer.parseInt(count);
                for (int i=0;i<foo;i++){
                    addVictimMarker(longitude.get(i),lat.get(i));
                }
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {

            }
        });
}

    private void initialize() {
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        chat = (ImageView) findViewById(R.id.chat);
        ImageView settings = (ImageView) findViewById(R.id.settings);
        ImageView emergency = (ImageView) findViewById(R.id.emergency);
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

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.chat_dialog);
                dialog.show();


                assi = (LinearLayout) dialog.findViewById(R.id.GoogleAssisstChat);
                comm = (LinearLayout) dialog.findViewById(R.id.CommunityChat);

                comm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent);

                    }


                });
                assi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }


                });
            }
       });


       settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.becomevol);
                dialog.show();


                Button yes = (Button) dialog.findViewById(R.id.yesbtn);
                Button no = (Button) dialog.findViewById(R.id.nobtn);
                final TextView msg = (TextView) dialog.findViewById(R.id.addedvol);

                msg.setVisibility(View.GONE);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        msg.setVisibility(View.VISIBLE);

                    }


                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    dialog.dismiss();
                    }


                });
            }
       });
       emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.emergency);
                dialog.show();

                Button exit = (Button) dialog.findViewById(R.id.exitbtn);

              exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    dialog.dismiss();
                    }


                });
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
                       addVictimMarker(39.9523773,-75.1903640);
                       addVictimMarker(39.952373,-75.1903640);
                       addVictimMarker(39.9523721,-75.1903601);
                       addVictimMarker(39.9523677,-75.1903240);
                       addVictimMarker(39.9523778,-75.1901640);
                       addDroneMarker(39.9521136,-75.1906977);
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
            String uri = "@drawable/voladd";
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

    public void customTest(){
        longitude.add(0,39.9524966);
        longitude.add(1,39.9523778);
        longitude.add(2,39.9526468);
        longitude.add(3,39.9523568);
        longitude.add(4,39.9522348);
        longitude.add(5,39.9527648);
        longitude.add(6,39.9523764);
        longitude.add(7,39.9524247);
        longitude.add(8,39.9523752);
        longitude.add(9,39.9523246);
        longitude.add(10,39.952758);

        lat.add(0,-75.1905943);
        lat.add(1,-75.1903762);
        lat.add(2,-75.1903242);
        lat.add(3,-75.1903432);
        lat.add(4,-75.1903243);
        lat.add(5,-75.1903654);
        lat.add(6,-75.1903267);
        lat.add(7,-75.1903769);
        lat.add(8,-75.1903346);
        lat.add(9,-75.1903086);
        lat.add(10,-75.1903977);


    }

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };
}
