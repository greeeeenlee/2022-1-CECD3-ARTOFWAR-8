package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.artofwar.R
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_inquire_list.*
import retrofit2.Call
import retrofit2.Response

class InquireListActivity : AppCompatActivity() {

    var UserInquireList= arrayListOf<UserInquire>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquire_list)

        val access_token=intent.getStringExtra("access_token")
        val inquirearr=ArrayList<JsonElement>()

        imgbtn_category.setOnClickListener {
            val intent= Intent(this,CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }

        val service=VideoRequest.RetrofitAPI5.emgMedService
        service.getInquireInfo(access_token)
            .enqueue(object :retrofit2.Callback<inquirelist>{
                override fun onResponse(call: Call<inquirelist>, response: Response<inquirelist>) {

                    val inquirecount=response.body()?.inquireInfo?.size()?.toInt()

                    if(inquirecount==null || inquirecount==0){//문의내역이 없는 경우
                        listView_inquire.visibility= View.GONE
                        tv_noinquire.text="문의사항이 없습니다."
                    }
                    else{
                        tv_noinquire.visibility=View.GONE
                        for(i in 0..(inquirecount-1)!!){
                            val temp=response.body()!!.inquireInfo[i.toString()]
                            inquirearr.add(temp)

                            val temp_obj=temp.asJsonObject //jsonelement->jsonobject
                            val qid=replacestr(temp_obj.get("qid").toString())
                            val name=replacestr(temp_obj.get("name").toString())
                            val content=replacestr(temp_obj.get("content").toString())
                            val question=replacestr(temp_obj.get("question").toString())

                            val temp_inquiredata=UserInquire(qid.toInt(),name,content,question)
                            UserInquireList.add(i,temp_inquiredata)
                        }
                    }

                    val Adapter=UserInquireAdapter(this@InquireListActivity,UserInquireList)
                    listView_inquire.adapter=Adapter

                    //리스트뷰 클릭 이벤트
                    listView_inquire.setOnItemClickListener { adapterView, view, i, l ->
                        val clickedInquire=UserInquireList[i]
                        Log.d("LOG_test_clickinquire",clickedInquire.toString())
                        val intent=Intent(this@InquireListActivity,InquireDetailActivity::class.java)
                        intent.putExtra("access_token",access_token)
                        intent.putExtra("InquireInfo",clickedInquire)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<inquirelist>, t: Throwable) {
                    //실패
                    val dialog=Alert_Dialog(this@InquireListActivity)
                    dialog.showDialog(11)
                    finish()
                    Log.d("LOG_test_fail",t.message.toString())
            }

            })
    }

    private fun replacestr(str:String):String{
        var finalstr=str.replace("\"","") //따옴표 제거
        finalstr=finalstr.replace("\\","") //역슬래시 제거
        return finalstr
    }
}