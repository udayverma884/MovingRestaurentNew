package com.movingrestaurent;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RestaurentDetail extends AppCompatActivity implements View.OnClickListener {

    String restaurantName="";
    String latitude="";
    String longitude="";
    String restaurantDescription="";
    TextView tvRestaurantName,tvRestaurantAddress,tvRestaurantDescription,menu;
    ImageView ivRestDetail;
    String restaurant_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_detail);
        menu=(TextView)findViewById(R.id.menu);
        tvRestaurantName=(TextView)findViewById(R.id.tv_restaurant_name);
        tvRestaurantAddress=(TextView)findViewById(R.id.tv_restaurant_address);
        tvRestaurantDescription=(TextView)findViewById(R.id.tv_restaurant_description);
        ivRestDetail=(ImageView)findViewById(R.id.iv_back_restaurant_detail);
        menu.setOnClickListener(this);
        ivRestDetail.setOnClickListener(this);

        restaurantName=getIntent().getStringExtra("restaurantName");
        latitude=getIntent().getStringExtra("latitude");
        longitude=getIntent().getStringExtra("longitude");
        restaurantDescription=getIntent().getStringExtra("description");
        restaurant_id=getIntent().getStringExtra("restaurant_id");
        getCityName(Double.parseDouble(latitude),Double.parseDouble(longitude));



        tvRestaurantName.setText(restaurantName);
        tvRestaurantDescription.setText(restaurantDescription);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_restaurant_detail:
                finish();

                break;
            case R.id.menu:
                Intent intent =new Intent(RestaurentDetail.this,MenuActivity.class);
                intent.putExtra("restaurant_id",restaurant_id);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
    private void getCityName(double latitude,double longitude) {
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
            tvRestaurantAddress.setText(cityName);


        }
    }
}
