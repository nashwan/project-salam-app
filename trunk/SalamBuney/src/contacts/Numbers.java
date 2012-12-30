package contacts;

import java.util.ArrayList;

import com.example.salambuney.OnDialogDoneListener;
import com.example.salambuney.R;
import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;
import com.example.salambuney.models.Contact;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Numbers extends Activity implements OnClickListener,
		OnDialogDoneListener {
	ListView templatesTL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numbers);
		showNumbers(getData());

		templatesTL = (ListView) findViewById(R.id.listNumbers);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		showNumbers(getData());
	}

	private void showNumbers(Cursor cursor) {

		int count = 0;

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		while (cursor.moveToNext()) {

			String contact_id = cursor.getString(0);
			String person_name = cursor.getString(1);
			String contact_number = cursor.getString(2);

			Contact contact = new Contact(contact_id, contact_number,
					person_name);
			contacts.add(contact);

		}

		TextView info = (TextView) findViewById(R.id.tvInforNumbers);
		if (cursor.getCount() == 0) {

			info.setVisibility(View.VISIBLE);
		} else {
			info.setVisibility(View.GONE);
		}

		ListView templatesTL = (ListView) findViewById(R.id.listNumbers);
		templatesTL.setAdapter(new ContactArrayAdapter(this,
				android.R.layout.simple_expandable_list_item_1, contacts));

		// templatesTL = (TableLayout) findViewById(R.id.listNumbers);
		// while (cursor.moveToNext()) {
		//
		// String contact_id = cursor.getString(0);
		// String person_name = cursor.getString(1);
		// String contact_number = cursor.getString(2);
		//
		// // create new row
		// TableRow tr = ( TableRow) getLayoutInflater().inflate(
		// R.layout.table_row_template, null);
		// tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		//
		//
		// tr.setTag(contact_id);
		// tr.setPadding(5, 1, 5, 1);
		// tr.setOnClickListener(this);
		// tr.setClickable(true);
		//
		// // set message styles
		// TextView messageTV = (TextView) getLayoutInflater().inflate(
		// R.layout.row_template, null);
		//
		// messageTV.setLayoutParams(new LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
		//
		// messageTV.setTextSize(17);
		// messageTV.setTypeface(Typeface.DEFAULT_BOLD);
		// messageTV.setPadding(4, 8, 2, 4);
		// messageTV.setGravity(Gravity.TOP);
		//
		// TextView messageTV2 = (TextView) getLayoutInflater().inflate(
		// R.layout.row_template, null);
		//
		// messageTV2.setLayoutParams(new LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
		//
		// messageTV2.setTextSize(17);
		// messageTV2.setTypeface(Typeface.DEFAULT_BOLD);
		// messageTV2.setPadding(4, 8, 2, 4);
		// messageTV2.setGravity(Gravity.TOP);
		// messageTV2.setText(contact_number);
		// messageTV2.setTextColor(Color.parseColor("#b6b6b6"));
		//
		// messageTV.setText(person_name); // set message
		// messageTV.setTag(contact_id);
		//
		// tr.addView(messageTV); // sets message id to the row.
		// tr.addView(messageTV2);
		// templatesTL.addView(tr);
		// count++;
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getTag() != null) {

			ContactOptionDialog cdf = ContactOptionDialog.newInstance(v
					.getTag().toString());
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			cdf.show(ft, "my-dialog-tag");
		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_CONTACT,
				SalaamDBProvider.FROM_CONTACT_TABLE, null, null, null);
	}

	
	@Override
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
		// TODO Auto-generated method stub
		try {

			if (!cancelled) {

				int deletedCount = getContentResolver().delete(
						SalaamDBProvider.CONTENT_URI_CONTACT,
						SalaamDB.CONTACT_ID + "=" + message, null);

				showNumbers(getData());

			}

		} catch (Exception ex) {
			Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
		}
	}

}
