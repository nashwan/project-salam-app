package com.example.salambuney;

import java.text.SimpleDateFormat;
import java.util.Date;

import templates.SMSScheduleReciever;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MessageScheduleReadyToSend extends Activity implements
		OnClickListener {

	PopupWindow popUp;
	TextView tvMessage, tvTo, tvTime, tvCounter;
	Button btnCancel, btnSendImmediately;

	private CountDownTimer myTimer;
	private String smsNumber = "";
	private String smsTextToSent = "";
	private int totalNumbersToSent = 0;
	private String time = "";
	private String[] numbers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// POP UP SCREEN
		setContentView(R.layout.activity_mesage_sender_ready_to_send);
		Intent intent = getIntent();

		numbers = intent.getStringArrayExtra("numbers");
		smsTextToSent = intent.getStringExtra("message");
		time = intent.getStringExtra("time");
		totalNumbersToSent = intent.getIntExtra("totalNumbersToSent", 0);

		tvMessage = (TextView) findViewById(R.id.tvMessageScheduleReadyToSend);
		tvMessage.setText(smsTextToSent);

		tvCounter = (TextView) findViewById(R.id.tvCounterReadyToSend);
		tvTo = (TextView) findViewById(R.id.tvRecipientScheduleReadyToSend);
		tvTime = (TextView) findViewById(R.id.tvTimeScheduleReadyToSend);
		tvTime.setText(time);

		String numbersstr = "";
		boolean addedComma = false;
		for (int i = 0; i < numbers.length; i++) {

			if (numbers[i] != null) {
				if (addedComma == false)
					numbersstr = numbers[i];
				else {
					numbersstr += ", " + numbers[i];
					addedComma = true;
				}
			}
		}
		tvTo.setText(numbersstr);

		btnCancel = (Button) findViewById(R.id.btnCancelScheduleReadyToSend);
		btnSendImmediately = (Button) findViewById(R.id.btnSendImmediatelyScheduleReadyToSend);
		btnCancel.setOnClickListener(this);
		btnSendImmediately.setOnClickListener(this);

		myTimer = new CountDownTimer(30000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

				tvCounter
						.setText("If not cancelled this salaam message will be sent in  "
								+ millisUntilFinished / 1000 + " seconds.");
			}

			@Override
			public void onFinish() {

				SendScheduleService sender = new SendScheduleService(
						getApplicationContext());
				sender.execute();

				finish();
			}

		}.start();


		//vibrate
		Vibrator vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrate.vibrate(500);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnSendImmediatelyScheduleReadyToSend:

			SendScheduleService senderService = new SendScheduleService(
					getApplicationContext());
			senderService.execute();
			myTimer.cancel();
			finish();
			break;
		case R.id.btnCancelScheduleReadyToSend:

			myTimer.cancel();

			AlarmManager am = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, SMSScheduleReciever.class);
			PendingIntent sender = PendingIntent.getBroadcast(this,
					SharedConfig.ALARM_SMS_SCHEDULE_MESSAGE, intent, 0);
			am.cancel(sender);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager
					.cancel(SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE);

			Toast.makeText(this, "Scheduled salaam message cleared.",
					Toast.LENGTH_LONG).show();

			finish();
			break;
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
			Toast.makeText(this,
					"Error occured while saving into sent messages",
					Toast.LENGTH_SHORT).show();

			return false;
		}
	}

	public boolean storeInHistory(String message, String numbers) {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String strDate = dateFormat.format(date);

			ContentValues values = new ContentValues();
			values.put(SalaamDB.HISTORY_MESSAGE, message);
			values.put(SalaamDB.HISTORY_SENT_TIME, strDate);
			values.put(SalaamDB.HISTORY_CONATCT, numbers);

			getContentResolver().insert(SalaamDBProvider.CONTENT_URI_HISTORY,
					values);
			return true;

		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(this, "Error occured while saving history",
					Toast.LENGTH_SHORT).show();
			return false;

		}

	}

	private class SendScheduleService extends AsyncTask<Void, Integer, Void> {

		Context ctx;

		public SendScheduleService(Context inCtx) {
			ctx = inCtx;
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				boolean sentSuccessfully = false;
				for (int i = 0; i < numbers.length; i++) {

					if (numbers[i] != null)
						sentSuccessfully = sendMessage(smsTextToSent,
								numbers[i]);
				}

				String numbersstr = "";
				boolean firstOne = true;
				for (int i = 0; i < numbers.length; i++) {

					if (numbers[i] != null) {
						if (firstOne) {
							numbersstr = numbers[i];
							firstOne = false;
						} else {
							numbersstr += ", " + numbers[i];

						}
					}
				}
				if (sentSuccessfully) {

					storeInHistory(smsTextToSent, numbersstr);
					for (int i = 0; i < numbers.length; i++) {

						if (numbers[i] != null) {
							StoreSMS(smsTextToSent, numbers[i]);
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager
					.cancel(SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE);

			 //NOTIFICATION
			NotificationManager notificationManagerSent = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher,
					"SMS Sent - Varah Salaam", System.currentTimeMillis());
	
			notification.setLatestEventInfo(getBaseContext(), "SMS Sent - Varah Salaam",
					"Salaam SMS: "+smsTextToSent, null);
	
			notification.defaults = Notification.DEFAULT_SOUND;
			notificationManager.notify(
					SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE, notification);

		}

		public boolean sendMessage(String message, String mobile) {

			smsNumber = mobile;
			smsTextToSent = message;

			String SENT = "SMS_SENT";
			String DELIVERED = "SMS_DELIVERED";
			try {

				// send sms
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(smsNumber, null, smsTextToSent, null, null);

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
