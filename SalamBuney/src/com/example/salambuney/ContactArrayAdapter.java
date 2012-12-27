package com.example.salambuney;

import java.util.ArrayList;
import com.example.salambuney.models.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {

	private ArrayList<Contact> contacts;

	public ContactArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Contact> contacts) {
		super(context, textViewResourceId, contacts);
		this.contacts = contacts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.table_row_template, null);
		}

		Contact contact = contacts.get(position);
		if (contact != null) {
			LinearLayout layout = (LinearLayout) v
					.findViewById(R.id.layoutForSingleContact);

			TextView name = (TextView) v.findViewById(R.id.textName);
			TextView number = (TextView) v.findViewById(R.id.textPhone);

			if (name != null) {
				name.setText(contact._Contact);
			}

			if (number != null) {
				number.setText(contact._Number);
			}
			
			if(layout != null)
			{
				layout.setTag(contact._Id);
				layout.setOnClickListener((OnClickListener) getContext());
			}
		}
		return v;
	}
}