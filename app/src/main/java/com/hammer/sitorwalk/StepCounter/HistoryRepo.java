package com.hammer.sitorwalk.StepCounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Cheng on 14/10/17.
 */

public class HistoryRepo {
    private HistoryHelper dbHelper;

    public HistoryRepo(Context context){
        dbHelper=new HistoryHelper(context);
    }

    public int insert(HistoryModel history){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(HistoryModel.KEY_DATE, history.getDate());
        values.put(HistoryModel.KEY_STEPS, history.getSteps());
        //
        long id = db.insert(HistoryModel.TABLE,null,values);
        db.close();
        return (int)id;
    }

    public ArrayList<HistoryModel> getList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                HistoryModel.KEY_ID+","+
                HistoryModel.KEY_DATE+","+
                HistoryModel.KEY_STEPS+" FROM "+ HistoryModel.TABLE;
        ArrayList<HistoryModel> historyList=new ArrayList<HistoryModel>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HistoryModel history=new HistoryModel(
                        cursor.getInt(cursor.getColumnIndex(HistoryModel.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(HistoryModel.KEY_DATE)),
                        cursor.getInt(cursor.getColumnIndex(HistoryModel.KEY_STEPS))
                );
                historyList.add(history);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }
}
