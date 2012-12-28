package com.example.salambuney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {

	private static String _id;

	public static AlertDialogFragment newInstance(String message, String title,
			String id) {
		AlertDialogFragment adf = new AlertDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("alert-message", message);
		bundle.putString("alert-title", title);
		_id = id;
		adf.setArguments(bundle);
		return adf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCancelable(true);
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
				.setTitle(this.getArguments().getString("alert-title"))
				.setPositiveButton("Ok", this)
				.setNegativeButton("Cancel", this)
				.setMessage(this.getArguments().getString("alert-message"));
		return b.create();
	}

	public void onClick(DialogInterface dialog, int which) {
		OnDialogDoneListener act = (OnDialogDoneListener) getActivity();
		boolean cancelled = false;
		if (which == AlertDialog.BUTTON_NEGATIVE) {
			cancelled = true;
		}
		act.onDialogDone("result", cancelled, _id);

	}
}