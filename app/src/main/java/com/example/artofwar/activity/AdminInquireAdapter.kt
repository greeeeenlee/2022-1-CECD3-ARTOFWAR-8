package com.example.artofwar.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.artofwar.R

class AdminInquireAdapter (val context: Context, val AdminInquireList: ArrayList<AdminInquire>) : BaseAdapter() {
    override fun getCount(): Int {
        return AdminInquireList.size
    }

    override fun getItem(position: Int): Any {
        return AdminInquireList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_admin_inquire,null)

        //val video_image = view.findViewById<ImageView>(R.id.iv_video_image)
        val title=view.findViewById<TextView>(R.id.tv_admin_inquire_title)
        val status=view.findViewById<TextView>(R.id.tv_admin_status)
        val status_detail=view.findViewById<TextView>(R.id.tv_admin_status_detail)

        val inquire=AdminInquireList[position]


        //TO DO : 비디오 썸네일 부분 구현해야함...
        //video_image.setImageResource(video.video_image)

        title.text=inquire.name
        //upload_date.text=inquire.inquire_date

        //비디오 유해성 판단 결과에 따라 에러 포인트가 결정됨
        //0:중립, 1:이미지, 2:음성, 3:이미지+음성
        val inquire_status=inquire.status.toInt()
        val inquire_status_detail=inquire.status_detail

        if(inquire_status==0){
            status.text="중립"
        }
        else if(inquire_status==1){
            status.text="이미지 유해"
        }
        else if(inquire_status==2){
            status.text="음성 유해"
        }
        else{
            status.text="판단중"
        }

        status_detail.text="동영상의 "+inquire_status_detail+"% 유해"

        return view
    }
}

