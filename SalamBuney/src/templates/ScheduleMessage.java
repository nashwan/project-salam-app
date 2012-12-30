package templates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.salambuney.R;
import com.example.salambuney.SalaamDBProvider;
import com.example.salambuney.ScheduleMessageInformation;
import com.example.salambuney.SharedConfig;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleMessage extends Activity implements OnClickListener {

	TextView tvMessageOnSender;
	LinearLayout listPhoneNumbers;
	Button btnCancel, btnSend;
	TimePicker timePicker;

	private String smsTextToSent, smsNumber;
	String[] numbers;
	int totalNumbersToSent = 0;
	boolean messageSent;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_schedule);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790404")));
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

		tvMessageOnSender = (TextView) findViewById(R.id.tvMessageOnSchedule);
		listPhoneNumbers = (LinearLayout) findViewById(R.id.layoutPhoneNumbersSchedule);

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelMessageSchedule);
		btnSend = (Button) findViewById(R.id.btnSendMessageSchedule);
		btnCancel.setOnClickListener(this);
		btnSend.setOnClickListener(this);

		// time picker
		timePicker = (TimePicker) findViewById(R.id.timePickerOnSchedular);

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
				checkBox.setTextColor(Color.parseColor("#4f290b"));
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
			case R.id.btnSendMessageSchedule:

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

					Date newDate = new Date();

					int hours = timePicker.getCurrentHour();
					int currentHour = newDate.getHours();

					if (currentHour > hours)
						hours = currentHour - hours;
					else
						hours = hours - currentHour;

					int minutes = timePicker.getCurrentMinute();

					if (newDate.getMinutes() > minutes)
						minutes = newDate.getMinutes() - minutes;
					else
						minutes = minutes - newDate.getMinutes();

					int seconds = (hours * 60 * 60) + (minutes * 60)
							- newDate.getSeconds();

					Date timeToSentMessage = new Date();
					timeToSentMessage.setHours(timePicker.getCurrentHour());
					timeToSentMessage.setMinutes(timePicker.getCurrentMinute());

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"HH:mm:ss");
					String strDate = dateFormat.format(timeToSentMessage);

					Intent smsSchedular = new Intent(this,
							SMSScheduleReciever.class);
					smsSchedular.putExtra("numbers", numbers);
					smsSchedular.putExtra("message", smsTextToSent);
					smsSchedular.putExtra("time", strDate);
					smsSchedular.putExtra("totalNumbersToSent",
							totalNumbersToSent);

					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							this.getApplicationContext(),
							SharedConfig.ALARM_SMS_SCHEDULE_MESSAGE,
							smsSchedular, 0);

					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + (seconds * 1000),
							pendingIntent);

					// NOTIFICATION
					Intent intent = new Intent(this,
							ScheduleMessageInformation.class);
					intent.putExtra("numbers", numbers);
					intent.putExtra("message", smsTextToSent);
					intent.putExtra("time", strDate);
					intent.setAction(timeToSentMessage.toString());
					intent.putExtra("totalNumbersToSent", totalNumbersToSent);
					PendingIntent pendingIntentNotificationClick = PendingIntent
							.getActivity(this, 0, intent, 0);
					NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.ic_launcher, "V.Salaam - SMS Scheduled",
							System.currentTimeMillis());

					notification.setLatestEventInfo(this,
							"V.Salaam - SMS Schedule", "Time: "+strDate +", Message: "
									+ smsTextToSent,
							pendingIntentNotificationClick);

					notification.flags |= Notification.FLAG_NO_CLEAR;

					notification.defaults = Notification.DEFAULT_SOUND;
					notificationManager.notify(
							SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE,
							notification);

					Toast.makeText(this, "SMS Schdeuled", Toast.LENGTH_LONG)
							.show();
					finish();

				} else {
					Toast.makeText(this, "No contacts selected.",
							Toast.LENGTH_SHORT).show();

				}

				break;
			case R.id.btnCancelMessageSchedule:

				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());

			Toast.makeText(this, "Error occured in message schedule.",
					Toast.LENGTH_SHORT).show();
		}
	}
}