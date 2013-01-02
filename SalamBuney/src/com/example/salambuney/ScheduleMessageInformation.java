package com.example.salambuney;

import templates.SMSScheduleReciever;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleMessageInformation extends Activity implements OnClickListener{

	PopupWindow popUp;
	TextView tvMessage, tvTo, tvTime;
	Button btnCancel, btnKeep;

	private String smsNumber = "";
	private String smsTextToSent = "";
	private int totalNumbersToSent = 0;
	private String time = "";
	private String[] numbers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// POP UP SCREEN
		setContentView(R.layout.activity_mesage_sender_popup);
		Intent intent = getIntent();

		numbers = intent.getStringArrayExtra("numbers");
		smsTextToSent = intent.getStringExtra("message");
		time = intent.getStringExtra("time");
		totalNumbersToSent = intent.getIntExtra("totalNumbersToSent", 0);

		tvTime = (TextView) findViewById(R.id.tvTimeScheduled);
		tvTime.setText(time);

		tvMessage = (TextView) findViewById(R.id.tvMessageScheduled);
		tvMessage.setText(smsTextToSent);

		tvTo = (TextView) findViewById(R.id.tvRecipientScheduled);

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

		btnCancel = (Button) findViewById(R.id.btnCancelSchedule);
		btnKeep = (Button) findViewById(R.id.btnKeepSchedule);
		btnCancel.setOnClickListener(this);
		btnKeep.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnCancelSchedule:
			AlarmManager am = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, SMSScheduleReciever.class);
			PendingIntent sender = PendingIntent.getBroadcast(this,
					SharedConfig.ALARM_SMS_SCHEDULE_MESSAGE, intent, 0);
			am.cancel(sender);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager
					.cancel(SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE);
			
			Toast.makeText(this, "Scheduled salaam message cleared.", Toast.LENGTH_LONG).show();

			finish();
			break;
		case R.id.btnKeepSchedule:
			finish();
			break;
		}
	}
}