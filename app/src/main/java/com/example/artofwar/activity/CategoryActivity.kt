package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        //TODO : 어떤 정보를 넘길지 -유저 아이디만?

        //동영상 업로드
        ll_video_upload.setOnClickListener{
            val intent=Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }

        //동영상 관리
        ll_video_manage.setOnClickListener {
            val intent=Intent(this,VideoListActivity::class.java)
            startActivity(intent)
        }

        //회원정보 수정
        ll_modify_profile.setOnClickListener {
            val intent=Intent(this,EditActivity::class.java)
            startActivity(intent)
        }

        //1대1 문의
        ll_qna.setOnClickListener {
            val intent=Intent(this,InquireListActivity::class.java)
            startActivity(intent)
        }

        //닫기
        imgbtn_close.setOnClickListener {
            finish()
        }
    }
}