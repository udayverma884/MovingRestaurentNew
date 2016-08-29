package com.movingrestaurent;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SelectionActivity extends Activity implements View.OnClickListener {
    private TextView tvCustomer,tvTruckOwner;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        findViewIds();
    }

    private void findViewIds() {
        tvCustomer=(TextView)findViewById(R.id.tv_select_customer);
        tvTruckOwner=(TextView)findViewById(R.id.tv_truck_owner);

        tvCustomer.setOnClickListener(this);
        tvTruckOwner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_select_customer:
                intent=new Intent(this,LoginActivity.class);
                intent.putExtra("userType","user");
                startActivity(intent);

                break;
            case R.id.tv_truck_owner:
                intent=new Intent(this,LoginActivity.class);
                intent.putExtra("userType","restaurant");
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
