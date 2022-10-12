package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_detail.*

class VideoDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        val video=intent.getSerializableExtra("VideoInfo") as UserVideo
        tv_title.text=video.video_title
        tv_category.text=video.big_category+" / "+video.small_category
        //tv_keyword.text=video.video_keyword
        tv_description.text=video.video_desc
        //비디오 유해성 판단 결과 (유해함, 유해하지 않음)에 따라 문의하기 버튼을 출력
        if(video.video_state==0){
            tv_result.text="적절하다고 판단됨"
            bt_inquire.setVisibility(View.GONE)
        }
        else{
            tv_result.text="부적절하다고 판단됨"
            bt_inquire.setVisibility(View.VISIBLE)
        }


        //삭제하기 버튼을 클릭시 팝업창 알람 띄우기
        bt_delete.setOnClickListener {
            val dialog=Alert_Delete(this)
            dialog.showDialog()

        }

        //수정하기 버튼을 클릭시 페이지 전환
        bt_modify.setOnClickListener {
            val intent=Intent(this, VideoEditActivity::class.java)
            startActivity(intent)
        }

        //문의하기 버튼을 클릭시 페이지 전환
        bt_inquire.setOnClickListener {
            val intent= Intent(this,VideoInquireActivity::class.java)
            intent.putExtra("VideoInfo",video)
            startActivity(intent)
        }

    }

}