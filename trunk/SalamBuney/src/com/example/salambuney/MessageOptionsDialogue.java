package com.example.salambuney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
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

public class MessageOptionsDialogue extends DialogFragment implements
		OnItemClickListener {

	private String smsTextToSent;
	private Activity mainActivity;

	public static MessageOptionsDialogue newInstance(String messageId) {
		MessageOptionsDialogue pdf = new MessageOptionsDialogue();
		pdf.setStyle(STYLE_NO_TITLE,0);
		Bundle bundle = new Bundle();
		
		bundle.putString("message_id", messageId);
		bundle.putString("title", "Message Option");
		pdf.setArguments(bundle);
		return pdf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		String message_id = getArguments().getString("message_id");

		String[] cols = new String[] { "Send now", "Schedule", "Edit", "Delete" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1, cols);
		View v = inflater.inflate(R.layout.message_prompt, container,
				false);

		ListView list = (ListView) v.findViewById(R.id.listMessageOptions);
		list.setTag(message_id);
		list.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
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

		if (adView.getTag().toString() != null) {

			String message_id = adView.getTag().toString();

			switch (position) {
			case 0: // Send Now

				Intent messageSender = new Intent(mainActivity,
						MessageSender.class);
				messageSender.putExtra("message_id", message_id);
				startActivity(messageSender);

				break;
			case 1: // Schedule
				Toast.makeText(mainActivity, "Schedule not implemented",
						Toast.LENGTH_SHORT).show();
				break;
			case 2: // Edit

				Intent intent = new Intent(mainActivity, EditSMSTemplate.class);
				intent.putExtra("message_id", message_id);
				startActivity(intent);

				break;
			case 3: // Delete
				AlertDialogFragment dialog = AlertDialogFragment.newInstance(
						"Are you sure to delete this template?", "Confirm ",
						message_id);
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				dialog.show(ft, "PROMPT");
				break;
			}
		}

	}

}
