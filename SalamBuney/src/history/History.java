package history;

import java.util.ArrayList;

import templates.NewSMSTemplate;

import com.example.salambuney.AlertDialogFragment;
import com.example.salambuney.OnDialogDoneListener;
import com.example.salambuney.R;
import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;

import contacts.NewContact;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class History extends Activity implements OnDialogDoneListener,
		OnClickListener {

	ListView templatesTL;

	@Override
	protected void onResume() {

		super.onResume();
		showHistory(getData());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

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

			com.example.salambuney.models.History history = new com.example.salambuney.models.History(
					history_id, message, time);
			contacts.add(history);

		}

		TextView info = (TextView) findViewById(R.id.tvInforHistory);
		if (cursor.getCount() == 0) {

			info.setVisibility(View.VISIBLE);
		} else {
			info.setVisibility(View.GONE);
		}

		templatesTL.setAdapter(new HistoryArrayAdapter(this,
				android.R.layout.simple_expandable_list_item_1, contacts));

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_HISTORY,
				SalaamDBProvider.FROM_HISTORY_TABLE, null, null,
				SalaamDB.HISTORY_ID + " DESC");
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getTag() != null) {

			HistoryDetailsDialog cdf = HistoryDetailsDialog.newInstance(v
					.getTag().toString());
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			cdf.show(ft, "my-dialog-tag");
		}

	}

	@Override
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {

		try {

			if (!cancelled) {

				int deletedCount = getContentResolver().delete(
						SalaamDBProvider.CONTENT_URI_HISTORY, null, null);
				if(deletedCount != 0)
					Toast.makeText(this, "Deleted all history.", Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(this, "History is empty.", Toast.LENGTH_SHORT)
					.show();
				showHistory(getData());

			}

		} catch (Exception ex) {
			Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
		}
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

				AlertDialogFragment dialog = AlertDialogFragment.newInstance(
						"Are you sure to delete all history?", "Confirm ", "");
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


}
