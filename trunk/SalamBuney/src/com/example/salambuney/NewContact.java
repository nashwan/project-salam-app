package com.example.salambuney;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewContact extends Activity implements OnClickListener {

	Button btnCancel, btnSave;
	EditText etPersonName, etPhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_new);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelContact);
		btnSave = (Button) findViewById(R.id.btnSaveContact);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		// textviews
		etPersonName = (EditText) findViewById(R.id.etPersonName);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSaveContact:

			Cursor cursor = managedQuery(
					SalaamDBProvider.CONTENT_URI_CONTACT,
					SalaamDBProvider.FROM_CONTACT_TABLE, null, null, null);

			String personName = etPersonName.getText().toString();
			String contactNumber = etPhoneNumber.getText().toString();
			int id = cursor.getCount() + 1;

			ContentValues values = new ContentValues();

			values.put(SalaamDB.CONTACT_ID, id);
			values.put(SalaamDB.CONTACT_NAME, personName);
			values.put(SalaamDB.CONTACT_PHONE, contactNumber);
			
			getContentResolver().insert(SalaamDBProvider.CONTENT_URI_CONTACT,
					values);
			Intent intentMain = new Intent(this, MainActivity.class);
			intentMain.putExtra("tab", 1);
			startActivity(intentMain);
			finish();

			break;
		case R.id.btnCancelContact:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
}
