package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_inquire_list.*
import kotlinx.android.synthetic.main.activity_video_list.*

class VideoListActivity : AppCompatActivity() {

    var UserVideoList = arrayListOf<UserVideo>(
        UserVideo(123,"첫번째 동영상","여가","참여 유도",
            "20220808",111,"vurl",0,"소개문1"),
        UserVideo(234,"두번째 동영상","운동","참여 유도",
            "20220808",222,"vurl",1,"소개문2",),
        UserVideo(345,"세번째 동영상","정보","정보 제공",
            "20220808",333,"vurl",2,"소개문3"),
        UserVideo(456,"네번째 동영상","문화 예술","정보 제공",
            "20220808",444,"vurl",11,"소개문4")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)


        val Adapter = UserVideoAdapter(this,UserVideoList)
        listView_video.adapter=Adapter

        //리스트뷰 클릭 이벤트
        listView_video.setOnItemClickListener { adapterView, view, i, l ->
            val clickedVideo=UserVideoList[i]
            val intent= Intent(this,VideoDetailActivity::class.java)
            intent.putExtra("VideoInfo",clickedVideo)
            startActivity(intent)
        }

    }
}