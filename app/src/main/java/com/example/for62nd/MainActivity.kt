package com.example.for62nd

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val dbName: String = "MemoDB"
    private val tableName: String = "MemoTable"
    private val dbVersion: Int = 1
    private var arrayListTitle: ArrayList<String> = arrayListOf()
    private var arrayListDetail: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun insertData(id: Int, title: String, detail: String) {
        try{
            val dbHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("id",id)
            values.put("title",title)
            values.put("detail",detail)
            database.insertOrThrow(tableName,null,values)
        }catch (exception: Exception) {
            Log.e("insertData",exception.toString())
        }
    }

    private fun updateData(whereId: Int, newTitle: String, newDetail: String) {
        try{
            val dbHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("title",newTitle)
            values.put("detail",newDetail)

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId.toString())
            database.update(tableName, values, whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("updateData", exception.toString())
        }
    }

    private fun deleteData(whereId: Int) {
        try {
            val dbHelper = MemoDatabaseOpenHelper(applicationContext, dbName, null, dbVersion)
            val database = dbHelper.writableDatabase

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId.toString())
            database.delete(tableName, whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }
    }

    private fun selectData() {
        //TODO implement here
    }
}
