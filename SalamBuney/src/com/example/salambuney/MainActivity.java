package com.example.salambuney;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		TextView tv_mobile_numer = (TextView) findViewById(R.id.tv_mobile_numer);
		tv_mobile_numer.setText("Mobile: "
				+ sharedPref.getString("mobile", "not set"));

		TextView tv_message = (TextView) findViewById(R.id.tv_message);
		tv_message.setText("Message: "
				+ sharedPref.getString("message", "not set"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_settings) {

			Intent intent = new Intent();
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);

		}

		return super.onOptionsItemSelected(item);
	}

	public void btnSendNowClick(View v) {

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		String smsNumber = sharedPref.getString("mobile", "not set");
		String smsText = sharedPref.getString("message", "not set");

		/*
		 * Uri uri = Uri.parse("smsto:" + smsNumber); Intent intent = new
		 * Intent(Intent.ACTION_SEND, uri); intent.putExtra("sms_body",
		 * smsText); startActivity(intent);
		 */

		String SENT = "SMS_SENT";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();					
					StoreSMS ();

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

		// send sms
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(smsNumber, null, smsText, sentPI, null);
		
	}
	
	public void StoreSMS ()
	{
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		String smsNumber = sharedPref.getString("mobile", "not set");
		String smsText = sharedPref.getString("message", "not set");
		
		// write to sent messages
		ContentValues values = new ContentValues();
		values.put("address", smsNumber);
		values.put("body", smsText);
		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
