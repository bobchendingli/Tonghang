package com.csb.database;


import com.csb.database.table.FileDownloadTable;
import com.csb.utils.GlobalContext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "tonghang.db";

    private static final int DATABASE_VERSION = 1;

    static final String CREATE_FILE_DOWNLOAD_TABLE_SQL = "create table " + FileDownloadTable.TABLE_NAME
            + "("
            + FileDownloadTable.UID + " integer primary key autoincrement,"
            + FileDownloadTable.FILE_ID + " text,"
            + FileDownloadTable.AID + " text,"
            + FileDownloadTable.MID + " text,"
            + FileDownloadTable.TITLE + " text,"
            + FileDownloadTable.PICURL + " text,"
            + FileDownloadTable.LOCAL_PATH + " text,"
            + FileDownloadTable.INFO_TYPE + " text,"
            + FileDownloadTable.CREATE_TIME + " text"
            + ");";

    


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FILE_DOWNLOAD_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	 deleteAllTable(db);
         onCreate(db);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (singleton == null) {
            singleton = new DatabaseHelper(GlobalContext.getInstance());
        }
        return singleton;
    }

    private void deleteAllTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + FileDownloadTable.TABLE_NAME);
    }

}
