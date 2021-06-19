package com.example.im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.im.model.dao.UserAccountTable;

/**
 * @Author cyh
 * @Date 2021/6/1 15:54
 */
public class UserAccountDB extends SQLiteOpenHelper {

    //构造
    public UserAccountDB(@Nullable Context context) {
        super(context,"account.db",null,1);
    }
    //数据库调用的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }
    //数据库更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
