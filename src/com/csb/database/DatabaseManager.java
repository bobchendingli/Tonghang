package com.csb.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: qii Date: 12-7-30
 */
public class DatabaseManager {

	private static DatabaseManager singleton = null;

	private SQLiteDatabase wsd = null;

	private SQLiteDatabase rsd = null;

	private DatabaseHelper databaseHelper = null;

	private DatabaseManager() {

	}

	public synchronized static DatabaseManager getInstance() {

		if (singleton == null) {
			DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
			SQLiteDatabase wsd = databaseHelper.getWritableDatabase();
			SQLiteDatabase rsd = databaseHelper.getReadableDatabase();

			singleton = new DatabaseManager();
			singleton.wsd = wsd;
			singleton.rsd = rsd;
			singleton.databaseHelper = databaseHelper;
		}

		return singleton;
	}

	public static void close() {
		if (singleton != null) {
			singleton.databaseHelper.close();
		}
	}
}
