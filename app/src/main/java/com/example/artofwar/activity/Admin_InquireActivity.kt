package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_inquire.*
import retrofit2.Call
import retrofit2.Response

class Admin_InquireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_inquire)

        //TODO : DB에서 사용자아이디 받아오기

        val access_token=intent.getStringExtra("access_token")
        val inquire=intent.getSerializableExtra("InquireInfo") as AdminInquire
        val inquire_qid=inquire.qid.toString()
        lateinit var video_storage_url:String

        val service=AdminRequest.RetrofitAPI3.emgMedService
        service.getAdminInquireInfo(access_token,inquire_qid)
            .enqueue(object :retrofit2.Callback<AdminInquireResponse>{
                override fun onResponse(
                    call: Call<AdminInquireResponse>,
                    response: Response<AdminInquireResponse>
                ) {
                    Log.d("LOG_test_inquire_1",response.body()?.message.toString())
                    val inquiredata=response.body()?.message
                    tv_inquire_id.text=inquiredata?.userID
                    tv_inquire_title.text=inquiredata?.name
                    tv_inquire_content.text=inquiredata?.content
                    video_storage_url=inquiredata?.storage_url!!

                }

                override fun onFailure(call: Call<AdminInquireResponse>, t: Throwable) {
                    //실패
                    val dialog=Alert_Dialog(this@Admin_InquireActivity)
                    dialog.showDialog(11)
                    Log.d("LOG_test_fail",t.message.toString())
                }

            })


        bt_video.setOnClickListener {
            val intent=Intent(this,VideoPlayerActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.putExtra("InquireInfo",inquire)
            intent.putExtra("video_storage_url",video_storage_url)
            startActivity(intent)
        }

        //취소하기 버튼을 클릭하는 경우
        bt_cancel.setOnClickListener {
            val intent=Intent(this,Admin_InquireListActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //보내기 버튼을 클릭하는 경우
        bt_answer.setOnClickListener {
            val answer=et_answer.text.toString()
            if(answer.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val responsedata=AdminAnswerRequest(answer)
                val service=AdminRequest.RetrofitAPI4.emgMedService
                service.setAnswer(access_token,responsedata,inquire_qid)
                    .enqueue(object :retrofit2.Callback<AdminAnswerResponse>{
                        override fun onResponse(
                            call: Call<AdminAnswerResponse>,
                            response: Response<AdminAnswerResponse>
                        ) {
                            Log.d("LOG_test_admin",response.body().toString())
                            if(response.body()?.message.toString()=="SUCCESS"){
                                val intent=Intent(this@Admin_InquireActivity,Admin_InquireDoneActivity::class.java)
                                intent.putExtra("access_token",access_token)
                                startActivity(intent)
                            }
                            else{
                                val dialog=Alert_Dialog(this@Admin_InquireActivity)
                                dialog.showDialog(17)
                            }
                        }

                        override fun onFailure(call: Call<AdminAnswerResponse>, t: Throwable) {
                            //실패
                            val dialog=Alert_Dialog(this@Admin_InquireActivity)
                            dialog.showDialog(11)
                            Log.d("LOG_test_fail",t.message.toString())
                        }

                    })

            }
        }
    }
}