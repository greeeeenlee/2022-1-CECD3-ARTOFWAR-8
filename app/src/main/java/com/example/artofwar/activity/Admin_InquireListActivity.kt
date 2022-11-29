package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.artofwar.R
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_admin_inquire_list.*
import kotlinx.android.synthetic.main.activity_admin_inquire_list.tv_noinquire
import retrofit2.Call
import retrofit2.Response

class Admin_InquireListActivity : AppCompatActivity() {

    var AdminInquireList= arrayListOf<AdminInquire>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_inquire_list)

        val access_token = intent.getStringExtra("access_token")
        val admininquirearr = ArrayList<JsonElement>()

        val service = AdminRequest.RetrofitAPI2.emgMedService
        service.getAdminInquireList(access_token)
            .enqueue(object : retrofit2.Callback<inquirelist> {
                override fun onResponse(call: Call<inquirelist>, response: Response<inquirelist>) {
                    val inquirecount = response.body()?.inquireInfo?.size()?.toInt()

                    if (inquirecount == null || inquirecount == 0) {
                        listView_admin.visibility = View.GONE
                        tv_noinquire.text = "답변하지 않은 문의사항이 없습니다."
                    } else {
                        tv_noinquire.visibility = View.GONE
                        for (i in 0..(inquirecount - 1)!!) {
                            val temp = response.body()!!.inquireInfo[i.toString()]
                            admininquirearr.add(temp)

                            val temp_object = temp.asJsonObject //jsonelement->jsonobject
                            val qid = replacestr(temp_object.get("qid").toString())
                            val status=replacestr(temp_object.get("status").toString())
                            val status_detail=replacestr(temp_object.get("status_detail").toString())
                            val name = replacestr(temp_object.get("name").toString())
                            val content = replacestr(temp_object.get("content").toString())
                            val question = replacestr(temp_object.get("question").toString())

                            val temp_inquiredata = AdminInquire(qid.toInt(),status,status_detail, name, content, question)
                            AdminInquireList.add(i, temp_inquiredata)
                        }
                    }

                    val Adapter =
                        AdminInquireAdapter(this@Admin_InquireListActivity, AdminInquireList)
                    listView_admin.adapter = Adapter

                    //리스트뷰 클릭 이벤트
                    listView_admin.setOnItemClickListener { adapterView, view, i, l ->
                        val clickedInquire = AdminInquireList[i]
                        Log.d("LOG_test_clickinquire", clickedInquire.toString())
                        val intent = Intent(
                            this@Admin_InquireListActivity,
                            Admin_InquireActivity::class.java
                        )
                        intent.putExtra("access_token", access_token)
                        intent.putExtra("InquireInfo", clickedInquire)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<inquirelist>, t: Throwable) {
                    //실패
                    val dialog = Alert_Dialog(this@Admin_InquireListActivity)
                    dialog.showDialog(11)
                    finish()
                    Log.d("LOG_test_fail", t.message.toString())
                }

            })
    }

    private fun replacestr(str:String):String {
        var finalstr=str.replace("\"","") //따옴표 제거
        finalstr=finalstr.replace("\\","") //역슬래시 제거
        return finalstr
    }
}