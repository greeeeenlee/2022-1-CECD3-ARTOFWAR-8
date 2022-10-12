package com.example.artofwar.activity

import com.google.gson.annotations.SerializedName
import java.io.File

data class HTTP_GET_Model(
    var something : String? =null ,
    var users : ArrayList<UserModel>? =null
)

data class UserModel(
    var idx : Int? =null ,
    var id : String?=null,
    var name : String? =null
)

data class PostModel(
    @SerializedName("ID")
    val id:String?,
    @SerializedName("pwd")
    val pwd:String?,
    @SerializedName("name")
    val name:String?
)

data class PostResult(
    @SerializedName("result")
    val result:String?,
    @SerializedName("status")
    val status:String?
)

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
    @SerializedName("count")
    val count: Int?
)

data class UploadRequestBody(
    @SerializedName("name")
    val name:String?,
    @SerializedName("mjclass")
    val mjclass:String?,
    @SerializedName("subclass")
    val subclass:String?
)

data class UploadResponseBody(
    @SerializedName("message")
    val message:String?
)

data class PublishResponseBody(
    @SerializedName("message")
    val message:String?
)

//data class LoginResponseHeader(
//    @SerializedName("access_token")
//    val access_token:String?
//)

//data class UploadRequestBody(
//    @SerializedName("videoFile")
//
//)