package com.movingrestaurent.https;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.fragmentuser.HomeFragmentUser;
import com.movingrestaurent.interfaces.OnFetchRestaurant;
import com.movingrestaurent.model.HomeScreenDataModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by divyanshingle on 31/10/15.
 */
public class FetchReataurant {
    public static void onFetchRestaurant(String URL, final Context context, HomeFragmentUser fragment , String latitude, String longitude) {

        final OnFetchRestaurant callback = (OnFetchRestaurant) fragment;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Fetching");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

       // params.put("loginUserId", new UserPreference().getUser(context).userId);
        params.put("latitude", latitude);
        params.put("longitude", longitude);


        String log = params.toString();
        Log.d("Add Param", log);
        client.setTimeout(100000);
        client.post(context, URL, params, new AsyncHttpResponseHandler() { @Override
        public void onSuccess(String s) {

            super.onSuccess(s);
            ArrayList<HomeScreenDataModel> restaurantList=new ArrayList<HomeScreenDataModel>();
            String msg="";
            if(s.contains("<pre>")){
                s = s.split("<pre>")[0];
            }
            Log.d("LOGIN res:",s);
            try {
                JSONObject object = new JSONObject(s);
                msg=object.optString("msg");
                if(object.optString("status").equals("1")){

                    JSONArray dataArray= object.optJSONArray("restaurant");

                    for (int i=0;i<dataArray.length();i++){
                        JSONObject obj=dataArray.optJSONObject(i);
                       String restaurantName= obj.optString("restaurant_name");
                       String restaurantLatitude= obj.optString("last_location_latitude");
                       String restaurantLongitude= obj.optString("last_location_longitude");
                        String restaurantDescription=obj.optString("restaurant_description");
                        String restaurantId=obj.optString("id");

                        HomeScreenDataModel homeScreenDataModel=new HomeScreenDataModel();
                        homeScreenDataModel.setLatitude(restaurantLatitude);
                        homeScreenDataModel.setLongitude(restaurantLongitude);
                        homeScreenDataModel.setRestaurantName(restaurantName);
                        homeScreenDataModel.setRestaurantDescription(restaurantDescription);
                        homeScreenDataModel.setId(restaurantId);
                        restaurantList.add(homeScreenDataModel);

                    }





                    callback.onSuccess(restaurantList);
                }else{
                    Toast.makeText(context, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    callback.onFailure(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onFailure(msg);
            }


        }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Log.d("LOGIN res fail:", s);
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                //callback.onFailure();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });

    }

}

