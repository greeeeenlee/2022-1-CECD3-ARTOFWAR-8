package com.example.artofwar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_inquire_list.*

class InquireListActivity : AppCompatActivity() {

    val UserInquireList= arrayListOf<UserInquire>(
        UserInquire(123,"첫번째 문의 제목","문의 내용",111,"관리자 답변","20220808"),
        UserInquire(456,"두번째 문의 제목","문의 내용",222,"","20220808"),
        UserInquire(789,"세번째 문의 제목","문의 내용", 333,"","20220808"),
        UserInquire(12,"네번째 문의 제목","문의 내용",444,"관리자 답변","20220808")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquire_list)

        val Adapter = UserInquireAdapter(this,UserInquireList)
        listView.adapter=Adapter



    }
}