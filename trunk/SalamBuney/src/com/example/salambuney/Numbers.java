package com.example.salambuney;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Numbers extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numbers);
		showHistory(getData());
	}

	private void showHistory(Cursor cursor) {

		TableLayout templatesTL = (TableLayout) findViewById(R.id.listNumbers);
		int count = 0;
		while (cursor.moveToNext()) {

			String contact_id = cursor.getString(0);
			String person_name = cursor.getString(1);
			String contact_number = cursor.getString(2);

			// create new row
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			if (count % 2 == 1)
				tr.setBackgroundColor(Color.WHITE);
			tr.setTag(contact_id);
			tr.setPadding(3, 2, 3, 2);

			tr.setClickable(true);

			// set message styles
			TextView messageTV = (TextView) getLayoutInflater().inflate(
					R.layout.row_template, null);

			messageTV.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));

			messageTV.setTextSize(17);
			messageTV.setTypeface(Typeface.DEFAULT_BOLD);
			messageTV.setPadding(4, 8, 2, 4);
			messageTV.setGravity(Gravity.TOP);

			TextView messageTV2 = (TextView) getLayoutInflater().inflate(
					R.layout.row_template, null);

			messageTV2.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));

			messageTV2.setTextSize(17);
			messageTV2.setTypeface(Typeface.DEFAULT_BOLD);
			messageTV2.setPadding(4, 8, 2, 4);
			messageTV2.setGravity(Gravity.TOP);
			messageTV2.setText(contact_number);
			messageTV2.setTextColor(Color.parseColor("#b6b6b6"));
			
			messageTV.setText(person_name); // set message
			messageTV.setTag(contact_id);

			tr.addView(messageTV); // sets message id to the row.
			tr.addView(messageTV2);
			templatesTL.addView(tr);
			count++;
		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_CONTACT,
				SalaamDBProvider.FROM_CONTACT_TABLE, null, null, null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_numbers, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_new_contact) {

			Intent intent = new Intent();
			intent.setClass(this, NewContact.class);
			startActivity(intent);

		}

		return super.onOptionsItemSelected(item);
	}


}
