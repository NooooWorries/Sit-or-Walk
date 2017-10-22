package com.hammer.sitorwalk.SitCounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hammer.sitorwalk.StepCounter.HistoryHelper;
import com.hammer.sitorwalk.SitCounter.SitModel;

import java.util.ArrayList;

/**
 * Created by Cheng on 22/10/17.
 */

public class SitRepo {
    private HistoryHelper dbHelper;

    public SitRepo(Context context){
        dbHelper=new HistoryHelper(context);
    }

    public int insert(SitModel sit){

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SitModel.KEY_DATE, sit.getDate());
        values.put(SitModel.KEY_SIT, sit.getSit());
        //
        long id = db.insert(SitModel.TABLE,null,values);
        db.close();
        return (int)id;
    }

    public ArrayList<SitModel> getList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                SitModel.KEY_ID+","+
                SitModel.KEY_DATE+","+
                SitModel.KEY_SIT+" FROM "+ SitModel.TABLE;
        ArrayList<SitModel> sitList=new ArrayList<SitModel>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                SitModel sit=new SitModel(
                        cursor.getInt(cursor.getColumnIndex(SitModel.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(SitModel.KEY_DATE)),
                        cursor.getInt(cursor.getColumnIndex(SitModel.KEY_SIT))
                );
                sitList.add(sit);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sitList;
    }
}
