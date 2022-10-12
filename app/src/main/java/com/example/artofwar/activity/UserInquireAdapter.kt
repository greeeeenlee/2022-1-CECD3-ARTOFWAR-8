package com.example.artofwar.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.artofwar.R

class UserInquireAdapter (val context: Context, val UserInquireList: ArrayList<UserInquire>) : BaseAdapter() {
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
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_inquire,null)

        val inquire_title=view.findViewById<TextView>(R.id.tv_inquire_title)
        val video_title=view.findViewById<TextView>(R.id.tv_inquire_date)
        val inquire_state=view.findViewById<TextView>(R.id.tv_inquire_state)


        val inquire=UserInquireList[position]

        inquire_title.text=inquire.inquire_title
        video_title.text=inquire.inquire_date

        val inquire_response=inquire.inquire_response

        //아직 관리자 답변이 달리지 않은 경우에는 답변 상태를 답변 대기라 표현, 아닌 경우 답변 완료라 표현
        if(inquire_response==""){
            inquire_state.setBackgroundResource(R.drawable.layout_bt_stroke_grey_3)
            inquire_state.text="답변 대기"
        }
        else{
            inquire_state.text="답변 완료"
        }

        return view
    }
}

