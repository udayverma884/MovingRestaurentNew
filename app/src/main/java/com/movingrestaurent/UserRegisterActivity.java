package com.movingrestaurent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;

import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.movingrestaurent.https.RegistrationApi;
import com.movingrestaurent.interfaces.OnRegistration;
import com.movingrestaurent.model.RegistrationBean;
import com.movingrestaurent.truckowner.TruckOwnerNavigationActivity;
import com.movingrestaurent.utils.PreferenceConnector;
import com.movingrestaurent.utils.Utils;
import com.movingrestaurent.webservicedetails.Urls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegisterActivity extends Activity implements View.OnClickListener,OnRegistration {
    private TextView tvDoneRegistration;
    private EditText etFirstName,etLastName,etMobile,etEmail,etPassword,etConfirmPassword,etRestaurantName,etRestaurantDescription,etAddress;
    String userType="";
    CircleImageView profilePic;
    private Uri outputFileUri;
    int YOUR_SELECT_PICTURE_REQUEST_CODE = 3;
    private Bitmap mPic;
    private boolean isImageUploded = true;
    String profileStr="";
    private PopupWindow popup;
    String imageStr="";
    ImageView ivBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        tvDoneRegistration=(TextView)findViewById(R.id.done_user_registration);

        etFirstName=(EditText)findViewById(R.id.first_name_reg_user) ;
        etLastName=(EditText)findViewById(R.id.last_name_reg_user) ;
        etMobile=(EditText)findViewById(R.id.mobile_reg_user) ;
        etEmail=(EditText)findViewById(R.id.email_reg_user) ;
        etPassword=(EditText)findViewById(R.id.password_reg_user) ;
        etConfirmPassword=(EditText)findViewById(R.id.confirm_pass_reg_user) ;
        profilePic=(CircleImageView)findViewById(R.id.profile);
        ivBack=(ImageView)findViewById(R.id.iv_back_registration);
        etRestaurantName=(EditText)findViewById(R.id.et_restaurant_name);
        etRestaurantDescription=(EditText)findViewById(R.id.et_restaurant_description);
        etAddress=(EditText)findViewById(R.id.et_restaurant_address);

        userType=getIntent().getStringExtra("userType");
        profilePic.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        tvDoneRegistration.setOnClickListener(this);

        if(userType.equalsIgnoreCase("user")){
            etRestaurantName.setVisibility(View.GONE);
            etRestaurantDescription.setVisibility(View.GONE);
        }else{
            etRestaurantName.setVisibility(View.VISIBLE);
            etRestaurantDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back_registration:
                finish();
                break;
            case R.id.profile:
                editImageWindow(v);
              //  openImageIntent();
                break;
            case R.id.takePhotoTV:
                popup.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(
                        android.os.Environment.getExternalStorageDirectory(),
                        "Things2Do_Image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
                break;
            case R.id.galleryTV:
                popup.dismiss();
                if (isSdPresent()) {
                    Intent intent1 = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 2);
                } else {
                }
                break;

            case R.id.cancelTV:
                popup.dismiss();
                break;

            case R.id.done_user_registration:


if(etFirstName.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
}else if(etLastName.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
}else if(userType.equalsIgnoreCase("restaurant")&&etRestaurantName.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter Restaurant Name", Toast.LENGTH_SHORT).show();
}else if(userType.equalsIgnoreCase("restaurant")&&etRestaurantDescription.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter About Restaurant", Toast.LENGTH_SHORT).show();
}else if(etAddress.getText().toString().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter Address", Toast.LENGTH_SHORT).show();
}
else if(etMobile.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter Contact Number", Toast.LENGTH_SHORT).show();
}else if(etEmail.getText().toString().trim().isEmpty()){
    Toast.makeText(UserRegisterActivity.this, "Please enter Email Address", Toast.LENGTH_SHORT).show();
}else if(!(Utils.isValidEmail(etEmail.getText().toString().trim()))){
    Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
}

else if(etPassword.getText().toString().trim().isEmpty()){
    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();}
else if(etConfirmPassword.getText().toString().trim().isEmpty()){
    Toast.makeText(this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
} else if(!etPassword.getText().toString().trim().equalsIgnoreCase(etConfirmPassword.getText().toString().trim())){
    Toast.makeText(this, "Password and Confirm Password Should be Same", Toast.LENGTH_SHORT).show();
}else{

   // RegistrationApi.onRegistration(Urls.REGISTRATION,this,etFirstName.getText().toString(),etLastName.getText().toString(),etEmail.getText().toString(),etMobile.getText().toString(),etPassword.getText().toString(),userType,imageStr,"","","","","");

    RegistrationApi.onRegistration(Urls.REGISTRATION, this, etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), etPassword.getText().toString(), userType, imageStr,"", "",etRestaurantName.getText().toString().trim(),etRestaurantDescription.getText().toString().trim(),etAddress.getText().toString().trim());

    /*  if(userType.equalsIgnoreCase("restaurant")){
        getLocationFromAddress(etAddress.getText().toString());
    }else{
        RegistrationApi.onRegistration(Urls.REGISTRATION,this,etFirstName.getText().toString(),etLastName.getText().toString(),etEmail.getText().toString(),etMobile.getText().toString(),etPassword.getText().toString(),userType,imageStr,"","","","","");
    }*/




}
              /*  Intent intent=new Intent(this,NavigatiomDrawerActivity.class);
                startActivity(intent);
                finish();*/
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
            Intent intent=new Intent(this, TruckOwnerNavigationActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
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

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "user" + File.separator);
        root.mkdirs();

        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        isImageUploded = false;
        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("Things2Do_Image.jpg")) {
                        f = temp;
                        break;
                    }
                }
                cropCapturedImage(Uri.fromFile(f));
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                File f = new File(picturePath);
                // For reposition
                cropCapturedImage(Uri.fromFile(f));

            } else if (requestCode == 3) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                try {

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    byte[] ba = bao.toByteArray();
                    imageStr = com.movingrestaurent.Base64.encode(ba);
                   // imageStr = resizeBase64Image(imageStr);
                    // imageStr = com.things2do.utils.Base64.encodeBytes(ba);
                    // facebookProPic = "";
                    profilePic.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cropCapturedImage(Uri picUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 0);
        cropIntent.putExtra("aspectY", 0);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 3);

    }

    private Bitmap getBitmap(String filePath) {
        File image = new File(filePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    /**
     * Rotate an image if required.
     *
     * @param img
     * @param selectedImage
     * @return
     */
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation = 0;
        try {
            rotation = getRotation(selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        } else {
            return img;
        }
    }

    /**
     * Get the rotation of the last image added.
     *
     * @param selectedImage
     * @return
     */
    private static int getRotation(Uri selectedImage) throws IOException {
        int rotation = 0;

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        rotation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        int orie = 0;
        switch (rotation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orie = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orie = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                orie = 270;
                break;
        }

        return orie;
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void editImageWindow(View view) {
        popup = new PopupWindow(UserRegisterActivity.this);
        View layout = getLayoutInflater().inflate(R.layout.edit_image_popup,
                null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        // Closes the popup window when touch outside of it - when looses
        // focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popup.showAsDropDown(view);

        TextView takePhotoTV = (TextView) layout.findViewById(R.id.takePhotoTV);
        takePhotoTV.setOnClickListener(this);

        TextView galleryTV = (TextView) layout.findViewById(R.id.galleryTV);
        galleryTV.setOnClickListener(this);

        TextView avatarTV = (TextView) layout.findViewById(R.id.cancelTV);

        avatarTV.setOnClickListener(this);
    }
    public static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
    public void getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                Toast.makeText(this,"Enter address does not exist",Toast.LENGTH_SHORT).show();

            }else {
                Address location = address.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                RegistrationApi.onRegistration(Urls.REGISTRATION, this, etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), etPassword.getText().toString(), userType, imageStr,latitude+ "", longitude+"",etRestaurantName.getText().toString().trim(),etRestaurantDescription.getText().toString().trim(),etAddress.getText().toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
