package com.example.salambuney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends  Activity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash_screen);
	        
	        Thread logoTimer = new Thread(){
	        	
	        	public void run(){
	        		try{
	        			sleep(1000);
	        			Intent newEntry = new Intent(getApplicationContext(), MainActivity.class);
	        			startActivity(newEntry);
	        		} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		finally{
	        			finish();
	        		}
	        	}
	        };
	        
	        logoTimer.start();
	    }


}
