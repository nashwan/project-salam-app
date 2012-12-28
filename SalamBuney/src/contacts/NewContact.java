package contacts;

import com.example.salambuney.R;
import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewContact extends Activity implements OnClickListener {

	Button btnCancel, btnSave;
	EditText etPersonName, etPhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_new);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790404")));
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
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

		switch (v.getId()) {
		case R.id.btnSaveContact:

			Cursor cursor = managedQuery(SalaamDBProvider.CONTENT_URI_CONTACT,
					SalaamDBProvider.FROM_CONTACT_TABLE, null, null, null);

			String personName = etPersonName.getText().toString();
			String contactNumber = etPhoneNumber.getText().toString();
			
			int lastEnteredId = 0;
			
			if (cursor.getCount() > 0) {
				cursor.moveToLast();
				lastEnteredId = cursor.getInt(0);
			}
			int id = lastEnteredId + 1;

			if (personName.trim().length() > 0
					&& contactNumber.trim().length() > 6) {
				ContentValues values = new ContentValues();

				values.put(SalaamDB.CONTACT_ID, id);
				values.put(SalaamDB.CONTACT_NAME, personName);
				values.put(SalaamDB.CONTACT_PHONE, contactNumber);

				getContentResolver().insert(
						SalaamDBProvider.CONTENT_URI_CONTACT, values);

				finish();
			} else {
				Toast.makeText(
						this,
						"Ensure you have provided a name and a 7 digit phone number.",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btnCancelContact:

			finish();
			break;
		}
	}
}
