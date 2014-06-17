package com.csb.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.csb.bean.FileBean;
import com.csb.database.table.FileDownloadTable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * User: qii
 * Date: 12-12-4
 */
public class FileDownloadDBTask {

    private FileDownloadDBTask() {

    }

    private static SQLiteDatabase getWsd() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

   

    public static void clear(String meetingId) {
        String sql = "delete from " + FileDownloadTable.TABLE_NAME + " where " + FileDownloadTable.MID + " = '" + meetingId + "'";
        getWsd().execSQL(sql);
    }
    
    public static List<String> getAll(String meetingId) {
    	 String sql = "select id from " + FileDownloadTable.TABLE_NAME + " where " + FileDownloadTable.MID + " = '" + meetingId + "'";
         Cursor c = getRsd().rawQuery(sql, null);
         List<String> result = new ArrayList<String>();
         while (c.moveToNext()) {
        	 result.add(c.getString(c.getColumnIndex(FileDownloadTable.FILE_ID)));
         }
         return result;
    }
}
