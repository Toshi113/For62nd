package com.example.for62nd

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.Exception
//TODO 新規作成実装
class MainActivity : AppCompatActivity() , RecyclerViewHolder.ItemClickListener{
    private val dbName: String = "MemoDB"
    private val tableName: String = "MemoTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<Int> = arrayListOf()
    private var arrayListTitle: ArrayList<String> = arrayListOf()
    private var arrayListDetail: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initActivity(){
        selectAll()
        var data = mutableListOf<MemoStructure>()
        repeat(arrayListTitle.count()) {
            data.add(MemoStructure(arrayListId[it],arrayListTitle[it],arrayListDetail[it]))
        }
        recyclerView_main.adapter = RecyclerAdapter(this,this,data)
        recyclerView_main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    private fun selectData(row: Int) {
        try{
            arrayListId.clear()
            arrayListTitle.clear()
            arrayListDetail.clear()

            val databaseOpenHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = databaseOpenHelper.readableDatabase

            val sql = "select id, title, detail from " + tableName + " where id = " + row.toString()

            val cursor = database.rawQuery(sql,null)
            if(cursor.count > 0) {
                cursor.moveToFirst()
                while(!cursor.isAfterLast) {
                    arrayListId.add(cursor.getInt(0))
                    arrayListTitle.add(cursor.getString(1))
                    arrayListDetail.add(cursor.getString(2))
                    cursor.moveToNext()
                }
            }
        }catch(exception: Exception) {
            Log.e("selectData", exception.toString())
        }
    }

    private fun selectAll() {
        try{
            arrayListTitle.clear()
            arrayListDetail.clear()

            val databaseOpenHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = databaseOpenHelper.readableDatabase

            val sql = "select id, title, detail from " + tableName

            val cursor = database.rawQuery(sql,null)
            if(cursor.count > 0) {
                cursor.moveToFirst()
                while(!cursor.isAfterLast) {
                    arrayListId.add(cursor.getInt(0))
                    arrayListTitle.add(cursor.getString(1))
                    arrayListDetail.add(cursor.getString(2))
                    cursor.moveToNext()
                }
            }
        }catch(exception: Exception) {
            Log.e("selectAll", exception.toString())
        }
    }

    override fun onItemClick(view: View, position: Int) {
        //ホントは安全でないキャストはしないほうがいい
        val id_of_view = view.id_container.getTag(1) as Int
        selectData(id_of_view)
        val title_of_view = arrayListTitle[0]
        val detail_of_view = arrayListDetail[0]

        val edit_fragment = EditFragment.newInstance(title_of_view,detail_of_view)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container,edit_fragment)
        fragmentTransaction.commit()
    }
}
