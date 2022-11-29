package com.example.artofwar.activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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

        val video_image = view.findViewById<ImageView>(R.id.iv_video_image)
        val title=view.findViewById<TextView>(R.id.tv_video_title)
        val upload_date=view.findViewById<TextView>(R.id.tv_video_upload_date)
        val state=view.findViewById<TextView>(R.id.tv_video_state)


        val video=UserVideoList[position]
        var imageName=video.storage_key.toString()
        imageName=imageName.replace(".mp4","")
        var image_ext=video.image_ext.toString()

        //뒤에 mp4삭제 앞에 image_붙이기
        var imgsrc="https://aowbuket.s3.us-west-1.amazonaws.com/image_"+imageName+"."+image_ext
        Glide.with(context)
            .load(imgsrc)
            .into(video_image)
        //video_image.setImageResource(video.video_image)
        title.text=video.name
        upload_date.text=video.upload_time?.substring(0,10)

        //비디오 유해성 판단에 따라 공개상태가 결정됨
        //0: 중립, 1: 이미지 유해, 2:음성유해, 3: 판단중
        val video_state=video.status

        if(video_state=="0"){
            state.text="공개"
        }
        else if(video_state=="1" || video_state=="2"){
            state.text="비공개"
        }
        else{
            state.text="판단중"
        }

        return view
    }
}

