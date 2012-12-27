package com.example.salambuney;

import java.util.ArrayList;

import com.example.salambuney.models.Contact;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class History extends Activity implements OnDialogDoneListener {

	ListView templatesTL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		showHistory(getData());

		templatesTL = (ListView) findViewById(R.id.listHistory);
	}

	private void showHistory(Cursor cursor) {
		templatesTL = (ListView) findViewById(R.id.listHistory);
		startManagingCursor(cursor);

	
		int count = 0;
		
		ArrayList<com.example.salambuney.models.History> contacts = new ArrayList<com.example.salambuney.models.History>();

		while (cursor.moveToNext()) {

			String history_id = cursor.getString(0);
			String message = cursor.getString(1);
			String time = cursor.getString(2);

			com.example.salambuney.models.History history = new com.example.salambuney.models.History(history_id, message,
					time);
			contacts.add(history);

		}
		
		templatesTL.setAdapter(new HistoryArrayAdapter(this,
				android.R.layout.simple_expandable_list_item_1, contacts));
//		
//		while (cursor.moveToNext()) {
//
//			String message = cursor.getString(1);
//			String date = cursor.getString(2);
//			String message_id = cursor.getString(0);
//
//			// create new row
//			TableRow tr = new TableRow(this);
//			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//					LayoutParams.WRAP_CONTENT));
//			
//			tr.setTag(message_id);
//			tr.setPadding(5, 1, 5, 1);
//
//			tr.setClickable(true);
//
//			// set message styles
//			TextView messageTV = (TextView) getLayoutInflater().inflate(
//					R.layout.row_template, null);
//
//			messageTV.setLayoutParams(new LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
//
//			messageTV.setTextSize(17);
//			messageTV.setTypeface(Typeface.DEFAULT_BOLD);
//			messageTV.setPadding(4, 8, 2, 4);
//			messageTV.setGravity(Gravity.TOP);
//
//			TextView messageTV2 = (TextView) getLayoutInflater().inflate(
//					R.layout.row_template, null);
//
//			messageTV2.setLayoutParams(new LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
//
//			messageTV2.setTextSize(17);
//			messageTV2.setTypeface(Typeface.DEFAULT_BOLD);
//			messageTV2.setPadding(4, 8, 2, 4);
//			messageTV2.setGravity(Gravity.TOP);
//			messageTV2.setText(date);
//			messageTV2.setTextColor(Color.parseColor("#b6b6b6"));
//
//			messageTV.setText(message); // set message
//			messageTV.setTag(message_id);
//
//			tr.addView(messageTV); // sets message id to the row.
//			tr.addView(messageTV2);
//			templatesTL.addView(tr);
//			count++;
//		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_HISTORY,
				SalaamDBProvider.FROM_HISTORY_TABLE, null, null,
				SalaamDB.HISTORY_ID + " DESC");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		try {
			if (item.getItemId() == R.id.menu_clear_history) {

				// delete all the data from the history table.
				

				AlertDialogFragment dialog = AlertDialogFragment
						.newInstance(
								"Are you sure to delete all history?",
								"Confirm ","");
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				dialog.show(ft, "PROMPT");

			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(this, "An error occured while deleting history.",
					Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
	
		try {

			if (!cancelled) {

				int deletedCount = getContentResolver().delete(
						SalaamDBProvider.CONTENT_URI_HISTORY, null, null);
				Toast.makeText(this, "Deleted all history.", Toast.LENGTH_SHORT)
						.show();
				showHistory(getData());

			}

		} catch (Exception ex) {
			Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
		}
	}

}
