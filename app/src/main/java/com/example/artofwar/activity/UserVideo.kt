package com.example.artofwar.activity

import java.io.Serializable

//동영상 객체-사용자 아이디, 동영상 제목, 대카테고리, 소카테고리, 동영상 날짜, 동영상 id,
// 동영상 s3 url, 동영상 유해성 여부(0:중립, 1:이미지, 2:음성, 3:이미지+음성), 동영상 소개문, 동영상 키워드
class UserVideo (

    val uid:Int,
    val video_title: String,
    val big_category:String,
    val small_category:String,
    val video_upload_date:String,
    val vid:Int,
    val vurl:String,
    val video_state:Int,
    val video_desc:String

    ) :Serializable {

    }