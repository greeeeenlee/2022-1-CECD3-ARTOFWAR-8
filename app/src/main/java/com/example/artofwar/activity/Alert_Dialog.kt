package com.example.artofwar.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.alert_modify_ok.*


class Alert_Dialog (context: Context){
    val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }


    fun showDialog(id:Int)
    {
        dialog.setContentView(R.layout.alert_modify_ok)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        //id에 따라 다이얼로그의 내용을 변경
        if(id==1){
            dialog.content.text="필수항목은 모두 입력해야 합니다."
        }
        else if(id==2){
            dialog.content.text="비밀번호와 비밀번호 확인값이 동일하지 않습니다."
        }
        else if(id==3){
            dialog.content.text="아이디 중복체크를 진행해야 합니다."
        }
        else if(id==4){
            dialog.content.text="이미 사용중인 아이디입니다."
        }

        else if(id==5){
            dialog.content.text="아이디나 비밀번호를 입력하세요."
        }

        else if(id==6) {
            dialog.content.text="분류를 선택하세요."
        }

        else if(id==7){
            dialog.content.text="파일을 선택해야 합니다."
        }
        else if(id==8){
            dialog.content.text="내용을 입력하세요."
        }
        else if(id==9){
            dialog.content.text="존재하지 않는 아이디입니다."
        }
        else if(id==10){
            dialog.content.text="비밀번호를 잘못 입력했습니다."
        }
        else if(id==11){
            dialog.content.text="잠시 후 다시 시도해주십시오."
        }
        else if(id==12){
            dialog.content.text="비밀번호 길이는 8-24자리여야 합니다."
        }
        else if(id==13){
            dialog.content.text="업로드에 실패하였습니다."
        }


        dialog.btn_ok.setOnClickListener {
            dialog.dismiss()

        }


    }


    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}