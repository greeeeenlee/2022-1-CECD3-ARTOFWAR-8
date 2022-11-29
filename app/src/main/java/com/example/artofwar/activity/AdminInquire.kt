package com.example.artofwar.activity

import java.io.Serializable

//관리자용 문의 내용
class AdminInquire (
    val qid:Int,
    val status:String,
    val status_detail:String,
    val name:String,
    val content:String,
    val question:String
): Serializable {

}