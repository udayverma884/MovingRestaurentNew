package com.movingrestaurent.fragmentuser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.movingrestaurent.R;
import com.movingrestaurent.RestaurentDetail;
import com.movingrestaurent.https.FetchReataurant;
import com.movingrestaurent.interfaces.OnFetchRestaurant;
import com.movingrestaurent.model.HomeScreenDataModel;
import com.movingrestaurent.model.UserMapModel;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.webservicedetails.Urls;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android on 5/16/2016.
 */
public class HomeFragmentUser extends Fragment implements View.OnClickListener, OnMapReadyCallback, OnFetchRestaurant {
    private SupportMapFragment mGoogleMapFragment;
    private GoogleMap mGoogleMap;
    private static final String MAP_FRG_TAG = "map_frg_tag";
    TextView tvList, tvMap;
    LinearLayout llFragment;
    ArrayList<UserMapModel> userMapList;
    private Marker marker;
    private HashMap<Marker, UserMapModel> resourcearray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.home_fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        tvList = (TextView) view.findViewById(R.id.tv_list);
        tvMap = (TextView) view.findViewById(R.id.tv_map);
        tvList.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        tvList.setBackgroundResource(R.drawable.rounded_corner4);
        tvMap.setBackgroundResource(R.drawable.rounded_corner3);
        llFragment = (LinearLayout) view.findViewById(R.id.ll_fragment);

        llFragment.setVisibility(View.VISIBLE);

        tvMap.setTextColor(Color.WHITE);
        tvList.setTextColor(Color.parseColor("#3AC1E2"));
        resourcearray = new HashMap<Marker, UserMapModel>();
        userMapList = new ArrayList<UserMapModel>();


        mGoogleMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentByTag(MAP_FRG_TAG);


        if (mGoogleMapFragment == null) {
            mGoogleMapFragment = SupportMapFragment.newInstance();
            mGoogleMapFragment.getMapAsync(this);
        }

        if (!mGoogleMapFragment.isAdded()) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fl_map_container, mGoogleMapFragment, MAP_FRG_TAG)
                    .commit();

        }
        // lvRestaurentList.setAdapter(new UserHomeAdapter(getActivity(),homeScreenUserList));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initMap();
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_list:
                tvList.setBackgroundResource(R.drawable.rounded_cornersnew);
                tvMap.setBackgroundResource(R.drawable.rounded_corners2);


                tvList.setTextColor(Color.WHITE);
                tvMap.setTextColor(Color.parseColor("#3AC1E2"));
                llFragment.setVisibility(View.GONE);


                break;
            case R.id.tv_map:
                tvList.setBackgroundResource(R.drawable.rounded_corner4);
                tvMap.setBackgroundResource(R.drawable.rounded_corner3);

                tvMap.setTextColor(Color.WHITE);
                tvList.setTextColor(Color.parseColor("#3AC1E2"));
                llFragment.setVisibility(View.VISIBLE);

                break;
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void initMap() {
        mGoogleMap = mGoogleMapFragment.getMap();
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            mGoogleMap.setMyLocationEnabled(true);
            UiSettings uiSettings = mGoogleMap.getUiSettings();
            uiSettings.setAllGesturesEnabled(false);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            //uiSettings.setZoomControlsEnabled(true);
            //uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setRotateGesturesEnabled(true);
            String lat = PreferenceConnector.readString(getActivity(), "latitude", "");
            String longi = PreferenceConnector.readString(getActivity(), "longitude", "");


            FetchReataurant.onFetchRestaurant(Urls.FETCH_RESTAURANT, getActivity(), this, lat, longi);

            // UserMapModel model = null;


            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();


                    //   UserMapModel model=userMapList.get(marker).getName();
                    return true;
                }
            });

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    UserMapModel mrp_bean = resourcearray.get(marker);
                    String restaurantName = mrp_bean.getName();
                    String latitude = mrp_bean.getLat();
                    String longitude = mrp_bean.getLongi();
                    String description = mrp_bean.getDescription();
                    String restaurant_id = mrp_bean.getId();


                    Intent intent = new Intent(getActivity(), RestaurentDetail.class);
                    intent.putExtra("restaurantName", restaurantName);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("description", description);
                    intent.putExtra("restaurant_id", restaurant_id);
                    startActivity(intent);

                }
            });

           /* mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.custom_info_window, null, false);
                    return view;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });*/
        }

    }

    @Override
    public void onSuccess(ArrayList<HomeScreenDataModel> restaurantList) {
        UserMapModel model = null;
        for (int i = 0; i < restaurantList.size(); i++) {
            model = new UserMapModel();
            String name = restaurantList.get(i).getRestaurantName();
            String latitude = restaurantList.get(i).getLatitude();
            String longitude = restaurantList.get(i).getLongitude();
            String description = restaurantList.get(i).getRestaurantDescription();
            String id = restaurantList.get(i).getId();
            model.setLat(latitude);
            model.setLongi(longitude);
            model.setName(name);
            model.setDescription(description);
            model.setId(id);
            userMapList.add(model);


            //   userMapList.add(model);
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(userMapList.get(i).getLat()), Double.parseDouble(userMapList.get(i).getLongi())
                    )));


            try {
                resourcearray.put(marker, model);
                mGoogleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            } catch (Exception e) {

            }

        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(userMapList.get(0).getLat()), Double.parseDouble(userMapList.get(0).getLongi())), 12.0f));
    }


    @Override
    public void onFailure(String msg) {

    }

    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        public MarkerInfoWindowAdapter() {

        }


        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);


            UserMapModel mrp_bean = resourcearray.get(marker);


            TextView markerLabel = (TextView) v.findViewById(R.id.tvName);


            try {
                if (mrp_bean.equals(null)) {

                } else {
                    if (mrp_bean.getName().equals(null)) {
                        markerLabel.setText("No Restaurent");
                    } else {
                        markerLabel.setText(mrp_bean.getName());


                    }
                }

            } catch (Exception e) {

            }


            return v;
        }
    }
}
