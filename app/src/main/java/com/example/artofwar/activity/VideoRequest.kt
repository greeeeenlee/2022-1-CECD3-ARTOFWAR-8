package com.example.artofwar.activity


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call


interface VideoRequest {

    //동영상 정보 요청
    @Headers("content-type:application/json")
    @GET("/video/getList")
    fun getVideoInfo(
        @Header("Authorization") token:String?
    ):Call<vlist>
    object RetrofitAPI{

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
                .baseUrl(RetrofitAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 문의 작성
    @Headers("content-type:application/json")
    @POST("/uInquire/create/{storage_key}")
    fun postInquire(
        @Header ("Authorization") token: String?,
        @Body data:InquireRequestBody,
        @Path("storage_key") storage_key: String?
    ):Call<InquireResponseBody>
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
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 삭제 요청
    @Headers("content-type:application/json")
    @GET("/video/delete/{storage_key}")
    fun deleteVideo(
        @Header("Authorization") token:String?,
        @Path ("storage_key") storage_key: String?
    ):Call<deleteResponseBody>
    object RetrofitAPI3{

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
                .baseUrl(RetrofitAPI3.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI3.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 정보 수정
    @Headers("content-type:application/json")
    @POST("/video/changeInfo/{storage_key}")
    fun modifyVideo(
        @Header("Authorization") token:String?,
        @Body data:modifyRequestBody,
        @Path ("storage_key") storage_key:String?
    ):Call<modifyResponseBody>
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
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //유저별 문의 리스트
    @Headers("content-type:application/json")
    @GET("/uInquire/getList")
    fun getInquireInfo(
        @Header("Authorization") token:String?
    ):Call<inquirelist>
    object RetrofitAPI5{
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
                .baseUrl(RetrofitAPI5.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI5.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 문의 상세정보
    @Headers("content-type:application/json")
    @GET("/uInquire/manage/{qid}")
    fun getInquiredetail(
        @Header("Authorization") token:String?,
        @Path ("qid") qid: String?
    ):Call<InquireDetailBody2>
    object RetrofitAPI6{

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
                .baseUrl(RetrofitAPI6.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI6.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 문의 수정
    @Headers("content-type:application/json")
    @PATCH("/uInquire/manage/{qid}")
    fun modifyInquire(
        @Header("Authorization") token:String?,
        @Body data:InquireRequestBody?,
        @Path ("qid") qid: String?
    ):Call<InquireResponseBody>
    object RetrofitAPI7{

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
                .baseUrl(RetrofitAPI7.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI7.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //동영상 삭제 요청
    @Headers("content-type:application/json")
    @GET("/uInquire/delete/{qid}")
    fun deleteInquire(
        @Header("Authorization") token:String?,
        @Path ("qid") qid: String?
    ):Call<DeleteInquireResponse>
    object RetrofitAPI8{

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
                .baseUrl(RetrofitAPI8.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI8.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }

    //판별 요청 재전송
    @Headers("content-type:application/json")
    @GET("/resend/{storage_key}")
    fun resendJudge(
        @Header("Authorization") token:String?,
        @Path ("storage_key") storage_key: String?
    ):Call<ResendResponse>
    object RetrofitAPI9{

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
                .baseUrl(RetrofitAPI9.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI9.okHttpClient)
                .build()
        }
        val emgMedService:VideoRequest by lazy{
            retrofit.create(VideoRequest::class.java)
        }
    }



}