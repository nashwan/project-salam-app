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

public class EditContact extends Activity implements OnClickListener {

	Button btnCancel, btnSave;
	EditText etPersonName, etPhoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelContactEdit);
		btnSave = (Button) findViewById(R.id.btnSaveContactEdit);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		// textviews
		etPersonName = (EditText) findViewById(R.id.etPersonNameEdit);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberEdit);
		
		Intent intent = getIntent();
		String contact_id = intent.getStringExtra("contact_id");

		Cursor cursor = managedQuery(SalaamDBProvider.CONTENT_URI_CONTACT,
				SalaamDBProvider.FROM_CONTACT_TABLE, " _id =" + contact_id,
				null, null);

		if (cursor.getCount() == 0) {

			finish();

		} else {

			cursor.moveToFirst();
			etPersonName.setText(cursor.getString(1));
			etPhoneNumber.setText(cursor.getString(2));
			etPhoneNumber.setTag(contact_id);

		}

	}

	@Override
	public void onClick(View v) {

		try {

			switch (v.getId()) {
			case R.id.btnSaveContactEdit:

				String personName = etPersonName.getText().toString();
				String number = etPhoneNumber.getText().toString();
				String id = etPhoneNumber.getTag().toString();

				ContentValues values = new ContentValues();

				values.put(SalaamDB.CONTACT_ID, id);
				values.put(SalaamDB.CONTACT_NAME, personName);
				values.put(SalaamDB.CONTACT_PHONE, number);
				
				getContentResolver().update(
						SalaamDBProvider.CONTENT_URI_CONTACT, values,
						"_id=" + id, null);
				Intent intentMain = new Intent(this, MainActivity.class);
				intentMain.putExtra("tab", 1);
				startActivity(intentMain);

				Toast.makeText(this, "Contact update successful.",
						Toast.LENGTH_SHORT).show();

				finish();

				break;
			case R.id.btnCancelContactEdit:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				intent.putExtra("tab", 1);
				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());

			Toast.makeText(this, "Error occured while updating the contact.",
					Toast.LENGTH_SHORT).show();
		}
	}
}