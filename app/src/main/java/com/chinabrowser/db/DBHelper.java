package com.chinabrowser.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chinabrowser.utils.LogUtils;

/**
 * Created by 95470 on 2018/4/24.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String REAL_TYPE = " REAL";
    private static final String PRIMARY_KEY = " PRIMARY KEY,";
    private static final String AUTO_PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT,";
    private static final String TEXT_TYPE_COMMA = TEXT_TYPE + ",";
    private static final String INT_TYPE_COMMA = INT_TYPE + ",";
    private static final String BLOB_TYPE_COMMA = BLOB_TYPE + ",";
    private static final String REAL_TYPE_COMMA = REAL_TYPE + ",";




    // 数据库文件名
    public static final String DB_NAME = "my_database.db";
    // 数据库表名
    public static final String TABLE_NAME = "t_person";
    // 数据库版本号
    public static final int DB_VERSION = 1;

    public static final String NAME = "title";
    public static final String AGE = "uel";
    public static final String TIME = "tm";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, " + NAME + " varchar, " + AGE + " varchar,"+ TIME+" integer" + ")";
        LogUtils.e("ZX","sql : "+sql);
        db.execSQL(sql);
    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
