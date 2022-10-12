package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_inquire.*

class VideoInquireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_inquire)

        val video=intent.getSerializableExtra("VideoInfo") as UserVideo
        tv_title.text=video.video_title
        tv_category.text=video.big_category+" / "+video.small_category
        //tv_keyword.text=video.video_keyword


        //카테고리 버튼을 클릭하는 경우
        imgbtn_category.setOnClickListener {
            val intent=Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        bt_cancel.setOnClickListener {
            val intent=Intent(this,VideoDetailActivity::class.java)
            startActivity(intent)
        }


        //TODO : DB에 문의 내용 저장하기
        bt_inquire.setOnClickListener {
            val inquire_title=et_inquire_title.text.toString()
            val inquire_content=et_inquire_content.text.toString()
            if(inquire_title.isEmpty() || inquire_content.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val intent= Intent(this,VideoInquireDoneActivity::class.java)
                startActivity(intent)
            }

        }

    }
}