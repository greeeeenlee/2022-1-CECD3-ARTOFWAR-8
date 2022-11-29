package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_edit.*
import retrofit2.Call
import retrofit2.Response

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val access_token=intent.getStringExtra("access_token")

        //아이디와 이름 가져오기
        val service=UserRequest.RetrofitAPI3.emgMedService
        service.getUserInfo(access_token)
            .enqueue(object :retrofit2.Callback<UserNameResponseBody2>{
                override fun onResponse(call: Call<UserNameResponseBody2>, response: Response<UserNameResponseBody2>){
                    tv_etid.setText(response.body()?.message?.id.toString())
                    tv_etname.setText(response.body()?.message?.name.toString())
                }
                override fun onFailure(call: Call<UserNameResponseBody2>, t:Throwable){
                    // 실패
                    Log.d("LOG_test_fail",t.message.toString())
                }
            })


        //취소하기 버튼을 누른 경우
        bt_cancel.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }
        imgbtn_home.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        //수정하기 버튼을 누른 경우
        bt_modify.setOnClickListener {
            val pw1=et_pw.text.toString()
            val pw2=et_pw2.text.toString()

            //수정한 비밀번호의 값이 정확히 입력되었는지 확인
            if(pw1.isEmpty() || pw2.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(1)
            }
            else if(pw1!=pw2){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(2)
            }
            else if(pw1.length<8 || pw1.length>24){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(12)
            }
            else{
                val userPwd=changePwd(
                    pw1
                )
                val service=UserRequest.RetrofitAPI4.emgMedService
                    .changePassword(access_token,userPwd)
                    .enqueue(object:retrofit2.Callback<changePwdResponse>{
                        override fun onResponse( call: Call<changePwdResponse>, response: Response<changePwdResponse>) {
                            when(response.code().toString()){
                                "200"->{
                                    val intent=Intent(this@EditActivity,EditDoneActivity::class.java)
                                    intent.putExtra("access_token",access_token)
                                    startActivity(intent)
                                }
                                "400","500"->{
                                    val dialog=Alert_Dialog(this@EditActivity)
                                    dialog.showDialog(11)
                                }
                            }
                        }

                        override fun onFailure(call: Call<changePwdResponse>, t: Throwable) {
                            Log.d("LOG_test_fail_GET",t.message.toString())
                        }

                    })

            }
        }
    }
}