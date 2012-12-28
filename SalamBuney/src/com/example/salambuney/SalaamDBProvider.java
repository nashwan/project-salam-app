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

	public static final String AUTHORITY = "com.example.salambuney";
	public static final String DATABASE_NAME = "salaam_db";

	private static final int MULTIPLES = 1;
	private static final int SINGLE = 2;

	// message template table related
	public static final String[] FROM_TEMPLATE_TABLE = { "_id", "message" };
	public static final String[] FROM_HISTORY_TABLE = { "_id", "message", "sent_time","contact" };
	public static final String[] FROM_CONTACT_TABLE = { "_id", "name", "phone" };
	
	
	public static final Uri CONTENT_URI_TEMPLATES = Uri
			.parse("content://com.example.salambuney/templates");
	public static final Uri CONTENT_URI_HISTORY = Uri
			.parse("content://com.example.salambuney/history");
	public static final Uri CONTENT_URI_CONTACT = Uri
			.parse("content://com.example.salambuney/contact");
	
	
	private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.salambuney.database";
	private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.salambuney.database";

	private SalaamDB salaamDB;
	private UriMatcher uriMatcher;

	@Override
	public boolean onCreate() {

		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "templates", MULTIPLES);
		uriMatcher.addURI(AUTHORITY, "templates/#", SINGLE);
		uriMatcher.addURI(AUTHORITY, "history", MULTIPLES);
		uriMatcher.addURI(AUTHORITY, "history/#", SINGLE);
		uriMatcher.addURI(AUTHORITY, "contact", MULTIPLES);
		uriMatcher.addURI(AUTHORITY, "contact/#", SINGLE);
		salaamDB = new SalaamDB(getContext());
		return true;
	}

	private String getTableName(Uri contentUri) {
		if (CONTENT_URI_TEMPLATES == contentUri)
			return SalaamDB.TEMPLATES_TABLE_NAME;
		if (CONTENT_URI_HISTORY == contentUri)
			return SalaamDB.HISTORY_TABLE_NAME;
		if (CONTENT_URI_CONTACT == contentUri)
			return SalaamDB.CONTACT_TABLE_NAME;
		else
			return "";
	}

	@Override
	public String getType(Uri uri) {

		switch (uriMatcher.match(uri)) {
		case MULTIPLES:
			return CONTENT_TYPE;
		case SINGLE:
			return CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = salaamDB.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case MULTIPLES:
			count = db.delete(getTableName(uri), selection, selectionArgs);
			break;
		case SINGLE:
			long id = Long.parseLong(uri.getPathSegments().get(1));
			count = db.delete(getTableName(uri), appendRowId(selection, id),
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = salaamDB.getWritableDatabase();

		if (uriMatcher.match(uri) != MULTIPLES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		long id = db.insertOrThrow(getTableName(uri), null, values);
		Uri newUri = ContentUris.withAppendedId(uri, id);
		getContext().getContentResolver().notifyChange(newUri, null);
		
		return newUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		if (uriMatcher.match(uri) == SINGLE) {
			long id = Long.parseLong(uri.getPathSegments().get(1));
			selection = appendRowId(selection, id);
		}

		SQLiteDatabase db = salaamDB.getReadableDatabase();
		Cursor cursor = db.query(getTableName(uri), projection, selection,
				selectionArgs, null, null, sortOrder);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = salaamDB.getWritableDatabase();
		int count;


		switch (uriMatcher.match(uri)) {
		case MULTIPLES:
			count = db.update(getTableName(uri), values, selection,
					selectionArgs);
			break;

		case SINGLE:
			long id = Long.parseLong(uri.getPathSegments().get(1));

			count = db.update(getTableName(uri), values,
					appendRowId(selection, id), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private String appendRowId(String selection, long id) {
		return _ID
				+ "="
				+ id
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: "");
	}

}
