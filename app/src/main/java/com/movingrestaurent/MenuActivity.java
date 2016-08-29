package com.movingrestaurent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.movingrestaurent.fragmentuser.HomeFragmentUser;
import com.movingrestaurent.interfaces.OnFetchRestaurant;
import com.movingrestaurent.model.HomeScreenDataModel;
import com.movingrestaurent.model.MenuModel;
import com.movingrestaurent.webservicedetails.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeSet;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvMenu;
    ArrayList<MenuModel> homeScreenUserList;

    private  MyCustomAdapter mAdapter;
    ImageView ivBackMenu;
    String restaurant_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        lvMenu=(ListView)findViewById(R.id.lvMenu);
        ivBackMenu=(ImageView)findViewById(R.id.iv_back_menu);
        ivBackMenu.setOnClickListener(this);
        mAdapter = new MyCustomAdapter();
        restaurant_id=getIntent().getStringExtra("restaurant_id");
         onFetchMenu(Urls.FETCH_MENU,this,restaurant_id);
    /*    mAdapter.addSeparatorItem("Indian Food");

        for (int i = 1; i < 50; i++) {
            mAdapter.addItem("Name " + i,"Price "+i);

            if (i== 10) {
                mAdapter.addSeparatorItem("Chinees");
            }
        }
        lvMenu.setAdapter(mAdapter);*/


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back_menu:
                finish();
                break;
        }

    }

    private class MyCustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private ArrayList<String> price = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item, final String s) {
            mData.add(item);
            price.add(s);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);

            price.add(item);

            // save separator position
            mSeparatorsSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.item1, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        holder.tvPrice=(TextView)convertView.findViewById(R.id.price);

                        break;
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.item2, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                        holder.tvPrice=(TextView)convertView.findViewById(R.id.price);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            if(price.size()!=0){
           holder.tvPrice.setText(price.get(position));}

            return convertView;
        }

    }
    public static class ViewHolder {
        public TextView textView,tvPrice;
    }
    private void onFetchMenu(String URL, final Activity context, String userId) {

       // final OnFetchRestaurant callback = (OnFetchRestaurant) context;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Fetching");
        dialog.setMessage("Please Wait!!");
        dialog.show();
        dialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id",userId);



        String log = params.toString();
        Log.d("Add Param", log);
        client.setTimeout(100000);
        client.post(context, URL, params, new AsyncHttpResponseHandler() { @Override
        public void onSuccess(String s) {

            super.onSuccess(s);
            ArrayList<HomeScreenDataModel> restaurantList=new ArrayList<HomeScreenDataModel>();
            String msg="";
            if(s.contains("<pre>")){
                s = s.split("<pre>")[0];
            }
            Log.d("LOGIN res:",s);
            try {
                JSONObject object = new JSONObject(s);
                msg=object.optString("msg");
                if(object.optString("status").equals("1")){

                    JSONArray dataArray= object.optJSONArray("restaurant_item");
                    JSONObject jsonObject=dataArray.optJSONObject(0);
                    String menuCategoryFirst=jsonObject.optString("category");
                    mAdapter.addSeparatorItem(menuCategoryFirst);
                    String menuCategoryOld=menuCategoryFirst;
                    for (int i=0;i<dataArray.length();i++){
                        JSONObject obj=dataArray.optJSONObject(i);
                        String menuCategory= obj.optString("category");
                        String menuName= obj.optString("name");
                        String price= obj.optString("price");
                     //   menuCategoryOld
                       // String fdfmdkf=menuCategory
                        if (menuCategoryOld.equalsIgnoreCase(menuCategory)) {
                         //   menuCategoryOld=menuCategory;
                            // mAdapter.addSeparatorItem(menuCategory);
                        }
                        else{
                            menuCategoryOld=menuCategory;
                            mAdapter.addSeparatorItem(menuCategory);

                        }
                        String mystring = getResources().getString(R.string.Rs);

                        mAdapter.addItem(menuName,mystring+" "+price);



                        lvMenu.setAdapter(mAdapter);

                        /*for (int i = 1; i < 50; i++) {
                            mAdapter.addItem("Name " + i,"Price "+i);

                            if (i== 10) {
                                mAdapter.addSeparatorItem("Chinees");
                            }
                        }*/




                    }





                  //  callback.onSuccess(restaurantList);
                }else{
                    Toast.makeText(context, object.optString("msg"), Toast.LENGTH_SHORT).show();
                   // callback.onFailure(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
               // callback.onFailure(msg);
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
