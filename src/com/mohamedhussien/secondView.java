package com.mohamedhussien;

import java.util.StringTokenizer;

import com.mohamedhussien.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import static com.mohamedhussien.HelpActivity.message;
import static com.mohamedhussien.HelpActivity.phoneNo;
import static com.mohamedhussien.HelpActivity.locations;

public class secondView extends Activity {
	AlertDialog alert;
	Context c = this;
	Drawable drawable;
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.secondview);

		ImageButton send = (ImageButton) findViewById(R.id.imageButton1);
		send.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				if (phoneNo.length() > 0 && message.trim().length() > 0) {
					StringTokenizer st = new StringTokenizer(phoneNo, ",");
					while (st.hasMoreElements()) {
						String tempMobileNumber = (String) st.nextElement();
							if (locations == null) {
								sendSMS(tempMobileNumber, message);
							} else {
								if(locations.length() < 60){
								sendSMS(tempMobileNumber, message +" "+ locations);
								}else{
								sendSMS(tempMobileNumber, message);
								sendSMS(tempMobileNumber, "my location is " + locations);
								}
							}
					}
				} else {
					drawable = c.getResources().getDrawable(R.drawable.caution);
					AlertDialog.Builder build = new AlertDialog.Builder(c);
					build.setIcon(drawable);
					build.setTitle("Caution");
	    			build.setMessage("Press Set to go back and enter your message and Contact's phone")
	    			.setPositiveButton("Set", new DialogInterface.OnClickListener() {		
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {		
	    					Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
	    					startActivity(intent);			
	    					
	    				}
	    			})
	    			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					dialog.cancel();
	    				}			
	    			});
	    			alert = build.create();
	    			alert.show();
	    			return false;
	    		}
				return false;
			}
		});

	}

	public void sendSMS(String phoneNumber, String message) {
		// TODO Auto-generated method stub
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(), "SMS delivered",
					// Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					// Toast.makeText(getBaseContext(), "SMS not delivered",
					// Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));
		try {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.getMessage().toString(),
					1).show();
		}
	}
}
