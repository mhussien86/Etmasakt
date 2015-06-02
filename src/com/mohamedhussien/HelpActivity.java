package com.mohamedhussien;

import java.io.IOException;
import java.util.List;

import com.airpush.android.Airpush;
import com.mohamedhussien.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class HelpActivity extends Activity {
	/** Called when the activity is first created. */
	// private static final String TAG = "LocationActivity";
	Button sendLocation;
	LocationManager locationManager;
	Geocoder geocoder;
	public static String locations;

	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;
	static String phoneNo;
	static String message;
	CheckBox check;
	Drawable drawable;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Airpush(getApplicationContext(), "23718","1320977817777527943",true,true,true);
		setContentView(R.layout.main);
		btnSendSMS = (Button) findViewById(R.id.save_btn);
		txtPhoneNo = (EditText) findViewById(R.id.phone_numbers);
		txtMessage = (EditText) findViewById(R.id.message_txt);
		check = (CheckBox) findViewById(R.id.check_box);

		
		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	
				if (check.isChecked()) {
					if (HaveNetworkConnection() == true) {
						locationManager = (LocationManager) getApplicationContext()
								.getSystemService(LOCATION_SERVICE);
						geocoder = new Geocoder(getApplicationContext());
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000,10,locationlistener);
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10,locationlistener);
						Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
							phoneNo = txtPhoneNo.getText().toString();
							message = txtMessage.getText().toString();
							Intent intent = new Intent(getApplicationContext(),secondView.class);
							startActivity(intent);

							}else{
							showGPSDisabledAlertToUser();
							}
						if (location != null) {
							// Log.d(TAG, location.toString());
							locationlistener.onLocationChanged(location);
						}
					}else{
						internetConnectionSetting();
					}
				} else {
					phoneNo = txtPhoneNo.getText().toString();
					message = txtMessage.getText().toString();
					Intent intent = new Intent(getApplicationContext(),secondView.class);
					startActivity(intent);

				}

			}
		});
	}
	private void showGPSDisabledAlertToUser(){
		drawable = getApplicationContext().getResources().getDrawable(R.drawable.caution);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setIcon(drawable);
		alertDialogBuilder.setTitle("Caution");
		alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
		.setCancelable(false)
		.setPositiveButton("Enable it now",new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int id){
		Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(callGPSSettingIntent);
		}
		});
		alertDialogBuilder.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int id){
		dialog.cancel();
		}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
		}

	private void internetConnectionSetting(){
		drawable = getApplicationContext().getResources().getDrawable(R.drawable.caution);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setIcon(drawable);
		alertDialogBuilder.setTitle("Caution");
		alertDialogBuilder.setMessage("no connection in your device. Would you like to enable it?")
		.setCancelable(false)
		.setPositiveButton("Enable it now",new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int id){
		Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		startActivity(callGPSSettingIntent);
		}
		});
		alertDialogBuilder.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int id){
		dialog.cancel();
		}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
		}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		txtPhoneNo.setText(phoneNo);
		txtMessage.setText(message);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		txtPhoneNo.setText(phoneNo);
		txtMessage.setText(message);
	}
public void onBackPressed() {
	phoneNo = txtPhoneNo.getText().toString();
	message = txtMessage.getText().toString();
	finish();
	
};
	LocationListener locationlistener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			locations = "";

			try {
				// This gets a list of addresses
				List<Address> addresses = geocoder.getFromLocation(
						location.getLatitude(), location.getLongitude(), 10);
				for (Address address : addresses) {

					locations = locations + address.getAddressLine(0);
				}

			} catch (IOException e) {
				Log.e("LocateMe", "Could not get Geocoder data", e);
			}

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, Menu.FIRST, Menu.NONE, "Help")
				.setIcon(R.drawable.help)
				.setCheckable(true)
				.setOnMenuItemClickListener(
						new MenuItem.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {

								Intent intent = new Intent(HelpActivity.this,
										Help.class);
								startActivity(intent);

								return false;
							}
						});
		return super.onCreateOptionsMenu(menu);

	}

	private boolean HaveNetworkConnection() {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;
		getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					HaveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					HaveConnectedMobile = true;
		}
		return HaveConnectedWifi || HaveConnectedMobile;
	}
	
	

	
}
