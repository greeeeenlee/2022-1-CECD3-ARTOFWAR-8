package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_inquire_list.*
import kotlinx.android.synthetic.main.activity_video_list.*

class Admin_InquireListActivity : AppCompatActivity() {

    val UserInquireList= arrayListOf<UserInquire>(
        UserInquire(123,"첫번째 문의","문의 내용1",111,"","20220202"),
        UserInquire(234,"두번째 문의","문의 내용2",222,"답변","20220202"),
        UserInquire(345,"세번째 문의","문의 내용3",333,"답변","20220202"),
        UserInquire(456,"네번째 문의","문의 내용4",444,"","20220202")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_inquire_list)


        val Adapter = AdminInquireAdapter(this, UserInquireList)
        listView_admin.adapter=Adapter

        //리스트뷰 클릭 이벤트
        listView_admin.setOnItemClickListener { adapterView, view, i, l ->
            val clickedInquire=UserInquireList[i]
            val intent=Intent(this,Admin_InquireActivity::class.java)
            intent.putExtra("InquireInfo",clickedInquire)
            startActivity(intent)
        }

    }


}