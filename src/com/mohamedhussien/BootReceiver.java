package com.mohamedhussien;

import com.airpush.android.Airpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if (Integer.parseInt(VERSION.SDK) > 3) {
			new Airpush(arg0, "23718", "1320977817777527943", false, true, true);

		}
	}
}
