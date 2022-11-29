package com.example.artofwar.activity


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call
interface RegisterRequest {


    //회원가입
    @Headers("content-type:application/json")
    @POST("/user/sign")
    fun addUserByEnqueue(@Body userInfo: SignUpRequestBody): Call<SignUpResponseBody>

    object RetrofitAPI {
        private const val BASE_URL = "http://54.219.68.194"

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        val emgMedService: RegisterRequest by lazy {
            retrofit.create(RegisterRequest::class.java)
        }
    }

    //로그인
    @Headers("content-type:application/json")
    @POST("/user/log")
    fun loginUserByEnqueue(@Body userInfo: LoginRequestBody): Call<LoginResponseBody>

    object RetrofitAPI2 {
        private const val BASE_URL = "http://54.219.68.194"

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(RetrofitAPI2.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI2.okHttpClient)
                .build()
        }
        val emgMedService: RegisterRequest by lazy {
            retrofit.create(RegisterRequest::class.java)
        }
    }

    //아이디 중복 체크
    @Headers("content-type:application/json")
    @GET("/user/checkID/{uid}")
    fun existUserID(@Path("uid") uid: String): Call<IsExisted>

    object RetrofitAPI3 {
        private const val BASE_URL = "http://54.219.68.194"

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(RetrofitAPI3.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPI3.okHttpClient)
                .build()
        }
        val emgMedService: RegisterRequest by lazy {
            retrofit.create(RegisterRequest::class.java)
        }
    }
}