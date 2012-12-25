package com.example.salambuney;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class History extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		showHistory(getData());
	}

	private void showHistory(Cursor cursor) {

		startManagingCursor(cursor);
		TableLayout templatesTL = (TableLayout) findViewById(R.id.listHistory);
		int count = 0;
		while (cursor.moveToNext()) {

			String message = cursor.getString(1);
			String date = cursor.getString(2);
			String message_id = cursor.getString(0);

			// create new row
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			if (count % 2 == 1)
				tr.setBackgroundColor(Color.WHITE);
			tr.setTag(message_id);
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
			messageTV2.setText(date);
			messageTV2.setTextColor(Color.parseColor("#b6b6b6"));
			
			messageTV.setText(message); // set message
			messageTV.setTag(message_id);

			tr.addView(messageTV); // sets message id to the row.
			tr.addView(messageTV2);
			templatesTL.addView(tr);
			count++;
		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_HISTORY,
				SalaamDBProvider.FROM_HISTORY_TABLE, null, null, null);
	}
}
