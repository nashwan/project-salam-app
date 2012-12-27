package com.example.salambuney;

import java.util.ArrayList;
import com.example.salambuney.models.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryArrayAdapter extends ArrayAdapter<History> {

	private ArrayList<History> histories;

	public HistoryArrayAdapter(Context context, int textViewResourceId,
			ArrayList<History> historyList) {
		super(context, textViewResourceId, historyList);
		this.histories = historyList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.table_row_template_history, null);
		}

		History history = histories.get(position);
		if (history != null) {
			LinearLayout layout = (LinearLayout) v
					.findViewById(R.id.layoutForSingleHistory);

			TextView message = (TextView) v.findViewById(R.id.textMessage);
			TextView time = (TextView) v.findViewById(R.id.textTime);
			

			if (message != null) {
				message.setText(history._Message);
			}

			if (time != null) {
				time.setText(history._Time);
			}
			
			if(layout != null)
			{
				layout.setTag(history._ID);
				//layout.setOnClickListener((OnClickListener) getContext());
			}
		}
		return v;
	}
}