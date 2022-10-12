package com.example.artofwar.activity

import java.io.Serializable

//문의 내용 객체-사용자아이디, 문의 제목, 문의 내용, 동영상 id, 관리자 답변, 문의날짜
class UserInquire (
    val uid:Int,
    val inquire_title:String,
    val inquire_content:String,
    val vid:Int,
    val inquire_response: String,
    val inquire_date:String
) : Serializable {

}