package com.example.artofwar.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity :AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //버튼에 밑줄 넣기
        bt_register.paintFlags= bt_register.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        bt_register.text=getString(R.string.underlined_text)

        //로그인 버튼을 클릭한 경우
        bt_login.setOnClickListener{
            //아이디나 비밀번호를 입력하지 않은 상태에서 로그인 버튼을 클릭한 경우
            if(et_id.text.toString().isEmpty() || et_pw.text.toString().isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(5)
            }


            val userData=LoginRequestBody(
                et_id.text.toString(),
                et_pw.text.toString()
            )
            val service=RegisterRequest.RetrofitAPI2.emgMedService
            service.loginUserByEnqueue(userData)
                .enqueue(object:retrofit2.Callback<LoginResponseBody>{
                    override fun onResponse(
                        call: Call<LoginResponseBody>,
                        response: Response<LoginResponseBody>
                    ) {
                        //status에 따른 alert 메세지 출력
                        when(response.code().toString()){
                            "200"->{
                                //자동로그인
                                sharedPreferences=getSharedPreferences("other",0)
                                editor=sharedPreferences.edit()
                                editor.putString("id",et_id.text.toString())
                                editor.putString("pw",et_pw.text.toString())
                                editor.apply()



                                val intent=Intent(this@LoginActivity, MainActivity::class.java)
                                //val intent=Intent(this@LoginActivity, Admin_MainActivity::class.java)
                                intent.putExtra("access_token","jwt "+response.body()?.access_token.toString())
                                startActivity(intent)
                            }
                            "400"->{
                                val dialog=Alert_Dialog(this@LoginActivity)
                                dialog.showDialog(9)
                            }
                            "401"->{
                                val dialog=Alert_Dialog(this@LoginActivity)
                                dialog.showDialog(10)
                            }
                            "500"->{
                                val dialog=Alert_Dialog(this@LoginActivity)
                                dialog.showDialog(11)
                            }

                        }

                    }

                    override fun onFailure(call: Call<LoginResponseBody>, t: Throwable) {
                        Log.d("log",t.message.toString())
                        Log.d("log","fail")
                    }
                })



            //val intent=Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }


        //회원가입하기 버튼을 클릭한 경우
        bt_register.setOnClickListener{
            val intent1=Intent(this,RegisterActivity::class.java)
            startActivity(intent1)
        }

    }
}