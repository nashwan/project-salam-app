package com.example.salambuney;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AppInformation extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_information);
		
		String[] developers = new String[] {"Yoosuf Nabeel Solih", "Mohamed Nasru", "Ibrahim Nashwan" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, developers);
		
		ListView list = (ListView) findViewById(R.id.listContriubuters);
		list.animate();
		
		list.setAdapter(adapter);
	}

}
