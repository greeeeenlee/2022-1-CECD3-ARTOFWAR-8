package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Call
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val access_token=intent.getStringExtra("access_token")


        //유저 이름 받아오기
        val service=UserRequest.RetrofitAPI3.emgMedService
        service.getUserInfo(access_token)
            .enqueue(object :retrofit2.Callback<UserNameResponseBody2>{
                override fun onResponse(call: Call<UserNameResponseBody2>, response: Response<UserNameResponseBody2>){
                    Log.d("LOG_test_message",response.body()?.message.toString())
                    Log.d("LOG_test_id",response.body()?.message?.id.toString())
                    Log.d("LOG_test_name",response.body()?.message?.name.toString())
                    tv_username.setText(response.body()?.message?.name.toString()+"님")
                }
                override fun onFailure(call: Call<UserNameResponseBody2>, t:Throwable){
                    // 실패
                    Log.d("LOG_test_fail",t.message.toString())
                }
            })

        //로그아웃 버튼
        imgbtn_logout.setOnClickListener {
            val service2=UserRequest.RetrofitAPI5.emgMedService
            service2.logout(access_token)
                .enqueue(object :retrofit2.Callback<LogoutResponse>{
                    override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>){
                        Log.d("LOG_test",response.body().toString())
                        if(response.body()?.message.toString()=="Logout success"){
                            val intent=Intent(this@CategoryActivity,LoginActivity::class.java)
                            ActivityCompat.finishAffinity(this@CategoryActivity)
                            startActivity(intent)
                        }
                        else{
                            val dialog=Alert_Dialog(this@CategoryActivity)
                            dialog.showDialog(14)
                        }
                    }
                    override fun onFailure(call: Call<LogoutResponse>, t:Throwable){
                        // 실패
                        Log.d("LOG_test_fail",t.message.toString())
                    }
                })
        }

        //동영상 업로드
        ll_video_upload.setOnClickListener{
            val intent=Intent(this,UploadActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //동영상 관리
        ll_video_manage.setOnClickListener {
            val intent=Intent(this,VideoListActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //회원정보 수정
        ll_modify_profile.setOnClickListener {
            val intent=Intent(this,EditActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //1대1 문의
        ll_qna.setOnClickListener {
            val intent=Intent(this,InquireListActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //닫기
        imgbtn_close.setOnClickListener {
            finish()
        }
    }
}