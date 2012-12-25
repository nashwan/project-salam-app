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
import android.widget.EditText;
import android.widget.Toast;

/*
 * Created by Yoosuf Nabeel Solih
 * on : 21st December 2012
 */
public class EditSMSTemplate extends Activity implements OnClickListener {

	Button btnCancel, btnSave;
	EditText etMessageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template_edit);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelEditSMS);
		btnSave = (Button) findViewById(R.id.btnSaveEditSMS);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		// textviews
		etMessageText = (EditText) findViewById(R.id.etEditSMSMessageText);

		Intent intent = getIntent();
		String message_id = intent.getStringExtra("message_id");

		Cursor cursor = managedQuery(SalaamDBProvider.CONTENT_URI_TEMPLATES,
				SalaamDBProvider.FROM_TEMPLATE_TABLE, " _id =" + message_id,
				null, null);

		if (cursor.getCount() == 0) {

			finish();

		} else {

			cursor.moveToFirst();
			String message = cursor.getString(1);
			etMessageText.setText(message);
			etMessageText.setTag(message_id);

		}

	}

	@Override
	public void onClick(View v) {

		try {

			switch (v.getId()) {
			case R.id.btnSaveEditSMS:

				Cursor cursor = managedQuery(
						SalaamDBProvider.CONTENT_URI_TEMPLATES,
						SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);

				String message = etMessageText.getText().toString();
				String id = etMessageText.getTag().toString();

				ContentValues values = new ContentValues();

				values.put(SalaamDB.TEMPLATE_ID, id);
				values.put(SalaamDB.TEMPLATE_MESSAGE, message);
				getContentResolver().update(
						SalaamDBProvider.CONTENT_URI_TEMPLATES, values,
						"_id=" + id, null);
				Intent intentMain = new Intent(this, MainActivity.class);
				startActivity(intentMain);

				Toast.makeText(this, "Tempalate update successful.",
						Toast.LENGTH_SHORT).show();

				finish();

				break;
			case R.id.btnCancelEditSMS:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());

			Toast.makeText(this, "Error occured while updating the tempalate.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
