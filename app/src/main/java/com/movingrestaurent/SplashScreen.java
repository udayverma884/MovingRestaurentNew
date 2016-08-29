package com.movingrestaurent;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.movingrestaurent.interfaces.OnLogin;
import com.movingrestaurent.truckowner.TruckOwnerNavigationActivity;
import com.movingrestaurent.utils.CurrentLocation;
import com.movingrestaurent.utils.LocationCapture;
import com.movingrestaurent.utils.LocationService;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.utils.Utils;




public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int TIME_OUT = 5000;
    Intent intent;
    private String regId;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    double latitude = 0.0, longitude = 0.0;
    private String android_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(new Utils().CheckInternet(this)){
            CheckGPS();

            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String dfdf=android_id;
            regId=android_id;
            PreferenceConnector.writeString(SplashScreen.this,"regId",regId);
            startCapturing();

            //  notificationMethod();


            //if(PreferenceConnector.readString(mContext,"regId","").length()==0)
            // notificationMethod();


        }else{
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    new Utils().showdailog(
                            "Check internet connection", SplashScreen.this);
                }
            });

        }


        //  switchScreen();



    }


    private NotificationRecieve reciever = null;

    private void notificationMethod() {

        GCMRegistrar.checkDevice(SplashScreen.this);
        GCMRegistrar.checkManifest(SplashScreen.this);
        registerReceiver(reciever,
                new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));

        regId = GCMRegistrar.getRegistrationId(SplashScreen.this);
        if(regId.equalsIgnoreCase("")){
            GCMRegistrar.register(SplashScreen.this, CommonUtilities.SENDER_ID);
            regId= GCMRegistrar.getRegistrationId(SplashScreen.this);
            String df=regId;








        }

        PreferenceConnector.writeString(SplashScreen.this,"regId",regId);
      //  startCapturing();
    }
    public void onDestroy() {
        try {
            unregisterReceiver(reciever);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {

        displayLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void setUpLocation() {
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //getCityName(latitude,longitude);


            //UpdateLocationApi.onUpdateLocation(Urls.UPDATE_CITY_NAME,this,latitude+"",longitude+"","");
            PreferenceConnector.writeString(this, "latitude", String.valueOf(latitude));
            PreferenceConnector.writeString(this, "longitude", String.valueOf(longitude));



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(PreferenceConnector.readBoolean(SplashScreen.this,"isLogin",false)){
if(PreferenceConnector.readString(SplashScreen.this,"userType","").equalsIgnoreCase("restaurant")){
    intent=new Intent(SplashScreen.this,TruckOwnerNavigationActivity.class);
    startActivity(intent);
    finish();
}else {
                        intent=new Intent(SplashScreen.this,NavigatiomDrawerActivity.class);
                        startActivity(intent);
                        finish();
}
                    }else{
                        intent=new Intent(SplashScreen.this,SelectionActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }
            }, TIME_OUT);


        } else {

            Toast.makeText(SplashScreen.this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
//            lblLocation
//                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }
    private void CheckGPS() {
        // Get Location Manager and check for GPS & Network location services
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    dialogInterface.dismiss();



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mLastLocation != null) {
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                                PreferenceConnector.writeString(SplashScreen.this, "latitude", String.valueOf(latitude));
                                PreferenceConnector.writeString(SplashScreen.this, "longitude", String.valueOf(longitude));

                                /*intent=new Intent(SplashScreen.this,HomeScreen.class);
                                startActivity(intent);
                                finish();*/
                            }else{
                                // finish();
                            }



                        }
                    }, TIME_OUT);

                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else {
            setUpLocation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    private void startCapturing() {
        PackageManager pm  = getPackageManager();
        ComponentName componentName = new ComponentName(this, LocationCapture.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Log.d("LoginActivity","startCapturing");
        CurrentLocation.getInstance().setConsumerLocation(LocationService.getInstance(this));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent syncIntent = new Intent(this, LocationCapture.class);
        PendingIntent pendingIntentSync = PendingIntent.getBroadcast(this, 1, syncIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), OnLogin.LOCATION_SYNC_TIME, pendingIntentSync);
    }
   /* private void getCityName(double latitude,double longitude) {
        double lat=0.0,longi=0.0;


        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0){
            String cityName="";
            cityName=addresses.get(0).getLocality();
            PreferenceConnector.writeString(this,"city",cityName);
            UpdateLocationApi.onUpdateLocation(Urls.UPDATE_CITY_NAME,this,latitude+"",longitude+"",cityName);
        }
    }*/
}
