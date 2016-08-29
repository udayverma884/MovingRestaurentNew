package com.movingrestaurent.interfaces;

import com.movingrestaurent.model.HomeScreenDataModel;
import com.movingrestaurent.model.RegistrationBean;

import java.util.ArrayList;

/**
 * Created by Android on 5/31/2016.
 */
public interface OnFetchRestaurant  {
    void onSuccess(ArrayList<HomeScreenDataModel> restaurantList);

    void onFailure(String msg);
}

