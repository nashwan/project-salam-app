package templates;

import com.example.salambuney.OnDialogDoneListener;
import com.example.salambuney.R;
import com.example.salambuney.SalaamDB;
import com.example.salambuney.SalaamDBProvider;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class Templates extends Activity implements OnClickListener,
		OnDialogDoneListener {

	public String smsTextToSent;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templates);
		try {
			showTemplates(getData());
		} catch (Exception ex) {
			System.out.println("Error: " + ex.toString());
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showTemplates(getData());
	}

	private void showTemplates(Cursor cursor) {

		TableLayout templatesTL = (TableLayout) findViewById(R.id.listTemplates);
		templatesTL.removeAllViews();
		while (cursor.moveToNext()) {

			String message = cursor.getString(1);
			String message_id = cursor.getString(0);

			// create new row
			TableRow tr = new TableRow(this);
			tr.setTag(message_id);
			tr.setPadding(5, 0, 5, 1);

			tr.setClickable(true);
			tr.setOnClickListener(this);

			// set message styles
			TextView messageTV = (TextView) getLayoutInflater().inflate(
					R.layout.row_template, null);

			messageTV.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));

			messageTV.setTextSize(18);
			messageTV.setPadding(8, 4, 2, 4);
			messageTV.setGravity(Gravity.TOP);

			messageTV.setText(message); // set message
			messageTV.setTag(message_id);
			messageTV.setOnClickListener(this);

			tr.addView(messageTV); // sets message id to the row.
			templatesTL.addView(tr);

		}

		TextView info = (TextView) findViewById(R.id.tvInforTemplates);
		if (cursor.getCount() == 0) {

			info.setVisibility(View.VISIBLE);
		} else {
			info.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getTag() != null) {

			MessageOptionsDialogue mdf = MessageOptionsDialogue.newInstance(v
					.getTag().toString());
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			mdf.show(getFragmentManager(), "Exiting");
		}

	}

	public Cursor getData() {
		return managedQuery(SalaamDBProvider.CONTENT_URI_TEMPLATES,
				SalaamDBProvider.FROM_TEMPLATE_TABLE, null, null, null);
	}

	@Override
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {

		try {

			if (!cancelled) {

				int deletedCount = getContentResolver().delete(
						SalaamDBProvider.CONTENT_URI_TEMPLATES,
						SalaamDB.TEMPLATE_ID + "=" + message, null);

				TableLayout templatesTL = (TableLayout) findViewById(R.id.listTemplates);
				templatesTL.removeAllViews();
				showTemplates(getData());

				Toast.makeText(this, "Template deleted.", Toast.LENGTH_SHORT)
						.show();

			}

		} catch (Exception ex) {
			Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
		}
	}
}
