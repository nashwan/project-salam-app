package com.example.salambuney;

import static android.provider.BaseColumns._ID;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class SalaamDBProvider extends ContentProvider {

	public static final int DATABASE_VERSION = 1;
	public static final String AUTHORITY = "com.example.salambuney";
	public static final String DATABASE_NAME = "salaam_db";
	
	//message template table related 
	private static final int TEMPLATES = 1;
	private static final int  TEMPLATES_ID = 2;
	public static final String TEMPLATES_TABLE_NAME = "templates";
	public static final String TEMPLATE_MESSAGE = "message";	
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.example.salambuney/templates"); 

	private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.salambuney";
	private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.salambuney";

	private SalaamDB salaamDB;
	private UriMatcher uriMatcher;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = salaamDB.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case TEMPLATES:
			count = db.delete(TEMPLATES_TABLE_NAME, selection, selectionArgs);
			break;
		case TEMPLATES_ID:
			long id = Long.parseLong(uri.getPathSegments().get(1));
			count = db.delete(TEMPLATES_TABLE_NAME, appendRowId(selection, id),
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {

		switch (uriMatcher.match(uri)) {
		case TEMPLATES:
			return CONTENT_TYPE;
		case TEMPLATES_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = salaamDB.getWritableDatabase();

		if (uriMatcher.match(uri) == TEMPLATES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		long id = db.insertOrThrow(TEMPLATES_TABLE_NAME, null, values);
		Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
		getContext().getContentResolver().notifyChange(newUri, null);
		return newUri;
	}

	@Override
	public boolean onCreate() {

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "templates", TEMPLATES);
		uriMatcher.addURI(AUTHORITY, "templates/#", TEMPLATES_ID);
		salaamDB = new SalaamDB(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		if (uriMatcher.match(uri) == TEMPLATES_ID) {
			long id = Long.parseLong(uri.getPathSegments().get(1));
			selection = appendRowId(selection, id);
		}

		SQLiteDatabase db = salaamDB.getReadableDatabase();
		Cursor cursor = db.query(TEMPLATES_TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = salaamDB.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case TEMPLATES:
			count = db.update(TEMPLATES_TABLE_NAME,values, selection, selectionArgs);
			break;
		case TEMPLATES_ID:
			long id = Long.parseLong(uri.getPathSegments().get(1));
			count = db.update(TEMPLATES_TABLE_NAME,values, appendRowId(selection, id),
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	private String appendRowId(String selection, long id)
	{
		return _ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : "");
	}

}
