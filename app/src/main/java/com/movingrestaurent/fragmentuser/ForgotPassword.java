package com.movingrestaurent.fragmentuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.movingrestaurent.R;

/**
 * Created by Android on 5/16/2016.
 */
public class ForgotPassword extends Fragment implements View.OnClickListener {
    TextView tvForgotDone;
    EditText etForgotEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.my_menu_restaurant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etForgotEmail=(EditText)view.findViewById(R.id.et_forgot_email);
        tvForgotDone=(TextView) view.findViewById(R.id.done_forgot);
        tvForgotDone.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.done_forgot:
                break;
            default:
                break;
        }
    }
}
