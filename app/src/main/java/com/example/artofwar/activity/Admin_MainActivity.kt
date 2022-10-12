package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.activity_admin_main.tv_date
import kotlinx.android.synthetic.main.activity_admin_main.tv_videocount
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class Admin_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val access_token="jwt "+intent.getStringExtra("access_token")
        if (access_token != null) {
            Log.d("LOG",access_token)
        }

        //문의 개수
        val service=UserRequest.RetrofitAPI2.emgMedService
        service.countUserInquire(access_token)
            .enqueue(object :retrofit2.Callback<CountInquire>{
                override fun onResponse(call: Call<CountInquire>, response: Response<CountInquire>){
                    if(response.isSuccessful){
                        //Log.d("LOG",response.body()?.count.toString())
                        tv_videocount.text=response.body()?.count.toString()+"개"
                    }
                }
                override fun onFailure(call: Call<CountInquire>, t:Throwable){
                    // 실패
                    Log.d("LOG",t.message.toString())
                    Log.d("LOG","fail")
                }
            })




        //현재 날짜
        val currentTime=System.currentTimeMillis()
        val sdf= SimpleDateFormat("yyyy-MM-dd")
        val nowdate=sdf.format(currentTime)
        tv_date.text=nowdate

        bt_answer.setOnClickListener {
            val intent=Intent(this,Admin_InquireListActivity::class.java)
            startActivity(intent)
        }
    }
}