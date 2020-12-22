package com.example.for62nd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val KEY_TITLE = "TITLE"
private const val KEY_DETAIL = "DETAIL"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() ,View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var Title: String? = null
    private var Detail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Title = it.getString(KEY_TITLE)
            Detail = it.getString(KEY_DETAIL)
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
        fun newInstance(title: String, detail: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TITLE, title)
                    putString(KEY_DETAIL, detail)
                }
            }
    }

    override fun onClick(v: View?) {
        //TODO ここにFragment終了処理andデータベースに保存(MainActivityの方に値を返す)
    }
}
