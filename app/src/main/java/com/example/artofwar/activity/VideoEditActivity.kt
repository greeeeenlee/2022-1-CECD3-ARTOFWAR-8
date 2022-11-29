
package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_edit.*
import retrofit2.Call
import retrofit2.Response

class VideoEditActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_edit)

        val video=intent.getSerializableExtra("VideoInfo") as UserVideo
        val access_token=intent.getStringExtra("access_token")

        et_title.setText(video.name)
        tv_division.text=video.mjclass+" / "+video.subclass
        if(video.introduction=="null"){
            et_description.setText("소개문 작문이 진행중입니다.")
        }
        else{
            et_description.setText(video.introduction)
        }
        if(video.status_detail=="null"){
            tv_result.text="판단이 진행중입니다."
        }
        else{
            val status_detail=video.status_detail?.toInt()
            Log.d("log_test_status_detail",status_detail.toString())

            if(status_detail==null || status_detail==0){
                tv_result.text="적절하다고 판단됨"
            }
            else if(status_detail in 1..10){
                tv_result.text="관리자 검토 필요 동영상 (이후 비공개 처리될 수 있음)"
            }
            else if(status_detail in 11..30){
                tv_result.text="유해 관심 동영상"
            }
            else if(status_detail in 31..50){
                tv_result.text="유해 주의 동영상"
            }
            else if(status_detail in 51..70){
                tv_result.text="유해 경계 동영상"
            }
            else if(status_detail in 71..100){
                tv_result.text="유해 심각 동영상"
            }
        }

        bt_cancel.setOnClickListener {
            val intent= Intent(this,VideoDetailActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.putExtra("VideoInfo",video)
            startActivity(intent)
        }

        bt_modify.setOnClickListener {
            val title=et_title.text.toString()
            val introduction=et_description.text.toString()

            if(title.isEmpty() || introduction.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val newvideodata=modifyRequestBody(title,introduction)
                val service=VideoRequest.RetrofitAPI4.emgMedService
                service.modifyVideo(access_token,newvideodata,video.storage_key)
                    .enqueue(object :retrofit2.Callback<modifyResponseBody>{
                        override fun onResponse(call: Call<modifyResponseBody>, response: Response<modifyResponseBody>){
                            Log.d("log_test_modify",response.toString())
                            if(response.code().toString()!="200"){
                                val dialog=Alert_Dialog(this@VideoEditActivity)
                                dialog.showDialog(15)
                            }
                            else{
                                val dialog=Alert_Modify(this@VideoEditActivity,video,access_token!!,title,introduction)
                                dialog.showDialog()
                                //finish()
                            }
                        }
                        override fun onFailure(call: Call<modifyResponseBody>, t:Throwable){
                            // 실패
                            val dialog=Alert_Dialog(this@VideoEditActivity)
                            dialog.showDialog(11)
                            Log.d("LOG_test_fail",t.message.toString())
                        }
                    })
            }
        }
    }
}