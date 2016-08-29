package com.movingrestaurent.fragmentuser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.movingrestaurent.NavigatiomDrawerActivity;
import com.movingrestaurent.R;
import com.movingrestaurent.SelectionActivity;
import com.movingrestaurent.https.RegistrationApi;
import com.movingrestaurent.https.UpdateRestaurantLocation;
import com.movingrestaurent.interfaces.OnUpdateLocation;
import com.movingrestaurent.truckowner.TruckOwnerNavigationActivity;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.utils.Utils;
import com.movingrestaurent.webservicedetails.Urls;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Android on 5/16/2016.
 */
public class ActiveDeactiveFragment extends Fragment  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener,OnUpdateLocation {
    private static final int TIME_OUT = 5000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    double latitude = 0.0, longitude = 0.0;
    TextView tv_update_location,tv_done_location;
    String cityName="";
    //double latitude;
    String status="0";
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String fdf="";








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.active_deactive, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Activ/Deactive");
        tv_update_location=(TextView)view.findViewById(R.id.tv_update_location);
        tv_done_location=(TextView)view.findViewById(R.id.tv_done_location);

        radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        tv_done_location.setOnClickListener(this);
        tv_update_location.setOnClickListener(this);
        status=PreferenceConnector.readString(getActivity(),"status","0");
        if(status.equalsIgnoreCase("1")){

            RadioButton btn = (RadioButton) getView().findViewById(R.id.active);
            btn.setChecked(true);
        }

        else {
            RadioButton btn = (RadioButton) getView().findViewById(R.id.deactive);
            btn.setChecked(true);
        }




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                radioButton = (RadioButton) getView().findViewById(selectedId);

                if (radioButton.getText().equals("ACTIVE")) {
               //     Utils.saveData(getActivity(), PREF, IS_KMS, "KMS");
                    PreferenceConnector.writeString(getActivity(),"status","1");
                    status="1";
                } else {
                //    Utils.saveData(getActivity(), PREF, IS_KMS, "MILES");
                    PreferenceConnector.writeString(getActivity(),"status","0");
                    status="0";
                    showDialog(getActivity(), "Alert!","Would you like to deactive your location");
                  //  UpdateRestaurantLocation.onUpdateLocation(Urls.UPDATE_RESTAURANT_LOCATION, getActivity(), this, cityName, latitude + "", longitude + "", status);
                }
            }
        });

        setUpLocation();


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
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
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
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                //finish();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
            PreferenceConnector.writeString(getActivity(), "latitude", String.valueOf(latitude));
            PreferenceConnector.writeString(getActivity(), "longitude", String.valueOf(longitude));





        } else {

            Toast.makeText(getActivity(), "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
//            lblLocation
//                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }
    public void getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                Toast.makeText(getActivity(),"Enter address does not exist",Toast.LENGTH_SHORT).show();

            }else {
                Address location = address.get(0);
   latitude = location.getLatitude();
      longitude = location.getLongitude();

              //  RegistrationApi.onRegistration(Urls.REGISTRATION, this, etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), etPassword.getText().toString(), userType, imageStr,latitude+ "", longitude+"",etRestaurantName.getText().toString().trim(),etRestaurantDescription.getText().toString().trim(),etAddress.getText().toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_done_location:
                //UpdateRestaurantLocation.onUpdateLocation(Urls.UPDATE_RESTAURANT_LOCATION,getActivity(),this,cityName,latitude+"",longitude+"",status);

                break;
            case R.id.tv_update_location:
                getCityName(latitude,longitude);

                break;
        }
    }
    private void getCityName(double latitude,double longitude) {
        double lat=0.0,longi=0.0;


        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0){

            cityName=addresses.get(0).getLocality();

            if(status.equalsIgnoreCase("1")) {

                UpdateRestaurantLocation.onUpdateLocation(Urls.UPDATE_RESTAURANT_LOCATION, getActivity(), this, cityName, latitude + "", longitude + "", status);

            }else{
                Toast.makeText(getActivity(),"Please select active first",Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onSuccess(String msg) {

        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

        Fragment fragment = new RestaurantFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    @Override
    public void onFailure(String msg) {

    }
    public void showDialog(final Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateRestaurantLocation.onUpdateLocation(Urls.UPDATE_RESTAURANT_LOCATION, getActivity(), ActiveDeactiveFragment.this, "",  "",  "", "0");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }


}
