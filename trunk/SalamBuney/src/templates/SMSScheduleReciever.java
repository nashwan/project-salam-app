package templates;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;
import com.example.salambuney.SchedularMessageSent;
import com.example.salambuney.SharedConfig;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSScheduleReciever extends BroadcastReceiver {

	Context _context;
	private String smsNumber;
	private String smsTextToSent;
	private int totalNumbersToSent = 0;
	private String[] numbers;
	private String time; 

	@Override
	public void onReceive(Context context, Intent intent) {

		_context = context;
		numbers = intent.getStringArrayExtra("numbers");
		smsTextToSent = intent.getStringExtra("message");
		time = intent.getStringExtra("time");
		totalNumbersToSent = intent.getIntExtra("totalNumbersToSent", 0);


		if (totalNumbersToSent >= 1) {

			SendScheduleService sender = new SendScheduleService(_context);
			sender.execute();
		} else {
			Toast.makeText(_context, "No contacts were setup.",
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
			_context.getContentResolver().insert(
					Uri.parse("content://sms/sent"), values);

			return true;
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(_context,
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

			_context.getContentResolver().insert(
					SalaamDBProvider.CONTENT_URI_HISTORY, values);
			return true;

		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(_context, "Error occured while saving history",
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

			
			NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager
					.cancel(SharedConfig.NOTIFICATION_SMS_SCHEDULE_MESSAGE);

			
			Intent intentPopUp = new Intent(_context,
					SchedularMessageSent.class);
			intentPopUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intentPopUp.putExtra("numbers", numbers);
			intentPopUp.putExtra("time", time);
			intentPopUp.putExtra("message", smsTextToSent);
			intentPopUp.putExtra("totalNumbersToSent", totalNumbersToSent);

			_context.startActivity(intentPopUp);

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
