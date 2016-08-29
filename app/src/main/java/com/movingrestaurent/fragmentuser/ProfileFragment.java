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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.movingrestaurent.NavigatiomDrawerActivity;
import com.movingrestaurent.R;
import com.movingrestaurent.RestaurentDetail;
import com.movingrestaurent.https.FetchReataurant;
import com.movingrestaurent.https.UpdateProfile;
import com.movingrestaurent.interfaces.OnFetchRestaurant;
import com.movingrestaurent.interfaces.OnRegistration;
import com.movingrestaurent.model.HomeScreenDataModel;
import com.movingrestaurent.model.RegistrationBean;
import com.movingrestaurent.model.UserMapModel;
import com.movingrestaurent.truckowner.TruckOwnerNavigationActivity;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.utils.Utils;
import com.movingrestaurent.webservicedetails.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android on 5/16/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener,OnRegistration {


    CircleImageView profilePic;
    EditText first_name_edit_user, last_name_edit_user, et_restaurant_name_edit, et_restaurant_description_edit, et_restaurant_address_edit,
            mobile_edit_user, email_edit_user;
    TextView done_user_edit;
    String userType = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

        profilePic = (CircleImageView) view.findViewById(R.id.profile_edit);
        first_name_edit_user = (EditText) view.findViewById(R.id.first_name_edit_user);
        last_name_edit_user = (EditText) view.findViewById(R.id.last_name_edit_user);
        et_restaurant_name_edit = (EditText) view.findViewById(R.id.et_restaurant_name_edit);
        et_restaurant_description_edit = (EditText) view.findViewById(R.id.et_restaurant_description_edit);
        et_restaurant_address_edit = (EditText) view.findViewById(R.id.et_restaurant_address_edit);
        mobile_edit_user = (EditText) view.findViewById(R.id.mobile_edit_user);
        email_edit_user = (EditText) view.findViewById(R.id.email_edit_user);

        done_user_edit = (TextView) view.findViewById(R.id.done_user_edit);
        done_user_edit.setOnClickListener(this);

        userType = PreferenceConnector.readString(getActivity(), "userType", "");
        first_name_edit_user.setText(PreferenceConnector.readString(getActivity(),"firstName",""));
        last_name_edit_user.setText(PreferenceConnector.readString(getActivity(),"lastName",""));
        et_restaurant_name_edit.setText(PreferenceConnector.readString(getActivity(),"restaurantName",""));
        et_restaurant_description_edit.setText(PreferenceConnector.readString(getActivity(),"restaurantDescription",""));
        et_restaurant_address_edit.setText(PreferenceConnector.readString(getActivity(),"address",""));

        email_edit_user.setText(PreferenceConnector.readString(getActivity(),"email",""));
        mobile_edit_user.setText(PreferenceConnector.readString(getActivity(),"mobile",""));
        String dfdsf=PreferenceConnector.readString(getActivity(),"profile_pic","").toString();
if(PreferenceConnector.readString(getActivity(),"profile_pic","").toString().length()!=0)
{
    Picasso.with(getActivity()).load(PreferenceConnector.readString(getActivity(),"profile_pic","")).into(profilePic);
}else {


}


        if (userType.equalsIgnoreCase("restaurant")) {
            et_restaurant_name_edit.setVisibility(View.VISIBLE);
            et_restaurant_description_edit.setVisibility(View.VISIBLE);

        } else {
            et_restaurant_name_edit.setVisibility(View.GONE);
            et_restaurant_description_edit.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.done_user_edit:


                if(first_name_edit_user.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter first name", Toast.LENGTH_SHORT).show();
                }else if(last_name_edit_user.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter last name", Toast.LENGTH_SHORT).show();
                }else if(userType.equalsIgnoreCase("restaurant")&&et_restaurant_name_edit.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Restaurant Name", Toast.LENGTH_SHORT).show();
                }else if(userType.equalsIgnoreCase("restaurant")&&et_restaurant_description_edit.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter About Restaurant", Toast.LENGTH_SHORT).show();
                }else if(et_restaurant_address_edit.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_SHORT).show();
                }
                else if(mobile_edit_user.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Contact Number", Toast.LENGTH_SHORT).show();
                }else if(email_edit_user.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Email Address", Toast.LENGTH_SHORT).show();
                }else if(!(Utils.isValidEmail(email_edit_user.getText().toString().trim()))){
                    Toast.makeText(getActivity(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }else{
                    //http://appxtechnology.com/moving_restaurant/Webservice/index.php?action=userprofile&actionMethod=updateUserProfile&first_name=anu&last_name=pa&email=anpal@gmail.com&password=111111&device_type=android&device_id=aaaaaaagf&latitude=22.7196&longitude=75.857458&mobile=12345&profile_pic=imagehere&address=101-c,rajendra




                    UpdateProfile.onUpdateProfile(Urls.UPDATE_PROFILE,getActivity(),this,first_name_edit_user.getText().toString(),last_name_edit_user.getText().toString(),email_edit_user.getText().toString(),mobile_edit_user.getText().toString(),userType,"",et_restaurant_name_edit.getText().toString(),  et_restaurant_description_edit.getText().toString(),et_restaurant_address_edit.getText().toString());


                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(RegistrationBean registrationBean) {

        String userId=registrationBean.user_id;
        String firstName=registrationBean.first_name;
        String lastName=registrationBean.last_name;
        String restaurantName=registrationBean.restaurant_name;
        String restaurantDescription=registrationBean.restaurant_description;
        String mobile=registrationBean.mobile;
        String userType=registrationBean.user_type;
        String address=registrationBean.address;
        String email=registrationBean.email;
        String profile_pic=registrationBean.userImage;

        PreferenceConnector.writeString(getActivity(),"UserId",userId);
        PreferenceConnector.writeString(getActivity(),"firstName",firstName);
        PreferenceConnector.writeString(getActivity(),"lastName",lastName);
        PreferenceConnector.writeString(getActivity(),"restaurantName",restaurantName);
        PreferenceConnector.writeString(getActivity(),"restaurantDescription",restaurantDescription);
        PreferenceConnector.writeString(getActivity(),"mobile",mobile);
        PreferenceConnector.writeString(getActivity(),"userType",userType);
        PreferenceConnector.writeString(getActivity(),"address",address);
        PreferenceConnector.writeString(getActivity(),"email",email);
        PreferenceConnector.writeString(getActivity(),"profile_pic",profile_pic);

        if(userType.equalsIgnoreCase("restaurant")){
            Fragment fragment = new RestaurantFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }else{
            Fragment fragment = new HomeFragmentUser();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }





        Toast.makeText(getActivity(),"Profile Update Successfully",Toast.LENGTH_SHORT).show();

      /*  if(userType.equalsIgnoreCase("restaurant")){
            Intent intent=new Intent(this, TruckOwnerNavigationActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this, NavigatiomDrawerActivity.class);
            startActivity(intent);
            finish();
        }
*/

    }

    @Override
    public void onFailure(String msg) {

    }
}
