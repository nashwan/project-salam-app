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
	public static int _ID = 0;
	public static int MESSAGE = 1;

	public SalaamDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table templates(_id integer, message text)");
		insertInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists templates");
		onCreate(db);
	}

	private void insertInitialData(SQLiteDatabase db) {
		// insert intial templates 
		db.execSQL("INSERT INTO templates VALUES(1,'I have fever')");
		db.execSQL("INSERT INTO templates VALUES(2,'Varah salaam')");
		db.execSQL("INSERT INTO templates VALUES(3,'Beyrah Hinganee eve')");
		db.execSQL("INSERT INTO templates VALUES(4,'Fever')");
		db.execSQL("INSERT INTO templates VALUES(5,'Balivee')");
		db.execSQL("INSERT INTO templates VALUES(6,'Adhives balive')");
	}

}
