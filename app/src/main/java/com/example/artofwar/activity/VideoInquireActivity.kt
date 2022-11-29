package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_inquire.*
import retrofit2.Call
import retrofit2.Response

class VideoInquireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_inquire)

        val video=intent.getSerializableExtra("VideoInfo") as UserVideo
        val access_token=intent.getStringExtra("access_token")

        tv_title.text=video.name
        tv_category.text=video.mjclass+" / "+video.subclass

        //카테고리 버튼을 클릭하는 경우
        imgbtn_category.setOnClickListener {
            val intent=Intent(this, CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        bt_cancel.setOnClickListener {
            val intent=Intent(this,VideoDetailActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.putExtra("VideoInfo",video)
            startActivity(intent)
        }


         bt_inquire.setOnClickListener {
            val inquire_title=et_inquire_title.text.toString()
            val inquire_content=et_inquire_content.text.toString()
            if(inquire_title.isEmpty() || inquire_content.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val inquiredata=InquireRequestBody(
                    inquire_title,inquire_content
                )
                val service=VideoRequest.RetrofitAPI2.emgMedService
                    .postInquire(access_token,inquiredata,video.storage_key)
                    .enqueue(object:retrofit2.Callback<InquireResponseBody>{
                        override fun onResponse(
                            call: Call<InquireResponseBody>,
                            response: Response<InquireResponseBody>
                        ) {
                            Log.d("LOG_test_response",response.toString())

                            when(response.code().toString()){
                                "200"->{
                                    val intent=Intent(this@VideoInquireActivity,VideoInquireDoneActivity::class.java)
                                    intent.putExtra("access_token",access_token)
                                    startActivity(intent)
                                }
                                "400","500"->{
                                    val dialog=Alert_Dialog(this@VideoInquireActivity)
                                    dialog.showDialog(11)
                                }
                            }
                        }

                        override fun onFailure(call: Call<InquireResponseBody>, t: Throwable) {
                            Log.d("LOG_test_fail_GET",t.message.toString())
                        }

                    })

            }

        }

    }
}