package com.example.salambuney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class MessageOptionsDialogue extends DialogFragment implements OnItemClickListener{

	private String smsTextToSent;
	private Activity mainActivity;

	public static MessageOptionsDialogue newInstance(String messageId) {
		MessageOptionsDialogue pdf = new MessageOptionsDialogue();
		pdf.setStyle(STYLE_NO_TITLE, 0);
		Bundle bundle = new Bundle();
		bundle.putString("message_id", messageId);
		pdf.setArguments(bundle);
		return pdf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		super.onCreateView(inflater, container, savedInstanceState);

		String message_id = getArguments().getString("message_id");

		String[] cols = new String[] { "Send now", "Schedule", "Edit", "Delete" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1, cols);
		View v = inflater.inflate(R.layout.message_options_dialog, container,
				false);

		ListView list = (ListView) v.findViewById(R.id.listMessageOptions);
		list.setTag(message_id);
		list.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void dismiss() {
		
		super.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> adView, View target, int position,
			long id) {
		// TODO Auto-generated method stub
		dismiss();
		mainActivity = getActivity();
		
		if (adView.getTag().toString() != null) {

			String message_id = adView.getTag().toString();

			switch (position) {
			case 0: // Send Now

				Intent messageSender = new Intent(mainActivity, MessageSender.class);
				messageSender.putExtra("message_id", message_id);
				startActivity(messageSender);

//				Toast.makeText(mainActivity, "Attempting to send message.",
//						Toast.LENGTH_SHORT).show();
//
//				Cursor cursor = mainActivity.managedQuery(
//						SalaamDBProvider.CONTENT_URI_TEMPLATES,
//						SalaamDBProvider.FROM_TEMPLATE_TABLE, "_id = "
//								+ message_id, null, null);
//
//				while (cursor.moveToNext()) {
//
//					String message = cursor.getString(1);
//					sentMessage(message);
//
//				}
				
				break;
			case 1: // Schedule
				Toast.makeText(mainActivity, "Schedule not implemented",
						Toast.LENGTH_SHORT).show();
				break;
			case 2: // Edit

				Intent intent = new Intent(mainActivity, EditSMSTemplate.class);
				intent.putExtra("message_id", message_id);
				startActivity(intent);

				break;
			case 3: // Delete
				Toast.makeText(mainActivity, "Delete not implemented",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

	public boolean sentMessage(String message) {

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(mainActivity);

		String smsNumber = sharedPref.getString("mobile", "not set");
		String smsText = message;
		smsTextToSent = message;

		String SENT = "SMS_SENT";

		try {

			PendingIntent sentPI = PendingIntent.getBroadcast(mainActivity, 0,
					new Intent(SENT), 0);

			// ---when the SMS has been sent---
			mainActivity.registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(arg0, "SMS sent", Toast.LENGTH_SHORT)
								.show();
						StoreSMS(smsTextToSent);
						storeInHistory(smsTextToSent);
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(arg0, "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(mainActivity.getBaseContext(), "No service",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(mainActivity.getBaseContext(), "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(mainActivity.getBaseContext(), "Radio off",
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
			Log.i("SALAAM", ex.toString());
			Toast.makeText(
					mainActivity.getBaseContext(),
					"Please try again. Sending message failed. Ensure proper settings.", Toast.LENGTH_SHORT).show();
			return false;
		}

	}

	public boolean StoreSMS(String message) {

		try {
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(mainActivity);

			String smsNumber = sharedPref.getString("mobile", "not set");
			String smsText = message;

			// write to sent messages
			ContentValues values = new ContentValues();
			values.put("address", smsNumber);
			values.put("body", smsText);
			mainActivity.getContentResolver()
					.insert(Uri.parse("content://sms/sent"), values);

			return true;
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(mainActivity.getBaseContext(),
					"Error occured while saving into sent messages",
					Toast.LENGTH_SHORT).show();

			return false;
		}
	}

	public boolean storeInHistory(String message) {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String strDate = dateFormat.format(date);

			ContentValues values = new ContentValues();
			values.put(SalaamDB.HISTORY_MESSAGE, message);
			values.put(SalaamDB.HISTORY_SENT_TIME, strDate);
			
			Uri uri = mainActivity.getContentResolver().insert(SalaamDBProvider.CONTENT_URI_HISTORY, values);

			return false;

		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(mainActivity, "Error occured while saving history",
					Toast.LENGTH_SHORT).show();
			return true;

		}

	}
	

}
