package com.example.artofwar.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.alert_delete.*
import kotlinx.android.synthetic.main.alert_delete_ok.*


class Alert_Delete (context: Context){
    val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }
    fun delete_ok(context: Context){
        val dialog_ok=Dialog(context)
        dialog_ok.setContentView(R.layout.alert_delete_ok)
        dialog_ok.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog_ok.setCanceledOnTouchOutside(true)
        dialog_ok.setCancelable(true)
        dialog_ok.show()

        dialog_ok.btn_ok.setOnClickListener {
            dialog_ok.dismiss()

            //화면 전환
            val intent=Intent(dialog.context,VideoListActivity::class.java)
            startActivity(dialog.context,intent,null)
        }
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.alert_delete)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()


        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

        //TODO : 삭제하기 클릭시 DB에서 삭제 진행
        dialog.btn_delete.setOnClickListener {

            delete_ok(dialog.context)
            dialog.dismiss()
        }

    }


    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}