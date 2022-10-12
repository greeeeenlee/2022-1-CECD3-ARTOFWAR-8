package com.example.artofwar.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.artofwar.R

class UserVideoAdapter (val context: Context, val UserVideoList: ArrayList<UserVideo>) : BaseAdapter() {
    override fun getCount(): Int {
        return UserVideoList.size
    }

    override fun getItem(position: Int): Any {
        return UserVideoList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_video,null)

        //val video_image = view.findViewById<ImageView>(R.id.iv_video_image)
        val title=view.findViewById<TextView>(R.id.tv_video_title)
        val upload_date=view.findViewById<TextView>(R.id.tv_video_upload_date)
        val state=view.findViewById<TextView>(R.id.tv_video_state)

        val video=UserVideoList[position]

        //TO DO : 비디오 썸네일 부분 구현해야함...
        //video_image.setImageResource(video.video_image)
        title.text=video.video_title
        upload_date.text=video.video_upload_date

        //비디오 유해성 판단에 따라 공개상태가 결정됨
        val video_state=video.video_state

        if(video_state==0){
            state.text="공개"
        }
        else if(video_state==1 || video_state==2 || video_state==3){
            state.text="비공개"
        }
        else{
            state.text="심사중"
        }


        return view
    }
}

