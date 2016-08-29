package com.movingrestaurent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONException;
import org.json.JSONObject;


public class GCMIntentService extends GCMBaseIntentService {
    String message = "",text="";
    String type = "";
    Intent notificationIntent;
    private static final String TAG = "GCMIntentService";
    boolean background = false;

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {

        // Constants.resIdNotification = registrationId;
        CommonUtilities.displayMessage(context,
                "Your device registred with GCM");
        /*SharedPrefrnceThings2Do.setDataInSharedPrefrence(context,
				Constants.DEVICE_TOKEN, registrationId);*/

    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {

        CommonUtilities.displayMessage(context,
                getString(R.string.gcm_unregistered));

    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        // *******************

        // if (intent.getAction().equals("com.challo.DISPLAY_MESSAGE")) {
        Bundle bn = intent.getExtras();
        String  message = bn.getString("data");
        try {
            JSONObject jsonObject=new JSONObject(message);
              text=jsonObject.optString("text");
            String fhfh="vfdgdc";
        } catch (JSONException e) {
            e.printStackTrace();
        }



       // type = bn.getString("notification_type");
        // }
        WakeLocker.acquire(context);
        WakeLocker.release();
        CommonUtilities.displayMessage(context, text);
        sendNotification(text);
        // notifies user
        //generateNotification(context, message);
        // *************

    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {

        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user

    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {

        CommonUtilities.displayMessage(context,
                getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message

        CommonUtilities.displayMessage(context,
                getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }



    private void sendNotification(String message) {
        JSONObject jsonObject = null;
        Intent intent = null;
        String msg = message;
      /*  try {
            jsonObject = new JSONObject(message);
            msg = jsonObject.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       /* if (jsonObject != null)  {
            intent = new Intent(this, SplashScreen.class);


        }*/

        intent = new Intent(this, NavigatiomDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("SoberApp")
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    // Use like this:

}
