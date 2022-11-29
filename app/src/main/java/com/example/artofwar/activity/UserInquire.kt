package com.example.artofwar.activity

import java.io.Serializable

//문의 내용 객체-동영상 아이디, 문의 제목, 문의 내용, 답변 내용
class UserInquire (    
    val qid:Int,
    val name:String,
    val content:String,
    val question:String
) : Serializable {

}

class UserInquireDetail (
    val qid:Int,
    val name:String,
    val content:String,
    val question:String,
    val video_name:String
) : Serializable {

}

