package com.example.artofwar.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_register.*

import kotlinx.android.synthetic.main.activity_register.bt_register
import kotlinx.android.synthetic.main.activity_register.et_id
import kotlinx.android.synthetic.main.activity_register.et_pw
import retrofit2.Call
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        var id_check=false //아이디 중복 체크 버튼 사용 유무
        var id_unique=false //아이디 유일성 유무
        var isExistBlank=false //빈칸 유무
        var isPwSame=false //비밀번호 동일 입력 유무
        var isPwLength=false //비밀번호 길이 적합성 유무(8-24자리)

        //아이디를 입력하면 중복체크를 진행해야 한다고 알림
        tv_info_id.visibility=View.GONE

        et_id.setOnFocusChangeListener { view, hasFocus ->
            if (!et_id.text.toString().isEmpty()) {
                tv_info_id.visibility = View.VISIBLE
            }
        }

        //아이디 중복 체크 버튼 클릭시
        //중복체크를 진행해주세요. / 사용가능한 아이디입니다. 이미 존재하는 아이디 입니다.
        bt_id_check.setOnClickListener {
            val uid=et_id.text.toString()
            val service=RegisterRequest.RetrofitAPI3.emgMedService

            service.existUserID(uid)
                .enqueue(object:retrofit2.Callback<IsExisted>{
                    override fun onResponse(call:Call<IsExisted>,response:Response<IsExisted>)
                    {
                        when(response.code().toString()){
                            "200"->{
                                tv_info_id.text="사용가능한 아이디입니다."
                                tv_info_id.setTextColor(Color.parseColor("#FF9646"))
                                id_check=true
                                id_unique=true
                            }
                            "400"->{
                                tv_info_id.text="이미 존재하는 아이디입니다."
                                tv_info_id.setTextColor(Color.RED)
                                id_check=true
                                id_unique=false
                            }
                            "500"->{
                                val dialog=Alert_Dialog(this@RegisterActivity)
                                dialog.showDialog(11)
                            }
                        }

                    }

                    override fun onFailure(call: Call<IsExisted>, t: Throwable) {
                        // 실패
                        Log.d("log",t.message.toString())
                        Log.d("log","id check fail")
                    }
                })
        }

        tv_info_pw.visibility=View.GONE
        et_pw2.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(et_pw.text.toString()!=et_pw2.text.toString()){
                    tv_info_pw.visibility=View.VISIBLE
                }
                else{
                    tv_info_pw.visibility=View.GONE
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        //취소 버튼 클릭시
        bt_cancel.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        //회원가입하기 버튼 클릭시-아이디 중복 체크 여부, 아이디 입력 여부, 비밀번호 동일 여부, 이름 입력 여부 확인
        bt_register.setOnClickListener {
            val id=et_id.text.toString()
            val pw=et_pw.text.toString()
            val pw2=et_pw2.text.toString()
            val name=et_name.text.toString()

            if(id.isEmpty() || pw.isEmpty() || pw2.isEmpty() || name.isEmpty())
            {
                if(id.isEmpty())
                    Log.d(TAG,"아이디 입력 안함")
                else if(pw.isEmpty())
                    Log.d(TAG,"비밀번호 입력 안함")
                else if(pw2.isEmpty())
                    Log.d(TAG,"비밀번호 입력 안함2")
                else if(name.isEmpty())
                    Log.d(TAG,"이름 입력 안함")
                isExistBlank=true
            }
            else{
                isExistBlank=false
                if(pw==pw2){
                    isPwSame=true
                    isPwLength = !(pw.length<8 || pw.length>24)
                }
            }


            //모두 입력하고, 정상적으로 등록을 할 수 있는 경우
            if(!isExistBlank &&isPwSame&&id_check&&id_unique&&isPwLength){
                val userData=SignUpRequestBody(
                    et_id.text.toString(),
                    et_pw.text.toString(),
                    et_name.text.toString()
                )
                val service=RegisterRequest.RetrofitAPI.emgMedService
                service.addUserByEnqueue(userData)
                    .enqueue(object:retrofit2.Callback<SignUpResponseBody>{
                        override fun onResponse(call:Call<SignUpResponseBody>,response:Response<SignUpResponseBody>)
                        {
                            //status에 따른 alert 메세지 출력
                            when(response.code().toString()){
                                "200"->{
                                    val intent=Intent(this@RegisterActivity, RegisterDoneActivity::class.java)
                                    startActivity(intent)
                                }
                                "400"->{
                                    val dialog=Alert_Dialog(this@RegisterActivity)
                                    dialog.showDialog(12)
                                }
                                "403"->{
                                    val dialog=Alert_Dialog(this@RegisterActivity)
                                    dialog.showDialog(1)
                                }
                                "500"->{
                                    val dialog=Alert_Dialog(this@RegisterActivity)
                                    dialog.showDialog(11)
                                }
                            }
                        }

                        override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                            // 실패
                            Log.d("LOG_fail",t.message.toString())
                        }
                    })
            }
            else{//다이얼로그 띄우기
                //입력안한 값이 있는 경우
                if(isExistBlank){
                    val dialog=Alert_Dialog(this)
                    dialog.showDialog(1)
                }
                //비밀번호 입력값이 동일하지 않은 경우
                else if(!isPwSame){
                    Log.d(TAG,"비밀번호 다르게 입력")
                    val dialog=Alert_Dialog(this)
                    dialog.showDialog(2)
                }
                //비밀번호 길이가 적합하지 않은 경우
                else if(!isPwLength){
                    val dialog=Alert_Dialog(this)
                    dialog.showDialog(12)
                }
                //아이디 입력값에 이상이 있는 경우-중복체크 미진행, 중복 아이디
                else if(!id_check || !id_unique){
                    if(!id_check){
                        val dialog=Alert_Dialog(this)
                        dialog.showDialog(3)
                    }
                    else{
                        val dialog=Alert_Dialog(this)
                        dialog.showDialog(4)
                    }
                }
            }
        }
    }




}