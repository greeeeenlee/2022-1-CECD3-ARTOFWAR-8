package com.example.artofwar.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.artofwar.R

class AdminInquireAdapter (val context: Context, val UserInquireList: ArrayList<UserInquire>) : BaseAdapter() {
    override fun getCount(): Int {
        return UserInquireList.size
    }

    override fun getItem(position: Int): Any {
        return UserInquireList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_admin_inquire,null)

        //val video_image = view.findViewById<ImageView>(R.id.iv_video_image)
        val title=view.findViewById<TextView>(R.id.tv_admin_inquire_title)
        val upload_date=view.findViewById<TextView>(R.id.tv_admin_inquire_date)
        val state=view.findViewById<TextView>(R.id.tv_admin_error_point)

        val inquire=UserInquireList[position]


        //TO DO : 비디오 썸네일 부분 구현해야함...
        //video_image.setImageResource(video.video_image)

        title.text=inquire.inquire_title
        upload_date.text=inquire.inquire_date

        //비디오 유해성 판단 결과에 따라 에러 포인트가 결정됨
        //0:중립, 1:이미지, 2:음성, 3:이미지+음성
        val error_point=0
        if(error_point==0){
            state.text="중립"
        }
        else if(error_point==1){
            state.text="이미지 유해"
        }
        else if(error_point==2){
            state.text="음성 유해"
        }
        else{
            state.text="이미지&음성 유해"
        }




        return view
    }
}

