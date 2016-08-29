/*
package com.movingrestaurent.https;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.fragmentuser.ForgotPassword;
import com.movingrestaurent.interfaces.OnAddUpdateMenu;
import com.movingrestaurent.interfaces.OnRegistration;
import com.movingrestaurent.model.RegistrationBean;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordApi {
    public static void OnForgot(String URL, final Context context, ForgotPassword forgot, String email) {
        final OnAddUpdateMenu callback = (OnAddUpdateMenu) forgot;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Registration");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("email", email);


        String log = params.toString();
        Log.d("Add Param", log);
        client.setTimeout(100000);
        client.post(context, URL.trim(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                String msg = "";
                if (s.contains("<pre>")) {
                    s = s.split("<pre>")[0];
                }
                Log.d("LOGIN res:", s);
                try {
                    JSONObject object = new JSONObject(s);
                    msg = object.optString("msg");
                    if (object.optString("status").equals("1")) {
                        RegistrationBean bean = new GsonBuilder().create().fromJson(s, RegistrationBean.class);
                        callback.onSuccess(bean);
                    } else {
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
*/
