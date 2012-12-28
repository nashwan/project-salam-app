package history;

import com.example.salambuney.R;
import com.example.salambuney.SalaamDBProvider;
import com.example.salambuney.R.id;
import com.example.salambuney.R.layout;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryDetailsDialog extends DialogFragment {

	private String _historyID;

	TextView tvMessage, tvTime, tvContacts;

	public static HistoryDetailsDialog newInstance(String historyID) {
		HistoryDetailsDialog pdf = new HistoryDetailsDialog();
		pdf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		Bundle bundle = new Bundle();
		bundle.putString("history_id", historyID);
		pdf.setArguments(bundle);
		return pdf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		String contact_id = getArguments().getString("history_id");
		_historyID = contact_id;

		View v = inflater.inflate(R.layout.history_details, container, false);
		Cursor cursor = getActivity().managedQuery(
				SalaamDBProvider.CONTENT_URI_HISTORY,
				SalaamDBProvider.FROM_HISTORY_TABLE, " _id =" + _historyID,
				null, null);

		if (cursor.getCount() == 0) {

			dismiss();

		} else {

			cursor.moveToFirst();

			// textviews
			tvMessage = (TextView)v.findViewById(R.id.tvHistoryDetailsMessage);
			tvContacts = (TextView) v.findViewById(R.id.tvHistoryDetailsContacts);
			tvTime = (TextView) v.findViewById(R.id.tvHistoryDetailsSentTime);
			
			
			tvMessage.setText(cursor.getString(1));
			tvTime.setText(cursor.getString(2));
			tvContacts.setText(cursor.getString(3));

		}

		return v;
	}
}
