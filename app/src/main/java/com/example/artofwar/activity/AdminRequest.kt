package com.example.artofwar.activity

import okhttp3.Address
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AdminRequest {
    //관리자 - 답변하지 않은 문의 개수
    @Headers("content-type:application/json")
    @GET("/aInquire/nullQ")
    fun countUserInquire(@Header("Authorization") token:String?): Call<CountInquire>
    object RetrofitAPI1{

        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient: OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level= HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit: Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI1.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI1.okHttpClient)
                .build()
        }
        val emgMedService:AdminRequest by lazy{
            retrofit.create(AdminRequest::class.java)
        }
    }

    //관리자 문의 리스트
    @Headers("content-type:application/json")
    @GET("/aInquire/getList")
    fun getAdminInquireList(
        @Header("Authorization") token: String?
    ):Call<inquirelist>
    object RetrofitAPI2{
        private const val BASE_URL="http://54.219.68.194"

        val okHttpClient:OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(RetrofitAPI2.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI2.okHttpClient)
                .build()
        }
        val emgMedService:AdminRequest by lazy{
            retrofit.create(AdminRequest::class.java)
        }
    }

    //문의 내용 확인
    @Headers("content-type:application/json")
    @GET("/aInquire/manage/{qid}")
    fun getAdminInquireInfo(
        @Header("Authorization") token: String?,
        @Path("qid") qid:String?
    ):Call<AdminInquireResponse>
    object RetrofitAPI3{
        private const val BASE_URL="http://54.219.68.194"

        val okHttpClient:OkHttpClient by lazy {
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
        val emgMedService:AdminRequest by lazy{
            retrofit.create(AdminRequest::class.java)
        }
    }

    //문의 내용 답변
    @Headers("content-type:application/json")
    @PATCH("/aInquire/manage/{qid}")
    fun setAnswer(
        @Header("Authorization") token:String?,
        @Body data:AdminAnswerRequest?,
        @Path ("qid") qid: String?
    ):Call<AdminAnswerResponse>
    object RetrofitAPI4{

        private const val BASE_URL="http://54.219.68.194"

        val okHttpClient:OkHttpClient by lazy{
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
        val emgMedService:AdminRequest by lazy{
            retrofit.create(AdminRequest::class.java)
        }
    }


}