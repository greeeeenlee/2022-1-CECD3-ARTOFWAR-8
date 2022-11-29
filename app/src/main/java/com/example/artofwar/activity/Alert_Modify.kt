package com.example.artofwar.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.alert_modify_ok.*
import retrofit2.Call
import retrofit2.Response

//동영상 수정
class Alert_Modify (context: Context,VideoInfo:UserVideo,access_token:String,newtitle:String,newintroduction:String){
    val context=context
    val dialog = Dialog(context)
    var VideoInfo=VideoInfo
    val access_token=access_token
    val newtitle=newtitle
    val newintroduction=newintroduction

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

        dialog.btn_ok.setOnClickListener {
            dialog.dismiss()
            VideoInfo.name=newtitle
            VideoInfo.introduction=newintroduction
            val intent=Intent(dialog.context,VideoDetailActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.putExtra("VideoInfo",VideoInfo)
            startActivity(dialog.context,intent,null)

        }

    }
    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}

//문의 수정
class Alert_Modify2 (context: Context,InquireInfo:UserInquire,access_token:String){
    val context=context
    val dialog = Dialog(context)
    val InquireInfo=InquireInfo
    val qid=InquireInfo.qid.toString()
    val access_token=access_token
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }


    fun showDialog()
    {
        dialog.setContentView(R.layout.alert_modify_ok2)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.btn_ok.setOnClickListener {

            dialog.dismiss()
            val intent=Intent(dialog.context,InquireDetailActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.putExtra("InquireInfo",InquireInfo)
            startActivity(dialog.context,intent,null)

        }

    }


    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}