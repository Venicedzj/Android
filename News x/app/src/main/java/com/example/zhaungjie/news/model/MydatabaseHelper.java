package com.example.zhaungjie.news.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by haoyu on 17-4-18.
 */

public class MydatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "create table info(" +
            "id integer primary key autoincrement,"+
            "username text," +
            "password text)";

    private Context mContext;

    public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mContext, "table created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
