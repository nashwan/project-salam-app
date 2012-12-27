package com.example.salambuney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageSender extends Activity implements OnClickListener {

	ProgressDialog pd = null;

	TextView tvMessageOnSender;
	LinearLayout listPhoneNumbers;
	Button btnCancel, btnSend;

	private String smsTextToSent, smsNumber;
	String[] numbers;
	int totalNumbersToSent = 0;

	@Override
	public void finish() {

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("tab", 0);
		startActivity(intent);
		super.finish();

	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_sender);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));

		tvMessageOnSender = (TextView) findViewById(R.id.tvMessageOnSender);
		listPhoneNumbers = (LinearLayout) findViewById(R.id.layoutPhoneNumbers);

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelMessageSender);
		btnSend = (Button) findViewById(R.id.btnSendMessageSender);
		btnCancel.setOnClickListener(this);
		btnSend.setOnClickListener(this);

		try {

			// get the message
			Intent intent = getIntent();
			String message_id = intent.getStringExtra("message_id");

			// get the message to send
			Cursor cursor = managedQuery(
					SalaamDBProvider.CONTENT_URI_TEMPLATES,
					SalaamDBProvider.FROM_TEMPLATE_TABLE,
					" _id =" + message_id, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() != 0) {
				tvMessageOnSender.setText(cursor.getString(1));
			}

			Cursor cursor2 = managedQuery(SalaamDBProvider.CONTENT_URI_CONTACT,
					SalaamDBProvider.FROM_CONTACT_TABLE, null, null, null);

			startManagingCursor(cursor2);

			while (cursor2.moveToNext()) {

				CheckBox checkBox = new CheckBox(this);
				checkBox.setText(cursor2.getString(1) + " - "
						+ cursor2.getString(2));
				checkBox.setChecked(true);
				checkBox.setTag(cursor2.getString(2));
				checkBox.setTextColor(Color.parseColor("#032f5b"));
				checkBox.setBackgroundResource(R.drawable.template_row_default);
				listPhoneNumbers.addView(checkBox);

			}

		} catch (Exception ex) {
			System.out.println("Error: " + ex.toString());
		}

	}

	@Override
	public void onClick(View v) {

		try {

			switch (v.getId()) {
			case R.id.btnSendMessageSender:

				smsTextToSent = tvMessageOnSender.getText().toString();
				numbers = new String[listPhoneNumbers.getChildCount() - 1];
				totalNumbersToSent = 0;
				if (listPhoneNumbers.getChildCount() > 1) {
					for (int i = 1; i < listPhoneNumbers.getChildCount(); i++) {
						CheckBox childs = (CheckBox) listPhoneNumbers
								.getChildAt(i);

						if (childs.isChecked()) {
							numbers[i - 1] = childs.getTag().toString();
							totalNumbersToSent++;
						}
					}

				}

				if (totalNumbersToSent >= 1) {
					SenderService sender = new SenderService(this);
					sender.execute();
				} else {
					Toast.makeText(this, "No contacts selected.",
							Toast.LENGTH_SHORT).show();

				}

				break;
			case R.id.btnCancelMessageSender:
				Intent intentMain = new Intent(this, MainActivity.class);
				startActivity(intentMain);
				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());

			Toast.makeText(this, "Error occured in message sender.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean StoreSMS(String message, String mobile) {

		try {

			String smsNumber = mobile;
			String smsText = message;

			// write to sent messages
			ContentValues values = new ContentValues();
			values.put("address", smsNumber);
			values.put("body", smsText);
			getContentResolver()
					.insert(Uri.parse("content://sms/sent"), values);

			return true;
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(getBaseContext(),
					"Error occured while saving into sent messages",
					Toast.LENGTH_SHORT).show();

			return false;
		}
	}

	public boolean storeInHistory(String message) {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String strDate = dateFormat.format(date);

			ContentValues values = new ContentValues();
			values.put(SalaamDB.HISTORY_MESSAGE, message);
			values.put(SalaamDB.HISTORY_SENT_TIME, strDate);

			Uri uri = getContentResolver().insert(
					SalaamDBProvider.CONTENT_URI_HISTORY, values);
			return true;

		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(getBaseContext(),
					"Error occured while saving history", Toast.LENGTH_SHORT)
					.show();
			return false;

		}

	}

	private class SenderService extends AsyncTask<Void, Integer, Void> {

		Context ctx;

		public SenderService(Context inCtx) {
			ctx = inCtx;
		}

		@Override
		protected Void doInBackground(Void... params) {

			pd.show();

			try {

				for (int i = 0; i < totalNumbersToSent; i++) {

					sendMessage(smsTextToSent, numbers[i]);
				}

				pd.cancel();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			finish();
			super.onPostExecute(result);

		}

		protected void onPreExecute() {

			pd = ProgressDialog.show(ctx, "Message Sender", "In Progress...",
					true);
		}

		public boolean sendMessage(String message, String mobile) {

			smsNumber = mobile;
			smsTextToSent = message;

			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";
			try {

				PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0,
						new Intent(SENT), 0);

				PendingIntent deliveryPI = PendingIntent.getBroadcast(ctx, 0,
						new Intent(DELIVERED), 0);

				// ---when the SMS has been sent---
				registerReceiver(new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent arg1) {
						switch (getResultCode()) {
						case Activity.RESULT_OK:
							Toast.makeText(getBaseContext(), "SMS delivered",
									Toast.LENGTH_SHORT).show();
							
							StoreSMS(smsTextToSent, smsNumber);
							storeInHistory(smsTextToSent);
							
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							Toast.makeText(context, "Generic failure",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							Toast.makeText(context, "No service",
									Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							Toast.makeText(context, "Null PDU",
									Toast.LENGTH_SHORT).show();

							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							Toast.makeText(context, "Radio off",
									Toast.LENGTH_SHORT).show();

							break;

						}

					}

				}, new IntentFilter(SENT));

				// ---when the SMS delivery report is recieved--
				registerReceiver(new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent arg1) {
						switch (getResultCode()) {

						case Activity.RESULT_OK:
							Toast.makeText(getBaseContext(), "SMS delivered",
									Toast.LENGTH_SHORT).show();

							break;
						case Activity.RESULT_CANCELED:
							Toast.makeText(getBaseContext(),
									"SMS not delivered", Toast.LENGTH_SHORT)
									.show();

							break;
						}

					}

				}, new IntentFilter(DELIVERED));

				// send sms
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(smsNumber, null, smsTextToSent, sentPI,
						deliveryPI);

				return true;
			} catch (Exception ex) {
				Log.i("SALAAM", ex.toString());
				Toast.makeText(
						ctx,
						"Please try again. Sending message failed. Ensure proper settings.",
						Toast.LENGTH_SHORT).show();
				return false;
			}

		}

	}
}
