package com.example.for62nd

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception


class MainFragment : Fragment(),RecyclerViewHolder.ItemClickListener, View.OnClickListener{

    private val dbName: String = "MemoDB"
    private val tableName: String = "MemoTable"
    private val dbVersion: Int = 1
    private var arrayListId: ArrayList<Int> = arrayListOf()
    private var arrayListTitle: ArrayList<String> = arrayListOf()
    private var arrayListDetail: ArrayList<String> = arrayListOf()

    private var m_recyclerView_main: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        val m_floatingActionButton_main: FloatingActionButton = view.findViewById(R.id.floatingActionButton_main)
        m_recyclerView_main = view.findViewById(R.id.recyclerView_main)
        m_floatingActionButton_main.setOnClickListener(this)
        initRecyclerView()
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private fun initRecyclerView(){
        selectAll()
        var data = mutableListOf<MemoStructure>()
        repeat(arrayListTitle.count()) {
            data.add(MemoStructure(arrayListId[it],arrayListTitle[it],arrayListDetail[it]))
        }
        m_recyclerView_main?.adapter = RecyclerAdapter(activity!!.applicationContext,this,data)
        m_recyclerView_main?.layoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    private fun selectData(row: Int) {
        try{
            arrayListId.clear()
            arrayListTitle.clear()
            arrayListDetail.clear()

            val databaseOpenHelper = MemoDatabaseOpenHelper(activity!!.applicationContext,dbName,null,dbVersion)
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

            val databaseOpenHelper = MemoDatabaseOpenHelper(activity!!.applicationContext,dbName,null,dbVersion)
            val database = databaseOpenHelper.readableDatabase

            val sql = "select id, title, detail from $tableName"

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

    private fun getLastId(): Int? {
        try{
            arrayListId.clear()
            val databaseOpenHelper = MemoDatabaseOpenHelper(activity!!.applicationContext,dbName,null,dbVersion)
            val database = databaseOpenHelper.readableDatabase

            val sql = "select id from $tableName"
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
        //各RecyclerViewのアイテムが押されたときの処理。編集画面に移行
        val idOfView = view.findViewById<Space>(R.id.id_container).getTag() as Int
        val titleOfView = view.findViewById<TextView>(R.id.title_textView).text.toString()
        val detailOfView = view.findViewById<TextView>(R.id.detail_textView).text.toString()

        val editFragment = EditFragment.newInstance(idOfView,titleOfView,detailOfView,false)
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.replace(R.id.fragment_container, editFragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View?) {
        //新規ボタンが押された処理。新規の編集画面へ移行
        val newID = getLastId()!! + 1
        val editFragment = EditFragment.newInstance(newID,"","",true)
        val fragmentTransaction =fragmentManager!!.beginTransaction()
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.replace(R.id.fragment_container, editFragment)
        fragmentTransaction.commit()
    }

}

