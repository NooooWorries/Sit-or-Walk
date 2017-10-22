package com.hammer.sitorwalk.StepCounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cheng on 14/10/17.
 */

public class HistoryHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME="history.db";

    public HistoryHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ HistoryModel.TABLE+"("
                +HistoryModel.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +HistoryModel.KEY_DATE+" TEXT, "
                +HistoryModel.KEY_STEPS+" INTEGER)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ HistoryModel.TABLE);
        onCreate(db);
    }
}
