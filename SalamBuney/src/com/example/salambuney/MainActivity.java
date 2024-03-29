package com.example.salambuney;

import history.History;
import contacts.NewContact;
import contacts.Numbers;
import templates.Templates;
import templates.NewSMSTemplate;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.TabActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	public String smsTextToSent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_main);
		//
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790404")));
		bar.setSubtitle("Sick Leaves");
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

		// Create tabs
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		// Tab for Templates
		TabSpec homeSpec = tabHost.newTabSpec("templates");
		homeSpec.setIndicator("Messages");
		Intent homeIntent = new Intent(this, Templates.class);
		homeSpec.setContent(homeIntent);

		// Tab for Numbers
		TabSpec numberSpec = tabHost.newTabSpec("numbers");
		numberSpec.setIndicator("Contacts");

		Intent numberIntent = new Intent(this, Numbers.class);
		numberSpec.setContent(numberIntent);

		// Tab for history
		TabSpec historySpec = tabHost.newTabSpec("history");
		historySpec.setIndicator("History");
		Intent historyIntent = new Intent(this, History.class);
		historySpec.setContent(historyIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(homeSpec);
		tabHost.addTab(numberSpec);
		tabHost.addTab(historySpec);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#ffebeb"));
			tv.setTextSize(13);
		}

		Intent intent = getIntent();
		int selectTab = intent.getIntExtra("tab", 0);
		tabHost.setCurrentTab(selectTab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_information) {

			Intent intent = new Intent();
			intent.setClass(this, AppInformation.class);
			startActivity(intent);

		}

	

		if (item.getItemId() == R.id.menu_new_template) {

			Intent intent = new Intent();
			intent.setClass(this, NewSMSTemplate.class);
			startActivity(intent);

		}

		if (item.getItemId() == R.id.menu_new_contact) {

			Intent intent = new Intent();
			intent.setClass(this, NewContact.class);
			startActivity(intent);

		}

		if (item.getItemId() == R.id.menu_settings) {

			Intent intent = new Intent();
			intent.setClass(this,SettingsActivity.class);
			startActivity(intent);

		}

		return super.onOptionsItemSelected(item);
	}

}
