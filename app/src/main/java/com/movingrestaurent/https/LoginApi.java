package com.movingrestaurent.https;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.interfaces.OnLogin;
import com.movingrestaurent.model.LoginBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 5/2/2016.
 */
public class LoginApi {

    public static void onLogin(String URL, final Context context, String email, String password,String device_id) {
        final OnLogin callback = (OnLogin) context;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Login");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();



        params.put("email", email);
        params.put("password", password);
        params.put("device_type", "android");
        params.put("device_id", device_id);


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
                        LoginBean bean = new GsonBuilder().create().fromJson(s, LoginBean.class);
                        callback.onSuccess(bean);
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
