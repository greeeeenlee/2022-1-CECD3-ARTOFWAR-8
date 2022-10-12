package com.example.artofwar.activity

//import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import retrofit2.create
//import java.util.*

interface RegisterRequest {


    @Headers("content-type:application/json")
    @POST("/sign")
    fun addUserByEnqueue(@Body userInfo:SignUpRequestBody):Call<SignUpResponseBody>
    object RetrofitAPI{
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
        val emgMedService:RegisterRequest by lazy{
            retrofit.create(RegisterRequest::class.java)
        }
    }

    @Headers("content-type:application/json")
    @POST("/user")
    fun loginUserByEnqueue(@Body userInfo:LoginRequestBody):Call<LoginResponseBody>
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
        val emgMedService:RegisterRequest by lazy{
            retrofit.create(RegisterRequest::class.java)
        }
    }

    @Headers("content-type:application/json")
    @GET("/sign/{uid}")
    fun existUserID(@Path("uid") uid:String):Call<IsExisted>
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
        val emgMedService:RegisterRequest by lazy{
            retrofit.create(RegisterRequest::class.java)
        }
    }

//    companion object {
//        var gson = GsonBuilder()
//            .setLenient()
//            .create()
//
//        fun create(): RegisterRequest {
//
//            val retrofit = Retrofit.Builder()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl("http://54.219.68.194")
//                .build()
//
//            return retrofit.create(RegisterRequest::class.java)
//        }
//    }

}