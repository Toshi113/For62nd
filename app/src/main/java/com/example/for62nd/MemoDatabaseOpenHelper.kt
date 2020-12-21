package com.example.for62nd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemoDatabaseOpenHelper(context: Context, databaseName:String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, databaseName, factory, version)  {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists MemoTable (id integer primary key, title text, detail text)");

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("alter table MemoTable add column deleteFlag integer default 0")
        }
    }
}