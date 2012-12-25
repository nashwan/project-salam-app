package com.example.salambuney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by: Yoosuf nabeel solih / 20 December 2012
 * 
 */
public class SalaamDB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "salaam_db";
	private static final int DATABASE_VERSION = 2;
	
	// message template table related
	public static final String TEMPLATES_TABLE_NAME = "templates";
	public static final String TEMPLATE_MESSAGE = "message";
	public static final String TEMPLATE_ID = "_id";
	
	// history table related
	public static final String HISTORY_TABLE_NAME = "history";
	public static final String HISTORY_MESSAGE = "message";
	public static final String HISTORY_ID = "_id";
	public static final String HISTORY_SENT_TIME = "sent_time";

	// contact table related
	public static final String CONTACT_TABLE_NAME = "contact";
	public static final String CONTACT_PHONE = "phone";
	public static final String CONTACT_ID = "_id";
	public static final String CONTACT_NAME = "name";
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {


		String sql = "CREATE TABLE " + HISTORY_TABLE_NAME + 
				" ("+ 
				HISTORY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HISTORY_MESSAGE  +" TEXT NOT NULL, " +
				HISTORY_SENT_TIME  +" TEXT NOT NULL)";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE " + TEMPLATES_TABLE_NAME + 
				" ("+ 
				TEMPLATE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				TEMPLATE_MESSAGE  +" TEXT NOT NULL) ";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE " + CONTACT_TABLE_NAME + 
				" ("+ 
				CONTACT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				CONTACT_NAME  +" TEXT NOT NULL, " +
				CONTACT_PHONE  +" TEXT NOT NULL)";
		
		db.execSQL(sql);
		
		insertInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists "+TEMPLATES_TABLE_NAME);
		db.execSQL("drop table if exists "+HISTORY_TABLE_NAME);
		db.execSQL("drop table if exists "+CONTACT_TABLE_NAME);
		onCreate(db);
	}
		
	private void insertInitialData( SQLiteDatabase db) {
		
		// insert intial templates 
		db.execSQL("INSERT INTO templates VALUES(1,'I have fever')");
		db.execSQL("INSERT INTO templates VALUES(2,'Varah salaam')");
		db.execSQL("INSERT INTO templates VALUES(3,'Beyrah Hinganee eve')");
		db.execSQL("INSERT INTO templates VALUES(4,'Fever')");
		db.execSQL("INSERT INTO templates VALUES(5,'Balivee')");
		db.execSQL("INSERT INTO templates VALUES(6,'Adhives balive')");
		
	}

	public SalaamDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	

	

}
