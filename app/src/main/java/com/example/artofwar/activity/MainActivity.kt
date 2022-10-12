package com.example.artofwar.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat




class MainActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val access_token=intent.getStringExtra("access_token")
        if (access_token != null) {
            Log.d("LOG",access_token)
        }


        //올린 동영상 개수
        val service=UserRequest.RetrofitAPI.emgMedService
        service.countUserVideo(access_token)
            .enqueue(object :retrofit2.Callback<CountVideo>{
                override fun onResponse(call: Call<CountVideo>, response: Response<CountVideo>){
                    if(response.isSuccessful){
                        tv_videocount.text=response.body()?.message.toString()+"개"
                    }
                }
                override fun onFailure(call: Call<CountVideo>,t:Throwable){
                    // 실패
                    Log.d("LOG_test",t.message.toString())
                    Log.d("LOG_test","fail")
                }
            })


        //현재 날짜
        val currentTime=System.currentTimeMillis()
        val sdf=SimpleDateFormat("yyyy-MM-dd")
        val nowdate=sdf.format(currentTime)
        tv_date.text=nowdate



        //동영상 업로드 버튼을 클릭한 경우
        bt_videoupload.setOnClickListener {
            val intent=Intent(this,UploadActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }
        //동영상 관리 버튼을 클릭한 경우
        bt_videomanage.setOnClickListener {
            val intent=Intent(this, VideoListActivity::class.java)
            startActivity(intent)
        }

        //하단 네비게이션 연결
        ll_home.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }
        ll_info.setOnClickListener {
            val intent=Intent(this,EditActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }
        ll_inquire.setOnClickListener {
            val intent=Intent(this,InquireListActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

    }
}