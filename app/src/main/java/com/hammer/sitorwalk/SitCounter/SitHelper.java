package com.hammer.sitorwalk.SitCounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hammer.sitorwalk.SitCounter.SitModel;

/**
 * Created by Cheng on 22/10/17.
 */

public class SitHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME="sit.db";

    public SitHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String CREATE_TABLE_SIT="CREATE TABLE "+ SitModel.TABLE+"("
                +SitModel.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +SitModel.KEY_DATE+" TEXT, "
                +SitModel.KEY_SIT+" INTEGER)";
        db.execSQL(CREATE_TABLE_SIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ SitModel.TABLE);
        onCreate(db);
    }
}
