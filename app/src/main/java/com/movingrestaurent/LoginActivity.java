package com.movingrestaurent;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import com.movingrestaurent.https.LoginApi;
import com.movingrestaurent.interfaces.OnLogin;
import com.movingrestaurent.model.LoginBean;
import com.movingrestaurent.truckowner.TruckOwnerNavigationActivity;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.webservicedetails.Urls;


public class LoginActivity extends Activity implements OnClickListener,OnLogin {


    private EditText mEmailView;
    private EditText mPasswordView;

    private Button tvLogin, tvRegister;
    String userType;
    ImageView ivLoginBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        tvLogin = (Button) findViewById(R.id.email_sign_in_button);
        tvRegister = (Button) findViewById(R.id.email_sign_up_button);
        ivLoginBack=(ImageView)findViewById(R.id.iv_back_login);

        userType=getIntent().getStringExtra("userType");

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivLoginBack.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_login:
                finish();
                break;
            case R.id.email_sign_in_button:

                String email = mEmailView.getText().toString().trim();
                String password = mPasswordView.getText().toString().trim();
                if (email.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email First", Toast.LENGTH_LONG).show();

                } else if (!isEmailValid(email)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();

                } else if (password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password First", Toast.LENGTH_LONG).show();

                } else if (!isPasswordValid(password)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Minimum 4 Character Password", Toast.LENGTH_LONG).show();
                } else {
                    LoginApi.onLogin(Urls.LOGIN,this,email,password,"");
                }
                break;
            case R.id.email_sign_up_button:
                Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
                intent.putExtra("userType",userType);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(LoginBean loginBean) {
        String userId=loginBean.user_id;
        String firstName=loginBean.first_name;
        String lastName=loginBean.last_name;
        String restaurantName=loginBean.restaurant_name;
        String restaurantDescription=loginBean.restaurant_description;
        String mobile=loginBean.mobile;
        String userType=loginBean.user_type;
        String latitude=loginBean.latitude;
        String longitude=loginBean.longitude;
        String address=loginBean.address;
        String email=loginBean.email;
        String profile_pic=loginBean.userImage;


        PreferenceConnector.writeString(this,"UserId",userId);
        PreferenceConnector.writeString(this,"firstName",firstName);
        PreferenceConnector.writeString(this,"lastName",lastName);
        PreferenceConnector.writeString(this,"restaurantName",restaurantName);
        PreferenceConnector.writeString(this,"restaurantDescription",restaurantDescription);
        PreferenceConnector.writeString(this,"mobile",mobile);
        PreferenceConnector.writeString(this,"userType",userType);
        PreferenceConnector.writeString(this,"address",address);
        PreferenceConnector.writeString(this,"email",email);
        PreferenceConnector.writeString(this,"profile_pic",profile_pic);





        PreferenceConnector.writeBoolean(this,"isLogin",true);
        if(userType.equalsIgnoreCase("restaurant")){

            PreferenceConnector.writeString(this,"address",address);
            Intent intent=new Intent(this, TruckOwnerNavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{

            PreferenceConnector.writeString(this,"address",address);
            Intent intent=new Intent(this, NavigatiomDrawerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }





    }

    @Override
    public void onFailure(String msg) {
   Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();


    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

}

