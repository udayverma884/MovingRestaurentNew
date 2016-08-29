package com.movingrestaurent.https;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.fragmentuser.ActiveDeactiveFragment;
import com.movingrestaurent.interfaces.OnLogin;
import com.movingrestaurent.interfaces.OnUpdateLocation;
import com.movingrestaurent.model.LoginBean;
import com.movingrestaurent.utils.PreferenceConnector;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 5/2/2016.
 */
public class UpdateRestaurantLocation {

    public static void onUpdateLocation(String URL, final Context context, ActiveDeactiveFragment fragment , String restaurantAddress, String latitude, String longitude, String status) {
        final OnUpdateLocation callback = (OnUpdateLocation) fragment;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Login");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();



        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("restaurant_address", restaurantAddress);
        params.put("user_id", PreferenceConnector.readString(context,"UserId",""));
        params.put("active", status);



        String log = params.toString();
        Log.d("Add Param", log);
        client.setTimeout(100000);
        client.post(context, URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                String msg="";
                Log.d("LOGIN res:",s);
                if(s.contains("<pre>")){
                    s = s.split("<pre>")[0];
                }
                Log.d("LOGIN res:",s);
                try {
                    JSONObject object = new JSONObject(s);
                    msg=   object.optString("msg");
                    if(object.optString("status").equals("1")){
                        //LoginBean bean = new GsonBuilder().create().fromJson(s, LoginBean.class);
                        callback.onSuccess(msg);
                    }
                    else{
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
