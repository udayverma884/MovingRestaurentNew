package com.movingrestaurent.fragmentuser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.R;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.webservicedetails.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EnterRestaurantMenu extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Spinner spinner;
    EditText et_enter_menu_name,et_menu_price,et_menu_description;
    TextView done_enter_menu;
    String category="";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.enter_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Menu");

        et_enter_menu_name=(EditText)view.findViewById(R.id.et_enter_menu_name);
        et_menu_price=(EditText)view.findViewById(R.id.et_menu_price);
        et_menu_description=(EditText)view.findViewById(R.id.et_menu_description);
        done_enter_menu=(TextView)view.findViewById(R.id.done_enter_menu);

        spinner=(Spinner)view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        done_enter_menu.setOnClickListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Please Select Category");
        categories.add("Chinese");
        categories.add("Indian Food");
        categories.add("Tea");
        categories.add("South Indian");
        categories.add("Fast Food");


        // Creating adapter for spinner
      /*  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.done_enter_menu:

                if (category.length()==0){
                    Toast.makeText(getActivity(), "Please Enter Category", Toast.LENGTH_SHORT).show();
                }else if(category.equalsIgnoreCase("Please Select Category"))
                    Toast.makeText(getActivity(), "Please Enter Category", Toast.LENGTH_SHORT).show();
                else if(et_enter_menu_name.getText().toString().trim().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            }  else if(et_menu_price.getText().toString().trim().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }  else if(et_menu_description.getText().toString().trim().length()==0){
                    Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }else {

                    onEnterMenu(Urls.ADD_MENU,getActivity(),this,category,et_enter_menu_name.getText().toString(),et_menu_price.getText().toString(),et_menu_description.getText().toString());
                }
                break;


            default:
                break;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        category = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onEnterMenu(String URL, final Context context, EnterRestaurantMenu fragment, String category, String name, String price, String description) {
        // final OnAddUpdateMenu callback = (OnAddUpdateMenu) fragment;
        // final OnUpdateLocation callback=(OnUpdateLocation)fragment;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Adding");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();



        params.put("price", price);
        params.put("name", name);
        params.put("category", category);
        params.put("user_id", PreferenceConnector.readString(context,"UserId",""));
        params.put("description", description);



        // http://appxtechnology.com/moving_restaurant/Webservice/index.php?action=userprofile&actionMethod=menu_item_restaurant&user_id=6&category=fruit&name=apple&price=120&description=Best friut

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
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        Fragment fragment = new RestaurantFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                        //  LoginBean bean = new GsonBuilder().create().fromJson(s, LoginBean.class);
                        // callback.onSuccess(msg);
                    }
                    else{
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        //   callback.onFailure(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  callback.onFailure(msg);
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
