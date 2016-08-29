package com.movingrestaurent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationRecieve extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		WakeLocker.acquire(context);

		WakeLocker.release();
	}

}
