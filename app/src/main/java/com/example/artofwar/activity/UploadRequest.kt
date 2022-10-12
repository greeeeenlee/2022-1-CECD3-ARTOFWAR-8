package com.example.artofwar.activity


import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File


interface UploadRequest {


    @Multipart
    @POST("/upload")
    fun uploadVideo(
        @Header("Authorization") Authorization:String?,
        @Part videoFile: MultipartBody.Part,
        @Part imageFile: MultipartBody.Part?,
        @Part ("name") name:String,
        @Part ("mjclass") mjclass:String,
        @Part ("subclass") subclass:String
    ):Call<UploadResponseBody>

    object RetrofitAPI{
        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply{
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val gson=GsonBuilder().setLenient().create()

        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }
        val emgMedService:UploadRequest by lazy{
            retrofit.create(UploadRequest::class.java)
        }
    }

    @Multipart
    @POST("/publish/")
    fun publish(
        @Part imageFile: MultipartBody.Part?,
        @Part ("name") name:String
    ):Call<PublishResponseBody>

    object RetrofitAPI2{
        private const val BASE_URL="http://54.219.68.194"

        private val okHttpClient:OkHttpClient by lazy{
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply{
                    level=HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }

        private val retrofit:Retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        val emgMedService:UploadRequest by lazy{
            retrofit.create(UploadRequest::class.java)
        }
    }


}