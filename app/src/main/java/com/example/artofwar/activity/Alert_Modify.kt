package com.example.artofwar.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.alert_modify_ok.*


class Alert_Modify (context: Context){
    val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }


    fun showDialog()
    {
        dialog.setContentView(R.layout.alert_modify_ok)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()



        //TODO : 수정하기 클릭시 DB에서 수정 진행
        dialog.btn_ok.setOnClickListener {
            dialog.dismiss()
            val intent=Intent(dialog.context,VideoDetailActivity::class.java)
            startActivity(dialog.context,intent,null)

        }


    }


    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}