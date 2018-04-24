package com.chinabrowser.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinabrowser.APP;
import com.chinabrowser.bean.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 95470 on 2018/4/24.
 */

public class HistoryManager {

    private SQLiteDatabase mDatabase;

    private static HistoryManager historyManager;

    private HistoryManager() {
        DBHelper dbHelper = new DBHelper(APP.getContext());
        mDatabase = dbHelper.getWritableDatabase();
    }

    public static HistoryManager getInstance(){

        if (historyManager==null){
            historyManager = new HistoryManager();
        }

        return historyManager;
    }


    /**
     * 插入
     * @param history
     */
    public void addHistory(History history){
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, history.getTitle());
        values.put(DBHelper.AGE, history.getUrl());
        values.put(DBHelper.TIME, history.getTime());
        mDatabase.insert(DBHelper.TABLE_NAME, null, values);
    }

    /**
     * 查询
     */
    public List<History> queryData(int which) {
        String sql = "select * from "+DBHelper.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(sql, null);

       // Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME, new String[]{DBHelper.NAME, DBHelper.AGE,DBHelper.TIME}, DBHelper.TIME + " > ?", new String[]{before}, null, null, DBHelper.TIME + " desc");// 注意空格！

        int nameIndex = cursor.getColumnIndex(DBHelper.NAME);
        int ageIndex = cursor.getColumnIndex(DBHelper.AGE);
        int timeIndex = cursor.getColumnIndex(DBHelper.TIME);
        List<History> histories = new ArrayList<>();
        while (cursor.moveToNext()) {
            History history = new History();
            history.setTitle(cursor.getString(nameIndex));
            history.setUrl(cursor.getString(ageIndex));
            history.setTime(cursor.getLong(timeIndex));
            histories.add(history);
        }

        Date date = new Date();
        Long before  ;
        if (which==1){
            before = date.getTime()- 24*60*60*1000 ;
        }else if (which==2){
            before = date.getTime()- 7*24*60*60*1000;
        }else {
            before = date.getTime()- 30*24*60*60*1000;
        }
        List<History> sh = new ArrayList<>();
        for (int i =0;i<histories.size();i++){
           History history = histories.get(i);
            if (history.getTime()<before){
                sh.add(history);
            }
        }


        return sh;

    }
}
