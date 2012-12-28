package templates;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Created by Yoosuf Nabeel Solih
 * on : 20th December 2012
 */

public class NewSMSTemplate extends Activity implements OnClickListener {

	Button btnCancel, btnSave;
	EditText etMessageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.template_new);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790404")));
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

		// buttons
		btnCancel = (Button) findViewById(R.id.btnCancelNewSMS);
		btnSave = (Button) findViewById(R.id.btnSaveNewSMS);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		// textviews
		etMessageText = (EditText) findViewById(R.id.etNewSMSMessageText);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		try {
			switch (v.getId()) {
			case R.id.btnSaveNewSMS:

				Cursor cursor = managedQuery(
						SalaamDBProvider.CONTENT_URI_TEMPLATES,
						SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);

				String message = etMessageText.getText().toString();

				
				int lastEnteredId = 0;
				if (cursor.getCount() > 0) {
					cursor.moveToLast();
					lastEnteredId = cursor.getInt(0);
				}
				int id = lastEnteredId + 1;

				if (message.trim().length() > 0) {

					ContentValues values = new ContentValues();

					values.put(SalaamDB.TEMPLATE_ID, id);
					values.put(SalaamDB.TEMPLATE_MESSAGE, message);
					getContentResolver().insert(
							SalaamDBProvider.CONTENT_URI_TEMPLATES, values);

					finish();
				} else {
					Toast.makeText(this, "Enter a message.", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			case R.id.btnCancelNewSMS:

				finish();
				break;
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(this,
					"An error occured while processing your request.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
