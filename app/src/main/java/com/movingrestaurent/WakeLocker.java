package com.movingrestaurent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {
	private static PowerManager.WakeLock wakeLock;

	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	public static void acquire(Context context) {
		if (wakeLock != null)
            synchronized (wakeLock) {
                // sanity check for null as this is a public method
                if (wakeLock != null) {
                   // Log.v(TAG, "Releasing wakelock");
                    try {
                        wakeLock.release();
                    } catch (Throwable th) {
                        // ignoring this exception, probably wakeLock was already released
                    }
                } else {
                    // should never happen during normal workflow
                    //Log.e(TAG, "Wakelock reference is null");
                }
            }

			//wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");
		wakeLock.acquire();
	}

	public static void release() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}
}
