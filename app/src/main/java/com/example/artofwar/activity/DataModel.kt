package com.example.artofwar.activity

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class SignUpRequestBody(
    @SerializedName("ID")
    val id:String?,
    @SerializedName("pwd")
    val pwd:String?,
    @SerializedName("name")
    val name:String?
)

data class SignUpResponseBody(
    @SerializedName("message")
    val message:String?
)

data class LoginRequestBody(
    @SerializedName("ID")
    val id:String?,
    @SerializedName("pwd")
    val pwd:String?
)

data class LoginResponseBody(
    @SerializedName("access_token")
    val access_token: String?
)

data class IsExisted(
    @SerializedName("message")
    val message:String?
)

data class CountVideo(
    @SerializedName("message")
    val message: Int?
)

data class CountInquire(
    @SerializedName("message")
    val message: Int?
)
data class UserNameResponseBody(
    @SerializedName("name")
    val name: String?,
    @SerializedName("id")
    val id: String?
)

data class UserNameResponseBody2(
    @SerializedName("message")
    val message:UserNameResponseBody
)

data class UploadResponseBody(
    @SerializedName("message")
    val message:String?
)


//회원정보 변경에 대한 내용
data class changePwd(
    @SerializedName("pwd")
    val pwd:String?
)
data class changePwdResponse(
    @SerializedName("detail")
    val detail:String?
)

//비디오 리스트 받아오기
data class vlist(
    @SerializedName("message")
    val videoInfo :JsonObject
)
data class inquirelist(
    @SerializedName("message")
    val inquireInfo:JsonObject
)

//문의 작성하기
data class InquireRequestBody(
    @SerializedName("name")
    val name: String?,
    @SerializedName("content")
    val content:String?
)
data class InquireResponseBody(
    @SerializedName("detail")
    val detail:String?
)

data class deleteResponseBody(
    @SerializedName("detail")
    val detail: String?
)

data class modifyRequestBody(
    @SerializedName("name")
    val name:String?,
    @SerializedName("introduction")
    val introduction:String?
)

data class modifyResponseBody(
    @SerializedName("detail")
    val detail: String?
)

data class InquireDetailBody(
    @SerializedName("name")
    val name:String?,
    @SerializedName("video_name")
    val video_name:String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("question")
    val question:String?
)
data class InquireDetailBody2(
    @SerializedName("message")
    val message: InquireDetailBody
)

data class DeleteInquireResponse(
    @SerializedName("message")
    val message: String?
)

data class LogoutResponse(
    @SerializedName("message")
    val message: String?
)

data class AdminInquireResponse(
    @SerializedName("message")
    val message: AdminInquireResponse2
)
data class AdminInquireResponse2(
    @SerializedName("userID")
    val userID:String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("video_name")
    val video_name: String?,
    @SerializedName("storage_key")
    val storage_key:String?,
    @SerializedName("content")
    val content:String?,
    @SerializedName("question")
    val question: String?,
    @SerializedName("storage_url")
    val storage_url: String?
)

data class AdminAnswerRequest(
    @SerializedName("question")
    val question: String?
)

data class AdminAnswerResponse(
    @SerializedName("message")
    val message: String?
)

data class ResendResponse(
    @SerializedName("message")
    val message: String?
)