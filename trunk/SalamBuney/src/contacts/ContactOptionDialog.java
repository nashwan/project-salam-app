package contacts;

import com.example.salambuney.AlertDialogFragment;
import com.example.salambuney.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class ContactOptionDialog extends DialogFragment implements
		OnItemClickListener {

	private Activity mainActivity;
	private String _contact_id;

	public static ContactOptionDialog newInstance(String messageId) {
		ContactOptionDialog pdf = new ContactOptionDialog();
		pdf.setStyle(STYLE_NO_TITLE, 0);
		Bundle bundle = new Bundle();
		bundle.putString("contact_id", messageId);
		pdf.setArguments(bundle);
		return pdf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		String contact_id = getArguments().getString("contact_id");
		_contact_id = contact_id;
		String[] cols = new String[] { "Edit", "Delete" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1, cols);
		View v = inflater.inflate(R.layout.message_prompt, container,
				false);

		ListView list = (ListView) v.findViewById(R.id.listMessageOptions);
		list.setTag(contact_id);
		list.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void dismiss() {

		super.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> adView, View target, int position,
			long id) {
		// TODO Auto-generated method stub
		dismiss();
		mainActivity = getActivity();

		try {

			if (adView.getTag().toString() != null) {

				String contact_id = adView.getTag().toString();
				_contact_id = contact_id;
				switch (position) {
				case 0: // Edit

					Intent messageSender = new Intent(mainActivity,
							EditContact.class);
					messageSender.putExtra("contact_id", contact_id);
					startActivity(messageSender);

					break;
				case 1: // Delete

					AlertDialogFragment dialog = AlertDialogFragment
							.newInstance(
									"Are you sure to delete this contact?",
									"Confirm ",contact_id);
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					dialog.show(ft, "PROMPT");
					break;

				}
			}
		} catch (Exception ex) {
			Log.i("SALAAM", ex.toString());
			Toast.makeText(getActivity(), "Delete contact failed.",
					Toast.LENGTH_SHORT).show();

		}

	}


}
