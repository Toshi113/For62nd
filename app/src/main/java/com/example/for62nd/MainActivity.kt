package com.example.for62nd

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), RecyclerViewHolder.ItemClickListener, EditFragment.FragmentListener, View.OnClickListener{
    private val dbName: String = "MemoDB"
    private val tableName: String = "MemoTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<Int> = arrayListOf()
    private var arrayListTitle: ArrayList<String> = arrayListOf()
    private var arrayListDetail: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floatingActionButton_main.setOnClickListener(this)
        initActivity()
        Log.i("INFORMATION","onCreate")
    }

    private fun initActivity(){
        selectAll()
        var data = mutableListOf<MemoStructure>()
        repeat(arrayListTitle.count()) {
            data.add(MemoStructure(arrayListId[it],arrayListTitle[it],arrayListDetail[it]))
        }
        recyclerView_main.adapter = RecyclerAdapter(this,this,data)
        recyclerView_main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Log.i("INFORMATION","initActivity")
        Log.i("INFORMATION",data.count().toString())
    }

    private fun insertData(id: Int, title: String, detail: String) {
        Log.i("INFORMATION","insertData")
        try{
            val dbHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("id",id)
            values.put("title",title)
            values.put("detail",detail)
            database.insertOrThrow(tableName,null,values)
            Log.i("INFORMATION",id.toString())
            Log.i("INFORMATION",title)
            Log.i("INFORMATION",detail)
        }catch (exception: Exception) {
            Log.e("insertData",exception.toString())
        }
    }

    private fun updateData(whereId: Int, newTitle: String, newDetail: String) {
        Log.i("INFORMATION","updateData")
        try{
            val dbHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("title",newTitle)
            values.put("detail",newDetail)

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId.toString())
            database.update(tableName, values, whereClauses, whereArgs)
            Log.i("INFORMATION",whereId.toString())
            Log.i("INFORMATION",newTitle)
            Log.i("INFORMATION",newDetail)
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
        Log.i("INFORMATION","selectAll")
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
            Log.i("INFORMATION",arrayListId.count().toString())
        }catch(exception: Exception) {
            Log.e("selectAll", exception.toString())
        }
    }

    private fun getLastId(): Int? {
        Log.i("INFORMATION","getLastId")
        try{
            arrayListId.clear()
            val databaseOpenHelper = MemoDatabaseOpenHelper(applicationContext,dbName,null,dbVersion)
            val database = databaseOpenHelper.readableDatabase

            val sql = "select id from " + tableName
            val cursor = database.rawQuery(sql,null)
            if(cursor.count > 0) {
                cursor.moveToFirst()
                while(!cursor.isAfterLast) {
                    arrayListId.add(cursor.getInt(0))
                    cursor.moveToNext()
                }
            }
            return if(arrayListId.count() == 0) {
                0
            }else {
                arrayListId.max()
            }
        }catch (exception: Exception) {
            Log.e("getLastId", exception.toString())
            throw exception
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Log.i("INFORMATION","onItemClick")
        //ホントは安全でないキャストはしないほうがいい
        //各RecyclerViewのアイテムが押されたときの処理。編集画面に移行
        val id_of_view = view.id_container.getTag(1) as Int
        val title_of_view = view.title_textView.text.toString()
        val detail_of_view = view.detail_textView.text.toString()

        val edit_fragment = EditFragment.newInstance(id_of_view,title_of_view,detail_of_view,false)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container,edit_fragment)
        fragmentTransaction.commit()
    }

    override fun onRemoved(id: Int?, title: String?, detail: String?,isNew: Boolean?) {
        Log.i("INFORMATION","onRemoved")
        if(isNew!!){
            Log.i("INFORMATION",id!!.toString())
            Log.i("INFORMATION",title!!)
            Log.i("INFORMATION",detail!!)
            //isNewがtrue,つまり新規作成の場合
            insertData(id!!,title!!,detail!!)
            initActivity()
        }else{
            //isNewがfalse,つまり編集の場合
            updateData(id!!,title!!,detail!!)
            initActivity()
        }
    }

    override fun onClick(v: View?) {
        Log.i("INFORMATION","onClick")
        //新規ボタンが押された処理。新規の編集画面へ移行
        val newID = getLastId()!! + 1
        val edit_fragment = EditFragment.newInstance(newID,"","",true)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container,edit_fragment)
        fragmentTransaction.commit()
        Log.i("INFORMATION",newID.toString())
    }
}
