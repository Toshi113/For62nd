package com.example.for62nd

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_edit.*

private const val KEY_ID = "ID"
private const val KEY_TITLE = "TITLE"
private const val KEY_DETAIL = "DETAIL"
private const val KEY_ISNEW = "ISNEW"

class EditFragment : Fragment() ,View.OnClickListener{

    private var Id: Int? = null
    private var Title: String? = null
    private var Detail: String? = null
    //新規の場合はtrue,編集の場合はfalse
    private var IsNew: Boolean? = null

    private var listener: FragmentListener? = null

    interface FragmentListener {
        fun onRemoved(id: Int?, title: String?, detail: String?, isNew: Boolean?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Id = it.getInt(KEY_ID)
            Title = it.getString(KEY_TITLE)
            Detail = it.getString(KEY_DETAIL)
            IsNew = it.getBoolean(KEY_ISNEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_edit, container, false)
        var editText_title:EditText = view.findViewById(R.id.editText_tite)
        var editText_detail:EditText = view.findViewById(R.id.editText_detail)
        var button_save: Button = view.findViewById(R.id.button_save)
        button_save.setOnClickListener(this)
        editText_title.setText(Title)
        editText_detail.setText(Detail)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int,title: String, detail: String, is_new: Boolean) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID, id)
                    putString(KEY_TITLE, title)
                    putString(KEY_DETAIL, detail)
                    putBoolean(KEY_ISNEW,is_new)
                }
            }
    }

    override fun onClick(v: View?) {
        //TODO ここにFragment終了処理andデータベースに保存(MainActivityの方に値を返す)
        fragmentManager!!.beginTransaction().remove(this).commit()
    }

    override fun onDestroy() {
        Log.i("INFORMATION","onDestroy")
        super.onDestroy()
        //TODO あとはここのtextをうまく取得するだけ!!
        listener?.onRemoved(Id, editText_tite.text.toString(), editText_detail.text.toString(),IsNew)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as MainActivity
    }
}
