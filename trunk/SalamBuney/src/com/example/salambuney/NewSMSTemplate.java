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
import android.widget.Toast;
/*
 * Created by Yoosuf Nabeel Solih
 * on : 20th December 2012
 */


public class NewSMSTemplate extends  Activity implements OnClickListener{
	

	Button btnCancel, btnSave;
	EditText etMessageText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.template_new);
		
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#064682")));
		
		//buttons
		btnCancel = (Button) findViewById(R.id.btnCancelNewSMS);
		btnSave = (Button) findViewById(R.id.btnSaveNewSMS);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		
		
		//textviews
		etMessageText = (EditText)findViewById(R.id.etNewSMSMessageText);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnSaveNewSMS:
			
			Cursor cursor = managedQuery(SalaamDBProvider.CONTENT_URI, SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);
			
			String message = etMessageText.getText().toString();
			int id  =  cursor.getCount() +1;
			
			ContentValues values = new ContentValues();
		
			values.put(SalaamDBProvider.TEMPLATE_ID,id);
	    	values.put(SalaamDBProvider.TEMPLATE_MESSAGE, message);
	    	getContentResolver().insert(SalaamDBProvider.CONTENT_URI, values);
	    	Intent intentMain = new Intent(this, MainActivity.class);
			startActivity(intentMain);
			finish();
			
			break;
		case R.id.btnCancelNewSMS:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	
}
