package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_inquire_modify.*
import retrofit2.Call
import retrofit2.Response

class InquireModifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquire_modify)

        val access_token=intent.getStringExtra("access_token")
        val inquiredetail=intent.getSerializableExtra("InquireDetail") as UserInquireDetail

        val inquire=UserInquire(inquiredetail.qid,inquiredetail.name,inquiredetail.content,inquiredetail.question)

        imgbtn_category.setOnClickListener {
            val intent= Intent(this,CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        et_inquire_title.setText(inquiredetail.name)
        tv_title.text=inquiredetail.video_name
        et_inquire.setText(inquiredetail.content)
        tv_inquire_response.text="아직 답변이 달리지 않았습니다."

        bt_cancel.setOnClickListener {
            finish()
        }

        bt_modify.setOnClickListener {
            val name=et_inquire_title.text.toString()
            val content=et_inquire.text.toString()

            if(name.isEmpty() || content.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val newinquiredata=InquireRequestBody(name,content)
                val service=VideoRequest.RetrofitAPI7.emgMedService
                service.modifyInquire(access_token,newinquiredata,inquire.qid.toString())
                    .enqueue(object :retrofit2.Callback<InquireResponseBody>{
                        override fun onResponse(call: Call<InquireResponseBody>, response: Response<InquireResponseBody>){
                            Log.d("log_test_modify",response.toString())

                            if(response.code().toString()!="200"){
                                val dialog=Alert_Dialog(this@InquireModifyActivity)
                                dialog.showDialog(15)
                            }
                            else{
                                val dialog=Alert_Modify2(this@InquireModifyActivity,inquire,access_token!!)
                                dialog.showDialog()
                            }
                        }
                        override fun onFailure(call: Call<InquireResponseBody>, t:Throwable){
                            // 실패
                            val dialog=Alert_Dialog(this@InquireModifyActivity)
                            dialog.showDialog(11)
                            Log.d("LOG_test_fail",t.message.toString())
                        }
                    })


            }
        }
    }
}


