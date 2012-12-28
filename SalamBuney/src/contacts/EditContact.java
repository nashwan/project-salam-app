package contacts;

import com.example.salambuney.R;
import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;
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
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790404")));
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
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

				if (personName.trim().length() > 0
						&& number.trim().length() > 6) {
					ContentValues values = new ContentValues();

					values.put(SalaamDB.CONTACT_ID, id);
					values.put(SalaamDB.CONTACT_NAME, personName);
					values.put(SalaamDB.CONTACT_PHONE, number);

					getContentResolver().update(
							SalaamDBProvider.CONTENT_URI_CONTACT, values,
							"_id=" + id, null);
					Toast.makeText(this, "Contact update successful.",
							Toast.LENGTH_SHORT).show();

					finish();
				} else {
					Toast.makeText(
							this,
							"Ensure you have provided a name and a 7 digit phone number.",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.btnCancelContactEdit:

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