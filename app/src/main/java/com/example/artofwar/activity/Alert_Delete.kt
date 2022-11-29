package com.example.artofwar.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_list.*
import kotlinx.android.synthetic.main.alert_delete.*
import kotlinx.android.synthetic.main.alert_delete_ok.*
import retrofit2.Call
import retrofit2.Response

//동영상 삭제
class Alert_Delete (context: Context,vid:String,access_token:String){
    val context=context
    val vid=vid
    val access_token=access_token
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
            intent.putExtra("access_token",access_token)
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

        dialog.btn_delete.setOnClickListener {
            Log.d("log_test_dialog","dialog_btn_delete_clicked")
            //동영상 삭제하기
            val service=VideoRequest.RetrofitAPI3.emgMedService
            service.deleteVideo(access_token,vid)
                .enqueue(object :retrofit2.Callback<deleteResponseBody>{
                    override fun onResponse(call: Call<deleteResponseBody>, response: Response<deleteResponseBody>){
                        Log.d("log_test_delete",response.toString())
                        if(response.code().toString()!="200"){
                            val dialog=Alert_Dialog(context)
                            dialog.showDialog(16)
                        }
                        else{
                            delete_ok(dialog.context)
                        }
                    }
                    override fun onFailure(call: Call<deleteResponseBody>, t:Throwable){
                        // 실패
                        val dialog=Alert_Dialog(context)
                        dialog.showDialog(11)
                        Log.d("LOG_test_fail",t.message.toString())
                    }
                })
            dialog.dismiss()
        }
        }

    }

//문의 삭제
class Alert_Delete2 (context: Context,qid:String,access_token:String){
    val context=context
    val qid=qid
    val access_token=access_token
    val dialog = Dialog(context)

    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }
    fun delete_ok(context: Context){
        val dialog_ok=Dialog(context)
        dialog_ok.setContentView(R.layout.alert_delete_ok2)
        dialog_ok.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog_ok.setCanceledOnTouchOutside(true)
        dialog_ok.setCancelable(true)
        dialog_ok.show()

        dialog_ok.btn_ok.setOnClickListener {
            dialog_ok.dismiss()

            //화면 전환
            val intent=Intent(dialog.context,InquireListActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(dialog.context,intent,null)
        }
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.alert_delete2)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()


        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_delete.setOnClickListener {
            Log.d("log_test_dialog","dialog_btn_delete_clicked")
            //문의 삭제하기
            val service=VideoRequest.RetrofitAPI8.emgMedService
            service.deleteInquire(access_token,qid)
                .enqueue(object :retrofit2.Callback<DeleteInquireResponse>{
                    override fun onResponse(call: Call<DeleteInquireResponse>, response: Response<DeleteInquireResponse>){
                        Log.d("log_test_Inquiredelete",response.toString())
                        if(response.code().toString()!="200"){
                            val dialog=Alert_Dialog(context)
                            dialog.showDialog(16)
                        }
                        else{
                            delete_ok(dialog.context)
                        }
                    }
                    override fun onFailure(call: Call<DeleteInquireResponse>, t:Throwable){
                        // 실패
                        val dialog=Alert_Dialog(context)
                        dialog.showDialog(11)
                        Log.d("LOG_test_fail",t.message.toString())
                    }
                })
            dialog.dismiss()
        }
    }
}

interface OnDialogClickListener
{
    fun onClicked(name: String)
}