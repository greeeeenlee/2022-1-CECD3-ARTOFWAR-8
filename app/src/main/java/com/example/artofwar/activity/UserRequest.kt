package com.example.artofwar.activity


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call


interface UserRequest {

    @Headers("content-type:application/json")
    @GET("/main/user")
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
    @Headers("content-type:application/json")
    @GET("/main/admin")
    fun countUserInquire(@Header("Authorization") token:String?):Call<CountInquire>
    object RetrofitAPI2{

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
                .baseUrl(RetrofitAPI2.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI2.okHttpClient)
                .build()
        }
        val emgMedService:UserRequest by lazy{
            retrofit.create(UserRequest::class.java)
        }
    }


}