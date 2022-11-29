package com.example.artofwar.activity


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call


interface UserRequest {

    //유저별 동영상 개수
    @Headers("content-type:application/json")
    @GET("/video/getNum")
    fun countUserVideo(@Header("Authorization") token:String?):Call<CountVideo>
    object RetrofitAPI{

        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI.okHttpClient)
                .build()
        }
        val emgMedService:UserRequest by lazy{
            retrofit.create(UserRequest::class.java)
        }
    }

    //유저 정보 반환
    @Headers("content-type:application/json")
    @GET("/user/getInfo")
    fun getUserInfo(@Header("Authorization") token:String?):Call<UserNameResponseBody2>
    object RetrofitAPI3{

        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI3.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI3.okHttpClient)
                .build()
        }
        val emgMedService:UserRequest by lazy{
            retrofit.create(UserRequest::class.java)
        }
    }


    //비밀번호 변경
    @Headers("content-type:application/json")
    @POST("/user/changepwd")
    fun changePassword(
        @Header ("Authorization") token: String?,
        @Body data:changePwd
    ):Call<changePwdResponse>
    object RetrofitAPI4{
        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI4.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI4.okHttpClient)
                .build()
        }
        val emgMedService:UserRequest by lazy{
            retrofit.create(UserRequest::class.java)
        }
    }

    //로그아웃
    @Headers("content-type:application/json")
    @GET("/user/log")
    fun logout(@Header("Authorization") token:String?):Call<LogoutResponse>
    object RetrofitAPI5{

        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI5.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI5.okHttpClient)
                .build()
        }
        val emgMedService:UserRequest by lazy{
            retrofit.create(UserRequest::class.java)
        }
    }

}