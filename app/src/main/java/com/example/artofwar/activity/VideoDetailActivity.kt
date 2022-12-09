package com.example.artofwar.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_detail.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*



class VideoDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        val access_token=intent.getStringExtra("access_token")
        val video=intent.getSerializableExtra("VideoInfo") as UserVideo
        var isJudged=false

        val video_uploadtime=timetoString(video.upload_time.toString()) //비디오 업로드 시간
        
        //현재 날짜와 시간
        val currentTime=System.currentTimeMillis()
        val sdf= SimpleDateFormat("yyyyMMddHHmmss")
        val nowtime=sdf.format(currentTime)
        //Log.d("LOG_test_nowtime",nowtime)

        //카테고리 버튼을 클릭하는 경우
        imgbtn_category.setOnClickListener {
            val intent=Intent(this, CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        tv_title.text=video.name.toString()
        tv_category.text=video.mjclass.toString()+" / "+video.subclass.toString()

        //비디오 유해성 판단 결과 (유해함, 유해하지 않음)에 따라 문의하기 버튼을 출력
        //0: 중립, 1: 이미지 유해, 2:음성유해, 3: 판단중
        if(video.status_detail=="null"){
            isJudged=false
            val dif=nowtime.toLong()-video_uploadtime.toLong()
            Log.d("LOG_test_dif",dif.toString())
            if(dif>10000){//한시간이 경과된 경우
                tv_result.text="판단이 진행되지 않았습니다."
                bt_inquire.text="요청 재전송"
            }
            else{//한시간이 안지난 경우
                tv_result.text="판단이 진행중입니다."
                bt_inquire.setVisibility(View.GONE)
            }
        }
        else{
            isJudged=true
            val status_detail=video.status_detail?.toInt()
            Log.d("log_test_status_detail",status_detail.toString())

            if(status_detail==null || status_detail==0){
                tv_result.text="적절하다고 판단됨"
                bt_inquire.setVisibility(View.GONE)
            }
            else if(status_detail in 1..10){
                tv_result.text="관리자 검토 필요 동영상 (이후 비공개 처리될 수 있음)"
                bt_inquire.setVisibility(View.GONE)
            }
            else if(status_detail in 11..30){
                tv_result.text="유해 관심 동영상"
                bt_inquire.setVisibility(View.VISIBLE)
            }
            else if(status_detail in 31..50){
                tv_result.text="유해 주의 동영상"
                bt_inquire.setVisibility(View.VISIBLE)
            }
            else if(status_detail in 51..70){
                tv_result.text="유해 경계 동영상"
                bt_inquire.setVisibility(View.VISIBLE)
            }
            else if(status_detail in 71..100){
                tv_result.text="유해 심각 동영상"
                bt_inquire.setVisibility(View.VISIBLE)
            }
        }
        
        
        //소개문 작성이 안된 경우-동영상 유해성 판단이 진행이 안된 경우 or 유해한 동영상이라고 판단된 경우
        if(video.introduction=="null"){
            if(isJudged==false){//동영상 유해성 판단이 진행이 안된 경우
                tv_description.text="소개문 작문이 진행중입니다."
            }
            else{//동영상 판단이 진행되었고, 소개문이 작성 안된 경우->유해한 동영상
                tv_description.text="유해한 동영상으로 판단되어 소개문 작문이 진행되지 않습니다."
            }
        }
        else{
            video.introduction=add_n(video.introduction.toString())
            tv_description.text=add_n(video.introduction.toString())
        }

        //뒤로가기 버튼을 클릭시
        imgbtn_back.setOnClickListener {
            finish()
        }
        imgbtn_home.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //결과 설명 버튼 클릭시
        imgbtn_resultdesc.setOnClickListener{
            val builder= AlertDialog.Builder(this)
            builder.setTitle("유해 결과 설명")
                .setMessage("유해성 판단은 이미지의 비율을 기준으로 판단됩니다. 판단 기준은 다음과 같습니다.\n\n11~30% 유해 관심 동영상 \n31~50% 유해 주의 동영상" +
                        "\n51~70% 유해 경계 동영상\n71~100% 유해 심각 동영상")
                builder.show()

        }
        //삭제하기 버튼을 클릭시 팝업창 알람 띄우기
        bt_delete.setOnClickListener {
            Log.d("log_click_btn","delete button clicked")
            val dialog=Alert_Delete(this@VideoDetailActivity,video.storage_key.toString(),access_token!!)
            dialog.showDialog()

        }

        //수정하기 버튼을 클릭시 페이지 전환
        bt_modify.setOnClickListener {
            val intent=Intent(this, VideoEditActivity::class.java)
            intent.putExtra("VideoInfo",video)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        //문의하기 버튼을 클릭시 페이지 전환
        bt_inquire.setOnClickListener {
            if(isJudged){//판단이 된 경우
                val intent= Intent(this,VideoInquireActivity::class.java)
                intent.putExtra("VideoInfo",video)
                intent.putExtra("access_token",access_token)
                startActivity(intent)
            }
            else{//판단이 안된 경우-판별 요청 재전송
                val service=VideoRequest.RetrofitAPI9.emgMedService
                service.resendJudge(access_token,video.storage_key)
                    .enqueue(object :retrofit2.Callback<ResendResponse>{
                        override fun onResponse(call: Call<ResendResponse>, response: Response<ResendResponse>){
                            Log.d("log_test_resend",response.toString())
                            if(response.code().toString()!="200"){
                                val dialog=Alert_Dialog(this@VideoDetailActivity)
                                dialog.showDialog(20)
                            }
                            else{
                                val dialog=Alert_Dialog(this@VideoDetailActivity)
                                dialog.showDialog(21)
                            }
                        }
                        override fun onFailure(call: Call<ResendResponse>, t:Throwable){
                            // 실패
                            val dialog=Alert_Dialog(this@VideoDetailActivity)
                            dialog.showDialog(11)
                            Log.d("LOG_test_fail",t.message.toString())
                        }
                    })
            }

        }
    }


    private fun timetoString(time :String):String{
        var result=time
        result=result.replace("-","")
        result=result.replace(":","")
        result=result.replace("T","")
        result=result.replace("Z","")
        result=result.replace(" ","")


        return result
    }
    
    //소개문 줄바꿈 표현하는 함수
    private fun add_n(introduction:String):String{
        var result=introduction
        result=introduction.replace("n","\n")
        return result
    }
}
