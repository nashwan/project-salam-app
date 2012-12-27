package com.example.salambuney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class Home extends Activity implements OnClickListener,
		OnDialogDoneListener {

	public String smsTextToSent;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templates);

		try {
			showTemplates(getData());
		} catch (Exception ex) {
			System.out.println("Error: " + ex.toString());
		}

	}

	private void showTemplates(Cursor cursor) {

		TableLayout templatesTL = (TableLayout) findViewById(R.id.listTemplates);

		while (cursor.moveToNext()) {

			String message = cursor.getString(1);
			String message_id = cursor.getString(0);

			// create new row
			TableRow tr = new TableRow(this);
			tr.setTag(message_id);
			tr.setPadding(5, 1, 5, 1);

			tr.setClickable(true);
			tr.setOnClickListener(this);

			// set message styles
			TextView messageTV = (TextView) getLayoutInflater().inflate(
					R.layout.row_template, null);

			messageTV.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));

			messageTV.setTextSize(17);
			messageTV.setTypeface(Typeface.DEFAULT_BOLD);
			messageTV.setPadding(4, 4, 2, 4);
			messageTV.setGravity(Gravity.TOP);
			// messageTV.setTextColor(Color.parseColor("#05407b"));

			// messageTV.setBackgroundColor(Color.parseColor("#c8e3fe"));
			messageTV.setText(message); // set message
			messageTV.setTag(message_id);
			messageTV.setOnClickListener(this);

			tr.addView(messageTV); // sets message id to the row.
			templatesTL.addView(tr);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getTag() != null) {

			MessageOptionsDialogue mdf = MessageOptionsDialogue.newInstance(v
					.getTag().toString());
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			mdf.show(ft, "my-dialog-tag");
		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_TEMPLATES,
				SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);
	}

	@Override
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
		// TODO Auto-generated method stub
		try {

			if (!cancelled) {

				int deletedCount = getContentResolver().delete(
						SalaamDBProvider.CONTENT_URI_TEMPLATES,
						SalaamDB.TEMPLATE_ID + "=" + message, null);

				TableLayout templatesTL = (TableLayout) findViewById(R.id.listTemplates);
				templatesTL.removeAllViews();
				showTemplates(getData());

				Toast.makeText(this, "Template deleted.", Toast.LENGTH_SHORT)
						.show();

			}

		} catch (Exception ex) {
			Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
		}
	}

}
