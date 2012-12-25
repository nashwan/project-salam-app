package com.example.salambuney;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageSender extends Activity implements OnClickListener {

	TextView tvMessageOnSender;
	LinearLayout listPhoneNumbers;
	Button btnCancel, btnSend;

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

				CheckBox childs = (CheckBox) listPhoneNumbers.getChildAt(1);
				
				// Cursor cursor = managedQuery(
				// SalaamDBProvider.CONTENT_URI_TEMPLATES,
				// SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);

				// String message = etMessageText.getText().toString();
				// String id = etMessageText.getTag().toString();

				// ContentValues values = new ContentValues();
				//
				// values.put(SalaamDB.TEMPLATE_ID, id);
				// values.put(SalaamDB.TEMPLATE_MESSAGE, message);
				// getContentResolver().update(
				// SalaamDBProvider.CONTENT_URI_TEMPLATES, values,
				// "_id=" + id, null);
				// Intent intentMain = new Intent(this, MainActivity.class);
				// startActivity(intentMain);

				Toast.makeText(this, "Incomplete:::::Childrens : " + childs.getText(),
						Toast.LENGTH_SHORT).show();

				//finish();

				break;
			case R.id.btnCancelMessageSender:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());

			Toast.makeText(this, "Error occured in message sender.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
