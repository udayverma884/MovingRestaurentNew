package com.movingrestaurent.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movingrestaurent.R;
import com.movingrestaurent.model.MenuModel;

import java.util.ArrayList;

/**
 * Created by Android on 5/17/2016.
 */
public class UserHomeAdapter extends BaseAdapter {

    private ArrayList<MenuModel> homeScreenUserList = new ArrayList<MenuModel>();
    private LayoutInflater inflater;
    Context context;

    public UserHomeAdapter(FragmentActivity activity, ArrayList<MenuModel> homeScreenUserList) {

        this.homeScreenUserList = homeScreenUserList;
        this.context = activity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addItem(final String item) {

    }

    public void addSeparatorItem(final String item) {


    }

    @Override
    public int getCount() {
        return homeScreenUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolderAppointment;
        if (convertView == null) {

            viewHolderAppointment = new ViewHolder();
            convertView = inflater.inflate(R.layout.home_user_adapter, null);


            convertView.setTag(viewHolderAppointment);

        } else {
            viewHolderAppointment = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    public static class ViewHolder {

    }
}
