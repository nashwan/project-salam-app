package com.example.salambuney;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class MainActivity extends Activity implements OnClickListener, OnLongClickListener {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));
		
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		// TextView tv_mobile_numer = (TextView)
		// findViewById(R.id.tv_mobile_numer);
		// tv_mobile_numer.setText("Mobile: "
		// + sharedPref.getString("mobile", "not set"));
		//
		// TextView tv_message = (TextView) findViewById(R.id.tv_message);
		// tv_message.setText("Message: "
		// + sharedPref.getString("message", "not set"));

		try {
			Cursor cursor = getData();
			showTemplates(cursor);

		} catch (Exception e) {
			// TODO: handle exception
		}
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
		
		if (item.getItemId() == R.id.menu_new_template) {

			Intent intent = new Intent();
			intent.setClass(this, NewSMSTemplate.class);
			startActivity(intent);

		}

		return super.onOptionsItemSelected(item);
	}

	// public void btnSendNowClick(View v) {
	//
	// SharedPreferences sharedPref = PreferenceManager
	// .getDefaultSharedPreferences(this);
	//
	// String smsNumber = sharedPref.getString("mobile", "not set");
	// String smsText = sharedPref.getString("message", "not set");
	//
	// /*
	// * Uri uri = Uri.parse("smsto:" + smsNumber); Intent intent = new
	// * Intent(Intent.ACTION_SEND, uri); intent.putExtra("sms_body",
	// * smsText); startActivity(intent);
	// */
	//
	// String SENT = "SMS_SENT";
	//
	// PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
	// SENT), 0);
	//
	// // ---when the SMS has been sent---
	// registerReceiver(new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context arg0, Intent arg1) {
	// switch (getResultCode()) {
	// case Activity.RESULT_OK:
	// Toast.makeText(getBaseContext(), "SMS sent",
	// Toast.LENGTH_SHORT).show();
	// StoreSMS();
	//
	// break;
	// case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	// Toast.makeText(getBaseContext(), "Generic failure",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case SmsManager.RESULT_ERROR_NO_SERVICE:
	// Toast.makeText(getBaseContext(), "No service",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case SmsManager.RESULT_ERROR_NULL_PDU:
	// Toast.makeText(getBaseContext(), "Null PDU",
	// Toast.LENGTH_SHORT).show();
	// break;
	// case SmsManager.RESULT_ERROR_RADIO_OFF:
	// Toast.makeText(getBaseContext(), "Radio off",
	// Toast.LENGTH_SHORT).show();
	// break;
	// }
	// }
	// }, new IntentFilter(SENT));
	//
	// // send sms
	// SmsManager sms = SmsManager.getDefault();
	// sms.sendTextMessage(smsNumber, null, smsText, sentPI, null);
	//
	// }
	//
	// public void StoreSMS() {
	// SharedPreferences sharedPref = PreferenceManager
	// .getDefaultSharedPreferences(this);
	//
	// String smsNumber = sharedPref.getString("mobile", "not set");
	// String smsText = sharedPref.getString("message", "not set");
	//
	// // write to sent messages
	// ContentValues values = new ContentValues();
	// values.put("address", smsNumber);
	// values.put("body", smsText);
	// getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	// }

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI, SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);
	}

	private void showTemplates(Cursor cursor) {

		TableLayout templatesTL = (TableLayout) findViewById(R.id.listTemplates);

		int count = 1;
		while (cursor.moveToNext()) {

			String message = cursor.getString(1);
			String message_id = cursor.getString(0);

			// create new row 
			TableRow tr =  new TableRow(this);
			tr.setTag(message_id);
			tr.setPadding(3, 2, 3, 2);
		
			tr.setClickable(true);
			tr.setOnClickListener(this);
			
			// set message styles
			TextView messageTV = (TextView) getLayoutInflater().inflate(
					R.layout.row_template, null);
			

			messageTV.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, 45, 1));

			messageTV.setTextSize(17);
			messageTV.setTypeface(Typeface.DEFAULT_BOLD);
			messageTV.setPadding(4, 8, 2, 4);
			messageTV.setGravity(Gravity.BOTTOM);
			//messageTV.setTextColor(Color.parseColor("#05407b"));

			//messageTV.setBackgroundColor(Color.parseColor("#c8e3fe"));
			messageTV.setText(message); // set message
			messageTV.setTag(message_id);
			messageTV.setOnClickListener(this);
			messageTV.setOnLongClickListener(this);
		
			tr.addView(messageTV); // sets message id to the row.
			templatesTL.addView(tr);
			count++;

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		
		default:
			if (v.getTag() != null) {
				Toast.makeText(this, "Attempting to send message.", Toast.LENGTH_SHORT).show();
				String id = (String) v.getTag();
				Cursor cursor = managedQuery(SalaamDBProvider.CONTENT_URI, SalaamDBProvider.FROM_TEMPLATE_TABLE, "_id = " + id, null, null);

				while (cursor.moveToNext()) {
					String message = cursor.getString(1);

					sentMessage(message);
				}

			}

		}

	}

	public boolean sentMessage(String message) {

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		String smsNumber = sharedPref.getString("mobile", "not set");
		String smsText = message;

		/*
		 * Uri uri = Uri.parse("smsto:" + smsNumber); Intent intent = new
		 * Intent(Intent.ACTION_SEND, uri); intent.putExtra("sms_body",
		 * smsText); startActivity(intent);
		 */

		String SENT = "SMS_SENT";

		try {

			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
					new Intent(SENT), 0);

			// ---when the SMS has been sent---
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent",
								Toast.LENGTH_SHORT).show();
						StoreSMS();

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

			return true;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			Toast.makeText(
					this,
					"Please try again. Sending message failed. MESSAGE : "
							+ message, Toast.LENGTH_SHORT).show();
			return false;
		}

	}

	public void StoreSMS() {

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
	

	@Override
	public boolean onLongClick(View v) {
		
		if (v.getTag() != null) {
			
			String id = (String) v.getTag();
			//start edit template intent
			Intent intent = new Intent(this, EditSMSTemplate.class);
			intent.putExtra("message_id", id);
			startActivity(intent);
			return true;

		}
		return false;
	}

}
