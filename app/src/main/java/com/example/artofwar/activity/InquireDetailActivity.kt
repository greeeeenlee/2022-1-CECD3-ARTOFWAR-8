package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_inquire_detail.*
import retrofit2.Call
import retrofit2.Response

class InquireDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquire_detail)

        val access_token=intent.getStringExtra("access_token")
        val inquire=intent.getSerializableExtra("InquireInfo") as UserInquire

        val inquire_qid=inquire.qid.toString()
        var inquire_question=""
        lateinit var inquire_detail:UserInquireDetail

        imgbtn_category.setOnClickListener {
            val intent= Intent(this,CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        imgbtn_home.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val service=VideoRequest.RetrofitAPI6.emgMedService
        service.getInquiredetail(access_token,inquire_qid)
            .enqueue(object :retrofit2.Callback<InquireDetailBody2>{
                override fun onResponse(call: Call<InquireDetailBody2>, response: Response<InquireDetailBody2>){
                    Log.d("LOG_test_inquiredetail",response.body().toString())

                    inquire_detail=UserInquireDetail(inquire_qid.toInt(),response.body()?.message?.name.toString(),response.body()?.message?.content.toString(),response.body()?.message?.question.toString(),response.body()?.message?.video_name.toString())

                    Log.d("LOG_test_uidetail",inquire_detail.toString())

                    tv_inquire_title.text=inquire_detail.name
                    tv_title.text=inquire_detail.video_name
                    tv_inquire.text=inquire_detail.content

                    val inquire_response=inquire_detail.question
                    inquire_question=inquire_detail.question

                    if(inquire_response=="null" || inquire_response==null){//답변이 없는 경우
                        tv_inquire_response.text="아직 답변이 달리지 않았습니다."
                    }
                    else{//답변이 달린 경우
                        tv_inquire_response.text=inquire_response
                    }

                }
                override fun onFailure(call: Call<InquireDetailBody2>, t:Throwable){
                    // 실패
                    val dialog=Alert_Dialog(this@InquireDetailActivity)
                    dialog.showDialog(11)
                    Log.d("LOG_test_fail",t.message.toString())
                }
            })



        //동영상 문의 수정하기 버튼 클릭
        bt_moidfy.setOnClickListener {
            //답변이 달린 이후에는 수정할 수 없음
            if(inquire_question=="" || inquire_question==null || inquire_question=="null"){
                val intent=Intent(this@InquireDetailActivity,InquireModifyActivity::class.java)
                intent.putExtra("access_token",access_token)
                intent.putExtra("Inquire",inquire)
                intent.putExtra("InquireDetail",inquire_detail)
                startActivity(intent)
            }
            else{
                val dialog=Alert_Dialog(this)
                dialog.showDialog(18)
            }
        }

        //동영상 문의 삭제하기 버튼 클릭
        bt_cancel.setOnClickListener {
            Log.d("LOG_test_delete","inquire_delete button clicked")
            Log.d("LOG_test_delete_q",inquire_qid)
            val dialog=Alert_Delete2(this@InquireDetailActivity, inquire_qid!!,access_token!!)
            dialog.showDialog()

        }

    }
}