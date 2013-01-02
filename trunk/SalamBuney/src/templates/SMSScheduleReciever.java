package templates;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.salambuney.MessageScheduleReadyToSend;
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
import android.os.CountDownTimer;
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

			Intent intentPopUp = new Intent(_context,
					MessageScheduleReadyToSend.class);
			intentPopUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			intentPopUp.putExtra("numbers", numbers);
			intentPopUp.putExtra("time", time);
			intentPopUp.putExtra("message", smsTextToSent);
			intentPopUp.putExtra("totalNumbersToSent", totalNumbersToSent);
			_context.startActivity(intentPopUp);

		} else {
			Toast.makeText(_context, "No contacts were setup.",
					Toast.LENGTH_SHORT).show();

		}

	}


}
